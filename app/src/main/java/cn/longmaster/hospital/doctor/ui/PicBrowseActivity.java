package cn.longmaster.hospital.doctor.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.common.FileUtil;
import cn.longmaster.doctorlibrary.util.handler.MessageSender;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.hospital.doctor.Display;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.AuxiliaryMaterialInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.DiagnosisFileInfo;
import cn.longmaster.hospital.doctor.core.entity.event.BrowserPicEvent;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.appointment.DeleteConsultPicRequester;
import cn.longmaster.hospital.doctor.core.requests.appointment.DeleteMaterialRequest;
import cn.longmaster.hospital.doctor.core.requests.rounds.RoundsDeleteMaterialRequest;
import cn.longmaster.hospital.doctor.core.upload.MaterialTask;
import cn.longmaster.hospital.doctor.core.upload.SingleFileInfo;
import cn.longmaster.hospital.doctor.core.upload.newupload.MaterialTaskManager;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.consult.PickPhotoActivity;
import cn.longmaster.hospital.doctor.view.AppActionBar;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.hospital.doctor.view.dialog.PhotoWayDialog;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.PermissionConstants;
import cn.longmaster.utils.PermissionHelper;
import cn.longmaster.utils.ToastUtils;
import cn.longmaster.utils.Utils;
import io.reactivex.disposables.Disposable;

/**
 * 大图预览
 * Created by Tengshuxiang on 2016-08-17.
 * Mod by biao on 2019/7/19
 */
public class PicBrowseActivity extends NewBaseActivity {
    private final String TAG = PicBrowseActivity.class.getSimpleName();

    private final int REQUEST_CODE_PHOTO = 100;//页面请求码:选择相片
    private final int REQUEST_CODE_CAMERA = 101; //页面请求码:选择相机

    @FindViewById(R.id.activity_pic_browse_viewpager)
    private HackyViewPager mHackyViewPager;
    @FindViewById(R.id.activity_pic_browse_title_bar)
    private AppActionBar mAppActionBar;
    @FindViewById(R.id.activity_pic_browse_bottom_bar_ll)
    private LinearLayout mBottomBar;
    @FindViewById(R.id.activity_pic_browse_hospital_tv)
    private TextView mHospitalTv;
    @FindViewById(R.id.activity_pic_browse_time_tv)
    private TextView mTimeTv;
    @FindViewById(R.id.activity_pic_browse_result_tv)
    private TextView mResultTv;
    @FindViewById(R.id.activity_pic_browse_info_layout_ll)
    private LinearLayout mInfoLayoutLl;
    @FindViewById(R.id.activity_pic_browse_fail_layout_ll)
    private LinearLayout mFailLayoutLl;
    @FindViewById(R.id.activity_pic_browse_fail_tv)
    private TextView mFailTv;
    @FindViewById(R.id.activity_pic_browse_upload_again_btn)
    private Button mUploadAgainBtn;

    @AppApplication.Manager
    private MaterialTaskManager mMaterialTaskManager;

    private PicBrowseNewAdapter mPicBrowseAdapter;
    private PicMaterialAdapter mPicMaterialAdapter;
    //private List<String> mLocalFilePaths = new ArrayList<>();
    private List<String> mServerUrls = new ArrayList<>();
    private int mIndex;
    //
    private List<SingleFileInfo> mSingleFileInfos;
    private List<AuxiliaryMaterialInfo> mAuxiliaryMaterialInfos = new ArrayList<>();
    //是否医嘱
    private boolean mIsDiagnosis;
    private boolean mIsRounds;
    private boolean mIsPass;
    private DiagnosisFileInfo mDiagnosisFileInfo;
    //是否是医助
    private boolean mIsAssistant;
    private PhotoWayDialog mPhotoWayDialog;//图片渠道对话框
    private String mPhotoFileName;//拍照返回图片路径
    private int mAppointInfoId;

