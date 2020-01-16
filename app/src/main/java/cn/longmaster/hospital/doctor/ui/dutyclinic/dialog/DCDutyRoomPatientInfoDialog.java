package cn.longmaster.hospital.doctor.ui.dutyclinic.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.img.BitmapUtil;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.executor.AppExecutors;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyRoomPatientItem;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyRoomPatientMedical;
import cn.longmaster.hospital.doctor.core.entity.event.BrowserPicEvent;
import cn.longmaster.hospital.doctor.core.manager.dutyclinic.DCManager;
import cn.longmaster.hospital.doctor.core.manager.room.ConsultRoomManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.rounds.RoundsDeleteMaterialRequest;
import cn.longmaster.hospital.doctor.core.upload.simple.BaseFileUploader;
import cn.longmaster.hospital.doctor.core.upload.simple.DCDutyPatientDiseasePicUploader;
import cn.longmaster.hospital.doctor.ui.PicBrowseActivity;
import cn.longmaster.hospital.doctor.ui.consult.PickPhotoActivity;
import cn.longmaster.hospital.doctor.ui.dutyclinic.adapter.DCDutyRoomPatientPicAdapter;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.PhoneUtil;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.TimePickUtils;
import cn.longmaster.utils.TimeUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/12/25 15:45
 * @description: 患者信息
 */
public class DCDutyRoomPatientInfoDialog extends DialogFragment {
    private final int REQUEST_CODE_FOR_PIC_SELECT = 200;
    private ImageView dialogDcDutyRoomPatientCloseIv;
    private TextView dialogDcDutyRoomPatientTitleTv;
    private View dialogDcDutyRoomPatientTitleV;
    private TextView patientNameTv;
    private EditText patientNameEt;
    private View patientNameV;
    private TextView patientPhoneTv;
    private EditText patientPhoneEt;
    private View patientPhoneV;
    private TextView patientIdCardTv;
    private EditText patientIdCardEt;
    private View patientIdCardV;
    private TextView patientSeeDoctorTimeTv;
    private TextView patientSeeDoctorTimeDescTv;
    private View patientSeeDoctorTimeV;
    private TextView selectAlbumTv;
    private View selectAlbumV;
    private RecyclerView dialogDcDutyRoomPatientRv;
    private TextView commitTv;
    private int orderId;
    private DCDutyRoomPatientItem patientItem;
    private String cureDate;
    @AppApplication.Manager
    private DCManager dcManager;
    private DCDutyRoomPatientPicAdapter dcDutyRoomPatientPicAdapter;
    private List<DCDutyRoomPatientMedical> medicalList = new ArrayList<>();
    private ConsultRoomManager mConsultRoomManager;
    private OnDismissListener onDismissListener;

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public DCDutyRoomPatientItem getPatientItem() {
        return patientItem;
    }

