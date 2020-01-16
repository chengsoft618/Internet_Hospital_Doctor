package cn.longmaster.hospital.doctor.ui.consult;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.common.FileUtil;
import cn.longmaster.doctorlibrary.util.fileloader.downloader.FileLoadListener;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.download.DownloadState;
import cn.longmaster.hospital.doctor.core.download.MediaDownloadInfo;
import cn.longmaster.hospital.doctor.core.download.MediaDownloadManager;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointmentInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.AuxiliaryMaterialInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.AuxiliaryMaterialResult;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.AppointmentItemForRelateInfo;
import cn.longmaster.hospital.doctor.core.entity.event.BrowserPicEvent;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.manager.consult.RecordManager;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.appointment.AuxiliaryMaterialRequester;
import cn.longmaster.hospital.doctor.core.requests.appointment.DeleteMaterialRequest;
import cn.longmaster.hospital.doctor.core.upload.SingleFileInfo;
import cn.longmaster.hospital.doctor.core.upload.newupload.MaterialTaskManager;
import cn.longmaster.hospital.doctor.ui.CaptureActivity;
import cn.longmaster.hospital.doctor.ui.PicBrowseActivity;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.consult.adapter.AppointmentRelateAdapter;
import cn.longmaster.hospital.doctor.ui.consult.adapter.DataManagerAdapter;
import cn.longmaster.hospital.doctor.ui.consult.adapter.DicomPageAdapter;
import cn.longmaster.hospital.doctor.ui.consult.record.PatientInformationActivity;
import cn.longmaster.hospital.doctor.ui.user.BrowserActivity;
import cn.longmaster.hospital.doctor.view.AppActionBar;
import cn.longmaster.hospital.doctor.view.IconView;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.hospital.doctor.view.dialog.PhotoChooseDialog;
import cn.longmaster.hospital.doctor.view.dialog.UploadTipDialog;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.PermissionConstants;
import cn.longmaster.utils.PermissionHelper;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.ToastUtils;
import cn.longmaster.utils.Utils;
import io.reactivex.disposables.Disposable;

/**
 * 会诊资料管理Activity
 * Created by Yang² on 2016/7/5.
 * Mod by Biao on 2017/22
 */
public class ConsultDataManageActivity extends NewBaseActivity implements FileLoadListener {
    private final int REQUEST_CODE_RECORD = 102; //页面请求码:关联病例
    private final int REQUEST_CODE_HIDE_CONTROL = 103; //隐藏控件
    private final int REQUEST_CODE_CAPTURE = 104; //相机
    private final int REQUEST_CODE_ALBUM = 105; //相册

    @FindViewById(R.id.activity_data_manage_title_bar)
    private AppActionBar mDataManageTitleBar;
    @FindViewById(R.id.activity_data_manage_patient_tv)
    private TextView mPatientTv;
    @FindViewById(R.id.activity_data_manage_consult_number_tv)
    private TextView mNumberTv;
    @FindViewById(R.id.activity_data_manage_group_rg)
    private RadioGroup mGroupRg;
    //远程会诊辅助检查
    @FindViewById(R.id.activity_data_manage_auxiliary_examination_layout_ll)
    private LinearLayout mAuxiliaryExaminationLayoutLl;
    @FindViewById(R.id.activity_data_manage_data_queue_upload_num_tv)
    private TextView mUploadDataNumber;//上传资料总项
    @FindViewById(R.id.activity_data_manage_pass_material_title_tv)
    private TextView mPassListTitleTv;//通过审核材料列表标题
    @FindViewById(R.id.activity_data_manage_pass_material_list)
    private RecyclerView mPassListRv;
    @FindViewById(R.id.activity_data_manage_auxiliary_imaging_layout_ll)
    private LinearLayout mImagingExaminationLl;
    @FindViewById(R.id.activity_data_manager_dcm_state_iv)
    private IconView mDicomStateIv;
    @FindViewById(R.id.activity_data_manager_no_dcm_tv)
    private TextView mNoDicomTv;
    @FindViewById(R.id.activity_data_manager_dcm_vp)
    private ViewPager mDicomVp;
    //关联病历
    @FindViewById(R.id.activity_data_manage_relate_record_layout_ll)
    private LinearLayout mRelateRecordLayoutLl;
    @FindViewById(R.id.act_data_manage_add_relate_record_rl)
    private RelativeLayout mRelatedRecordTitleTv;
    @FindViewById(R.id.activity_data_manage_related_record_rv)
    private RecyclerView mRelatedRecordRv;
    @FindViewById(R.id.activity_data_manage_mask_view)
    private View mMaskView;
    @FindViewById(R.id.activity_data_manage_remark_layout_fl)
    private FrameLayout mRemarkLayoutFl;