    @Override
    protected void handleIntent(Intent intent, Display display) {
        super.handleIntent(intent, display);
        BrowserPicEvent browserPicEvent = intent.getParcelableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_BROWSER_INFO);
        if (null != browserPicEvent) {
            mIsAssistant = browserPicEvent.isAssistant();
            mServerUrls = browserPicEvent.getServerFilePaths();
            mIndex = browserPicEvent.getIndex();
            mAppointInfoId = browserPicEvent.getAppointInfoId();
            mSingleFileInfos = browserPicEvent.getSingleFileInfos();
            mAuxiliaryMaterialInfos = browserPicEvent.getAuxiliaryMaterialInfos();
            //是否是医嘱
            mIsDiagnosis = browserPicEvent.isDiagnosis();
            //
            mIsRounds = browserPicEvent.isRounds();
            mIsPass = browserPicEvent.isPass();
            if (mIsDiagnosis) {
                mDiagnosisFileInfo = browserPicEvent.getDiagnosisFileInfo();
            }
        } else {
            finish();
        }
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_pic_browse;
    }

    @Override
    protected void initViews() {
        if (mServerUrls == null) {
            Logger.logI(Logger.COMMON, "setAdapter()本地图片路径为空！！！");
            return;
        }
        if (mIsAssistant) {
            mAppActionBar.setTitleSize(TypedValue.COMPLEX_UNIT_PX, getThisActivity().getResources().getDimension(R.dimen.font_size_14));
            mAppActionBar.removeFunction(AppActionBar.FUNCTION_RIGHT_TEXT);
            mPicMaterialAdapter = new PicMaterialAdapter(this, mServerUrls);
            if (LibCollections.isNotEmpty(mAuxiliaryMaterialInfos)) {
                fillMaterialInfo(mAuxiliaryMaterialInfos.get(mIndex));
            }
            mHackyViewPager.setAdapter(mPicMaterialAdapter);
            mHackyViewPager.setCurrentItem(mIndex);
        } else {
            mPicBrowseAdapter = new PicBrowseNewAdapter(this, mServerUrls);
            mPicBrowseAdapter.setOnPhotoTapListener((view, x, y) -> toggleFullScreen());
            mHackyViewPager.setAdapter(mPicBrowseAdapter);
            mHackyViewPager.setCurrentItem(mIndex);
            if (LibCollections.isNotEmpty(mSingleFileInfos)) {
                if (mSingleFileInfos.get(mIndex).getCheckState() == AppConstant.MaterialCheckState.STATE_CHECK_SUCCESS || mSingleFileInfos.get(mIndex).getCheckState() == AppConstant.MaterialCheckState.STATE_CHECK_FAIL) {
                    fillMaterialCheckInfo(mSingleFileInfos.get(mIndex));
                } else {
                    clearMaterialCheckInfo();
                }
            } else {
                if (!mIsDiagnosis) {
                    mAppActionBar.removeFunction(AppActionBar.FUNCTION_RIGHT_TEXT);
                    mBottomBar.setVisibility(View.GONE);
                }
            }
        }
        initListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_PHOTO:
                if (resultCode == RESULT_OK && data != null) {
                    List<String> photoList = data.getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SELECT_PATHS);
                    reUpload(photoList);
                    delServerPic(mSingleFileInfos.get(mIndex), mIndex);
                }
                break;

            case REQUEST_CODE_CAMERA:
                if (resultCode == RESULT_OK) {
                    Logger.logI(Logger.COMMON, TAG + "->onActivityResult->拍摄照片结果（-1：成功）：" + RESULT_OK);
                    List<String> photoList = data.getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATHS);
                    reUpload(photoList);
                    delServerPic(mSingleFileInfos.get(mIndex), mIndex);
                }
                break;
        }
    }

    @OnClick({R.id.activity_pic_browse_upload_again_btn})
    public void onClick(View view) {
        if (view.getId() == R.id.activity_pic_browse_upload_again_btn) {
            showPhotoWayDialog();
        }
    }

    private void initListener() {
        mHackyViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mIsAssistant) {
                    checkToast(position);
                }
                mIndex = position;
                changePage();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mAppActionBar.setRightTextOnClickListener(v -> {
            Logger.logD(Logger.COMMON, TAG + "->onDeleteClick()->mIsDiagnosis:" + mIsDiagnosis);
            if (mIsDiagnosis) {
                delDiagnosisFile();
            } else {
       /* if (mSingleFileInfos.get(mIndex).getCheckState() == AppConstant.MaterialCheckState.STATE_CHECK_SUCCESS) {
            showUnableDeleteDialog(R.string.data_manage_dialog_delete_message);
        } else {
            showConfirmDeleteDialog();
        }*/
                showConfirmDeleteDialog();
            }
        });
        mAppActionBar.setLeftOnClickListener(v -> onBackPressed());
    }

    private void reUpload(List<String> photoList) {
        if (LibCollections.isNotEmpty(photoList)) {
            mMaterialTaskManager.startUpload(mAppointInfoId, photoList, false);
        }
    }

    /**
     * 开关全屏
     */
    private void toggleFullScreen() {
        if (mAppActionBar.getVisibility() == View.GONE) {
            mAppActionBar.setVisibility(View.VISIBLE);
            mBottomBar.setVisibility(View.VISIBLE);
        } else {
            mAppActionBar.setVisibility(View.GONE);
            mBottomBar.setVisibility(View.GONE);
        }
    }

    private void showUnableDeleteDialog(int resId) {
        new CommonDialog.Builder(getThisActivity())
                .setTitle(R.string.data_manage_dialog_delete_title)
                .setMessage(resId)
                .setNegativeButton(R.string.data_manage_dialog_delete_ok, () -> {

                })
                .show();
    }

    private void showConfirmDeleteDialog() {
        if (mSingleFileInfos.get(mIndex).getCheckState() == AppConstant.MaterialCheckState.STATE_CHECK_SUCCESS) {
            new CommonDialog.Builder(getThisActivity())
                    .setTitle("此图片不可删除")
                    .setMessage("以通过审核的图片不可删除")
                    .setNegativeButton(R.string.confirm, () -> {

                    })
                    .show();
            return;
        }
        new CommonDialog.Builder(getThisActivity())
                .setMessage(R.string.is_delete_diagnosis_pic)
                .setPositiveButton(R.string.cancel, () -> {
                })
                .setNegativeButton(R.string.confirm, () -> deleteMaterial(mSingleFileInfos.get(mIndex), mIndex))
                .show();
    }

    /**
     * 删除医嘱图片
     */
    private void delDiagnosisFile() {
        CommonDialog.Builder builder = new CommonDialog.Builder(getThisActivity());
        builder.setMessage(R.string.is_delete_diagnosis_pic)
                .setPositiveButton(R.string.cancel, () -> {

                })
                .setNegativeButton(R.string.cancel, () -> {

                })
                .setNegativeButton(R.string.confirm, () -> {
                    final ProgressDialog progressDialog = createProgressDialog(getString(R.string.consult_deleting_voice_file));
                    if (mDiagnosisFileInfo.isLocalData()) {
                        progressDialog.cancel();
                        Intent intent = new Intent();
                        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DIAGNOSIS_INFO, mDiagnosisFileInfo);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        DeleteConsultPicRequester requester = new DeleteConsultPicRequester((baseResult, aVoid) -> {
                            progressDialog.cancel();
                            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                                Intent intent = new Intent();
                                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DIAGNOSIS_INFO, mDiagnosisFileInfo);
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                ToastUtils.showShort(R.string.consult_del_voice_file_field);
                            }
                        });
                        requester.appointmentId = mDiagnosisFileInfo.getAppointmentId();
                        requester.diagnosisPicture = mDiagnosisFileInfo.getDiagnosisPicture();
                        requester.doPost();
                    }
                })
                .show();
    }

    private void changePage() {
        if (LibCollections.size(mAuxiliaryMaterialInfos) < mIndex) {
            return;
        }
        if (mIsAssistant) {
            fillMaterialInfo(mAuxiliaryMaterialInfos.get(mIndex));
        } else {
            if (LibCollections.isNotEmpty(mSingleFileInfos)) {
                if (mSingleFileInfos.get(mIndex).getCheckState() == AppConstant.MaterialCheckState.STATE_CHECK_SUCCESS ||
                        mSingleFileInfos.get(mIndex).getCheckState() == AppConstant.MaterialCheckState.STATE_CHECK_FAIL) {
                    fillMaterialCheckInfo(mSingleFileInfos.get(mIndex));
                } else {
                    clearMaterialCheckInfo();
                }
            }
        }
    }

    private void checkToast(int position) {
        int size = LibCollections.size(mAuxiliaryMaterialInfos);
        if (position >= size) {
            return;
        }
        if (position > mIndex) {
            if (position == size - 1) {
                ToastUtils.showShort(R.string.pic_browse_last_pic_tips_none);
            } else if (!mAuxiliaryMaterialInfos.get(position).getMaterialCfgName().equals(mAuxiliaryMaterialInfos.get(position + 1).getMaterialCfgName())) {
                ToastUtils.showShort(R.string.pic_browse_last_pic_tips_next);
            }
        } else {
            if (position == 0) {
                ToastUtils.showShort(R.string.pic_browse_first_pic_tips_none);
            } else if (!mAuxiliaryMaterialInfos.get(position).getMaterialCfgName().equals(mAuxiliaryMaterialInfos.get(position - 1).getMaterialCfgName())) {
                ToastUtils.showShort(R.string.pic_browse_first_pic_tips_next);
            }
        }
    }

    /**
     * 填充材料审核信息
     */
    private void fillMaterialCheckInfo(SingleFileInfo singleFileInfo) {
        if (singleFileInfo != null) {
            if (singleFileInfo.getCheckState() == AppConstant.MaterialCheckState.STATE_CHECK_FAIL) {
                mInfoLayoutLl.setVisibility(View.GONE);
                mFailLayoutLl.setVisibility(View.VISIBLE);
                mFailTv.setText(getString(R.string.pic_browse_fail_check_result, singleFileInfo.getAuditDesc()));
                return;
            }
            mInfoLayoutLl.setVisibility(View.VISIBLE);
            mFailLayoutLl.setVisibility(View.GONE);
            mAppActionBar.setTitle(singleFileInfo.getMaterialName());
            mHospitalTv.setText(getString(R.string.pic_browse_check_hospital, singleFileInfo.getMaterialHosp()));
            if (!TextUtils.isEmpty(singleFileInfo.getMaterialDt())) {
                mTimeTv.setText(getString(R.string.pic_browse_check_time, singleFileInfo.getMaterialDt()));
            } else {
                mTimeTv.setText(getString(R.string.pic_browse_check_time, ""));
            }
            mResultTv.setText(getString(R.string.pic_browse_check_result, singleFileInfo.getMaterialResult()));
        }
    }

    /**
     * 清除材料信息
     */
    private void clearMaterialCheckInfo() {
        mAppActionBar.setTitle("");
        mHospitalTv.setText(getString(R.string.pic_browse_check_hospital, ""));
        mTimeTv.setText(getString(R.string.pic_browse_check_time, ""));
        mResultTv.setText(getString(R.string.pic_browse_check_result, ""));
        mInfoLayoutLl.setVisibility(View.GONE);
        mFailLayoutLl.setVisibility(View.GONE);
    }

    /**
     * 填充材料信息
     */
    private void fillMaterialInfo(AuxiliaryMaterialInfo auxiliaryMaterialInfo) {
        if (auxiliaryMaterialInfo != null) {
            mAppActionBar.setTitle(auxiliaryMaterialInfo.getMaterialCfgName() + "\n" + "(" + (mIndex + 1) + "/" + LibCollections.size(mAuxiliaryMaterialInfos) + ")");
            mHospitalTv.setText(getString(R.string.pic_browse_check_hospital, auxiliaryMaterialInfo.getMaterialHosp()));
            if (!TextUtils.isEmpty(auxiliaryMaterialInfo.getMaterialDt())) {
                mTimeTv.setText(getString(R.string.pic_browse_check_time, auxiliaryMaterialInfo.getMaterialDt()));
            } else {
                mTimeTv.setText(getString(R.string.pic_browse_check_time, ""));
            }
            mResultTv.setText(getString(R.string.pic_browse_check_result, auxiliaryMaterialInfo.getMaterialResult()));
        }
    }

    /**
     * 显示相片渠道dialog
     */
    private void showPhotoWayDialog() {
        if (mPhotoWayDialog == null) {
            mPhotoWayDialog = new PhotoWayDialog(this);
            mPhotoWayDialog.show();
            //设置显示位置以及大小
            Window win = mPhotoWayDialog.getWindow();
            WindowManager.LayoutParams lp = win.getAttributes();
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            lp.width = (int) (dm.widthPixels * 0.94);
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.BOTTOM;
            win.setAttributes(lp);
            //设置监听
            mPhotoWayDialog.setOnWayClickListener(new PhotoWayDialog.OnWayClickListener() {
                @Override
                public void onAlbumClick() {
                    Intent intent = new Intent(PicBrowseActivity.this, PickPhotoActivity.class);
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MAX_COUNT, 1);//单选
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, mAppointInfoId);//单选
                    startActivityForResult(intent, REQUEST_CODE_PHOTO);
                }

                @Override
                public void onCameraClick() {
                    Disposable disposable = PermissionHelper.init(getThisActivity()).addPermissionGroups(PermissionConstants.CAMERA)
                            .requestEachCombined()
                            .subscribe(permission -> {
                                if (permission.granted) {
                                    CaptureActivity.startActivity(PicBrowseActivity.this, true, REQUEST_CODE_CAMERA);
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
            });
        } else {
            mPhotoWayDialog.show();
        }
    }

    /**
     * 删除材料
     */
    private void deleteMaterial(final SingleFileInfo singleFileInfo, final int index) {
        if (mIsRounds) {
            deleteRoundsMaterial(singleFileInfo, index);
        } else {
            deleteConsultationMaterial(singleFileInfo, index);
        }
    }

    /**
     * 删除查房材料
     */
    private void deleteRoundsMaterial(final SingleFileInfo singleFileInfo, final int index) {
        final ProgressDialog progressDialog = createProgressDialog(getString(R.string.consult_deleting_voice_file));
        RoundsDeleteMaterialRequest request = new RoundsDeleteMaterialRequest((baseResult, s) -> {
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                onMaterialDelete(singleFileInfo, index);
            } else {
                ToastUtils.showShort(R.string.data_delete_fail);
            }
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
        });
        request.medicalId = mAppointInfoId;
        request.materialId = singleFileInfo.getMaterialId();
        request.materialPic = singleFileInfo.getServerFileName();
        request.doPost();
    }

    /**
     * 删除会诊资料
     *
     * @param singleFileInfo
     * @param index
     */
    private void deleteConsultationMaterial(final SingleFileInfo singleFileInfo, final int index) {
        final ProgressDialog progressDialog = createProgressDialog(getString(R.string.consult_deleting_voice_file));
        DeleteMaterialRequest request = new DeleteMaterialRequest((baseResult, s) -> {
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                onMaterialDelete(singleFileInfo, index);
            } else {
                ToastUtils.showShort(R.string.data_delete_fail);
            }
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
        });
        request.appointmentId = singleFileInfo.getAppointmentId();
        request.materialId = singleFileInfo.getMaterialId();
        request.materialPic = singleFileInfo.getServerFileName();
        request.doPost();
    }


    private void onMaterialDelete(SingleFileInfo singleFileInfo, int index) {
        ToastUtils.showShort(R.string.data_delete_success);
        FileUtil.deleteFile(singleFileInfo.getLocalFilePath());
        MaterialTask.delTask(singleFileInfo.getLocalFileName());

        if (LibCollections.size(mAuxiliaryMaterialInfos) >= index) {
            mAuxiliaryMaterialInfos.remove(index);
        }
        if (LibCollections.size(mSingleFileInfos) >= index) {
            mSingleFileInfos.remove(index);
        }
        if (LibCollections.size(mServerUrls) >= index) {
            mServerUrls.remove(index);
        }
        sendDeleteMessage(index);
        if (LibCollections.isEmpty(mAuxiliaryMaterialInfos)) {
            Intent i = new Intent();
            i.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_HIDE_CONTROL, true);
            setResult(RESULT_OK, i);
            finish();
        } else {
            mPicBrowseAdapter.notifyDataSetChanged();
            changePage();
        }
    }

    private void sendDeleteMessage(int index) {
        Message message = new Message();
        if (mIsPass) {
            message.what = AppConstant.CLIENT_MESSAGE_CODE_DELETE_PASS_MATERIAL;
        } else {
            message.what = AppConstant.CLIENT_MESSAGE_CODE_ON_DELETE_MATERIAL;
        }
        message.arg1 = index;
        MessageSender.sendMessage(message);
    }

    /**
     * 删除服务器图片
     */
    private void delServerPic(final SingleFileInfo singleFileInfo, final int index) {
        final ProgressDialog progressDialog = createProgressDialog(getString(R.string.consult_deleting_voice_file));
        DeleteMaterialRequest request = new DeleteMaterialRequest((baseResult, s) -> {
            progressDialog.cancel();
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                FileUtil.deleteFile(singleFileInfo.getLocalFilePath());
                MaterialTask.delTask(singleFileInfo.getLocalFileName());
                mAuxiliaryMaterialInfos.remove(index);
                //mServerUrls.remove(index);
                mSingleFileInfos.remove(index);
                sendDeleteMessage(index);
                if (LibCollections.isEmpty(mAuxiliaryMaterialInfos)) {
                    Intent i = new Intent();
                    i.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_HIDE_CONTROL, true);
                    setResult(RESULT_OK, i);
                    finish();
                } else {
                    mPicBrowseAdapter.notifyDataSetChanged();
                    changePage();
                }
            } else {
                ToastUtils.showShort(R.string.no_network_connection);
            }
        });
        request.appointmentId = singleFileInfo.getAppointmentId();
        request.materialId = singleFileInfo.getMaterialId();
        request.materialPic = singleFileInfo.getServerFileName();
        request.doPost();
    }
}
