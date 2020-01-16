package cn.longmaster.hospital.doctor.ui.dutyclinic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.util.AudioDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import cn.longmaster.doctorlibrary.util.common.CommonUtils;
import cn.longmaster.doctorlibrary.util.img.BitmapUtil;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.executor.AppExecutors;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyPatientDiseaseItemInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyPrognoteDataInfo;
import cn.longmaster.hospital.doctor.core.entity.event.BrowserPicEvent;
import cn.longmaster.hospital.doctor.core.manager.dutyclinic.DCManager;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.upload.simple.BaseFileUploader;
import cn.longmaster.hospital.doctor.core.upload.simple.DCDutyPatientDiseasePicUploader;
import cn.longmaster.hospital.doctor.core.upload.simple.DCDutyPatientDiseaseVoiceUploader;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.dutyclinic.adapter.DCDutyDataUploadPicAdapter;
import cn.longmaster.hospital.doctor.ui.dutyclinic.adapter.DCDutyDataUploadVoiceAdapter;
import cn.longmaster.hospital.doctor.ui.dutyclinic.dialog.DCDutyTreatTypeChooseDialog;
import cn.longmaster.hospital.doctor.ui.dutyclinic.dialog.DCDutyVisitPlanDetailsRemovePointDialog;
import cn.longmaster.hospital.doctor.util.DialogHelper;
import cn.longmaster.hospital.doctor.util.MaxLengthWatcher;
import cn.longmaster.hospital.doctor.view.AppActionBar;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.TimePickUtils;
import cn.longmaster.utils.TimeUtils;
import cn.longmaster.utils.ToastUtils;
import cn.longmaster.utils.Utils;

/**
 * @author ABiao_Abiao
 * @date 2019/12/17 21:12
 * @description: 医生上传的病例资料
 */
public class DCDutyPatientDiseaseUpLoadActivity extends NewBaseActivity {
    private final int REQUEST_CODE_FOR_SURVEY_ALBUM = 100;
    private final int REQUEST_CODE_FOR_SURVEY_CAMERA = 102;
    private final int REQUEST_CODE_FOR_CHECK_ALBUM = 104;
    private final int REQUEST_CODE_FOR_CHECK_CAMERA = 106;
    private final int REQUEST_CODE_FOR_PRESCRIPTION_ALBUM = 108;
    private final int REQUEST_CODE_FOR_PRESCRIPTION_CAMERA = 110;
    private final int TYPE_FOR_DIAGNOSIS = 1;
    private final int TYPE_FOR_SURVEY = 2;
    private final int TYPE_FOR_CHECK = 3;
    private final int TYPE_FOR_PRESCRIPTION = 4;

