package cn.longmaster.hospital.doctor.ui.rounds;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.common.FileUtil;
import cn.longmaster.doctorlibrary.util.fileloader.downloader.FileLoadListener;
import cn.longmaster.doctorlibrary.util.handler.MessageHandler;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.hospital.doctor.Display;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.download.DownloadState;
import cn.longmaster.hospital.doctor.core.download.MediaDownloadInfo;
import cn.longmaster.hospital.doctor.core.download.MediaDownloadManager;
import cn.longmaster.hospital.doctor.core.entity.consult.AuxiliaryMaterialInfo;
import cn.longmaster.hospital.doctor.core.entity.event.BrowserPicEvent;
import cn.longmaster.hospital.doctor.core.entity.rounds.RoundsMedicalDetailsInfo;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.rounds.MedicalAuxiliaryInspectRequester;
import cn.longmaster.hospital.doctor.core.requests.rounds.RoundsDeleteMaterialRequest;
import cn.longmaster.hospital.doctor.core.requests.rounds.RoundsMedicalDetailsRequester;
import cn.longmaster.hospital.doctor.core.upload.MaterialTask;
import cn.longmaster.hospital.doctor.core.upload.SingleFileInfo;
import cn.longmaster.hospital.doctor.core.upload.newupload.MaterialTaskManager;
import cn.longmaster.hospital.doctor.ui.CaptureActivity;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.consult.ConsultDataManageActivity;
import cn.longmaster.hospital.doctor.ui.consult.PickPhotoActivity;
import cn.longmaster.hospital.doctor.ui.consult.UploadPictureActivity;
import cn.longmaster.hospital.doctor.ui.consult.VideoPlayerActivity;
import cn.longmaster.hospital.doctor.ui.consult.adapter.DataManagerAdapter;
import cn.longmaster.hospital.doctor.ui.user.BrowserActivity;
import cn.longmaster.hospital.doctor.view.AppActionBar;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.hospital.doctor.view.dialog.PhotoChooseDialog;
import cn.longmaster.hospital.doctor.view.dialog.UploadTipDialog;
import cn.longmaster.utils.PermissionConstants;
import cn.longmaster.utils.PermissionHelper;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.ToastUtils;
import cn.longmaster.utils.Utils;
import io.reactivex.disposables.Disposable;

/**
 * 患者资料管理Activity
 * mod by biao on 2019/7/18
 */
public class RoundsDataManagerActivity extends NewBaseActivity implements FileLoadListener {
    private final String TAG = ConsultDataManageActivity.class.getSimpleName();
    private final int REQUEST_CODE_HIDE_CONTROL = 103; //隐藏控件
    private final int REQUEST_CODE_CAPTURE = 104; //相机
    private final int REQUEST_CODE_ALBUM = 105; //相册

    @FindViewById(R.id.activity_rounds_data_manage_title_bar)
    private AppActionBar mDataManageTitleBar;
    @FindViewById(R.id.activity_rounds_data_manage_patient_tv)
    private TextView mPatientTv;
    @FindViewById(R.id.activity_rounds_data_manage_consult_number_tv)
    private TextView mNumberTv;
    @FindViewById(R.id.activity_rounds_data_manage_group_rg)
    private RadioGroup mGroupRg;
    @FindViewById(R.id.activity_rounds_data_manage_auxiliary_examination_rb)
    private RadioButton mAuxiliaryExaminationRb;
    //远程会诊辅助检查
    @FindViewById(R.id.activity_rounds_data_manage_data_queue_upload_num_tv)
    private TextView mUploadDataNumber;//上传资料总项
    @FindViewById(R.id.activity_rounds_data_manage_auxiliary_examination_layout_ll)
    private LinearLayout mAuxiliaryExaminationLayoutLl;
    @FindViewById(R.id.activity_rounds_data_manage_not_pass_material_title_ll)
    private LinearLayout mNotPassListTitleLl;//未通过审核材料列表标题
    @FindViewById(R.id.activity_rounds_data_manage_not_pass_material_list)
    private RecyclerView mNotPassListRv;
    @FindViewById(R.id.activity_rounds_data_manage_pass_material_title_tv)
    private TextView mPassListTitleTv;//通过审核材料列表标题
    @FindViewById(R.id.activity_rounds_data_manage_pass_material_list)
    private RecyclerView mPassListRv;

