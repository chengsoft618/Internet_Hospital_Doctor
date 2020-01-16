package cn.longmaster.hospital.doctor.ui.consult.consultant;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import cn.longmaster.doctorlibrary.util.common.DateUtil;
import cn.longmaster.doctorlibrary.util.common.MD5Util;
import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.img.BitmapUtil;
import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.remote.ScheduleOrImageInfo;
import cn.longmaster.hospital.doctor.core.entity.consultant.AppointmentId;
import cn.longmaster.hospital.doctor.core.entity.consultant.PaymentInfo;
import cn.longmaster.hospital.doctor.core.entity.consultant.PaymentPic;
import cn.longmaster.hospital.doctor.core.entity.consultant.SchedulePaymentInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.appointment.ScheduleServiceRequester;
import cn.longmaster.hospital.doctor.core.requests.consultant.GetSchedulePaymentRequester;
import cn.longmaster.hospital.doctor.core.requests.consultant.ModifySchedulePaymentRequester;
import cn.longmaster.hospital.doctor.core.upload.newupload.UploadPicFileInfo;
import cn.longmaster.hospital.doctor.core.upload.newupload.UploadState;
import cn.longmaster.hospital.doctor.core.upload.simple.FileUploadResult;
import cn.longmaster.hospital.doctor.core.upload.simple.PaymentPictureUploader;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.ui.CaptureActivity;
import cn.longmaster.hospital.doctor.ui.SimplePicBrowseActivity;
import cn.longmaster.hospital.doctor.ui.consult.PickPhotoActivity;
import cn.longmaster.hospital.doctor.ui.consult.adapter.PaymentImgGridViewAdapter;
import cn.longmaster.hospital.doctor.view.ScrollGridView;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.upload.OnNginxUploadStateCallback;
import cn.longmaster.utils.PermissionConstants;
import cn.longmaster.utils.PermissionHelper;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.Utils;
import io.reactivex.disposables.Disposable;

/**
 * 支付确认单
 * Created by Yang² on 2017/12/22.
 */

public class PaymentConfirmActivity extends BaseActivity {
    private static final String TAG = PaymentConfirmActivity.class.getSimpleName();
    private final int REQUEST_CODE_PHOTO = 100;//页面请求码:选择相片
    private final int REQUEST_CODE_CAMERA = 101; //页面请求码:选择相机
    private final int REQUEST_CODE_PREVIEW = 102; //页面请求码:图片预览
    private final int PAYMENT_PIC_MAX_NUM = 2;

    @FindViewById(R.id.activity_payment_confirm_schedule_id_tv)
    private TextView mScheduleIdTv;
    @FindViewById(R.id.activity_payment_confirm_time_tv)
    private TextView mTimeTv;
    @FindViewById(R.id.activity_payment_confirm_num_tv)
    private TextView mNumTv;
    @FindViewById(R.id.activity_payment_confirm_doctor_tv)
    private TextView mDoctorTv;
    @FindViewById(R.id.activity_payment_confirm_hospital_tv)
    private TextView mHospitalTv;
    @FindViewById(R.id.activity_payment_confirm_consult_id_tv)
    private TextView mConsultIdTv;
    @FindViewById(R.id.activity_payment_confirm_way_rg)
    private RadioGroup mWayRg;
    @FindViewById(R.id.activity_payment_confirm_duration_rb)
    private RadioButton mDurationRb;
    @FindViewById(R.id.activity_payment_confirm_record_rb)
    private RadioButton mRecordRb;
    @FindViewById(R.id.activity_payment_confirm_date_tv)
    private TextView mDateTv;
    @FindViewById(R.id.activity_payment_confirm_right_arrow_iv)
    private ImageView mRightArrowIv;
    @FindViewById(R.id.activity_payment_confirm_begin_time_tv)
    private TextView mBeginTimeTv;
    @FindViewById(R.id.activity_payment_confirm_end_time_tv)
    private TextView mEndTimeTv;
    @FindViewById(R.id.activity_payment_confirm_duration_et)
    private EditText mDurationEt;
    @FindViewById(R.id.activity_payment_confirm_money_et)
    private EditText mMoneyEt;
    @FindViewById(R.id.activity_payment_confirm_img_list_sgv)
    private ScrollGridView mImgListSgv;
    @FindViewById(R.id.activity_payment_confirm_save_btn)
    private Button mSaveBtn;
    @AppApplication.Manager
    private DoctorManager mDoctorManager;