    @FindViewById(R.id.act_dc_duty_patient_data_up_load_aab)
    private AppActionBar actDcDutyPatientDataUpLoadAab;
    @FindViewById(R.id.act_dc_duty_patient_data_up_load_treat_time_ll)
    private LinearLayout actDcDutyPatientDataUpLoadTreatTimeLl;
    @FindViewById(R.id.act_dc_duty_patient_data_up_load_treat_time_tv)
    private TextView actDcDutyPatientDataUpLoadTreatTimeTv;
    @FindViewById(R.id.act_dc_duty_patient_data_up_load_treat_type_ll)
    private LinearLayout actDcDutyPatientDataUpLoadTreatTypeLl;
    @FindViewById(R.id.act_dc_duty_patient_data_up_load_treat_type_tv)
    private TextView actDcDutyPatientDataUpLoadTreatTypeTv;
    @FindViewById(R.id.act_dc_duty_patient_data_up_load_diagnosis_et)
    private EditText actDcDutyPatientDataUpLoadDiagnosisEt;
    @FindViewById(R.id.act_dc_duty_patient_data_up_load_survey_et)
    private EditText actDcDutyPatientDataUpLoadSurveyEt;
    @FindViewById(R.id.act_dc_duty_patient_data_up_load_survey_pic_rv)
    private RecyclerView actDcDutyPatientDataUpLoadSurveyPicRv;
    @FindViewById(R.id.act_dc_duty_patient_data_up_load_survey_voice_rv)
    private RecyclerView actDcDutyPatientDataUpLoadSurveyVoiceRv;
    @FindViewById(R.id.act_dc_duty_patient_data_up_load_survey_pic_add_iv)
    private ImageView actDcDutyPatientDataUpLoadSurveyPicAddIv;
    @FindViewById(R.id.act_dc_duty_patient_data_up_load_survey_voice_add_iv)
    private ImageView actDcDutyPatientDataUpLoadSurveyVoiceAddIv;
    @FindViewById(R.id.act_dc_duty_patient_data_up_load_check_et)
    private EditText actDcDutyPatientDataUpLoadCheckEt;
    @FindViewById(R.id.act_dc_duty_patient_data_up_load_check_pic_rv)
    private RecyclerView actDcDutyPatientDataUpLoadCheckPicRv;
    @FindViewById(R.id.act_dc_duty_patient_data_up_load_check_voice_rv)
    private RecyclerView actDcDutyPatientDataUpLoadCheckVoiceRv;
    @FindViewById(R.id.act_dc_duty_patient_data_up_load_check_pic_add_iv)
    private ImageView actDcDutyPatientDataUpLoadCheckPicAddIv;
    @FindViewById(R.id.act_dc_duty_patient_data_up_load_check_voice_add_iv)
    private ImageView actDcDutyPatientDataUpLoadCheckVoiceAddIv;
    @FindViewById(R.id.act_dc_duty_patient_data_up_load_prescription_et)
    private EditText actDcDutyPatientDataUpLoadPrescriptionEt;
    @FindViewById(R.id.act_dc_duty_patient_data_up_load_prescription_pic_rv)
    private RecyclerView actDcDutyPatientDataUpLoadPrescriptionPicRv;
    @FindViewById(R.id.act_dc_duty_patient_data_up_load_prescription_voice_rv)
    private RecyclerView actDcDutyPatientDataUpLoadPrescriptionVoiceRv;
    @FindViewById(R.id.act_dc_duty_patient_data_up_load_prescription_pic_add_iv)
    private ImageView actDcDutyPatientDataUpLoadPrescriptionPicAddIv;
    @FindViewById(R.id.act_dc_duty_patient_data_up_load_prescription_voice_add_iv)
    private ImageView actDcDutyPatientDataUpLoadPrescriptionVoiceAddIv;
    @FindViewById(R.id.act_dc_duty_patient_data_up_load_confirm_tv)
    private TextView actDcDutyPatientDataUpLoadConfirmTv;
    @FindViewById(R.id.act_dc_duty_patient_data_up_load_delete_tv)
    private TextView actDcDutyPatientDataUpLoadDeleteTv;
    @FindViewById(R.id.act_dc_duty_patient_data_up_load_operator_ll)
    private LinearLayout actDcDutyPatientDataUpLoadOperatorLl;
    @FindViewById(R.id.act_dc_duty_patient_data_up_load_operator_tv)
    private TextView actDcDutyPatientDataUpLoadOperatorTv;
    private DCDutyDataUploadPicAdapter dcDutyDataUploadSurveyPicAdapter;
    private DCDutyDataUploadPicAdapter dcDutyDataUploadCheckPicAdapter;
    private DCDutyDataUploadPicAdapter dcDutyDataUploadPrescriptionPicAdapter;
    private DCDutyDataUploadVoiceAdapter dcDutyDataUploadSurveyVoiceAdapter;
    private DCDutyDataUploadVoiceAdapter dcDutyDataUploadCheckVoiceAdapter;
    private DCDutyDataUploadVoiceAdapter dcDutyDataUploadPrescriptionVoiceAdapter;
    @AppApplication.Manager
    DCManager dcManager;
    private int mMedicalId;
    /**
     * 病程id
     */
    private int mDiseaseId;
    private String mRecordTime;
    private int mRecordType;

