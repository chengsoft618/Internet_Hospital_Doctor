package cn.longmaster.hospital.doctor.ui.rounds;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.longmaster.doctorlibrary.util.common.DateUtil;
import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.handler.MessageSender;
import cn.longmaster.doctorlibrary.util.img.BitmapUtil;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.hospital.doctor.Display;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.common.BannerAndQuickEnterInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointmentInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.BaseDiagnosisInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.DiagnosisContentInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.DiagnosisFileInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.DoctorDiagnosisAllInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.DoctorDiagnosisInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.UploadDiagnosisInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.FirstJourneyPicInfo;
import cn.longmaster.hospital.doctor.core.manager.consult.RecordManager;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.config.BannerQuickEnterRequester;
import cn.longmaster.hospital.doctor.core.requests.consult.record.IssueDIagnosisRequester;
import cn.longmaster.hospital.doctor.core.upload.simple.PicDiagnosisFileUploader;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.rounds.adapter.IssueDoctorOrderAdapter;
import cn.longmaster.hospital.doctor.util.DialogHelper;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import cn.longmaster.hospital.doctor.view.MyGridView;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.upload.OnNginxUploadStateCallback;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * @author
 * @mod biao
 * @date 2019/8/23 10:42
 * @description: 出具医嘱Activity
 */
public class IssueDoctorOrderActivity extends NewBaseActivity {
    private final String TAG = IssueDoctorOrderActivity.class.getSimpleName();
    private final int REQUEST_CODE_PHOTO = 200;//页面请求码:选择相片
    private final int REQUEST_CODE_CAMERA = 201; //页面请求码:选择相机
    private final int REQUEST_CODE_DELETE_PIC = 202; //删除医嘱图片
    @FindViewById(R.id.activity_issue_doctor_order_template_tv)
    private TextView mTemplateTv;
    @FindViewById(R.id.activity_issue_doctor_order_template_GridView)
    private MyGridView mGridView;
    @FindViewById(R.id.activity_issue_doctor_order_text_input_et)
    private EditText mEditText;

    @AppApplication.Manager
    private RecordManager mRecordManager;
    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;

    private List<UploadDiagnosisInfo> mUploadDiagnosisInfos = new ArrayList<>();
    private AppointmentInfo mAppointmentInfo;
    private String mUploadPhotoName;
    private IssueDoctorOrderAdapter mMedicalAdapter;
    private List<FirstJourneyPicInfo> mDoctorOrderPicList = new ArrayList<>();
    private DoctorDiagnosisInfo mDiagnosisInfo;
    private int mRecureNum = 0;
    private String mDiagnosisConten;
    private List<BaseDiagnosisInfo> mDiagnosisInfos = new ArrayList<>();