    @AppApplication.Manager
    private ConsultManager mConsultManager;
    @AppApplication.Manager
    private MediaDownloadManager mMediaDownloadManager;
    @AppApplication.Manager
    private MaterialTaskManager mMaterialTaskManager;
    @AppApplication.Manager
    private RecordManager recordManager;
    //患者信息
    private PatientInfo mPatientInfo;
    //预约信息
    private AppointmentInfo mAppointInfo;
    //预约Id
    private int mAppointInfoId;
    //材料信息（服务器返回）
    private List<AuxiliaryMaterialInfo> mAuxiliaryMaterialInfos;
    //审核未通过材料列表
    private List<SingleFileInfo> mNotPassMaterials = new ArrayList<>();
    //审核通过材料列表
    private List<SingleFileInfo> mPassMaterials = new ArrayList<>();
    //是否影像会诊
    private boolean mIsImagingConsult = false;
    private ProgressDialog mProgressDialog;
    private Animation animPushUpIn, animPushUpOut, animShadowAlphaIn, animShadowAlphaOut;
    private boolean isShow = false;
    private DataManagerAdapter mPassListAdapter;
    private DataManagerAdapter mNotPassListAdapter;
    private AppointmentRelateAdapter appointmentRelateAdapter;
    private boolean mFromClinic;
    private int mDataTotal = 0;
    private boolean isShowSecondText;