    private String mUploadPhotoName;
    private String checkString;
    private String surveyString;
    private String prescriptionString;
    private String deletedPic = "是否删除该图片";
    private String deletedVoice = "是否删除该语音";
    private String deletedMedicalData = "是否删除当前病程资料";
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<>();
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private String resultType = "json";
    private AudioDetector audioDetector;
    private String audioName = TimeUtils.getNowMills() + "";
    private long audioDuration;// 单位毫秒
    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Logger.logD(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                ToastUtils.showShort("初始化失败，错误码：" + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            }
        }
    };

    @Override
    protected void initDatas() {
        initSpeech();
        mMedicalId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DC_DUTY_MEDICAL_ID, 0);
        mDiseaseId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DC_DUTY_DISEASE_ID, 0);
        dcDutyDataUploadSurveyPicAdapter = new DCDutyDataUploadPicAdapter(R.layout.item_dc_duty_data_up_load_pic, new ArrayList<>(0));
        dcDutyDataUploadSurveyPicAdapter.setMod(mDiseaseId != 0);
        dcDutyDataUploadSurveyPicAdapter.setOnItemClickListener((adapter, view, position) -> {
            showPic(position, dcDutyDataUploadSurveyPicAdapter.getData());
        });
        dcDutyDataUploadSurveyPicAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                showPicDeletedDialog(adapter, position, deletedPic);
            }
        });
        dcDutyDataUploadCheckPicAdapter = new DCDutyDataUploadPicAdapter(R.layout.item_dc_duty_data_up_load_pic, new ArrayList<>(0));
        dcDutyDataUploadCheckPicAdapter.setMod(mDiseaseId != 0);
        dcDutyDataUploadCheckPicAdapter.setOnItemClickListener((adapter, view, position) -> {
            showPic(position, dcDutyDataUploadCheckPicAdapter.getData());
        });
        dcDutyDataUploadCheckPicAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                showPicDeletedDialog(adapter, position, deletedPic);
            }
        });
        dcDutyDataUploadPrescriptionPicAdapter = new DCDutyDataUploadPicAdapter(R.layout.item_dc_duty_data_up_load_pic, new ArrayList<>(0));
        dcDutyDataUploadPrescriptionPicAdapter.setMod(mDiseaseId != 0);
        dcDutyDataUploadPrescriptionPicAdapter.setOnItemClickListener((adapter, view, position) -> {
            showPic(position, dcDutyDataUploadPrescriptionPicAdapter.getData());
        });
        dcDutyDataUploadPrescriptionPicAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                showPicDeletedDialog(adapter, position, deletedPic);
            }
        });
        dcDutyDataUploadSurveyVoiceAdapter = new DCDutyDataUploadVoiceAdapter(R.layout.item_dc_duty_data_up_load_voice, new ArrayList<>(0));
        dcDutyDataUploadSurveyVoiceAdapter.setOnItemClickListener((adapter, view, position) -> {
            DCDutyPrognoteDataInfo item = (DCDutyPrognoteDataInfo) adapter.getItem(position);
            if (null != item) {
                dcDutyDataUploadSurveyVoiceAdapter.playVoice(position);
            }
        });
        dcDutyDataUploadSurveyVoiceAdapter.setOnItemChildClickListener((adapter, view, position) -> showPicDeletedDialog(adapter, position, deletedVoice));
        dcDutyDataUploadCheckVoiceAdapter = new DCDutyDataUploadVoiceAdapter(R.layout.item_dc_duty_data_up_load_voice, new ArrayList<>(0));
        dcDutyDataUploadCheckVoiceAdapter.setOnItemClickListener((adapter, view, position) -> {
            DCDutyPrognoteDataInfo item = (DCDutyPrognoteDataInfo) adapter.getItem(position);
            if (null != item) {
                dcDutyDataUploadCheckVoiceAdapter.playVoice(position);
            }
        });
        dcDutyDataUploadCheckVoiceAdapter.setOnItemChildClickListener((adapter, view, position) -> showPicDeletedDialog(adapter, position, deletedVoice));
        dcDutyDataUploadPrescriptionVoiceAdapter = new DCDutyDataUploadVoiceAdapter(R.layout.item_dc_duty_data_up_load_voice, new ArrayList<>(0));
        dcDutyDataUploadPrescriptionVoiceAdapter.setOnItemClickListener((adapter, view, position) -> {
            DCDutyPrognoteDataInfo item = (DCDutyPrognoteDataInfo) adapter.getItem(position);
            if (null != item) {
                dcDutyDataUploadCheckVoiceAdapter.playVoice(position);
            }
        });
        dcDutyDataUploadPrescriptionVoiceAdapter.setOnItemChildClickListener((adapter, view, position) -> showPicDeletedDialog(adapter, position, deletedVoice));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_dc_duty_patient_data_up_load;
    }

    @Override
    protected void initViews() {
        actDcDutyPatientDataUpLoadSurveyPicRv.setLayoutManager(RecyclerViewUtils.getGridLayoutManager(getThisActivity(), 3));
        actDcDutyPatientDataUpLoadSurveyPicRv.setAdapter(dcDutyDataUploadSurveyPicAdapter);
        actDcDutyPatientDataUpLoadSurveyVoiceRv.setLayoutManager(RecyclerViewUtils.getGridLayoutManager(getThisActivity(), 2));
        actDcDutyPatientDataUpLoadSurveyVoiceRv.setAdapter(dcDutyDataUploadSurveyVoiceAdapter);

        actDcDutyPatientDataUpLoadCheckPicRv.setLayoutManager(RecyclerViewUtils.getGridLayoutManager(getThisActivity(), 3));
        actDcDutyPatientDataUpLoadCheckPicRv.setAdapter(dcDutyDataUploadCheckPicAdapter);
        actDcDutyPatientDataUpLoadCheckVoiceRv.setLayoutManager(RecyclerViewUtils.getGridLayoutManager(getThisActivity(), 2));
        actDcDutyPatientDataUpLoadCheckVoiceRv.setAdapter(dcDutyDataUploadCheckVoiceAdapter);

        actDcDutyPatientDataUpLoadPrescriptionPicRv.setLayoutManager(RecyclerViewUtils.getGridLayoutManager(getThisActivity(), 3));
        actDcDutyPatientDataUpLoadPrescriptionPicRv.setAdapter(dcDutyDataUploadPrescriptionPicAdapter);
        actDcDutyPatientDataUpLoadPrescriptionVoiceRv.setLayoutManager(RecyclerViewUtils.getGridLayoutManager(getThisActivity(), 2));
        actDcDutyPatientDataUpLoadPrescriptionVoiceRv.setAdapter(dcDutyDataUploadPrescriptionVoiceAdapter);
        initListener();
        getDiseaseInfo(mDiseaseId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_FOR_SURVEY_CAMERA:
                    startUpload(TYPE_FOR_SURVEY, mUploadPhotoName);
                    break;
                case REQUEST_CODE_FOR_SURVEY_ALBUM:
                    startUploads(TYPE_FOR_SURVEY, data);
                    break;
                case REQUEST_CODE_FOR_CHECK_CAMERA:
                    startUpload(TYPE_FOR_CHECK, mUploadPhotoName);
                    break;
                case REQUEST_CODE_FOR_CHECK_ALBUM:
                    startUploads(TYPE_FOR_CHECK, data);
                    break;
                case REQUEST_CODE_FOR_PRESCRIPTION_CAMERA:
                    startUpload(TYPE_FOR_PRESCRIPTION, mUploadPhotoName);
                    break;
                case REQUEST_CODE_FOR_PRESCRIPTION_ALBUM:
                    startUploads(TYPE_FOR_PRESCRIPTION, data);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onStop() {
        if (mIat != null) {
            mIat.cancel();
            mIat.destroy();
        }
        super.onStop();
    }

    private void showPicDeletedDialog(BaseQuickAdapter adapter, int position, String titleContent) {
        new CommonDialog.Builder(getThisActivity())
                .setTitle(titleContent)
                .setCancelable(false)
                .setPositiveButton("取消", () -> {
                })
                .setNegativeButton("确定", () -> {
                    adapter.remove(position);
                })
                .show();
    }

    private void startUploadVoice(int type, String fileName) {
        String path = fileName;
        ProgressDialog dialog = createProgressDialog("正在上传...", false);
        dialog.show();
        DCDutyPatientDiseaseVoiceUploader uploader = new DCDutyPatientDiseaseVoiceUploader(new BaseFileUploader.DefulteUploadStateCallback() {
            @Override
            public void onUploadProgresssChange(String sessionId, long successBytes, long blockByte, long totalBytes) {

            }

            @Override
            public void onUploadComplete(String sessionId, int code, String content) {
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String result = jsonObject.getString("file_name");
                    DCDutyPrognoteDataInfo info = new DCDutyPrognoteDataInfo();
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(fileName);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    info.setDuration((int) (audioDuration / 1000));
                    info.setDataType(2);
                    info.setType(type);
                    info.setMaterialPic(result);
                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (type == TYPE_FOR_CHECK) {
                                dcDutyDataUploadCheckVoiceAdapter.addData(info);
                            } else if (type == TYPE_FOR_PRESCRIPTION) {
                                dcDutyDataUploadPrescriptionVoiceAdapter.addData(info);
                            } else if (type == TYPE_FOR_SURVEY) {
                                dcDutyDataUploadSurveyVoiceAdapter.addData(info);
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }

            @Override
            public void onUploadCancle(String sessionId) {
                dialog.dismiss();
            }

            @Override
            public void onUploadException(String sessionId, Exception exception) {
                dialog.dismiss();
            }
        });
        uploader.setFilePath(path);
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        uploader.startUpload();
    }

    private void startUpload(int type, String fileName) {
        String path = fileName;
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
        ProgressDialog dialog = createProgressDialog("正在上传...", false);
        dialog.show();
        DCDutyPatientDiseasePicUploader uploader = new DCDutyPatientDiseasePicUploader(new BaseFileUploader.DefulteUploadStateCallback() {
            @Override
            public void onUploadProgresssChange(String sessionId, long successBytes, long blockByte, long totalBytes) {

            }

            @Override
            public void onUploadComplete(String sessionId, int code, String content) {
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String result = jsonObject.getString("file_name");
                    DCDutyPrognoteDataInfo info = new DCDutyPrognoteDataInfo();
                    info.setDataType(3);
                    info.setType(type);
                    info.setMaterialPic(result);
                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (type == TYPE_FOR_CHECK) {
                                dcDutyDataUploadCheckPicAdapter.addData(info);
                            } else if (type == TYPE_FOR_PRESCRIPTION) {
                                dcDutyDataUploadPrescriptionPicAdapter.addData(info);
                            } else if (type == TYPE_FOR_SURVEY) {
                                dcDutyDataUploadSurveyPicAdapter.addData(info);
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }

            @Override
            public void onUploadCancle(String sessionId) {
                dialog.dismiss();
            }

            @Override
            public void onUploadException(String sessionId, Exception exception) {
                dialog.dismiss();
            }
        });
        uploader.setFilePath(path);
        uploader.startUpload();
    }

    private void startUploads(int type, Intent data) {
        if (data != null) {
            List<String> albumPhotoList = data.getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SELECT_PATHS);
            for (String path : albumPhotoList) {
                startUpload(type, path);
            }
        }
    }

    private void initListener() {
        actDcDutyPatientDataUpLoadTreatTimeLl.setOnClickListener(v -> {
            CommonUtils.hideSoftInput(getThisActivity());
            TimePickUtils.showBottomPickTime(getThisActivity(), (date, v1) -> {
                actDcDutyPatientDataUpLoadTreatTimeTv.setText(TimeUtils.date2String(date, "yyyy年MM月dd日"));
                mRecordTime = TimeUtils.date2String(date, "yyyy-MM-dd HH:mm:ss");
            });
        });
        actDcDutyPatientDataUpLoadTreatTypeLl.setOnClickListener(v -> {
            showTreatTypeDialog();
        });
        actDcDutyPatientDataUpLoadDiagnosisEt.addTextChangedListener(new MaxLengthWatcher(100,actDcDutyPatientDataUpLoadDiagnosisEt));
        actDcDutyPatientDataUpLoadSurveyEt.addTextChangedListener(new MaxLengthWatcher(500,actDcDutyPatientDataUpLoadSurveyEt));
        actDcDutyPatientDataUpLoadCheckEt.addTextChangedListener(new MaxLengthWatcher(500,actDcDutyPatientDataUpLoadCheckEt));
        actDcDutyPatientDataUpLoadPrescriptionEt.addTextChangedListener(new MaxLengthWatcher(500,actDcDutyPatientDataUpLoadPrescriptionEt));
        actDcDutyPatientDataUpLoadSurveyPicAddIv.setOnClickListener(v -> {
            showChoosePicDialog(REQUEST_CODE_FOR_SURVEY_ALBUM, REQUEST_CODE_FOR_SURVEY_CAMERA);
        });
        actDcDutyPatientDataUpLoadCheckPicAddIv.setOnClickListener(v -> {
            showChoosePicDialog(REQUEST_CODE_FOR_CHECK_ALBUM, REQUEST_CODE_FOR_CHECK_CAMERA);
        });
        actDcDutyPatientDataUpLoadPrescriptionPicAddIv.setOnClickListener(v -> {
            showChoosePicDialog(REQUEST_CODE_FOR_PRESCRIPTION_ALBUM, REQUEST_CODE_FOR_PRESCRIPTION_CAMERA);
        });
        actDcDutyPatientDataUpLoadSurveyVoiceAddIv.setOnClickListener(v -> {
            showRecordingDialog(TYPE_FOR_SURVEY);
        });
        actDcDutyPatientDataUpLoadCheckVoiceAddIv.setOnClickListener(v -> {
            showRecordingDialog(TYPE_FOR_CHECK);
        });
        actDcDutyPatientDataUpLoadPrescriptionVoiceAddIv.setOnClickListener(v -> {
            showRecordingDialog(TYPE_FOR_PRESCRIPTION);
        });
        actDcDutyPatientDataUpLoadDeleteTv.setOnClickListener(v -> {
            new CommonDialog.Builder(getThisActivity())
                    .setTitle(deletedMedicalData)
                    .setCancelable(false)
                    .setPositiveButton("取消", () -> {
                    })
                    .setNegativeButton("确定", () -> {
                        dcManager.deleteMedicalData(mMedicalId, mDiseaseId, new DefaultResultCallback<Void>() {
                            @Override
                            public void onSuccess(Void aVoid, BaseResult baseResult) {
                                getThisActivity().setResult(Activity.RESULT_OK);
                                getDisplay().finish();
                            }
                        });
                    })
                    .show();
        });
        actDcDutyPatientDataUpLoadConfirmTv.setOnClickListener(v -> {
            if (mRecordType == 0) {
                ToastUtils.showShort("请选择就诊类型");
                return;
            }
            if (StringUtils.isEmpty(mRecordTime)) {
                ToastUtils.showShort("请选择就诊日期");
                return;
            }
            dcManager.updateMedicalData(mMedicalId, mDiseaseId, mRecordTime, mRecordType, createConfirmData(), new DefaultResultCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid, BaseResult baseResult) {
                    getThisActivity().setResult(Activity.RESULT_OK);
                    getDisplay().finish();
                }
            });
        });
    }

    private List<DCDutyPrognoteDataInfo> createConfirmData() {
        List<DCDutyPrognoteDataInfo> mDCDutyVisitPlantTempItems = new ArrayList<>();
        if (!StringUtils.isEmpty(getString(actDcDutyPatientDataUpLoadDiagnosisEt))) {
            DCDutyPrognoteDataInfo diagnosisDataInfo = new DCDutyPrognoteDataInfo();
            diagnosisDataInfo.setType(1);
            diagnosisDataInfo.setDataType(1);
            diagnosisDataInfo.setDataVal(getString(actDcDutyPatientDataUpLoadDiagnosisEt));
            mDCDutyVisitPlantTempItems.add(diagnosisDataInfo);
        }
        if (!StringUtils.isEmpty(getString(actDcDutyPatientDataUpLoadSurveyEt))) {
            DCDutyPrognoteDataInfo surveyDataInfo = new DCDutyPrognoteDataInfo();
            surveyDataInfo.setType(2);
            surveyDataInfo.setDataType(1);
            surveyDataInfo.setDataVal(getString(actDcDutyPatientDataUpLoadSurveyEt));
            mDCDutyVisitPlantTempItems.add(surveyDataInfo);
        }
        if (!StringUtils.isEmpty(getString(actDcDutyPatientDataUpLoadCheckEt))) {
            DCDutyPrognoteDataInfo checkDataInfo = new DCDutyPrognoteDataInfo();
            checkDataInfo.setType(3);
            checkDataInfo.setDataType(1);
            checkDataInfo.setDataVal(getString(actDcDutyPatientDataUpLoadCheckEt));
            mDCDutyVisitPlantTempItems.add(checkDataInfo);
        }
        if (!StringUtils.isEmpty(getString(actDcDutyPatientDataUpLoadPrescriptionEt))) {
            DCDutyPrognoteDataInfo prescriptionDataInfo = new DCDutyPrognoteDataInfo();
            prescriptionDataInfo.setType(4);
            prescriptionDataInfo.setDataType(1);
            prescriptionDataInfo.setDataVal(getString(actDcDutyPatientDataUpLoadPrescriptionEt));
            mDCDutyVisitPlantTempItems.add(prescriptionDataInfo);
        }

        mDCDutyVisitPlantTempItems.addAll(dcDutyDataUploadCheckPicAdapter.getData());
        mDCDutyVisitPlantTempItems.addAll(dcDutyDataUploadSurveyPicAdapter.getData());
        mDCDutyVisitPlantTempItems.addAll(dcDutyDataUploadPrescriptionPicAdapter.getData());
        mDCDutyVisitPlantTempItems.addAll(dcDutyDataUploadCheckVoiceAdapter.getData());
        mDCDutyVisitPlantTempItems.addAll(dcDutyDataUploadSurveyVoiceAdapter.getData());
        mDCDutyVisitPlantTempItems.addAll(dcDutyDataUploadPrescriptionVoiceAdapter.getData());
        return mDCDutyVisitPlantTempItems;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showRecordingDialog(int type) {
        if (mIat == null) {
            initSpeech();
        }
        setSpeechParams();
        View layout = LayoutInflater.from(getThisActivity()).inflate(R.layout.dialog_dc_duty_vioce_record, null);
        final Dialog dialog = new AlertDialog.Builder(getThisActivity(), R.style.custom_noActionbar_window_style).create();
        dialog.show();
        dialog.setContentView(layout);
        dialog.setCanceledOnTouchOutside(false);

        Window win = dialog.getWindow();
        if (null == win) {
            return;
        }
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);
        ImageButton dialogDcDutyVoiceRecordStartIb = layout.findViewById(R.id.dialog_dc_duty_voice_record_start_ib);
        TextView dialogDcDutyVoiceRecordCancelTv = layout.findViewById(R.id.dialog_dc_duty_voice_record_cancel_tv);
        dialogDcDutyVoiceRecordCancelTv.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialogDcDutyVoiceRecordStartIb.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                audioName = TimeUtils.getNowMills() + "";
                audioDuration = TimeUtils.getNowMills();
                mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, getVoiceFilePath(audioName));
                mIat.startListening(new DefaultRecognizerListener(type));
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                audioDuration = TimeUtils.getNowMills() - audioDuration;
                if (mIat.isListening()) {
                    mIat.stopListening();
                }
                if (!StringUtils.isEmpty(audioName) || audioDuration > 1000) {
                    startUploadVoice(type, getVoiceFilePath(audioName));
                } else {
                    ToastUtils.showShort("说话时间太短");
                }
            }
            return false;
        });
    }

    private String getVoiceFilePath(String fileName) {
        return SdManager.getInstance().getTempPath() + "msc/" + fileName + ".amr";
    }

    /**
     * 初始化讯飞录音
     */
    private void initSpeech() {
        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
        // 请勿在“=”与appid之间添加任何空字符或者转义符
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID + "=5dfc3f5c");
        audioDetector = AudioDetector.createDetector(getThisActivity(), AudioDetector.TYPE_META);
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        //初始化识别无UI识别对象
        //使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(getThisActivity(), mInitListener);
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
    }

    private void setSpeechParams() {
        //AudioDetector audioDetector = AudioDetector.createDetector(getThisActivity(),)
        //设置语法ID和 SUBJECT 为空，以免因之前有语法调用而设置了此参数；或直接清空所有参数，具体可参考 DEMO 的示例。
        mIat.setParameter(SpeechConstant.CLOUD_GRAMMAR, null);
        mIat.setParameter(SpeechConstant.SUBJECT, null);
        //设置返回结果格式，目前支持json,xml以及plain 三种格式，其中plain为纯听写文本内容
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        //此处engineType为“cloud”
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, "cloud");
        //设置语音输入语言，zh_cn为简体中文
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        //设置结果返回语言
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 设置语音前端点:静音超时时间，单位ms，即用户多长时间不说话则当做超时处理
        //取值范围{1000～10000}
        mIat.setParameter(SpeechConstant.VAD_BOS, "14000");
        //设置语音后端点:后端点静音检测时间，单位ms，即用户停止说话多长时间内即认为不再输入，
        //自动停止录音，范围{0~10000}
        mIat.setParameter(SpeechConstant.VAD_EOS, "11000");
        //设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
    }

    private String printResult(RecognizerResult results) {
        String text = parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        return resultBuffer.toString();
    }

    private String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