    @AppApplication.Manager
    private MediaDownloadManager mMediaDownloadManager;
    @AppApplication.Manager
    private MaterialTaskManager mMaterialTaskManager;

    private List<AuxiliaryMaterialInfo> mAuxiliaryMaterialInfos;//材料信息（服务器返回）
    private List<SingleFileInfo> mNotPassMaterials = new ArrayList<>();//审核未通过材料列表
    private List<SingleFileInfo> mPassMaterials = new ArrayList<>();//审核通过材料列表
    private boolean mIsImagingConsult = false;//是否影像会诊
    private ProgressDialog mProgressDialog;
    private DataManagerAdapter mPassListAdapter;
    private DataManagerAdapter mNotPassListAdapter;
    private boolean mFromClinic;
    private int mMedicalId;//病历Id
    private int mDataTotal = 0;
    MessageHandler mHandler = new MessageHandler(new MessageHandler.HandlerMessageListener() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AppConstant.CLIENT_MESSAGE_CODE_ON_DELETE_MATERIAL:
                    mNotPassMaterials.remove(msg.arg1);
                    mNotPassListAdapter.notifyDataSetChanged();
                    mDataTotal--;
                    mUploadDataNumber.setText(getString(R.string.data_queue_upload_num, mDataTotal));
                    break;

                case AppConstant.CLIENT_MESSAGE_CODE_DELETE_PASS_MATERIAL:
                    mPassMaterials.remove(msg.arg1);
                    mPassListAdapter.notifyDataSetChanged();
                    mDataTotal--;
                    mUploadDataNumber.setText(getString(R.string.data_queue_upload_num, mDataTotal));
                    break;
            }
        }
    });

    @Override
    protected void handleIntent(Intent intent, Display display) {
        super.handleIntent(intent, display);
        boolean isShowSecondText = intent.getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SHOW_SECOND_TEXT, true);
        if (!isShowSecondText) {
            mDataManageTitleBar.removeFunction(AppActionBar.FUNCTION_RIGHT_SECOND_TEXT);
        }
        mMedicalId = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDICAL_ID, 0);
        mFromClinic = intent.getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_FROM_CLINIC, false);
    }

    @Override
    protected void initDatas() {
        mHandler.registMessage(AppConstant.CLIENT_MESSAGE_CODE_ON_DELETE_MATERIAL);
        mHandler.registMessage(AppConstant.CLIENT_MESSAGE_CODE_DELETE_PASS_MATERIAL);
        mNotPassListAdapter = new DataManagerAdapter(R.layout.item_data_manage_photo, mNotPassMaterials);
        mPassListAdapter = new DataManagerAdapter(R.layout.item_data_manage_photo, mPassMaterials);
        mNotPassListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            SingleFileInfo singleFileInfo = (SingleFileInfo) adapter.getItem(position);
            if (null == singleFileInfo) {
                return;
            }
            if (view.getId() == R.id.item_data_manage_photo_delete_iv) {
                deleteMaterial(singleFileInfo, false);
            }
        });
        mNotPassListAdapter.setOnItemClickListener((adapter, view, position) -> {
            SingleFileInfo singleFileInfo = (SingleFileInfo) adapter.getItem(position);
            if (null != singleFileInfo) {
                handleDataItemClick(singleFileInfo, position, false);
            }
        });
        mNotPassListAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            mNotPassListAdapter.toggleDelete(position);
            return false;
        });

        mPassListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            SingleFileInfo singleFileInfo = (SingleFileInfo) adapter.getItem(position);
            if (null == singleFileInfo) {
                return;
            }
            if (view.getId() == R.id.item_data_manage_photo_delete_iv) {
                deleteMaterial(singleFileInfo, true);
            }
        });
        mPassListAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (adapter.getItemViewType(position) == DataManagerAdapter.TYPE_CHOOSE) {
                showPicChoose();
            } else {
                SingleFileInfo info = (SingleFileInfo) adapter.getItem(position);
                if (null != info) {
                    handleDataItemClick(info, position, true);
                }
            }

        });
        mPassListAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            mPassListAdapter.toggleDelete(position);
            return false;
        });

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_rounds_data_manage;
    }

    @Override
    protected void initViews() {
        mNotPassListRv.setLayoutManager(RecyclerViewUtils.getGridLayoutManager(this, 4));
        mNotPassListRv.setNestedScrollingEnabled(false);
        mPassListRv.setLayoutManager(RecyclerViewUtils.getGridLayoutManager(this, 4));
        mPassListRv.setNestedScrollingEnabled(false);
        mNotPassListRv.setAdapter(mNotPassListAdapter);
        mPassListRv.setAdapter(mPassListAdapter);
        getPatientInfo(mMedicalId);
        getMaterialList(mMedicalId);
        addListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_HIDE_CONTROL:
                    boolean hideControl = data.getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_HIDE_CONTROL, false);
                    Logger.logD(Logger.APPOINTMENT, TAG + "onActivityResult-->mHideControl:" + hideControl);
                    if (hideControl) {
                        mNotPassListRv.setVisibility(View.GONE);
                    } else {
                        mNotPassListRv.setVisibility(View.VISIBLE);
                    }
                    break;

                case REQUEST_CODE_CAPTURE:
                    ArrayList<String> capturePhotoList = data.getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATHS);
                    startUpload(capturePhotoList);
                    break;

                case REQUEST_CODE_ALBUM:
                    ArrayList<String> albumPhotoList = data.getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SELECT_PATHS);
                    startUpload(albumPhotoList);
                    break;
            }
        }

    }

    private void showPicChoose() {
        PhotoChooseDialog photoChooseDialog = PhotoChooseDialog.getInstance();
        photoChooseDialog.setListener(new PhotoChooseDialog.OnClickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onCameraClick() {
                Disposable disposable = PermissionHelper.init(getThisActivity()).addPermissionGroups(PermissionConstants.CAMERA)
                        .requestEachCombined()
                        .subscribe(permission -> {
                            if (permission.granted) {
                                CaptureActivity.startActivity(getThisActivity(), false, REQUEST_CODE_CAPTURE);
                                // `permission.name` is granted !
                            } else {
                                new CommonDialog.Builder(getThisActivity())
                                        .setTitle("权限授予")
                                        .setMessage(R.string.camera_permission_not_granted)
                                        .setPositiveButton("取消", () -> finish())
                                        .setCancelable(false)
                                        .setNegativeButton("确定", Utils::gotoAppDetailSetting)
                                        .show();
                            }
                        });
                compositeDisposable.add(disposable);
            }

            @Override
            public void onAlbumClick() {
                Intent intent = new Intent(getThisActivity(), PickPhotoActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, mMedicalId);
                startActivityForResult(intent, REQUEST_CODE_ALBUM);
            }
        });
        photoChooseDialog.show(getSupportFragmentManager(), "");
    }

    private void startUpload(ArrayList<String> photoList) {
        Logger.logI(Logger.COMMON, TAG + "-->startUpload-->photoList:" + photoList + ",mMedicalId:" + mMedicalId);
        if (mFromClinic) {
            mMaterialTaskManager.startUpload(mMedicalId, photoList, false);
            finish();
        } else {
            Intent intent = new Intent(this, UploadPictureActivity.class);
            intent.putStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATHS, photoList);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, mMedicalId);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.unregistMessage(AppConstant.CLIENT_MESSAGE_CODE_ON_DELETE_MATERIAL);
        mHandler.unregistMessage(AppConstant.CLIENT_MESSAGE_CODE_DELETE_PASS_MATERIAL);
        mMediaDownloadManager.unRegLoadListener(this);
    }

    @OnClick({R.id.activity_rounds_data_manage_mask_view,
            R.id.activity_rounds_data_manage_remark_layout_fl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_rounds_data_manage_mask_view:
                break;

            case R.id.activity_rounds_data_manage_remark_layout_fl:
                //什么也不干，只为拦截点击和滑动事件
                break;
            default:
                break;
        }
    }

    private void addListener() {
        mGroupRg.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.activity_rounds_data_manage_auxiliary_examination_rb:
                    mAuxiliaryExaminationRb.setTextColor(getCompatColor(R.color.color_white));
                    if (!mIsImagingConsult) {
                        mAuxiliaryExaminationLayoutLl.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        });
        mMediaDownloadManager.regLoadListener(this);
        mDataManageTitleBar.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getPatientInfo(int medicalId) {
        RoundsMedicalDetailsRequester requester = new RoundsMedicalDetailsRequester(new DefaultResultCallback<RoundsMedicalDetailsInfo>() {
            @Override
            public void onSuccess(RoundsMedicalDetailsInfo roundsMedicalDetailsInfo, BaseResult baseResult) {
                Logger.logI(Logger.APPOINTMENT, "baseResult:" + baseResult + ",roundsMedicalDetailsInfo：" + roundsMedicalDetailsInfo);
                String gender = getString(roundsMedicalDetailsInfo.getGender() == 1 ? R.string.gender_male : R.string.gender_female);
                mPatientTv.setText(roundsMedicalDetailsInfo.getPatientName() + "\t\t" + gender + "\t" + roundsMedicalDetailsInfo.getAge());
                mNumberTv.setText(medicalId + "");
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                ToastUtils.showShort(R.string.consult_room_state_net_bad);
            }
        });
        requester.setMedicalId(medicalId);
        requester.doPost();
    }

    /**
     * 请求材料列表
     */
    private void getMaterialList(int medicalId) {
        showProgressDialog();
        MedicalAuxiliaryInspectRequester requester = new MedicalAuxiliaryInspectRequester(new DefaultResultCallback<List<AuxiliaryMaterialInfo>>() {
            @Override
            public void onSuccess(List<AuxiliaryMaterialInfo> auxiliaryMaterialInfos, BaseResult baseResult) {
                if (mIsImagingConsult) {
                    dealImagingMaterail(auxiliaryMaterialInfos);
                } else {
                    mAuxiliaryMaterialInfos = auxiliaryMaterialInfos;
                    mDataTotal = mAuxiliaryMaterialInfos.size();
                    mUploadDataNumber.setText(getString(R.string.data_queue_upload_num, mDataTotal));
                    extractMaterial();
                }
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                if (mIsImagingConsult) {
                    dealImagingMaterail(null);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissProgressDialog();
            }
        });
        requester.setMedicalId(medicalId);
        requester.doPost();
    }

    private void showProgressDialog() {
        if (isFinishing()) {
            return;
        }
        if (mProgressDialog == null) {
            mProgressDialog = createProgressDialog(getString(R.string.loading));
        } else if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (isFinishing()) {
            return;
        }
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * 提取通过与未通过的材料
     */
    private void extractMaterial() {
        if (mAuxiliaryMaterialInfos == null) {
            return;
        }

        final List<AuxiliaryMaterialInfo> passMaterialInfos = new ArrayList<>();
        final List<AuxiliaryMaterialInfo> unPassMaterialInfos = new ArrayList<>();
        for (AuxiliaryMaterialInfo auxiliaryMaterialInfo : mAuxiliaryMaterialInfos) {
            if (auxiliaryMaterialInfo.getCheckState() == AppConstant.MaterialCheckState.STATE_CHECK_SUCCESS) {
                passMaterialInfos.add(auxiliaryMaterialInfo);
            } else {
                unPassMaterialInfos.add(auxiliaryMaterialInfo);
            }
        }

        mMediaDownloadManager.getDownloadInfosFromDB(mMedicalId, mediaDownloadInfos -> {
            mPassMaterials.addAll(getFileInfos(passMaterialInfos, mediaDownloadInfos));
            mNotPassMaterials.addAll(getFileInfos(unPassMaterialInfos, mediaDownloadInfos));
            if (mNotPassMaterials.size() == 0) {
                mNotPassListRv.setVisibility(View.GONE);
            } else {
                mNotPassListRv.setVisibility(View.VISIBLE);
            }
            mNotPassListAdapter.notifyDataSetChanged();
            mPassListAdapter.notifyDataSetChanged();
            dismissProgressDialog();
        });
    }

    /**
     * 构建材料文件
     *
     * @param list 资料列表
     * @return
     */
    private List<SingleFileInfo> getFileInfos(List<AuxiliaryMaterialInfo> list, Map<String, MediaDownloadInfo> mediaDownloadInfoMap) {
        List<SingleFileInfo> singleFileInfos = new ArrayList<>();
        for (AuxiliaryMaterialInfo materialInfo : list) {
            SingleFileInfo singleFileInfo = new SingleFileInfo("0", SdManager.getInstance().getOrderPicPath(materialInfo.getMaterialPic(), materialInfo.getAppointmentId() + ""));
            singleFileInfo.setLocalFileName(materialInfo.getMaterialPic());
            singleFileInfo.setLocalFilePath(SdManager.getInstance().getOrderPicPath(materialInfo.getMaterialPic(), materialInfo.getAppointmentId() + ""));
            singleFileInfo.setServerFileName(materialInfo.getMaterialPic());
            singleFileInfo.setIsFromServer(true);
            singleFileInfo.setAppointmentId(materialInfo.getAppointmentId());
            singleFileInfo.setMaterialId(materialInfo.getMaterialId());
            singleFileInfo.setCheckState(materialInfo.getCheckState());
            singleFileInfo.setAuditDesc(materialInfo.getAuditDesc());
            singleFileInfo.setMaterailType(materialInfo.getMaterialType());
            singleFileInfo.setMediaType(materialInfo.getMediaType());
            singleFileInfo.setDicom(materialInfo.getDicom());
            singleFileInfo.setMaterialName(materialInfo.getMaterialName());
            singleFileInfo.setMaterialResult(materialInfo.getMaterialResult());
            singleFileInfo.setMaterialDt(materialInfo.getMaterialDt());
            singleFileInfo.setMaterialHosp(materialInfo.getMaterialHosp());
            if (singleFileInfo.getMaterailType() == AppConstant.MaterailType.MATERAIL_TYPE_MEDIA
                    && singleFileInfo.getMediaType() == AppConstant.MediaType.MEDIA_TYPE_VIDEO) {
                if (FileUtil.isFileExist(mMediaDownloadManager.getFilePath(singleFileInfo.getLocalFileName(), mMedicalId))) {
                    singleFileInfo.setMediaDownloadInfo(new MediaDownloadInfo(mMedicalId, DownloadState.DOWNLOAD_SUCCESS, singleFileInfo.getLocalFileName()));
                } else {
                    MediaDownloadInfo mediaDownloadInfo = mediaDownloadInfoMap.get(singleFileInfo.getLocalFileName());
                    if (mediaDownloadInfo != null) {
                        if (!FileUtil.isFileExist(mMediaDownloadManager.getTempPath(singleFileInfo.getLocalFileName(), mMedicalId))) {
                            mediaDownloadInfo.setState(mMediaDownloadManager.isDownLoading(singleFileInfo.getLocalFileName())
                                    ? DownloadState.DOWNLOADING : DownloadState.NOT_DOWNLOAD);
                        } else {
                            if (mediaDownloadInfo.getState() == DownloadState.DOWNLOADING && !mMediaDownloadManager.isDownLoading(singleFileInfo.getLocalFileName())) {
                                mediaDownloadInfo.setState(DownloadState.DOWNLOAD_PAUSE);
                            }
                        }
                    } else {
                        mediaDownloadInfo = new MediaDownloadInfo(mMedicalId,
                                mMediaDownloadManager.isDownLoading(singleFileInfo.getLocalFileName()) ? DownloadState.DOWNLOADING : DownloadState.NOT_DOWNLOAD,
                                singleFileInfo.getLocalFileName());
                    }
                    singleFileInfo.setMediaDownloadInfo(mediaDownloadInfo);
                }
            }
            singleFileInfos.add(singleFileInfo);
        }
        return singleFileInfos;
    }

    /**
     * 删除材料
     */
    private void deleteMaterial(final SingleFileInfo singleFileInfo, final boolean isPass) {
        final ProgressDialog progressDialog = createProgressDialog(getString(R.string.consult_deleting_voice_file));
        RoundsDeleteMaterialRequest request = new RoundsDeleteMaterialRequest(new OnResultListener<String>() {
            @Override
            public void onResult(BaseResult baseResult, String s) {
                if (baseResult.getCode() == RESULT_SUCCESS) {
                    ToastUtils.showShort(R.string.data_delete_success);
                    mDataTotal--;
                    mUploadDataNumber.setText(getString(R.string.data_queue_upload_num, mDataTotal));
                    if (isPass) {
                        FileUtil.deleteFile(singleFileInfo.getLocalFilePath());
                        MaterialTask.delTask(singleFileInfo.getLocalFileName());
                        mPassMaterials.remove(singleFileInfo);
                        mPassListAdapter.notifyDataSetChanged();
                    } else {
                        if (mNotPassMaterials.size() == 1) {
                            mNotPassListRv.setVisibility(View.GONE);
                        } else {
                            mNotPassListRv.setVisibility(View.VISIBLE);
                        }
                        FileUtil.deleteFile(singleFileInfo.getLocalFilePath());
                        MaterialTask.delTask(singleFileInfo.getLocalFileName());
                        mNotPassMaterials.remove(singleFileInfo);
                        mNotPassListAdapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.showShort(R.string.data_delete_fail);
                }
                progressDialog.cancel();
            }
        });
        request.medicalId = mMedicalId;
        request.materialId = singleFileInfo.getMaterialId();
        request.materialPic = singleFileInfo.getServerFileName();
        request.doPost();
    }

    private void handleDataItemClick(SingleFileInfo singleFileInfo, int position, boolean isPass) {
        if (singleFileInfo.getMaterailType() == AppConstant.MaterailType.MATERAIL_TYPE_DICOM
                || singleFileInfo.getMaterailType() == AppConstant.MaterailType.MATERAIL_TYPE_WSI) {
            Intent intent = new Intent(getThisActivity(), BrowserActivity.class);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, singleFileInfo.getDicom());
            startActivity(intent);
        } else if (singleFileInfo.getMaterailType() == AppConstant.MaterailType.MATERAIL_TYPE_MEDIA) {
            if (singleFileInfo.getMediaType() == AppConstant.MediaType.MEDIA_TYPE_VIDEO) {
                if (singleFileInfo.getMediaDownloadInfo() != null) {
                    switch (singleFileInfo.getMediaDownloadInfo().getState()) {
                        case DownloadState.NOT_DOWNLOAD:
                            downloadMedia(singleFileInfo.getMediaDownloadInfo());
                            break;

                        case DownloadState.DOWNLOADING:
                            pauseDownload(singleFileInfo.getLocalFileName());
                            break;

                        case DownloadState.DOWNLOAD_SUCCESS:
                            playVideo(singleFileInfo);
                            break;

                        case DownloadState.DOWNLOAD_FAILED:
                            //重新下载;
                            Logger.logI(Logger.APPOINTMENT, "重新下载");
                            downloadMedia(singleFileInfo.getMediaDownloadInfo());
                            break;

                        case DownloadState.DOWNLOAD_PAUSE:
                            //继续下载;
                            Logger.logI(Logger.APPOINTMENT, "继续下载");
                            downloadMedia(singleFileInfo.getMediaDownloadInfo());
                            break;
                    }
                }
                return;
            }
            String path = "";
            String url = "";
            if (!TextUtils.isEmpty(singleFileInfo.getLocalFileName())) {
                path = SdManager.getInstance().getOrderVideoPath(singleFileInfo.getLocalFileName(), singleFileInfo.getAppointmentId() + "");
                url = AppConfig.getDfsUrl() + "3004/1/" + singleFileInfo.getLocalFileName();
            }
            Intent intent = new Intent();
            intent.setClass(getThisActivity(), VideoPlayerActivity.class);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_TITLE, singleFileInfo.getMaterialName());
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_TYPE, singleFileInfo.getMediaType());
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_PATH, path);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_URL, url);
            startActivity(intent);
        } else {
            if (isPass) {
                startPicBrowse(mPassMaterials, position, isPass);
            } else {
                startPicBrowse(mNotPassMaterials, position, isPass);
            }
        }
    }

    private void downloadMedia(final MediaDownloadInfo downloadInfo) {
        mMediaDownloadManager.fileDownload(downloadInfo);
    }

    private void pauseDownload(String fileName) {
        mMediaDownloadManager.pauseDownload(fileName);
    }

    private void playVideo(SingleFileInfo info) {
        String path = "";
        String url = "";
        if (!TextUtils.isEmpty(info.getLocalFileName())) {
            path = SdManager.getInstance().getOrderVideoPath(info.getLocalFileName(), info.getAppointmentId() + "");
            url = AppConfig.getDfsUrl() + "3004/1/" + info.getLocalFileName();
        }
        Logger.logD(Logger.APPOINTMENT, "-->path:" + path);
        Intent intent = new Intent();
        intent.setClass(this, VideoPlayerActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_TITLE, info.getMaterialName());
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_TYPE, info.getMediaType());
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_PATH, path);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_URL, url);
        startActivity(intent);
    }

    /**
     * 启动大图预览
     */
    private void startPicBrowse(List<SingleFileInfo> singleFileInfos, int index, boolean isPass) {
        Logger.logD(Logger.APPOINTMENT, "startPicBrowse():index:" + index + ", singleFileInfos:" + singleFileInfos);
        ArrayList<String> servicePaths = new ArrayList<>();
        List<SingleFileInfo> newSingleFileInfo = new ArrayList<>();
        int finalIndex = index;
        for (SingleFileInfo singleFileInfo : singleFileInfos) {
            if (singleFileInfo.getMaterailType() != AppConstant.MaterailType.MATERAIL_TYPE_PIC) {
                if (singleFileInfos.indexOf(singleFileInfo) < index) {
                    finalIndex--;
                }
                continue;
            }
            servicePaths.add(AppConfig.getMaterialDownloadUrl() + singleFileInfo.getServerFileName());
            newSingleFileInfo.add(singleFileInfo);
        }
        BrowserPicEvent browserPicEvent = new BrowserPicEvent();
        browserPicEvent.setServerFilePaths(servicePaths);
        browserPicEvent.setIndex(finalIndex);
        browserPicEvent.setRounds(true);
        browserPicEvent.setPass(isPass);
        browserPicEvent.setAppointInfoId(mMedicalId);
        browserPicEvent.setSingleFileInfos(newSingleFileInfo);
        browserPicEvent.setAuxiliaryMaterialInfos(mAuxiliaryMaterialInfos);
        getDisplay().startPicBrowseActivity(browserPicEvent, REQUEST_CODE_HIDE_CONTROL);
    }

    /**
     * 填充dicom
     *
     * @param auxiliaryMaterialInfos
     */
    private void fillDicom(List<AuxiliaryMaterialInfo> auxiliaryMaterialInfos) {
        List<List<AuxiliaryMaterialInfo>> groupList = groupData(auxiliaryMaterialInfos);
        // mDicomVp.setAdapter(new DicomPageAdapter(getThisActivity(), groupList));
    }

    /**
     * 分組材料
     *
     * @return 分組材料
     */
    private List<List<AuxiliaryMaterialInfo>> groupData(List<AuxiliaryMaterialInfo> auxiliaryMaterialInfos) {
        List<AuxiliaryMaterialInfo> newMaterialInfos = new ArrayList<>();
        //提取dicom图片
        for (AuxiliaryMaterialInfo auxiliaryMaterialInfo : auxiliaryMaterialInfos) {
            if (auxiliaryMaterialInfo.getMaterialType() == 1) {
                newMaterialInfos.add(auxiliaryMaterialInfo);
            }
        }
        List<List<AuxiliaryMaterialInfo>> groupList = new ArrayList<>();
        int index = 0;
        while (index < newMaterialInfos.size()) {
            List<AuxiliaryMaterialInfo> materialInfoLists = new ArrayList<>();
            materialInfoLists.add(newMaterialInfos.get(index));
            index++;
            if (index < newMaterialInfos.size()) {
                materialInfoLists.add(newMaterialInfos.get(index));
                index++;
            }
            if (index < newMaterialInfos.size()) {
                materialInfoLists.add(newMaterialInfos.get(index));
                index++;
            }
            if (index < newMaterialInfos.size()) {
                materialInfoLists.add(newMaterialInfos.get(index));
                index++;
            }
            groupList.add(materialInfoLists);
        }
        return groupList;
    }

    /**
     * 处理影像材料
     *
     * @param auxiliaryMaterialInfos
     */
    private void dealImagingMaterail(List<AuxiliaryMaterialInfo> auxiliaryMaterialInfos) {
    }

    private void showNoNetworkDialog() {
        new CommonDialog.Builder(getThisActivity())
                .setMessage(R.string.no_network)
                .setNegativeButton(R.string.video_dialog_ok, new CommonDialog.OnNegativeBtnClickListener() {
                    @Override
                    public void onNegativeBtnClicked() {
                    }
                })
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .show();
    }

    public void rightClick(View view) {
        new UploadTipDialog().show(getSupportFragmentManager(), "UploadTipDialog");
    }

    @Override
    public void onStartDownload(String filePath) {
        Logger.logI(Logger.APPOINTMENT, "onStartDownload");
        MediaDownloadInfo mediaDownloadInfo = new MediaDownloadInfo();
        mediaDownloadInfo.setLocalFileName(mMediaDownloadManager.getFileName(filePath));
        mediaDownloadInfo.setState(DownloadState.DOWNLOADING);
        upDateDownLoadInfo(mediaDownloadInfo);
    }

    @Override
    public void onLoadProgressChange(String filePath, long totalSize, long currentSize) {
        Logger.logI(Logger.APPOINTMENT, "onLoadProgressChange");
        MediaDownloadInfo mediaDownloadInfo = new MediaDownloadInfo();
        mediaDownloadInfo.setLocalFileName(mMediaDownloadManager.getFileName(filePath));
        mediaDownloadInfo.setState(DownloadState.DOWNLOADING);
        mediaDownloadInfo.setCurrentSize(currentSize);
        mediaDownloadInfo.setTotalSize(totalSize);
        upDateDownLoadInfo(mediaDownloadInfo);
    }

    @Override
    public void onLoadFailed(String filePath, String reason) {
        Logger.logI(Logger.APPOINTMENT, "onLoadFailed");
        MediaDownloadInfo mediaDownloadInfo = new MediaDownloadInfo();
        mediaDownloadInfo.setLocalFileName(mMediaDownloadManager.getFileName(filePath));
        mediaDownloadInfo.setState(DownloadState.DOWNLOAD_FAILED);
        upDateDownLoadInfo(mediaDownloadInfo);
    }

    @Override
    public void onLoadSuccessful(String filePath) {
        Logger.logI(Logger.APPOINTMENT, "onLoadSuccessful");
        MediaDownloadInfo mediaDownloadInfo = new MediaDownloadInfo();
        mediaDownloadInfo.setLocalFileName(mMediaDownloadManager.getFileName(filePath));
        mediaDownloadInfo.setState(DownloadState.DOWNLOAD_SUCCESS);
        upDateDownLoadInfo(mediaDownloadInfo);
    }

    @Override
    public void onLoadStopped(String filePath) {
        Logger.logI(Logger.APPOINTMENT, "onLoadStopped");
        MediaDownloadInfo mediaDownloadInfo = new MediaDownloadInfo();
        mediaDownloadInfo.setLocalFileName(mMediaDownloadManager.getFileName(filePath));
        mediaDownloadInfo.setState(DownloadState.DOWNLOAD_PAUSE);
        upDateDownLoadInfo(mediaDownloadInfo);
    }

    private void upDateDownLoadInfo(MediaDownloadInfo info) {
        for (int i = 0; i < mPassMaterials.size(); i++) {
            SingleFileInfo singleFileInfo = mPassMaterials.get(i);
            if (singleFileInfo.getMediaDownloadInfo() != null
                    && singleFileInfo.getLocalFileName().equals(info.getLocalFileName())) {
                singleFileInfo.getMediaDownloadInfo().setState(info.getState());
                singleFileInfo.getMediaDownloadInfo().setCurrentSize(info.getCurrentSize());
                singleFileInfo.getMediaDownloadInfo().setTotalSize(info.getTotalSize());
                mPassListAdapter.notifyItemChanged(i);
                break;
            }
        }
    }
}
