package cn.longmaster.hospital.doctor.ui.rounds;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.common.FileUtil;
import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.img.BitmapUtil;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.Display;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.rounds.FirstJourneyInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.FirstJourneyPicInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.MedicalFileInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.RoundsMedicalDetailsInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.RoundsMedicalInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.WaitRoundsPatientInfo;
import cn.longmaster.hospital.doctor.core.manager.rounds.PatientManager;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.config.DoctorBaseRequester;
import cn.longmaster.hospital.doctor.core.requests.rounds.CorrelationVisitRequester;
import cn.longmaster.hospital.doctor.core.upload.simple.FirstJourneyUploader;
import cn.longmaster.hospital.doctor.core.requests.rounds.RoundsMedicalDetailsRequester;
import cn.longmaster.hospital.doctor.core.requests.rounds.UploadFirstJourneyRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.doctor.SearchActivity;
import cn.longmaster.hospital.doctor.ui.rounds.adapter.FirstCourseMedicalAdapter;
import cn.longmaster.hospital.doctor.util.DialogHelper;
import cn.longmaster.hospital.doctor.view.MyGridView;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.upload.OnNginxUploadStateCallback;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.PhoneUtil;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/7/3 16:28
 * @description: 新建患者
 */
public class RoundsPatientAddActivity extends NewBaseActivity {
    @FindViewById(R.id.iv_tool_bar_back)
    private ImageView ivToolBarBack;
    @FindViewById(R.id.tv_tool_bar_title)
    private TextView tvToolBarTitle;
    @FindViewById(R.id.tv_tool_bar_sub)
    private TextView tvToolBarSub;
    @FindViewById(R.id.activity_rounds_add_medical_highlight_ic)
    private CheckBox activityRoundsAddMedicalHighlightIc;
    @FindViewById(R.id.activity_rounds_add_medical_record_in_hospital_num_et)
    private EditText activityRoundsAddMedicalRecordInHospitalNumEt;
    @FindViewById(R.id.activity_rounds_add_medical_record_name_et)
    private EditText activityRoundsAddMedicalRecordNameEt;
    @FindViewById(R.id.activity_rounds_add_medical_record_rg)
    private RadioGroup activityRoundsAddMedicalRecordRg;
    @FindViewById(R.id.activity_rounds_add_medical_record_man_rb)
    private RadioButton activityRoundsAddMedicalRecordManRb;
    @FindViewById(R.id.activity_rounds_add_medical_record_women_rb)
    private RadioButton activityRoundsAddMedicalRecordWomenRb;
    @FindViewById(R.id.activity_rounds_add_medical_record_age_et)
    private EditText activityRoundsAddMedicalRecordAgeEt;
    @FindViewById(R.id.activity_rounds_add_medical_phone_et)
    private EditText activityRoundsAddMedicalPhoneEt;
    @FindViewById(R.id.activity_rounds_add_medical_record_doctor_tv)
    private TextView activityRoundsAddMedicalRecordDoctorTv;
    @FindViewById(R.id.activity_rounds_add_medical_diagnosis_et)
    private EditText activityRoundsAddMedicalDiagnosisEt;
    @FindViewById(R.id.activity_rounds_add_medical_record_patient_survey_et)
    private EditText activityRoundsAddMedicalRecordPatientSurveyEt;
    @FindViewById(R.id.activity_rounds_add_medical_mgv)
    private MyGridView activityRoundsAddMedicalMgv;
    @FindViewById(R.id.activity_rounds_add_del_tv)
    private TextView activityRoundsAddDelTv;
    @AppApplication.Manager
    UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    PatientManager mPatientManager;
    //选择首诊医生
    private final int REQUEST_CODE_SEARCH_DOCTOR = 100;
    //页面请求码:选择相片
    private final int REQUEST_CODE_PHOTO = 200;
    //页面请求码:选择相机
    private final int REQUEST_CODE_CAMERA = 201;
    private RoundsMedicalInfo mRoundsMedicalInfo;
    private FirstCourseMedicalAdapter mMedicalAdapter;
    private List<FirstJourneyPicInfo> mFirstJourneyPicList = new ArrayList<>();
    private String mUploadPhotoName;
    private int mMedicalId;
    private int mOrderId;
    //是否从查房预约信息填写触发
    private boolean isFromRounds;
    //添加修改弹框
    private ProgressDialog mProgressDialog;