//				如果需要多候选结果，解析数组其他字段
//				for(int j = 0; j < items.length(); j++)
//				{
//					JSONObject obj = items.getJSONObject(j);
//					ret.append(obj.getString("w"));
//				}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

    private void displayEditText(boolean isLast, int type, RecognizerResult recognizerResult) {
        String finalResult = "";
        if (type == TYPE_FOR_CHECK && isLast) {
            finalResult = getString(actDcDutyPatientDataUpLoadCheckEt);
            finalResult += printResult(recognizerResult);
            actDcDutyPatientDataUpLoadCheckEt.setText(finalResult);
        } else if (type == TYPE_FOR_SURVEY && isLast) {
            finalResult = getString(actDcDutyPatientDataUpLoadSurveyEt);
            finalResult += printResult(recognizerResult);
            actDcDutyPatientDataUpLoadSurveyEt.setText(finalResult);
        } else if (type == TYPE_FOR_PRESCRIPTION && isLast) {
            finalResult = getString(actDcDutyPatientDataUpLoadPrescriptionEt);
            finalResult += printResult(recognizerResult);
            actDcDutyPatientDataUpLoadPrescriptionEt.setText(finalResult);
        }
    }

    private void showTreatTypeDialog() {
        DCDutyTreatTypeChooseDialog dialog = new DCDutyTreatTypeChooseDialog(getThisActivity(), R.style.custom_noActionbar_window_style);
        dialog.setOnTreatTypeChooseListener(dcDutyTreatType -> {
            mRecordType = dcDutyTreatType.getId();
            actDcDutyPatientDataUpLoadTreatTypeTv.setText(dcDutyTreatType.getName());
        });
        dialog.show();
    }

    private void showPic(int position, List<DCDutyPrognoteDataInfo> items) {
        List<String> picUrls = new ArrayList<>();
        for (int i = 0; i < LibCollections.size(items); i++) {
            picUrls.add(AppConfig.getFirstJourneyUrl() + items.get(i).getMaterialPic());
        }
        BrowserPicEvent browserPicEvent = new BrowserPicEvent();
        browserPicEvent.setIndex(position);
        browserPicEvent.setAssistant(false);
        browserPicEvent.setServerFilePaths(picUrls);
        //browserPicEvent.setAuxiliaryMaterialInfos(checkInfos);
        getDisplay().startPicBrowseActivity(browserPicEvent, 0);
    }

    private void showChoosePicDialog(int albumRequestCode, int cameraRequestCode) {
        mUploadPhotoName = SdManager.getInstance().getTempPath() + System.currentTimeMillis() + ".jpg";
        new DialogHelper.ChoosePicsBuilder().setActivity(getThisActivity())
                .setUploadFirstJoureny(false)
                .setMaxCount(1)
                .setRequestCodeAlbum(albumRequestCode)
                .setRequestCodeCamera(cameraRequestCode)
                .setUploadPhotoName(mUploadPhotoName)
                .show();
    }

    private void getDiseaseInfo(int diseaseId) {
        if (!isAddDisease()) {
            dcManager.getDiseaseInfo(diseaseId, new DefaultResultCallback<DCDutyPatientDiseaseItemInfo>() {
                @Override
                public void onSuccess(DCDutyPatientDiseaseItemInfo dcDutyPatientDiseaseInfo, BaseResult baseResult) {
                    if (null != dcDutyPatientDiseaseInfo) {
                        mRecordTime = dcDutyPatientDiseaseInfo.getRecordDt();
                        mRecordType = dcDutyPatientDiseaseInfo.getAttr();
                        displayDiseaseInfo(dcDutyPatientDiseaseInfo);
                    }
                }
            });
        }else {
            actDcDutyPatientDataUpLoadTreatTimeTv.setText(TimeUtils.millis2String(TimeUtils.getNowMills(), "yyyy年MM月dd日"));
            actDcDutyPatientDataUpLoadTreatTypeTv.setText("复诊");
        }
    }

    private void displayDiseaseInfo(@NonNull DCDutyPatientDiseaseItemInfo info) {
        actDcDutyPatientDataUpLoadTreatTimeLl.setEnabled(false);
        actDcDutyPatientDataUpLoadTreatTypeLl.setEnabled(false);
        actDcDutyPatientDataUpLoadOperatorLl.setVisibility(View.VISIBLE);
        actDcDutyPatientDataUpLoadDeleteTv.setVisibility(View.VISIBLE);
        actDcDutyPatientDataUpLoadConfirmTv.setText("保存");
        if (!StringUtils.isEmpty(info.getRecordDt())) {
            actDcDutyPatientDataUpLoadTreatTimeTv.setText(TimeUtils.string2String(info.getRecordDt(), "yyyy-MM-dd HH:mm:ss", "yyyy年MM月dd日"));
        }
        actDcDutyPatientDataUpLoadTreatTypeTv.setText(info.getAttrDesc());
        actDcDutyPatientDataUpLoadDiagnosisEt.setText(dcManager.getPrognoteContent(info, 1));
        actDcDutyPatientDataUpLoadSurveyEt.setText(dcManager.getPrognoteContent(info, 2));
        actDcDutyPatientDataUpLoadCheckEt.setText(dcManager.getPrognoteContent(info, 3));
        actDcDutyPatientDataUpLoadPrescriptionEt.setText(dcManager.getPrognoteContent(info, 4));
        actDcDutyPatientDataUpLoadOperatorTv.setText(info.getOpName());
        dcDutyDataUploadSurveyVoiceAdapter.setNewData(dcManager.getPrognoteVoices(info, 2));
        dcDutyDataUploadCheckVoiceAdapter.setNewData(dcManager.getPrognoteVoices(info, 3));
        dcDutyDataUploadSurveyPicAdapter.setNewData(dcManager.getPrognotePics(info, 2));
        dcDutyDataUploadCheckPicAdapter.setNewData(dcManager.getPrognotePics(info, 3));
        dcDutyDataUploadPrescriptionPicAdapter.setNewData(dcManager.getPrognotePics(info, 4));
        dcDutyDataUploadPrescriptionVoiceAdapter.setNewData(dcManager.getPrognoteVoices(info, 4));
    }

    private boolean isAddDisease() {
        return mDiseaseId == 0;
    }

    class DefaultRecognizerListener implements RecognizerListener {
        private int type;

        public DefaultRecognizerListener(int type) {
            this.type = type;
        }

        @Override
        public void onVolumeChanged(int i, byte[] bytes) {

        }

        @Override
        public void onBeginOfSpeech() {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean isLast) {
            Logger.logD(Logger.COMMON, "onResult#" + isLast);
            if (resultType.equals("json")) {
                displayEditText(isLast, type, recognizerResult);
                //startUpload();
            }
        }

        @Override
        public void onError(SpeechError speechError) {

        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    }
}