    @Override
    protected void handleIntent(Intent intent, Display display) {
        super.handleIntent(intent, display);
        mAppointmentInfo = (AppointmentInfo) intent.getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_INFO);
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_issue_doctor_order;
    }

    @Override
    protected void initViews() {
        mTemplateTv.setText(getUnderLine("会诊意见书写参考模板"));
        mMedicalAdapter = new IssueDoctorOrderAdapter(this, mDoctorOrderPicList);
        mMedicalAdapter.setIssueDoctorOrder(true);
        mGridView.setAdapter(mMedicalAdapter);
        mMedicalAdapter.setOnRetryUploadClickListener(position -> {
            mDoctorOrderPicList.get(position).setUpLoadState(AppConstant.UploadFirstJourneyState.UPLOAD_FIRST_JOURNEY_STATE_WAIT);
            mMedicalAdapter.notifyDataSetChanged();
            startUpload();
        });
        mMedicalAdapter.setOnDeletePicClickListener(position -> {
            mDoctorOrderPicList.remove(position);
            mMedicalAdapter.notifyDataSetChanged();
        });
        mMedicalAdapter.setOnPicItemClickListener(position -> {
            Logger.logD(Logger.APPOINTMENT, "onPicItemClickListener->setOnItemClickListener->position()" + position);
            if (position < mDoctorOrderPicList.size()) {
                List<String> localFilePaths = new ArrayList<>();
                List<String> serverPicUrls = new ArrayList<>();
                List<String> picName = new ArrayList<>();
                for (FirstJourneyPicInfo info : mDoctorOrderPicList) {
                    localFilePaths.add(info.getPicPath());
                    serverPicUrls.add(info.getServiceUrl());
                    picName.add(info.getPicName());
                }
                Intent intent = new Intent(IssueDoctorOrderActivity.this, FirstJourneyPicBrowseActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_FIRST_JOURNEY_PIC_LIST, (Serializable) localFilePaths);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SERVER_URL, (Serializable) serverPicUrls);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PIC_NAME, (Serializable) mDoctorOrderPicList);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_ORDER, true);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_INDEX, position);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTINFO_ID, getAppointmentId());
                startActivityForResult(intent, REQUEST_CODE_DELETE_PIC);
            } else {
                mUploadPhotoName = SdManager.getInstance().getTempPath() + System.currentTimeMillis() + ".jpg";
                new DialogHelper.ChoosePicsBuilder()
                        .setRequestCodeAlbum(REQUEST_CODE_PHOTO)
                        .setRequestCodeCamera(REQUEST_CODE_CAMERA)
                        .setActivity(IssueDoctorOrderActivity.this)
                        .setUploadPhotoName(mUploadPhotoName)
                        .show();
            }
        });
        getDiagnosis(mAppointmentInfo);
    }

    public SpannableStringBuilder getUnderLine(String str) {
        if (StringUtils.isTrimEmpty(str)) {
            return null;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str);
        UnderlineSpan underlineSpan = new UnderlineSpan();
        spannableStringBuilder.setSpan(underlineSpan, 0, StringUtils.length(str), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
    }

    @OnClick({R.id.activity_issue_doctor_order_submission,
            R.id.activity_issue_doctor_order_template_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_issue_doctor_order_submission:
                uploadDiagnosis();
                break;
            case R.id.activity_issue_doctor_order_template_tv:
                showIntroductionDialog();
                break;
            default:
                break;
        }
    }

    private void showIntroductionDialog() {
        View view = LayoutInflater.from(getThisActivity()).inflate(R.layout.layout_dialog_template, null);
        ImageView mTemplateImageView = view.findViewById(R.id.layout_dialog_template_image);
        Button closeBtn = view.findViewById(R.id.dialog_introduction_close_btn);
        Dialog mTemplateDialog = new Dialog(getThisActivity(), R.style.custom_noActionbar_window_style);
        mTemplateDialog.show();
        mTemplateDialog.setContentView(view);
        mTemplateDialog.setCanceledOnTouchOutside(true);
        mTemplateDialog.setCancelable(true);
        Window win = mTemplateDialog.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        closeBtn.setOnClickListener(v -> mTemplateDialog.dismiss());
        getBannerQuickEnter((baseResult, bannerAndQuickEnterInfos) -> {
            Logger.logD(Logger.APPOINTMENT, "getBannerQuickEnter->baseResult:" + baseResult + ",bannerAndQuickEnterInfos:" + bannerAndQuickEnterInfos);
            if (baseResult.getCode() == 0 && bannerAndQuickEnterInfos != null) {
                if (mTemplateImageView != null && bannerAndQuickEnterInfos.size() > 0) {
                    String filePath = SdManager.getInstance().getBannerPath() + bannerAndQuickEnterInfos.get(0).getPicturePath();
                    filePath = filePath.replace(filePath.substring(filePath.lastIndexOf(".")), "");
                    String urls = AppConfig.getBannerDownloadUrl() + "0/" + bannerAndQuickEnterInfos.get(0).getPicturePath();
                    Logger.logD(Logger.COMMON, "->getBannerQuickEnter:filePath:" + filePath + ", urls:" + urls);
                    GlideUtils.showViewIssueDoctorBannerView(getThisActivity(), mTemplateImageView, urls);
                }
            }
        });
    }

    private void getBannerQuickEnter(OnResultListener<List<BannerAndQuickEnterInfo>> onResultListener) {
        BannerQuickEnterRequester requester = new BannerQuickEnterRequester(onResultListener);
        requester.token = "0";
        requester.bannerType = 8;
        requester.doPost();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                File file = new File(mUploadPhotoName);
                if (!file.exists()) {
                    Logger.logD(Logger.APPOINTMENT, "IssueDoctorOrderActivity->onActivityResult()file.exists()：" + file.exists());
                    return;
                }
                FirstJourneyPicInfo info = new FirstJourneyPicInfo();
                info.setPicPath(mUploadPhotoName);
                info.setUpLoadState(AppConstant.UploadFirstJourneyState.UPLOAD_FIRST_JOURNEY_STATE_WAIT);
                mDoctorOrderPicList.add(info);
                mMedicalAdapter.notifyDataSetChanged();
                startUpload();
                Logger.logD(Logger.APPOINTMENT, "IssueDoctorOrderActivity->onActivityResult()通过相机拍摄的图片路径：" + mUploadPhotoName);
                break;

            case REQUEST_CODE_PHOTO:
                if (data != null) {
                    ArrayList<String> albumPhotoList = data.getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SELECT_PATHS);
                    Logger.logD(Logger.APPOINTMENT, "IssueDoctorOrderActivity->onActivityResult()通过选择相册的图片路径：" + albumPhotoList);
                    for (String path : albumPhotoList) {
                        String newFilePath = SdManager.getInstance().getTempPath() + System.currentTimeMillis() + ".jpg";
                        copyFile(path, newFilePath);
                    }
                }
                break;

            case REQUEST_CODE_DELETE_PIC:
                if (data != null) {
                    int position = data.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_INDEX, 0);
                    String picName = data.getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PIC_NAME);
                    Logger.logD(Logger.APPOINTMENT, "IssueDoctorOrderActivity->onActivityResult-->position：" + position);
                    mDoctorOrderPicList.remove(position);
                    mMedicalAdapter.notifyDataSetChanged();
                    for (int i = 0; i < mUploadDiagnosisInfos.size(); i++) {
                        if (mUploadDiagnosisInfos.get(i).getDiagnosisPicture().equals(picName)) {
                            mUploadDiagnosisInfos.remove(i);
                            break;
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                if (new File(newPath).exists()) {
                    FirstJourneyPicInfo picInfo = new FirstJourneyPicInfo();
                    picInfo.setPicPath(newPath);
                    picInfo.setUpLoadState(AppConstant.UploadFirstJourneyState.UPLOAD_FIRST_JOURNEY_STATE_WAIT);
                    mDoctorOrderPicList.add(picInfo);
                }
                mMedicalAdapter.notifyDataSetChanged();
                startUpload();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
    }

    private void startUpload() {
        boolean isUploadIng = false;
        for (FirstJourneyPicInfo info : mDoctorOrderPicList) {
            if (info.getUpLoadState() == AppConstant.UploadFirstJourneyState.UPLOAD_FIRST_JOURNEY_STATE_ING) {
                isUploadIng = true;
                break;
            }
        }
        if (isUploadIng) {
            return;
        }
        for (int i = 0; i < mDoctorOrderPicList.size(); i++) {
            String path = mDoctorOrderPicList.get(i).getPicPath();
            if (mDoctorOrderPicList.get(i).getUpLoadState() == AppConstant.UploadFirstJourneyState.UPLOAD_FIRST_JOURNEY_STATE_WAIT) {
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
                mDoctorOrderPicList.get(i).setUpLoadState(AppConstant.UploadFirstJourneyState.UPLOAD_FIRST_JOURNEY_STATE_ING);
                uploaderFirstJourney(path, i);
                break;
            }
        }
    }

    private void uploaderFirstJourney(final String photoFileName, final int position) {
        PicDiagnosisFileUploader uploader = new PicDiagnosisFileUploader(new OnNginxUploadStateCallback() {
            @Override
            public void onUploadProgresssChange(String s, final long l, long l1, final long l2) {
                Logger.logD(Logger.APPOINTMENT, "FirstJourneyUploader->onUploadProgresssChange->String()" + s + ",l:" + l + ",l1:" + l1 + ",l2:" + l2);
                /*AppHandlerProxy.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        float pressent = (float) l / l2 * 100;
                        mDoctorOrderPicList.get(position).setProgress(pressent);
                        mMedicalAdapter.notifyDataSetChanged();
                    }
                });*/
            }

            @Override
            public void onUploadComplete(String s, int i, final String s1) {
                Logger.logD(Logger.APPOINTMENT, "FirstJourneyUploader->onUploadComplete->String()" + s + ",s1:" + s1);
                Logger.logD(Logger.APPOINTMENT, TAG + "#uploaderFirstJourney#PicDiagnosisFileUploader#onUploadComplete thread name:" + Thread.currentThread().getName());
                AppHandlerProxy.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        String picName = "";
                        try {
                            JSONObject object = new JSONObject(s1);
                            picName = object.getString("file_name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mDoctorOrderPicList.get(position).setUpLoadState(AppConstant.UploadFirstJourneyState.UPLOAD_FIRST_JOURNEY_STATE_SUCCESS);
                        if (!StringUtils.isEmpty(picName)) {
                            mDoctorOrderPicList.get(position).setServiceUrl(createDiagnosisPicUrl(picName, getAppointmentId()));
                        }
                        mDoctorOrderPicList.get(position).setPicName(picName);
                        Logger.logD(Logger.APPOINTMENT, "FirstJourneyUploader->onUploadComplete->mDoctorOrderPicList:" + mDoctorOrderPicList);
                        mMedicalAdapter.notifyDataSetChanged();

                        UploadDiagnosisInfo info = new UploadDiagnosisInfo();
                        info.setRecureNum(0);
                        info.setDiagnosisPicture(picName);
                        mUploadDiagnosisInfos.add(info);
                        startUpload();
                    }
                });
            }

            @Override
            public void onUploadCancle(String s) {
                Logger.logD(Logger.APPOINTMENT, "FirstJourneyUploader->onUploadCancle->String()" + s);
            }

            @Override
            public void onUploadException(String s, Exception e) {
                Logger.logD(Logger.APPOINTMENT, "FirstJourneyUploader->onUploadException->String()" + s + " ,e:" + e);
                AppHandlerProxy.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        mDoctorOrderPicList.get(position).setUpLoadState(AppConstant.UploadFirstJourneyState.UPLOAD_FIRST_JOURNEY_STATE_FAIL);
                        mMedicalAdapter.notifyDataSetChanged();
                        startUpload();
                    }
                });
            }
        });
        uploader.filePath = photoFileName;
        uploader.appointmentId = getAppointmentId();
        uploader.startUpload();
    }

    /**
     * 上传病历
     */
    private void uploadDiagnosis() {
        if (mDoctorOrderPicList.size() == 0 && StringUtils.isEmpty(mEditText.getText().toString())) {
            ToastUtils.showShort("请填写至少一项内容。");
            return;
        }
        boolean isUploadIng = false;
        for (FirstJourneyPicInfo info : mDoctorOrderPicList) {
            if (info.getUpLoadState() == AppConstant.UploadFirstJourneyState.UPLOAD_FIRST_JOURNEY_STATE_ING) {
                isUploadIng = true;
                break;
            }
        }
        if (isUploadIng) {
            ToastUtils.showShort("有图片正在上传，请稍后提交。");
            return;
        }
        final ProgressDialog progressDialog = createProgressDialog(getString(R.string.data_uploading));
        IssueDIagnosisRequester requester = new IssueDIagnosisRequester(new OnResultListener<Void>() {
            @Override
            public void onResult(BaseResult baseResult, Void aVoid) {
                progressDialog.cancel();
                Logger.logD(Logger.APPOINTMENT, "uploadDiagnosis->IssueDIagnosisRequester->baseResult()" + baseResult.getCode());
                if (baseResult.getCode() == RESULT_SUCCESS) {
                    ToastUtils.showShort("会诊意见提交成功！");
                    Message message = new Message();
                    message.arg1 = getAppointmentId();
                    MessageSender.sendMessage(message);
                    Intent intent = new Intent();
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, getAppointmentId());
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    ToastUtils.showShort(R.string.data_upload_faild);
                }
            }
        });
        requester.appointmentId = getAppointmentId();
        requester.doctorId = AppApplication.getInstance().getManager(UserInfoManager.class).getCurrentUserInfo().getUserId();
        requester.recureDt = "";
        for (int i = 0; i < mUploadDiagnosisInfos.size(); i++) {
            mRecureNum += 1;
            mUploadDiagnosisInfos.get(i).setRecureNum(mRecureNum);
        }
        for (int i = 0; i < mDiagnosisInfos.size(); i++) {
            if (!mDiagnosisInfos.get(i).isMedia()) {
                DiagnosisContentInfo diagnosisContentInfo = (DiagnosisContentInfo) mDiagnosisInfos.get(i);
                UploadDiagnosisInfo uploadDiagnosisInfo = new UploadDiagnosisInfo();
                uploadDiagnosisInfo.setRecureNum(diagnosisContentInfo.getRecureNum());
                uploadDiagnosisInfo.setRecureDesc(diagnosisContentInfo.getContent());
                mUploadDiagnosisInfos.add(uploadDiagnosisInfo);
            }
        }
        addTextDiagnosis(mEditText.getText().toString());
        requester.diagnosisInfoList = mUploadDiagnosisInfos;
        requester.doPost();
    }

    /**
     * 添加文本医嘱
     *
     * @param text 文本医嘱
     */
    private void addTextDiagnosis(String text) {
        UploadDiagnosisInfo info = new UploadDiagnosisInfo();
        if (mDiagnosisInfos.size() > 0) {
            Logger.logD(Logger.APPOINTMENT, "uploadDiagnosis->addTextDiagnosisww");
            info.setRecureNum(mRecureNum = mDiagnosisInfos.get(mDiagnosisInfos.size() - 1).getRecureNum() + 1);
        } else {
            Logger.logD(Logger.APPOINTMENT, "uploadDiagnosis->addTextDiagnosisee");
            info.setRecureNum(0);
        }
        info.setRecureDesc(text);
        mUploadDiagnosisInfos.add(info);
    }

    public void leftClick(View view) {
        showExitDialog();
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    /**
     * 显示退出对话框
     */
    private void showExitDialog() {
        CommonDialog.Builder builder = new CommonDialog.Builder(getThisActivity());
        builder.setTitle(R.string.finish_issue_diagnosis_title)
                .setMessage(R.string.finish_issue_diagnosis_message)
                .setMessageGravity(Gravity.LEFT)
                .setNegativeButton(R.string.confirm, () -> finish())
                .setPositiveButton(R.string.cancel, () -> {

                })
                .show();
    }

    /**
     * 拉取医嘱
     */
    private void getDiagnosis(AppointmentInfo appointmentInfo) {
        if (appointmentInfo == null) {
            return;
        }
        final ProgressDialog progressDialog = createProgressDialog(getString(R.string.loading_data));
        mRecordManager.getDoctorDiagnosis(appointmentInfo.getBaseInfo().getAppointmentId(), (baseResult, doctorDiagnosisInfo) -> {
            Logger.logI(Logger.APPOINTMENT, "getDiagnosis->doctorDiagnosisInfo:" + doctorDiagnosisInfo);
            progressDialog.cancel();
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                mDiagnosisInfo = doctorDiagnosisInfo;
                initDiagnosisList();
                initDoctorDiagnosisView(doctorDiagnosisInfo);
            } else {
                ToastUtils.showShort(R.string.load_data_faild);
            }
        });
    }

    /**
     * 初始化医嘱列表
     */
    private void initDiagnosisList() {
        mDiagnosisInfos.clear();
        if (mDiagnosisInfo.getDiagnosisContentList() != null && mDiagnosisInfo.getDiagnosisContentList().size() > 0) {
            mDiagnosisInfos.addAll(mDiagnosisInfo.getDiagnosisContentList());
        }
        Logger.logD(Logger.APPOINTMENT, TAG + "->initDiagnosisList()->DiagnosisInfos:" + mDiagnosisInfos);
        if (mDiagnosisInfos.size() > 0) {
            Collections.sort(mDiagnosisInfos, new Comparator<BaseDiagnosisInfo>() {
                @Override
                public int compare(BaseDiagnosisInfo lhs, BaseDiagnosisInfo rhs) {
                    int res = 0;
                    long l;
                    long r;
                    l = DateUtil.dateToMillisecond(lhs.getInsertDt());
                    r = DateUtil.dateToMillisecond(rhs.getInsertDt());
                    if (l != r) {
                        res = l > r ? 1 : -1;
                    }
                    return res;
                }
            });
        }

        if (mDiagnosisInfos.size() > 0) {
            mRecureNum = mDiagnosisInfos.get(mDiagnosisInfos.size() - 1).getRecureNum();
        }
        Logger.logD(Logger.APPOINTMENT, TAG + "->initDiagnosisList()->DiagnosisInfos:" + mDiagnosisInfos + ",mRecureNum:" + mRecureNum);
    }

    private void initDoctorDiagnosisView(DoctorDiagnosisInfo doctorDiagnosisInfo) {
        if (doctorDiagnosisInfo != null) {
            List<DoctorDiagnosisAllInfo> doctorDiagnosisAllInfoList = new ArrayList<>();
            if (doctorDiagnosisInfo.getDiagnosisFileList().size() > 0) {
                for (DiagnosisFileInfo diagnosisFileInfo : doctorDiagnosisInfo.getDiagnosisFileList()) {
                    DoctorDiagnosisAllInfo doctorDiagnosisAllInfo = new DoctorDiagnosisAllInfo(diagnosisFileInfo);
                    doctorDiagnosisAllInfoList.add(doctorDiagnosisAllInfo);
                }
            }
            if (doctorDiagnosisInfo.getDiagnosisContentList().size() > 0) {
                for (int i = 0; i < doctorDiagnosisInfo.getDiagnosisContentList().size(); i++) {
                    if (i + 1 == doctorDiagnosisInfo.getDiagnosisContentList().size()) {
                        mDiagnosisConten = doctorDiagnosisInfo.getDiagnosisContentList().get(i).getContent();
                    }
                }
            }
            Logger.logI(Logger.APPOINTMENT, "getDoctorDiagnosis->doctorDiagnosisAllInfoList:" + doctorDiagnosisAllInfoList);
            for (int i = 0; i < doctorDiagnosisAllInfoList.size(); i++) {
                String local = SdManager.getInstance().getDepartmentPath(doctorDiagnosisAllInfoList.get(i).getDiagnosisPicture());
                String url = createDiagnosisPicUrl(doctorDiagnosisAllInfoList.get(i).getDiagnosisPicture(), getAppointmentId());
                FirstJourneyPicInfo info = new FirstJourneyPicInfo();
                info.setPicPath(local);
                info.setServiceUrl(url);
                info.setOldData(true);
                info.setPicName(doctorDiagnosisAllInfoList.get(i).getDiagnosisPicture());
                info.setUpLoadState(AppConstant.UploadFirstJourneyState.UPLOAD_FIRST_JOURNEY_STATE_SUCCESS);
                info.setProgress(100);
                mDoctorOrderPicList.add(info);
            }
            mMedicalAdapter.notifyDataSetChanged();
            mEditText.setText(mDiagnosisConten);
        }
    }

    private String createDiagnosisPicUrl(String name, int appointmentId) {
        return AppConfig.getDfsUrl() + "3010/0/" + name + "/" + appointmentId;
    }

    private int getAppointmentId() {
        return mAppointmentInfo.getBaseInfo().getAppointmentId();
    }
}