    @Override
    protected void handleIntent(Intent intent, Display display) {
        mMedicalId = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_MEDICAL_ID, 0);
        mOrderId = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_ID, 0);
        isFromRounds = intent.getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_FROM_ROUNDS, false);
    }

    @Override
    protected void initDatas() {
        if (null == mRoundsMedicalInfo) {
            mRoundsMedicalInfo = new RoundsMedicalInfo();
        }
        mMedicalAdapter = new FirstCourseMedicalAdapter(this, mFirstJourneyPicList);
        mMedicalAdapter.setOnRetryUploadClickListener(position -> {
            mFirstJourneyPicList.get(position).setUpLoadState(AppConstant.UploadFirstJourneyState.UPLOAD_FIRST_JOURNEY_STATE_WAIT);
            mMedicalAdapter.notifyDataSetChanged();
            startUpload();
        });
        mMedicalAdapter.setOnDeletePicClickListener(position -> {
            mFirstJourneyPicList.remove(position);
            mMedicalAdapter.notifyDataSetChanged();
        });
        mMedicalAdapter.setOnPicItemClickListener(position -> {
            Logger.logD(Logger.APPOINTMENT, "FirstJourneyUploader->setOnItemClickListener->position()" + position);
            if (position < mFirstJourneyPicList.size()) {
                List<String> localFilePaths = new ArrayList<>();
                List<String> serverPicUrls = new ArrayList<>();
                for (FirstJourneyPicInfo info : mFirstJourneyPicList) {
                    localFilePaths.add(info.getPicPath());
                    serverPicUrls.add(AppConfig.getFirstJourneyUrl() + info.getServiceUrl());
                }
                Intent intent = new Intent(getThisActivity(), FirstJourneyPicBrowseActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_FIRST_JOURNEY_PIC_LIST, (Serializable) localFilePaths);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SERVER_URL, (Serializable) serverPicUrls);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_INDEX, position);
                startActivity(intent);
            } else {
                showPhotographModeDialog();
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_rounds_patient_add;
    }

    @Override
    protected void initViews() {
        tvToolBarSub.setVisibility(View.VISIBLE);
        activityRoundsAddMedicalMgv.setAdapter(mMedicalAdapter);
        initListener();
        if (isModPatient()) {
            tvToolBarSub.setText("保存");
            tvToolBarTitle.setText("编辑患者");
            getMedicalId(mMedicalId);
        } else {
            activityRoundsAddDelTv.setVisibility(View.GONE);
            tvToolBarSub.setText("提交");
            tvToolBarTitle.setText("添加患者");
        }
        getDoctorInfo(mUserInfoManager.getCurrentUserInfo().getUserId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SEARCH_DOCTOR:
                    int firstDoctorId = data.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SHARE_DOCTOR_ID, 0);
                    if (0 != firstDoctorId) {
                        getDoctorInfo(firstDoctorId);
                    }
                    break;
                case REQUEST_CODE_CAMERA:
                    if (null == mUploadPhotoName) {
                        return;
                    }
                    File file = new File(mUploadPhotoName);
                    if (!file.exists()) {
                        Logger.logD(Logger.APPOINTMENT, "RoundsPatientAddActivity->onActivityResult()file.exists()：" + file.exists());
                        return;
                    }
                    FirstJourneyPicInfo info = new FirstJourneyPicInfo();
                    info.setPicPath(mUploadPhotoName);
                    info.setUpLoadState(AppConstant.UploadFirstJourneyState.UPLOAD_FIRST_JOURNEY_STATE_WAIT);
                    mFirstJourneyPicList.add(info);
                    mMedicalAdapter.notifyDataSetChanged();
                    startUpload();
                    Logger.logD(Logger.APPOINTMENT, "RoundsPatientAddActivity->onActivityResult()通过相机拍摄的图片路径：" + mUploadPhotoName);
                    break;

                case REQUEST_CODE_PHOTO:
                    if (data != null) {
                        ArrayList<String> albumPhotoList = data.getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SELECT_PATHS);
                        Logger.logD(Logger.APPOINTMENT, "RoundsPatientAddActivity->onActivityResult()通过选择相册的图片路径：" + albumPhotoList);
                        for (String path : albumPhotoList) {
                            String newFilePath = SdManager.getInstance().getTempPath() + System.currentTimeMillis() + ".jpg";
                            FileUtil.copyFile(path, newFilePath);
                            if (FileUtil.isFileExist(newFilePath)) {
                                FirstJourneyPicInfo picInfo = new FirstJourneyPicInfo();
                                picInfo.setPicPath(newFilePath);
                                picInfo.setUpLoadState(AppConstant.UploadFirstJourneyState.UPLOAD_FIRST_JOURNEY_STATE_WAIT);
                                mFirstJourneyPicList.add(picInfo);
                                mMedicalAdapter.notifyDataSetChanged();
                                startUpload();
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void getDoctorInfo(int firstDoctorId) {
        mRoundsMedicalInfo.setAttdocId(firstDoctorId);
        DoctorBaseRequester requester = new DoctorBaseRequester((baseResult, doctorBaseInfo) -> {
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                if (null != doctorBaseInfo) {
                    mRoundsMedicalInfo.setHospitalId(doctorBaseInfo.getHospitalId());
                    activityRoundsAddMedicalRecordDoctorTv.setText(doctorBaseInfo.getRealName());
                }
            }
        });
        requester.doctorId = firstDoctorId;
        requester.token = "109";
        requester.doPost();
    }

    private void initListener() {
        ivToolBarBack.setOnClickListener(v -> onBackPressed());
        tvToolBarSub.setOnClickListener(v -> {
            JSONArray array = new JSONArray();
            for (FirstJourneyPicInfo info : mFirstJourneyPicList) {
                if (info.getUpLoadState() != AppConstant.UploadFirstJourneyState.UPLOAD_FIRST_JOURNEY_STATE_SUCCESS) {
                    ToastUtils.showShort("有图片上传中，请等待");
                    return;
                }
                array.put(info.getServiceUrl());
            }
            mRoundsMedicalInfo.setInHospitalNum(getString(activityRoundsAddMedicalRecordInHospitalNumEt));
            mRoundsMedicalInfo.setPatientName(getString(activityRoundsAddMedicalRecordNameEt));
            mRoundsMedicalInfo.setPatientNameAge(getString(activityRoundsAddMedicalRecordAgeEt));
            mRoundsMedicalInfo.setPhoneNumber(getString(activityRoundsAddMedicalPhoneEt));
            mRoundsMedicalInfo.setDiagnosis(getString(activityRoundsAddMedicalDiagnosisEt));
            mRoundsMedicalInfo.setPatientIllness(getString(activityRoundsAddMedicalRecordPatientSurveyEt));
            mRoundsMedicalInfo.setMedicalFileList(array);
            mRoundsMedicalInfo.setMedicalId(mMedicalId);
            mRoundsMedicalInfo.setOrderId(mOrderId);
            if (StringUtils.isContainChinese(mRoundsMedicalInfo.getInHospitalNum())) {
                ToastUtils.showShort("住院号只能填写数字和字母");
                return;
            }
            if (StringUtils.isTrimEmpty(mRoundsMedicalInfo.getPatientName())) {
                ToastUtils.showShort("请填写患者姓名");
                return;
            }
            if (mRoundsMedicalInfo.getPatientNameSex() == 0) {
                ToastUtils.showShort("请选择患者性别");
                return;
            }
            if (StringUtils.isTrimEmpty(mRoundsMedicalInfo.getPatientNameAge())) {
                ToastUtils.showShort("请填写患者年龄");
                return;
            }
            if (!StringUtils.isEmpty(mRoundsMedicalInfo.getPhoneNumber())) {
                if (!PhoneUtil.isPhoneNumber(mRoundsMedicalInfo.getPhoneNumber())) {
                    ToastUtils.showShort("弹框提示请输入正确的手机号");
                    return;
                }
            }
            if (mRoundsMedicalInfo.getAttdocId() == 0) {
                ToastUtils.showShort("请选择首诊医生");
                return;
            }
            if (StringUtils.isTrimEmpty(mRoundsMedicalInfo.getDiagnosis())) {
                ToastUtils.showShort("请填写诊断疾病");
                return;
            }
            if (StringUtils.isTrimEmpty(mRoundsMedicalInfo.getPatientIllness())) {
                ToastUtils.showShort("请填写病情摘要");
                return;
            }

            commitPatient(mRoundsMedicalInfo);
            //checkInHospital(mRoundsMedicalInfo);
        });
        activityRoundsAddMedicalRecordDoctorTv.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SEARCH_TYPE, AppConstant.SEARCH_TYPE_DOCTOR);
            intent.putExtra("add_medical_record", true);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SEARCH_HINT, getString(R.string.search_doctor_hint));
            startActivityForResult(intent, REQUEST_CODE_SEARCH_DOCTOR);
        });
        activityRoundsAddMedicalHighlightIc.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mRoundsMedicalInfo.setImportant(1);
            } else {
                mRoundsMedicalInfo.setImportant(0);
            }
        });
        activityRoundsAddMedicalRecordRg.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.activity_rounds_add_medical_record_man_rb:
                    mRoundsMedicalInfo.setPatientNameSex(1);
                    break;
                case R.id.activity_rounds_add_medical_record_women_rb:
                    mRoundsMedicalInfo.setPatientNameSex(2);
                    break;
                default:
                    break;
            }
        });
        activityRoundsAddDelTv.setOnClickListener(v -> {
            new CommonDialog.Builder(this)
                    .setMessage("此操作将删除该患者所有资料确定删除该患者?")
                    .setPositiveButton(R.string.cancel, () -> {

                    })
                    .setNegativeButton(R.string.confirm, () -> {
                        mPatientManager.deletePatient(mMedicalId + "", new PatientManager.OnPatientDeleteListener() {
                            @Override
                            public void onSuccess(String msg) {
                                ToastUtils.showShort(msg);
                                Intent intent = new Intent();
                                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATIENT_DELETE, true);
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            }

                            @Override
                            public void onFail(String msg) {
                                ToastUtils.showShort(msg);
                            }
                        });
                    })
                    .show();
        });
        activityRoundsAddMedicalDiagnosisEt.setOnTouchListener((view, event) -> {
            if (canVerticalScroll(activityRoundsAddMedicalDiagnosisEt)) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                }
            }
            return false;
        });
        activityRoundsAddMedicalRecordPatientSurveyEt.setOnTouchListener((view, event) -> {
            if (canVerticalScroll(activityRoundsAddMedicalRecordPatientSurveyEt)) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                }
            }
            return false;
        });
    }

    private void commitPatient(RoundsMedicalInfo roundsMedicalInfo) {
        showProgressDialog();
        mPatientManager.savePatient(roundsMedicalInfo, new PatientManager.OnPatientSaveListener() {
            @Override
            public void onSuccess(String msg, boolean isMod, WaitRoundsPatientInfo info) {
                ToastUtils.showShort(msg);
                if (isMod) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    if (roundsMedicalInfo.getOrderId() == 0) {
                        if (isFromRounds) {
                            finishByFromRounds(info);
                        } else {
                            setResult(RESULT_OK);
                            finish();
                        }
                    } else {
                        addPatientToOrder(roundsMedicalInfo.getOrderId(), roundsMedicalInfo.getMedicalId());
                    }
                }
            }

            @Override
            public void onFail(String msg, int code) {
                if (code == 102) {
                    showNoteDialog(msg);
                } else {
                    ToastUtils.showShort(msg);
                }
            }

            @Override
            public void onComplete() {
                dismissProgressDialog();
            }
        });
    }

    private void showNoteDialog(String msg) {
        new CommonDialog.Builder(getThisActivity())
                .setTitle("提示")
                .setMessage(msg)
                .setPosBtnTextColor(ContextCompat.getColorStateList(this, R.color.color_049eff))
                .setPositiveButton("我知道了", () -> activityRoundsAddMedicalRecordInHospitalNumEt.requestFocus()).show();
    }

    private void startUpload() {
        boolean isUploadIng = false;
        for (FirstJourneyPicInfo info : mFirstJourneyPicList) {
            if (info.getUpLoadState() == AppConstant.UploadFirstJourneyState.UPLOAD_FIRST_JOURNEY_STATE_ING) {
                isUploadIng = true;
                break;
            }
        }
        if (isUploadIng) {
            return;
        }
        for (int i = 0; i < mFirstJourneyPicList.size(); i++) {
            String path = mFirstJourneyPicList.get(i).getPicPath();
            if (mFirstJourneyPicList.get(i).getUpLoadState() == AppConstant.UploadFirstJourneyState.UPLOAD_FIRST_JOURNEY_STATE_WAIT) {
                String extraName = path.substring(path.lastIndexOf(".") + 1);
                Logger.logD(Logger.APPOINTMENT, "->startUpload()->文件后缀名:" + extraName);
                if (extraName.matches("[pP][nN][gG]")
                        || extraName.matches("[jJ][pP][eE][gG]")
                        || extraName.matches("[jJ][pP][gG]")) {
                    String newFilePath = path.replace(extraName, "jpg");
                    BitmapUtil.savePNG2JPEG(path, newFilePath);
                    BitmapUtil.compressImageFile(newFilePath);
                    path = newFilePath;
                }
                mFirstJourneyPicList.get(i).setUpLoadState(AppConstant.UploadFirstJourneyState.UPLOAD_FIRST_JOURNEY_STATE_ING);
                uploaderFirstJourney(path, i);
                break;
            }
        }
    }

    private void uploaderFirstJourney(final String photoFileName, final int position) {
        FirstJourneyUploader uploader = new FirstJourneyUploader(new OnNginxUploadStateCallback() {
            @Override
            public void onUploadProgresssChange(String s, final long l, long l1, final long l2) {
                Logger.logD(Logger.APPOINTMENT, "FirstJourneyUploader->onUploadProgresssChange->String()" + s + ",l:" + l + ",l1:" + l1 + ",l2:" + l2);
                AppHandlerProxy.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        float pressent = (float) l / l2 * 100;
                        mFirstJourneyPicList.get(position).setProgress(pressent);
                        mMedicalAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onUploadComplete(final String s, int i, final String s1) {
                Logger.logD(Logger.APPOINTMENT, "FirstJourneyUploader->onUploadComplete->String()" + s + ",s1:" + s1);
                AppHandlerProxy.runOnUIThread(() -> {
                    String serviceUrl = "";
                    try {
                        JSONObject object = new JSONObject(s1);
                        serviceUrl = object.getString("file_name");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (position == 0) {
                        if (!StringUtils.isEmpty(serviceUrl)) {
                            uploadFirstJourneyRequester(serviceUrl);
                        }
                    }

                    mFirstJourneyPicList.get(position).setUpLoadState(AppConstant.UploadFirstJourneyState.UPLOAD_FIRST_JOURNEY_STATE_SUCCESS);
                    if (!StringUtils.isEmpty(serviceUrl)) {
                        mFirstJourneyPicList.get(position).setServiceUrl(serviceUrl);
                    }
                    Logger.logD(Logger.APPOINTMENT, "FirstJourneyUploader->onUploadComplete->mFirstJourneyPicList:" + mFirstJourneyPicList);
                    mMedicalAdapter.notifyDataSetChanged();
                    startUpload();
                });
            }

            @Override
            public void onUploadCancle(String s) {
                Logger.logD(Logger.APPOINTMENT, "FirstJourneyUploader->onUploadCancle->String()" + s);
                AppHandlerProxy.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            @Override
            public void onUploadException(String s, Exception e) {
                Logger.logD(Logger.APPOINTMENT, "FirstJourneyUploader->onUploadException->String()" + s + " ,e:" + e);
                AppHandlerProxy.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        mFirstJourneyPicList.get(position).setUpLoadState(AppConstant.UploadFirstJourneyState.UPLOAD_FIRST_JOURNEY_STATE_FAIL);
                        mMedicalAdapter.notifyDataSetChanged();
                        startUpload();
                    }
                });
            }
        });
        uploader.filePath = photoFileName;
        uploader.ext = "jpg";
        uploader.actType = 1;
        uploader.startUpload();
    }

    private void uploadFirstJourneyRequester(String mServerUrl) {
        UploadFirstJourneyRequester requester = new UploadFirstJourneyRequester(new DefaultResultCallback<FirstJourneyInfo>() {
            @Override
            public void onSuccess(FirstJourneyInfo firstJourneyInfo, BaseResult baseResult) {
                Logger.logI(Logger.COMMON, "UploadFirstJourneyRequester-baseResult：" + baseResult + ",firstJourneyInfo:" + firstJourneyInfo);
                if (firstJourneyInfo != null) {
                    if (activityRoundsAddMedicalRecordNameEt.getText().length() == 0) {
                        activityRoundsAddMedicalRecordNameEt.setText(firstJourneyInfo.getPatientName());
                    }
                    if (activityRoundsAddMedicalRecordAgeEt.getText().length() == 0) {
                        activityRoundsAddMedicalRecordAgeEt.setText(firstJourneyInfo.getAge());
                    }
                    if (firstJourneyInfo.getGender().contains(getString(R.string.gender_female))) {
                        activityRoundsAddMedicalRecordWomenRb.setChecked(true);
                    } else if (firstJourneyInfo.getGender().contains(getString(R.string.gender_male))) {
                        activityRoundsAddMedicalRecordWomenRb.setChecked(true);
                    }
                }
            }
        });
        requester.fileName = mServerUrl;
        requester.doPost();
    }

    private void showPhotographModeDialog() {
        if (LibCollections.size(mFirstJourneyPicList) >= 9) {
            ToastUtils.showShort(getString(R.string.photo_picker_max_tips, 9));
            return;
        }
        mUploadPhotoName = SdManager.getInstance().getTempPath() + System.currentTimeMillis() + ".jpg";
        new DialogHelper.ChoosePicsBuilder()
                .setActivity(getThisActivity())
                .setRequestCodeAlbum(REQUEST_CODE_PHOTO)
                .setRequestCodeCamera(REQUEST_CODE_CAMERA)
                .setUploadPhotoName(mUploadPhotoName)
                .setCountPics(LibCollections.size(mFirstJourneyPicList))
                .setUploadFirstJoureny(true)
                .setMaxCount(9)
                .show();
    }

    private void getMedicalId(int medicalId) {
        RoundsMedicalDetailsRequester requester = new RoundsMedicalDetailsRequester(new DefaultResultCallback<RoundsMedicalDetailsInfo>() {
            @Override
            public void onSuccess(RoundsMedicalDetailsInfo roundsMedicalDetailsInfo, BaseResult baseResult) {
                populateMedicalInfo(roundsMedicalDetailsInfo);
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                ToastUtils.showShort(R.string.no_network_connection);
            }
        });
        requester.setMedicalId(medicalId);
        requester.doPost();
    }

    private void populateMedicalInfo(RoundsMedicalDetailsInfo roundsMedicalDetailsInfo) {
        activityRoundsAddMedicalHighlightIc.setChecked(roundsMedicalDetailsInfo.getImportant() == 1);
        //住院号
        activityRoundsAddMedicalRecordInHospitalNumEt.setText(roundsMedicalDetailsInfo.getHospitalizaId());
        activityRoundsAddMedicalRecordNameEt.setText(roundsMedicalDetailsInfo.getPatientName());
        if (roundsMedicalDetailsInfo.getGender() == 1) {
            activityRoundsAddMedicalRecordManRb.setChecked(true);
        } else {
            activityRoundsAddMedicalRecordWomenRb.setChecked(true);
        }
        activityRoundsAddMedicalRecordAgeEt.setText(roundsMedicalDetailsInfo.getAge());
        activityRoundsAddMedicalPhoneEt.setText(roundsMedicalDetailsInfo.getPhoneNum());
        getDoctorInfo(roundsMedicalDetailsInfo.getAttdocId());
        //医生名称
        //activityRoundsAddMedicalRecordDoctorTv.setText(roundsMedicalDetailsInfo.getAttdocId() + "医生ID");
        activityRoundsAddMedicalDiagnosisEt.setText(roundsMedicalDetailsInfo.getAttendingDisease());
        activityRoundsAddMedicalRecordPatientSurveyEt.setText(roundsMedicalDetailsInfo.getPatientIllness());
        if (LibCollections.isNotEmpty(roundsMedicalDetailsInfo.getMedicalFileList())) {
            for (MedicalFileInfo info : roundsMedicalDetailsInfo.getMedicalFileList()) {
                FirstJourneyPicInfo firstJourneyPicInfo = new FirstJourneyPicInfo();
                firstJourneyPicInfo.setServiceUrl(info.getFileName());
                firstJourneyPicInfo.setUpLoadState(AppConstant.UploadFirstJourneyState.UPLOAD_FIRST_JOURNEY_STATE_SUCCESS);
                mFirstJourneyPicList.add(firstJourneyPicInfo);
            }
        }
        if (roundsMedicalDetailsInfo.getOrderId() != 0) {
            activityRoundsAddDelTv.setVisibility(View.GONE);
        } else {
            activityRoundsAddDelTv.setVisibility(View.VISIBLE);
        }
        mMedicalAdapter.notifyDataSetChanged();
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = createProgressDialog(getString(R.string.loading));
        } else if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * 查房详情添加患者
     *
     * @param orderId
     * @param medicalId
     */
    private void addPatientToOrder(int orderId, int medicalId) {
        CorrelationVisitRequester requester = new CorrelationVisitRequester(new DefaultResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, BaseResult baseResult) {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                ToastUtils.showShort(R.string.no_network_connection);
            }
        });
        requester.actType = 0;
        requester.orderId = orderId;
        requester.medicalId = medicalId;
        requester.doPost();
    }

    private void finishByFromRounds(WaitRoundsPatientInfo info) {
        ArrayList<WaitRoundsPatientInfo> sparseArray = new ArrayList<>(1);
        sparseArray.add(info);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_WAIT_ROUNDS_PATIENT_INFO, sparseArray);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private boolean isModPatient() {
        return mMedicalId != 0;
    }

    private boolean isBindToOrder() {
        return mOrderId != 0;
    }

    /**
     * link {https://blog.csdn.net/z191726501/article/details/50701165 }
     * EditText竖直方向是否可以滚动
     *
     * @param editText 需要判断的EditText
     * @return true：可以滚动   false：不可以滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() - editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if (scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }
}