    @Override
    protected void initDatas() {
        isShowSecondText = getIntent().getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SHOW_SECOND_TEXT, true);
        mPatientInfo = (PatientInfo) getIntent().getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATIENT_INFO);
        mAppointInfo = (AppointmentInfo) getIntent().getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_INFO);
        if (mAppointInfo != null) {
            mAppointInfoId = mAppointInfo.getBaseInfo().getAppointmentId();
        } else {
            mAppointInfoId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, 0);
        }
        mFromClinic = getIntent().getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_FROM_CLINIC, false);
        initAnim();
        initAdapter();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_consult_data_manage;
    }

    @Override
    protected void initViews() {
        if (!isShowSecondText) {
            mDataManageTitleBar.removeFunction(AppActionBar.FUNCTION_RIGHT_SECOND_TEXT);
        }
        mPassListRv.setLayoutManager(RecyclerViewUtils.getGridLayoutManager(this, 4));
        mPassListRv.setAdapter(mPassListAdapter);
        mPassListRv.setNestedScrollingEnabled(false);
        mRelatedRecordRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getThisActivity()));
        mRelatedRecordRv.setAdapter(appointmentRelateAdapter);
        initListener();
        fillAppointmentInfo(mAppointInfo, mAppointInfoId);
        fillPatientInfo(mAppointInfoId, mPatientInfo);
    }

    @Override
    protected void onStart() {
        super.onStart();
        reqMaterialList(mAppointInfoId);
        reqRelatedRecord(mAppointInfoId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_RECORD:
                if (resultCode == RESULT_OK) {
                    reqRelatedRecord(mAppointInfoId);
                }
                break;

            case REQUEST_CODE_HIDE_CONTROL:

                break;

            case REQUEST_CODE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    ArrayList<String> capturePhotoList = data.getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATHS);
                    startUpload(capturePhotoList);
                }
                break;

            case REQUEST_CODE_ALBUM:
                if (resultCode == RESULT_OK) {
                    ArrayList<String> albumPhotoList = data.getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SELECT_PATHS);
                    startUpload(albumPhotoList);
                }
                break;
            default:
                break;
        }
    }

    private void startUpload(ArrayList<String> photoList) {
        Logger.logI(Logger.COMMON, TAG + "-->startUpload-->photoList:" + photoList);
        if (mFromClinic) {
            mMaterialTaskManager.startUpload(mAppointInfoId, photoList, false);
            finish();
        } else {
            Intent intent = new Intent(this, UploadPictureActivity.class);
            intent.putStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATHS, photoList);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, mAppointInfoId);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        mMediaDownloadManager.unRegLoadListener(this);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isShow) {
                dismissUpWin();
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({
            R.id.activity_data_manage_mask_view,
            R.id.activity_data_manage_remark_layout_fl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_data_manage_mask_view:
                if (isShow) {
                    dismissUpWin();
                }
                break;

            case R.id.activity_data_manage_remark_layout_fl:
                //什么也不干，只为拦截点击和滑动事件
                break;
            default:
                break;

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
                Intent intent = new Intent(ConsultDataManageActivity.this, PickPhotoActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, mAppointInfoId);
                startActivityForResult(intent, REQUEST_CODE_ALBUM);
            }
        });
        photoChooseDialog.show(getSupportFragmentManager(), "");
    }

    private void initListener() {
        mRelatedRecordTitleTv.setOnClickListener(v -> {
            if (null == mAppointInfo) {
                return;
            }
            ArrayList<Integer> infoHashMap = new ArrayList<>();
            for (AppointmentItemForRelateInfo info : appointmentRelateAdapter.getData()) {
                infoHashMap.add(info.getRelationId());
            }
            getDisplay().startSelectRelateRecordActivity(mAppointInfoId, mAppointInfo.getBaseInfo().getUserId(), infoHashMap, REQUEST_CODE_RECORD);
        });
        mDataManageTitleBar.setLeftOnClickListener(v -> {
            onBackPressed();
        });
        mGroupRg.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.activity_data_manage_auxiliary_examination_rb:
                    if (!mIsImagingConsult) {
                        mAuxiliaryExaminationLayoutLl.setVisibility(View.VISIBLE);
                    } else {
                        mImagingExaminationLl.setVisibility(View.VISIBLE);
                    }
                    mRelateRecordLayoutLl.setVisibility(View.GONE);
                    break;
                case R.id.activity_data_manage_relate_record_rb:
                    if (!mIsImagingConsult) {
                        mAuxiliaryExaminationLayoutLl.setVisibility(View.GONE);
                    } else {
                        mImagingExaminationLl.setVisibility(View.GONE);
                    }
                    mRelateRecordLayoutLl.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        });
        mMediaDownloadManager.regLoadListener(this);
    }

    private void initAnim() {
        animPushUpIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_up_in);
        animPushUpOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_up_out);
        animShadowAlphaIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shadow_alpha_in);
        animShadowAlphaOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shadow_alpha_out);
    }


    private void initAdapter() {
        mNotPassListAdapter = new DataManagerAdapter(R.layout.item_data_manage_photo, mNotPassMaterials);
        mPassListAdapter = new DataManagerAdapter(R.layout.item_data_manage_photo, mPassMaterials);
        mNotPassListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            SingleFileInfo singleFileInfo = (SingleFileInfo) adapter.getItem(position);
            if (null != singleFileInfo) {
                if (view.getId() == R.id.item_data_manage_photo_delete_iv) {
                    deleteMaterial(singleFileInfo, position, false);
                }
            }
        });

        mNotPassListAdapter.setOnItemClickListener((adapter, view, position) -> {
            SingleFileInfo info = (SingleFileInfo) adapter.getItem(position);
            if (null != info) {
                handleDataItemClick(info, position, false);
            }
        });
        mNotPassListAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            mNotPassListAdapter.toggleDelete(position);
            return false;
        });

        mPassListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            SingleFileInfo singleFileInfo = (SingleFileInfo) adapter.getItem(position);
            if (null != singleFileInfo) {
                if (view.getId() == R.id.item_data_manage_photo_delete_iv) {
                    deleteMaterial(singleFileInfo, position, true);
                }
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
        appointmentRelateAdapter = new AppointmentRelateAdapter(R.layout.item_consult_relate_consult, new ArrayList<>());
        appointmentRelateAdapter.setOnItemClickListener((adapter, view, position) -> {
            AppointmentItemForRelateInfo info = (AppointmentItemForRelateInfo) adapter.getItem(position);
            if (null != info) {
                Intent intent = new Intent(getThisActivity(), PatientInformationActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, info.getRelationId());
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_RELATE_RECORD, true);
                startActivity(intent);
            }
        });
    }

    private void fillAppointmentInfo(AppointmentInfo appointInfo, int appointInfoId) {
        if (appointInfo == null) {
            reqAppointInfo(appointInfoId);
            return;
        }
        if (appointInfo.getExtendsInfo() != null) {
            mIsImagingConsult = appointInfo.getExtendsInfo().getScheduingType() == AppConstant.SchedulingType.SCHEDULING_TYPE_IMAGE_CONSULT;
            if (mIsImagingConsult) {
                mAuxiliaryExaminationLayoutLl.setVisibility(View.GONE);
                mImagingExaminationLl.setVisibility(View.VISIBLE);
            }
        }
        //老预约可能没有额外信息
        //getStatisticsData(appointInfo);
    }

    private void reqAppointInfo(int appointInfoId) {
        final ProgressDialog progressDialog = createProgressDialog(getString(R.string.loading_data));
        mConsultManager.getAppointmentInfo(appointInfoId, (baseResult, appointmentInfo) -> {
            progressDialog.cancel();
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS && appointmentInfo != null) {
                mAppointInfo = appointmentInfo;
                fillAppointmentInfo(appointmentInfo, appointInfoId);
            }
        });
    }

    private void fillPatientInfo(int appointInfoId, PatientInfo patientInfo) {
        if (patientInfo == null) {
            reqPatientInfo(appointInfoId);
            return;
        }
        String gender = getString(patientInfo.getPatientBaseInfo().getGender() == 0 ? R.string.gender_male : R.string.gender_female);
        mPatientTv.setText(patientInfo.getPatientBaseInfo().getRealName() + "\t\t" + gender + "\t" + patientInfo.getPatientBaseInfo().getAge());
        mNumberTv.setText(patientInfo.getPatientBaseInfo().getAppointmentId() + "");
    }

    private void reqPatientInfo(int appointInfoId) {
        mConsultManager.getPatientInfo(appointInfoId, new ConsultManager.OnPatientInfoLoadListener() {
            @Override
            public void onSuccess(PatientInfo patientInfo) {
                if (patientInfo != null) {
                    mPatientInfo = patientInfo;
                    fillPatientInfo(appointInfoId, patientInfo);
                }
            }

            @Override
            public void onFailed(int code, String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    /**
     * 请求材料列表
     */
    private void reqMaterialList(int appointInfoId) {
        showProgressDialog();
        AuxiliaryMaterialRequester requester = new AuxiliaryMaterialRequester(new DefaultResultCallback<AuxiliaryMaterialResult>() {
            @Override
            public void onSuccess(AuxiliaryMaterialResult auxiliaryMaterialResult, BaseResult baseResult) {
                if (null == auxiliaryMaterialResult) {
                    return;
                }
                if (mIsImagingConsult) {
                    dealImagingMaterail(auxiliaryMaterialResult.getAuxiliaryMaterialInfos());
                } else {
                    mAuxiliaryMaterialInfos = auxiliaryMaterialResult.getAuxiliaryMaterialInfos();
                    mDataTotal = LibCollections.size(mAuxiliaryMaterialInfos);

                    extractMaterial(appointInfoId, mAuxiliaryMaterialInfos);
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
        requester.appointmentId = appointInfoId;
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
    private void extractMaterial(int appointInfoId, List<AuxiliaryMaterialInfo> auxiliaryMaterialInfos) {
        if (auxiliaryMaterialInfos == null) {
            return;
        }

        final List<AuxiliaryMaterialInfo> passMaterialInfos = new ArrayList<>();
        final List<AuxiliaryMaterialInfo> unPassMaterialInfos = new ArrayList<>();
        for (AuxiliaryMaterialInfo auxiliaryMaterialInfo : auxiliaryMaterialInfos) {
            if (auxiliaryMaterialInfo.getCheckState() == AppConstant.MaterialCheckState.STATE_CHECK_SUCCESS) {
                passMaterialInfos.add(auxiliaryMaterialInfo);
            } else {
                unPassMaterialInfos.add(auxiliaryMaterialInfo);
            }
        }
        mDataTotal = LibCollections.size(passMaterialInfos);
        mUploadDataNumber.setText(getString(R.string.data_queue_upload_num, mDataTotal));
        mMediaDownloadManager.getDownloadInfosFromDB(appointInfoId, mediaDownloadInfos -> {
            List<SingleFileInfo> pass = getFileInfos(passMaterialInfos, mediaDownloadInfos);
            mPassListAdapter.setNewData(pass);
            if (LibCollections.isNotEmpty(pass)) {
                mPassListRv.setVisibility(View.VISIBLE);
            }
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
                if (FileUtil.isFileExist(mMediaDownloadManager.getFilePath(singleFileInfo.getLocalFileName(), mAppointInfoId))) {
                    singleFileInfo.setMediaDownloadInfo(new MediaDownloadInfo(mAppointInfoId, DownloadState.DOWNLOAD_SUCCESS, singleFileInfo.getLocalFileName()));
                } else {
                    MediaDownloadInfo mediaDownloadInfo = mediaDownloadInfoMap.get(singleFileInfo.getLocalFileName());
                    if (mediaDownloadInfo != null) {
                        if (!FileUtil.isFileExist(mMediaDownloadManager.getTempPath(singleFileInfo.getLocalFileName(), mAppointInfoId))) {
                            mediaDownloadInfo.setState(mMediaDownloadManager.isDownLoading(singleFileInfo.getLocalFileName())
                                    ? DownloadState.DOWNLOADING : DownloadState.NOT_DOWNLOAD);
                        } else {
                            if (mediaDownloadInfo.getState() == DownloadState.DOWNLOADING && !mMediaDownloadManager.isDownLoading(singleFileInfo.getLocalFileName())) {
                                mediaDownloadInfo.setState(DownloadState.DOWNLOAD_PAUSE);
                            }
                        }
                    } else {
                        mediaDownloadInfo = new MediaDownloadInfo(mAppointInfoId,
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
    private void deleteMaterial(final SingleFileInfo singleFileInfo, int position, final boolean isPass) {
        final ProgressDialog progressDialog = createProgressDialog(getString(R.string.consult_deleting_voice_file));
        DeleteMaterialRequest request = new DeleteMaterialRequest((baseResult, s) -> {
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                mDataTotal--;
                mUploadDataNumber.setText(getString(R.string.data_queue_upload_num, mDataTotal));
                if (isPass) {
                    mPassListAdapter.remove(position);
                    if (mPassListAdapter.getItemCount() <= 0) {
                        mPassListRv.setVisibility(View.GONE);
                    } else {
                        mPassListRv.setVisibility(View.VISIBLE);
                    }
                } else {

                }
                ToastUtils.showShort(R.string.data_delete_success);
            } else {
                ToastUtils.showShort(R.string.data_delete_fail);
            }
            progressDialog.cancel();
        });
        request.appointmentId = mAppointInfoId;
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
                        default:
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
                startPicBrowse(mPassListAdapter.getData(), position, isPass);
            } else {
                startPicBrowse(mNotPassListAdapter.getData(), position, isPass);
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
        Intent intent = new Intent(getThisActivity(), PicBrowseActivity.class);
        BrowserPicEvent browserPicEvent = new BrowserPicEvent();
        browserPicEvent.setServerFilePaths(servicePaths);
        browserPicEvent.setIndex(finalIndex);
        browserPicEvent.setAppointInfoId(mAppointInfoId);
        browserPicEvent.setPass(isPass);
        browserPicEvent.setSingleFileInfos(newSingleFileInfo);
        browserPicEvent.setAuxiliaryMaterialInfos(mAuxiliaryMaterialInfos);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_BROWSER_INFO, browserPicEvent);
        startActivityForResult(intent, REQUEST_CODE_HIDE_CONTROL);
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
        if (LibCollections.isEmpty(auxiliaryMaterialInfos)) {
            mNoDicomTv.setVisibility(View.VISIBLE);
            mDicomVp.setVisibility(View.GONE);
        } else {
            mDicomStateIv.getTopIconIv().setImageResource(R.drawable.ic_data_manage_completed);
            mDicomStateIv.getBottomTextTv().setText(R.string.data_manager_dicom_complete);
            List<List<AuxiliaryMaterialInfo>> groupList = groupData(auxiliaryMaterialInfos);
            mDicomVp.setAdapter(new DicomPageAdapter(getThisActivity(), groupList));
        }
    }

    /**
     * 取消备注
     */
    private void dismissUpWin() {
        isShow = false;
        mMaskView.setVisibility(View.GONE);
        mMaskView.startAnimation(animShadowAlphaOut);
        mRemarkLayoutFl.setVisibility(View.GONE);
        mRemarkLayoutFl.startAnimation(animPushUpOut);
    }

    /**
     * 请求已经关联的病历
     */
    private void reqRelatedRecord(int appointInfoId) {
        recordManager.getRelateRecords(appointInfoId, new OnResultListener<List<AppointmentItemForRelateInfo>>() {
            @Override
            public void onResult(BaseResult baseResult, List<AppointmentItemForRelateInfo> appointmentItemForRelateInfos) {
                if (baseResult.getCode() != OnResultListener.RESULT_SUCCESS) {
                    ToastUtils.showShort(R.string.no_network_connection);
                    return;
                }
                appointmentRelateAdapter.setNewData(appointmentItemForRelateInfos);
            }
        });
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