    public void setPatientItem(DCDutyRoomPatientItem patientItem) {
        this.patientItem = patientItem;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.AppTheme);
        dcManager = AppApplication.getInstance().getManager(DCManager.class);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        onDismissListener.onDismiss();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mConsultRoomManager = AppApplication.getInstance().getManager(ConsultRoomManager.class);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.dialog_dc_duty_room_patient_info, null);
        dialogDcDutyRoomPatientCloseIv = rootView.findViewById(R.id.dialog_dc_duty_room_patient_close_iv);
        dialogDcDutyRoomPatientTitleTv = (TextView) rootView.findViewById(R.id.dialog_dc_duty_room_patient_title_tv);
        dialogDcDutyRoomPatientTitleV = (View) rootView.findViewById(R.id.dialog_dc_duty_room_patient_title_v);
        patientNameTv = (TextView) rootView.findViewById(R.id.patient_name_tv);
        patientNameEt = (EditText) rootView.findViewById(R.id.patient_name_et);
        patientNameV = (View) rootView.findViewById(R.id.patient_name_v);
        patientPhoneTv = (TextView) rootView.findViewById(R.id.patient_phone_tv);
        patientPhoneEt = (EditText) rootView.findViewById(R.id.patient_phone_et);
        patientPhoneV = (View) rootView.findViewById(R.id.patient_phone_v);
        patientIdCardTv = (TextView) rootView.findViewById(R.id.patient_id_card_tv);
        patientIdCardEt = (EditText) rootView.findViewById(R.id.patient_id_card_et);
        patientIdCardV = (View) rootView.findViewById(R.id.patient_id_card_v);
        patientSeeDoctorTimeTv = (TextView) rootView.findViewById(R.id.patient_see_doctor_time_tv);
        patientSeeDoctorTimeDescTv = (TextView) rootView.findViewById(R.id.patient_see_doctor_time_desc_tv);
        patientSeeDoctorTimeV = (View) rootView.findViewById(R.id.patient_see_doctor_time_v);
        selectAlbumTv = (TextView) rootView.findViewById(R.id.select_album_tv);
        selectAlbumV = (View) rootView.findViewById(R.id.select_album_v);
        dialogDcDutyRoomPatientRv = (RecyclerView) rootView.findViewById(R.id.dialog_dc_duty_room_patient_rv);
        commitTv = (TextView) rootView.findViewById(R.id.commit_tv);
        initDatas();
        //dcDutyRoomPatientPicAdapter.remove();
        initViews();
        initListener();
        builder.setView(rootView);
        return builder.create();
    }

    private void initDatas() {
        dcDutyRoomPatientPicAdapter = new DCDutyRoomPatientPicAdapter(R.layout.item_dc_duty_room_patient_pic, new ArrayList<>(0));
        dcDutyRoomPatientPicAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            showDeleteDialog(adapter, position);
            return false;
        });
        dcDutyRoomPatientPicAdapter.setOnItemClickListener((adapter, view, position) -> {
            showPic(position, adapter.getData());
        });

        cureDate = TimeUtils.getNowString();
    }

    private void showDeleteDialog(BaseQuickAdapter adapter, int position) {
        DCDutyRoomPatientMedical medical = (DCDutyRoomPatientMedical) adapter.getItem(position);
        medicalList.remove(medical);
        if (null != medical) {
            new CommonDialog.Builder(getActivity())
                    .setMessage("确定将选择的图片删除吗？")
                    .setPositiveButton(R.string.cancel, () -> {

                    })
                    .setNegativeButton("删除", () -> {
                        if (medical.getMedicalId() == 0) {
                            adapter.remove(position);
                        } else {
                            RoundsDeleteMaterialRequest request = new RoundsDeleteMaterialRequest((baseResult, s) -> {
                                if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                                    adapter.remove(position);
                                } else {
                                    ToastUtils.showShort(R.string.data_delete_fail);
                                }
                            });
                            request.medicalId = medical.getMedicalId();
                            //request.materialId = medical.getId();
                            request.materialPic = medical.getFileName();
                            request.doPost();
                        }

                    })
                    .setCanceledOnTouchOutside(false)
                    .setCancelable(false)
                    .show();
        }
    }

    private void initViews() {
        dialogDcDutyRoomPatientRv.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        //dialogDcDutyRoomPatientRv.addItemDecoration(new GridSectionAverageGapItemDecoration(10,10,20,15));
//        dialogDcDutyRoomPatientRv.setNestedScrollingEnabled(false);
//        dialogDcDutyRoomPatientRv.setHasFixedSize(true);
        dialogDcDutyRoomPatientRv.setAdapter(dcDutyRoomPatientPicAdapter);
        dialogDcDutyRoomPatientTitleTv.setText(isAdd() ? "新建患者" : "上传资料");
        patientSeeDoctorTimeDescTv.setText(TimeUtils.getNowDayString());
    }

    private void initListener() {
        dialogDcDutyRoomPatientCloseIv.setOnClickListener(v -> {
            dismiss();
        });
        if (!isAdd()) {
            displayPatientInfo(patientItem);
        }
        patientSeeDoctorTimeDescTv.setOnClickListener(v -> {
            TimePickUtils.showDialogPickTime(getContext(), (date, v1) -> {
                patientSeeDoctorTimeDescTv.setText(TimeUtils.date2String(date, "yyyy-MM-dd"));
                cureDate = TimeUtils.date2String(date, "yyyy-MM-dd HH:mm:ss");
            });
        });
        selectAlbumTv.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PickPhotoActivity.class);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PIC_COUNT, 0);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MAX_COUNT, 1);
            startActivityForResult(intent, REQUEST_CODE_FOR_PIC_SELECT);
        });
        commitTv.setOnClickListener(v -> {
            if (StringUtils.isEmpty(getString(patientNameEt))) {
                ToastUtils.showShort("请填写患者姓名");
                return;
            }
            if (StringUtils.isEmpty(getString(patientPhoneEt))) {
                ToastUtils.showShort("请填写患者电话");
                return;
            }
            if (!PhoneUtil.isPhoneNumber(getString(patientPhoneEt))) {
                ToastUtils.showShort("请填写正确的患者电话");
                return;
            }
            dcManager.updateRoomPatient(orderId, patientItem == null ? 0 : patientItem.getMedicalId(), getString(patientNameEt)
                    , getString(patientPhoneEt), getString(patientIdCardEt), cureDate, medicalList, new DefaultResultCallback<String>() {
                        @Override
                        public void onSuccess(String s, BaseResult baseResult) {
                            ToastUtils.showShort(getString(patientNameEt) + "患者资料已上传");
                            mConsultRoomManager.sendDoctorDataMakeSureOpt(getString(patientNameEt));
                            dismiss();
                        }

                        @Override
                        public void onFail(BaseResult result) {
                            super.onFail(result);
                            if (result.getCode() == 102) {
                                ToastUtils.showShort("患者联系电话不能填写医生或销售代表电话");
                            } else if (result.getCode() == 120) {
                                ToastUtils.showShort("患者联系电话不能填写医生或销售代表电话");
                            } else {
                                ToastUtils.showShort(R.string.no_network_connection);
                            }
                        }

                        @Override
                        public void onFinish() {

                        }
                    });
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_FOR_PIC_SELECT) {
                if (data != null) {
                    List<String> albumPhotoList = data.getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SELECT_PATHS);
                    for (String path : albumPhotoList) {
                        startUpload(path);
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startUpload(String path) {
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
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String result = jsonObject.getString("file_name");
                    DCDutyRoomPatientMedical medical = new DCDutyRoomPatientMedical();
                    medical.setInsertDt(TimeUtils.getNowString());
                    medical.setFileName(result);
                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            dcDutyRoomPatientPicAdapter.addData(medical);
                            medicalList.add(medical);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        dialog.setOnDismissListener(dialog1 -> {
            //uploader.startUpload();
        });
    }

    private void displayPatientInfo(DCDutyRoomPatientItem info) {
        cureDate = info.getCureDt();
        patientNameEt.setText(info.getPatientName());
        patientPhoneEt.setText(info.getPhoneNum());
        patientIdCardEt.setText(info.getCardNo());
        if (!StringUtils.isEmpty(info.getCureDt())) {
            patientSeeDoctorTimeDescTv.setText(TimeUtils.string2String(info.getCureDt(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"));
        }

        dcDutyRoomPatientPicAdapter.setNewData(info.getCheckList());
    }

    private String getString(TextView tv) {
        return tv == null ? "" : tv.getText().toString().trim();
    }

    private boolean isAdd() {
        return patientItem == null;
    }

    /**
     * 创建等待对话框
     *
     * @param msg        要显示的消息
     * @param cancelable 是否可以取消
     * @return 返回对话框
     */
    protected ProgressDialog createProgressDialog(String msg, boolean cancelable) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(false);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(cancelable);
        return progressDialog;
    }

    private void showPic(int position, List<DCDutyRoomPatientMedical> items) {
        List<String> picUrls = new ArrayList<>();
        for (int i = 0; i < LibCollections.size(items); i++) {
            picUrls.add(AppConfig.getFirstJourneyUrl() + items.get(i).getFileName());
        }
        BrowserPicEvent browserPicEvent = new BrowserPicEvent();
        browserPicEvent.setIndex(position);
        browserPicEvent.setAssistant(false);
        browserPicEvent.setServerFilePaths(picUrls);
        Intent intent = new Intent(getActivity(), PicBrowseActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_BROWSER_INFO, browserPicEvent);
        startActivity(intent);
    }

    public interface OnDismissListener {
        void onDismiss();
    }
}