    private Dialog mPhotoWayDialog;

    private PaymentImgGridViewAdapter mGridViewAdapter;
    private int mScheduleId;
    private boolean mEditMode = false;

    private List<UploadPicFileInfo> mUploadPicList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirm);
        ViewInjecter.inject(this);
        initData();
        setDefault();
        getScheduleInfo();
        getSchedulePayment();
        initView();
        addListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                if (resultCode == RESULT_OK) {
                    ArrayList<String> photoList = data.getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATHS);
                    for (String path : photoList) {
                        String newPath = revisePictureFormat(path);
                        addLocalPic(path);
                        uploadPicture(path);
                    }
                }
                break;

            case REQUEST_CODE_PHOTO:
                if (resultCode == RESULT_OK) {
                    ArrayList<String> photoList = data.getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SELECT_PATHS);
                    for (String path : photoList) {
                        String newPath = revisePictureFormat(path);
                        addLocalPic(path);
                        uploadPicture(path);
                    }
                }
                break;

            case REQUEST_CODE_PREVIEW:
                if (mEditMode && resultCode == RESULT_OK && data != null) {
                    ArrayList<String> delList = data.getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DELETE_PIC);
                    //遍历删除
                    Iterator<UploadPicFileInfo> it = mUploadPicList.iterator();
                    while (it.hasNext()) {
                        UploadPicFileInfo uploadPicFileInfo = it.next();
                        if (uploadPicFileInfo != null && !StringUtils.isEmpty(uploadPicFileInfo.getUrlPath())) {
                            for (String url : delList) {
                                if (uploadPicFileInfo.getUrlPath().equals(url)) {
                                    it.remove();
                                }
                            }
                        }
                    }
                    mGridViewAdapter.setData(mUploadPicList);
                }
                break;
        }
    }

    private void initData() {
        mScheduleId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SCHEDULE_ID, 0);
    }

    private void initView() {
        mGridViewAdapter = new PaymentImgGridViewAdapter(getActivity());
        mImgListSgv.setAdapter(mGridViewAdapter);
    }

    private void addListener() {
        mGridViewAdapter.setOnItemAddClickListener(new PaymentImgGridViewAdapter.onItemAddClickListener() {
            @Override
            public void onItemAdd() {
                if (checkPicNum()) {
                    showPhotoWayDialog();
                }
            }
        });
        mGridViewAdapter.setOnItemDeleteClickListener(new PaymentImgGridViewAdapter.onItemDeleteClickListener() {
            @Override
            public void onItemDelete(int position, UploadPicFileInfo uploadPicFileInfo) {
                mGridViewAdapter.setSelectPosition(-1);
                mUploadPicList.remove(position);
                mGridViewAdapter.setData(mUploadPicList);
            }
        });
        mGridViewAdapter.setOnItemClickListener(new PaymentImgGridViewAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position, UploadPicFileInfo uploadPicFileInfo) {
                if (uploadPicFileInfo.getState() == UploadState.NOT_UPLOADED || uploadPicFileInfo.getState() == UploadState.UPLOADING) {
                    return;
                }
                if (uploadPicFileInfo.getState() == UploadState.UPLOAD_FAILED) {
                    //重传
                    upDatePicState(uploadPicFileInfo.getLocalFilePath(), UploadState.NOT_UPLOADED);
                    uploadPicture(uploadPicFileInfo.getLocalFilePath());
                    return;
                }
                ArrayList<String> localFilePaths = new ArrayList<>();
                ArrayList<String> serverUrls = new ArrayList<>();
                for (UploadPicFileInfo picFileInfo : mUploadPicList) {
                    if (picFileInfo.getState() == UploadState.UPLOAD_SUCCESS) {
                        localFilePaths.add(SdManager.getInstance().getPaymentPic() + picFileInfo.getServerFileName());
                        serverUrls.add(picFileInfo.getUrlPath());
                    }
                }
                Intent intent = new Intent(getActivity(), SimplePicBrowseActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOCALPATHS, localFilePaths);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SERVERURLS, serverUrls);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_INDEX, serverUrls.indexOf(uploadPicFileInfo.getUrlPath()));
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SHOW_DELETE, mEditMode);
                startActivityForResult(intent, REQUEST_CODE_PREVIEW);
                mGridViewAdapter.setSelectPosition(-1);
                mGridViewAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getScheduleInfo() {
        ScheduleServiceRequester requester = new ScheduleServiceRequester(new OnResultListener<ScheduleOrImageInfo>() {
            @Override
            public void onResult(BaseResult baseResult, ScheduleOrImageInfo scheduleOrImageInfo) {
                if (baseResult.getCode() != RESULT_SUCCESS) {
                    return;
                }
                displayScheduleInfo(scheduleOrImageInfo);
                getDoctorInfo(scheduleOrImageInfo.getDoctorId());
            }
        });
        requester.scheduingId = mScheduleId;
        requester.doPost();
    }

    private void getDoctorInfo(int doctorId) {
        mDoctorManager.getDoctor(doctorId, new DoctorManager.OnDoctorInfoLoadListener() {
            @Override
            public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                mDoctorTv.setText(getString(R.string.consult_payment_confirm_doctor, doctorBaseInfo.getRealName()));
                mHospitalTv.setText(doctorBaseInfo.getHospitalName());
            }

            @Override
            public void onFailed(int code, String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void getSchedulePayment() {
        GetSchedulePaymentRequester requester = new GetSchedulePaymentRequester(new OnResultListener<SchedulePaymentInfo>() {
            @Override
            public void onResult(BaseResult baseResult, SchedulePaymentInfo schedulePaymentInfo) {
                if (baseResult.getCode() != RESULT_SUCCESS) {
                    return;
                }
                if (schedulePaymentInfo != null) {
                    mEditMode = schedulePaymentInfo.getPaymentInfo() == null;
                    displaySchedulePayment(schedulePaymentInfo);
                    setViewEnabled();
                }
            }
        });
        requester.scheduingId = mScheduleId;
        requester.doPost();
    }

    private void setDefault() {
        mScheduleIdTv.setText(getString(R.string.consult_payment_confirm_schedule_id, mScheduleId + ""));
        mTimeTv.setText("");
        mNumTv.setText(getString(R.string.consult_payment_confirm_num, " "));
        mDoctorTv.setText(getString(R.string.consult_payment_confirm_doctor, ""));
        mHospitalTv.setText("");
        mConsultIdTv.setText("");
    }

    private void setViewEnabled() {
        mSaveBtn.setText(mEditMode ? R.string.save : R.string.consult_payment_confirm_modify);
        mDurationRb.setEnabled(mEditMode);
        mRecordRb.setEnabled(mEditMode);
        mDateTv.setEnabled(mEditMode);
        mRightArrowIv.setEnabled(mEditMode);
        mBeginTimeTv.setEnabled(mEditMode);
        mEndTimeTv.setEnabled(mEditMode);
        mDurationEt.setEnabled(mEditMode);
        mMoneyEt.setEnabled(mEditMode);
        mGridViewAdapter.setClickable(mEditMode);
        if (mEditMode) {
            if (mUploadPicList.size() == 0
                    || !StringUtils.isEmpty(mUploadPicList.get(mUploadPicList.size() - 1).getLocalFilePath())
                    || !StringUtils.isEmpty(mUploadPicList.get(mUploadPicList.size() - 1).getUrlPath())) {
                mUploadPicList.add(new UploadPicFileInfo());
            }
        } else {
            if (mUploadPicList.size() > 0
                    && StringUtils.isEmpty(mUploadPicList.get(mUploadPicList.size() - 1).getLocalFilePath())
                    && StringUtils.isEmpty(mUploadPicList.get(mUploadPicList.size() - 1).getUrlPath())) {
                mUploadPicList.remove(mUploadPicList.size() - 1);
            }
        }
        mGridViewAdapter.setData(mUploadPicList);
    }

    private void displayScheduleInfo(ScheduleOrImageInfo scheduleOrImageInfo) {
        if (scheduleOrImageInfo == null) {
            return;
        }
        if (!StringUtils.isEmpty(scheduleOrImageInfo.getBeginDt()) && !StringUtils.isEmpty(scheduleOrImageInfo.getEndDt())) {
            if (scheduleOrImageInfo.getBeginDt().contains("2099")) {
                mTimeTv.setText(R.string.time_to_be_determined);
            } else {
                mTimeTv.setText(DateUtil.getTime(scheduleOrImageInfo.getBeginDt(), "MM月dd日 HH:mm")
                        + "-" + DateUtil.getTime(scheduleOrImageInfo.getEndDt(), "HH:mm"));
            }
        }
    }

    private void displaySchedulePayment(SchedulePaymentInfo schedulePaymentInfo) {
        if (schedulePaymentInfo == null) {
            return;
        }
        if (schedulePaymentInfo.getAppointmentList() != null && schedulePaymentInfo.getAppointmentList().size() > 0) {
            mConsultIdTv.setText(getAppointmentIds(schedulePaymentInfo.getAppointmentList()));
            mNumTv.setText(getString(R.string.consult_payment_confirm_num, schedulePaymentInfo.getAppointmentList().size() + ""));
        }
        PaymentInfo paymentInfo = schedulePaymentInfo.getPaymentInfo();
        if (paymentInfo != null) {
            if (paymentInfo.getPaymentType() == AppConstant.PaymentType.PAYMENT_TYPE_DURATION) {
                mDurationRb.setChecked(true);
            } else if (paymentInfo.getPaymentType() == AppConstant.PaymentType.PAYMENT_TYPE_RECORD) {
                mRecordRb.setChecked(true);
            }
            mDateTv.setText(DateUtil.getTime(paymentInfo.getBeginDt(), "yyyy-MM-dd"));
            mBeginTimeTv.setText(DateUtil.getTime(paymentInfo.getBeginDt(), "HH:mm"));
            mEndTimeTv.setText(DateUtil.getTime(paymentInfo.getEndDt(), "HH:mm"));
            mDurationEt.setText(paymentInfo.getDtLength());
            mMoneyEt.setText(paymentInfo.getPayValue());
            setPaymentPic(paymentInfo);
        }
    }

    private String getAppointmentIds(List<AppointmentId> appointmentList) {
        String ids = "";
        for (int i = 0; i < appointmentList.size(); i++) {
            ids += appointmentList.get(i).getAppointmentId();
            if (i != appointmentList.size() - 1) {
                ids += ",";
            }
        }
        return ids;
    }

    private void setPaymentPic(PaymentInfo paymentInfo) {
        if (paymentInfo.getPaymentPicList() != null && paymentInfo.getPaymentPicList().size() > 0) {
            mUploadPicList.clear();
            for (PaymentPic paymentPic : paymentInfo.getPaymentPicList()) {
                if (!StringUtils.isEmpty(paymentPic.getFileName())) {
                    UploadPicFileInfo uploadPicFileInfo = new UploadPicFileInfo();
                    uploadPicFileInfo.setServerFileName(paymentPic.getFileName());
                    uploadPicFileInfo.setUrlPath(paymentInfo.getPicHead() + paymentPic.getFileName());
                    uploadPicFileInfo.setState(UploadState.UPLOAD_SUCCESS);
                    mUploadPicList.add(uploadPicFileInfo);
                }
            }
            mGridViewAdapter.setData(mUploadPicList);
        }
    }

    private void addLocalPic(String filePath) {
        if (!StringUtils.isEmpty(filePath)) {
            mUploadPicList.remove(mUploadPicList.size() - 1);
            UploadPicFileInfo uploadPicFileInfo = new UploadPicFileInfo(filePath);
            mUploadPicList.add(uploadPicFileInfo);
            mUploadPicList.add(new UploadPicFileInfo());
            mGridViewAdapter.setData(mUploadPicList);
        }

    }

    private void upDatePicState(String filePath, int state, String urlName) {
        for (UploadPicFileInfo picFileInfo : mUploadPicList) {
            if (filePath.equals(picFileInfo.getLocalFilePath())) {
                picFileInfo.setState(state);
                if (!StringUtils.isEmpty(urlName)) {
                    picFileInfo.setServerFileName(urlName);
                    picFileInfo.setUrlPath(AppConfig.getDfsUrl() + "3026/" + "0/" + urlName);
                }
            }
        }
        AppHandlerProxy.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                mGridViewAdapter.setData(mUploadPicList);
            }
        });
    }

    private void upDatePicState(String filePath, int state) {
        upDatePicState(filePath, state, "");
    }

    @OnClick({R.id.activity_payment_confirm_date_tv
            , R.id.activity_payment_confirm_right_arrow_iv
            , R.id.activity_payment_confirm_begin_time_tv
            , R.id.activity_payment_confirm_end_time_tv
            , R.id.activity_payment_confirm_save_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_payment_confirm_date_tv:
            case R.id.activity_payment_confirm_right_arrow_iv:
                pickDate(mDateTv);
                break;

            case R.id.activity_payment_confirm_begin_time_tv:
                if (!isFastClick()) {
                    pickTime(mBeginTimeTv);
                }
                break;

            case R.id.activity_payment_confirm_end_time_tv:
                if (!isFastClick()) {
                    pickTime(mEndTimeTv);
                }
                break;

            case R.id.activity_payment_confirm_save_btn:
                //保存，修改
                if (!mEditMode) {
                    mEditMode = true;
                    setViewEnabled();
                    return;
                }
                if (checkInput()) {
                    if (!isFastClick()) {
                        showConfirmDialog();
                    }
                }
                break;
        }
    }

    public void rightClick(View view) {
        Intent intent = new Intent(getActivity(), SchedulingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private boolean checkInput() {
        if (!mDurationRb.isChecked() && !mRecordRb.isChecked()) {
            showToast(R.string.consult_payment_confirm_toast);
            return false;
        }
        if (StringUtils.isEmpty(mDateTv.getText().toString().trim())) {
            showToast(R.string.consult_payment_confirm_toast);
            return false;
        }
        if (StringUtils.isEmpty(mBeginTimeTv.getText().toString().trim())) {
            showToast(R.string.consult_payment_confirm_toast);
            return false;
        }
        if (StringUtils.isEmpty(mEndTimeTv.getText().toString().trim())) {
            showToast(R.string.consult_payment_confirm_toast);
            return false;
        }
        if (mDurationRb.isChecked() && StringUtils.isEmpty(mDurationEt.getText().toString().trim())) {
            showToast(R.string.consult_payment_confirm_toast);
            return false;
        }
        if (StringUtils.isEmpty(mMoneyEt.getText().toString().trim())) {
            showToast(R.string.consult_payment_confirm_toast);
            return false;
        }
        return true;
    }

    /**
     * 选择日期
     */
    private void pickDate(final TextView textView) {
        //生成一个DatePickerDialog对象，并显示。显示的DatePickerDialog控件可以选择年月日，并设置
        final Calendar calendar = Calendar.getInstance(Locale.CHINA);
        String date = textView.getText().toString();
        if (!StringUtils.isEmpty(date)) {
            calendar.setTime(new Date(DateUtil.dateToMillisecond(date + " 00:00:00")));
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //修改日历控件的年，月，日
                //这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                textView.setText(DateUtil.getTime(calendar.getTime(), "yyyy-MM-dd"));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    /**
     * 选择时间
     *
     * @param textView
     */
    private void pickTime(final TextView textView) {
        final Calendar calendar = Calendar.getInstance(Locale.CHINA);
        String time = textView.getText().toString();
        if (!StringUtils.isEmpty(time)) {
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.split(":")[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(time.split(":")[1]));
        }
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //同DatePickerDialog控件
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                textView.setText(DateUtil.getTime(calendar.getTime(), "HH:mm"));
                textView.setHint("");
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    /**
     * 确认框
     */
    private void showConfirmDialog() {
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.layout_payment_confirm_dialog, null);
        final Dialog confirmDialog = new AlertDialog.Builder(getActivity(), R.style.custom_noActionbar_window_style).create();
        confirmDialog.show();
        confirmDialog.setContentView(layout);
        confirmDialog.setCanceledOnTouchOutside(true);
        confirmDialog.setCancelable(true);

        Window win = confirmDialog.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);

        TextView durationTv = (TextView) layout.findViewById(R.id.layout_payment_confirm_duration_tv);
        TextView moneyTv = (TextView) layout.findViewById(R.id.layout_payment_confirm_money_tv);
        TextView cancelTv = (TextView) layout.findViewById(R.id.layout_payment_confirm_cancel_tv);
        TextView confirmTv = (TextView) layout.findViewById(R.id.layout_payment_confirm_confirm_tv);

        durationTv.setText(mDurationEt.getText().toString().trim());
        moneyTv.setText(mMoneyEt.getText().toString().trim());

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
            }
        });
        confirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
                modifySchedulePayment();
            }
        });
    }

    private void modifySchedulePayment() {
        final ProgressDialog dialog = createProgressDialog(getString(R.string.data_committing));
        ModifySchedulePaymentRequester requester = new ModifySchedulePaymentRequester(new OnResultListener<Void>() {
            @Override
            public void onResult(BaseResult baseResult, Void aVoid) {
                dialog.dismiss();
                if (baseResult.getCode() == RESULT_SUCCESS) {
                    showToast(R.string.data_commit_success);
                    mEditMode = false;
                    reSetPicData();
                    setViewEnabled();
                } else {
                    showToast(R.string.no_network_connection);
                }
            }
        });
        requester.scheduingId = mScheduleId;
        requester.paymentType = mDurationRb.isChecked() ? AppConstant.PaymentType.PAYMENT_TYPE_DURATION : AppConstant.PaymentType.PAYMENT_TYPE_RECORD;
        requester.beginDt = mDateTv.getText().toString() + " " + mBeginTimeTv.getText().toString() + ":00";
        requester.endDt = mDateTv.getText().toString() + " " + mEndTimeTv.getText().toString() + ":00";
        requester.dtLength = mDurationEt.getText().toString().trim();
        requester.payValue = mMoneyEt.getText().toString().trim();
        requester.picList = getPicList();
        requester.doPost();
    }

    private List<String> getPicList() {
        List<String> picList = new ArrayList<>();
        for (UploadPicFileInfo picFileInfo : mUploadPicList) {
            if (picFileInfo != null && !StringUtils.isEmpty(picFileInfo.getServerFileName())) {
                picList.add(picFileInfo.getServerFileName());
            }
        }
        return picList;
    }

    private void reSetPicData() {
        for (UploadPicFileInfo picFileInfo : mUploadPicList) {
            if (picFileInfo != null && !StringUtils.isEmpty(picFileInfo.getLocalFilePath())) {
                picFileInfo.setLocalFilePath("");
            }
        }
    }

    /**
     * 选择拍照还是相册
     */
    private void showPhotoWayDialog() {
        if (mPhotoWayDialog != null) {
            if (mPhotoWayDialog.isShowing()) {
                return;
            }
            mPhotoWayDialog.show();
            return;
        }
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.layout_payment_photo_way_dialog, null);
        mPhotoWayDialog = new AlertDialog.Builder(getActivity(), R.style.customAnimation_noActionbar_window_style).create();
        mPhotoWayDialog.show();
        mPhotoWayDialog.setContentView(layout);
        mPhotoWayDialog.setCanceledOnTouchOutside(true);
        mPhotoWayDialog.setCancelable(true);

        Window win = mPhotoWayDialog.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);

        LinearLayout cameraLl = (LinearLayout) layout.findViewById(R.id.layout_payment_photo_way_camera_ll);
        LinearLayout albumLl = (LinearLayout) layout.findViewById(R.id.layout_payment_photo_way_album_ll);
        TextView cancelTv = (TextView) layout.findViewById(R.id.layout_payment_photo_way_cancel_tv);

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoWayDialog.dismiss();
            }
        });
        cameraLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoWayDialog.dismiss();
                Disposable disposable = PermissionHelper.init(getActivity()).addPermissionGroups(PermissionConstants.CAMERA)
                        .requestEachCombined()
                        .subscribe(permission -> {
                            if (permission.granted) {
                                CaptureActivity.startActivity(getActivity(), false, true, PAYMENT_PIC_MAX_NUM + 1 - mUploadPicList.size(), REQUEST_CODE_CAMERA);
                                // `permission.name` is granted !
                            } else {
                                new CommonDialog.Builder(getActivity())
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
        albumLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoWayDialog.dismiss();
                Intent intent = new Intent(getActivity(), PickPhotoActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_PAYMENT, true);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MAX_COUNT, PAYMENT_PIC_MAX_NUM + 1 - mUploadPicList.size());
                startActivityForResult(intent, REQUEST_CODE_PHOTO);
            }
        });
    }

    private boolean checkPicNum() {
        if (mUploadPicList.size() - 1 >= PAYMENT_PIC_MAX_NUM) {
            showToast(getString(R.string.consult_payment_confirm_pic_upload_max, PAYMENT_PIC_MAX_NUM));
            return false;
        }
        return true;
    }

    private String revisePictureFormat(String path) {
        if (StringUtils.isEmpty(path)) {
            return "";
        }
        String newFileName = MD5Util.md5(System.currentTimeMillis() + "" + Math.random() * 1000) + ".jpg";
        String newFilePath = SdManager.getInstance().getPaymentPic() + newFileName;
        String extraName = path.substring(path.lastIndexOf(".") + 1);
        Logger.logD(Logger.APPOINTMENT, TAG + "->startUpload()->文件后缀名:" + extraName);
        //需要将png或者jpeg格式的图片保存为jpg格式,不然服务器无法生成缩略图
        if (extraName.matches("[pP][nN][gG]")
                || extraName.matches("[jJ][pP][eE][gG]")
                || extraName.matches("[jJ][pP][gG]")) {
            BitmapUtil.savePNG2JPEG(path, newFilePath);
            BitmapUtil.compressImageFile(newFilePath);
        }
        return newFileName;
    }

    private void uploadPicture(final String filePath) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->uploadPicture()->");
        PaymentPictureUploader uploader = new PaymentPictureUploader(new OnNginxUploadStateCallback() {
            @Override
            public void onUploadComplete(String sessionId, final int httpResultCode, final String content) {
                Logger.logD(Logger.APPOINTMENT, TAG + "->onUploadComplete()->sessionId:" + sessionId + ",httpResultCode:" + httpResultCode + ",content:" + content);
                if (httpResultCode == 200 && !StringUtils.isEmpty(content)) {
                    try {
                        FileUploadResult result = JsonHelper.toObject(new JSONObject(content), FileUploadResult.class);
                        if (result.getCode() == 0) {
                            upDatePicState(filePath, UploadState.UPLOAD_SUCCESS, result.getFileName());
                        } else {
                            upDatePicState(filePath, UploadState.UPLOAD_FAILED);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    upDatePicState(filePath, UploadState.UPLOAD_FAILED);
                }
            }

            @Override
            public void onUploadProgresssChange(String sessionId, long successBytes, long blockByte, long totalBytes) {
                float progressLength = (float) (successBytes + blockByte) / totalBytes * 100;
                Logger.logD(Logger.APPOINTMENT, TAG + "->onUploadProgresssChange()->sessionId:" + sessionId + ",progressLength:" + progressLength);
            }

            @Override
            public void onUploadException(String sessionId, Exception exception) {
                Logger.logD(Logger.APPOINTMENT, TAG + "->onUploadException()->sessionId:" + sessionId);
                upDatePicState(filePath, UploadState.UPLOAD_FAILED);
            }

            @Override
            public void onUploadCancle(String sessionId) {
                Logger.logD(Logger.APPOINTMENT, TAG + "->onUploadCancle()->sessionId:" + sessionId);
                upDatePicState(filePath, UploadState.UPLOAD_FAILED);
            }
        });
        uploader.filePath = filePath;
        uploader.startUpload();
    }


}
