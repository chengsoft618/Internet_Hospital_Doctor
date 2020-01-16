package cn.longmaster.hospital.doctor.core.requests.user;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * @author ABiao_Abiao
 * @date 2019/7/15 21:52
 * @description: 3.1.59    添加确认单
 */
public class ConfirmAddRequest extends BaseApiUrlRequester<Void> {
    private String hospitalId;
    private String appointmentId;
    private String payValue;
    private String fileName;

    public ConfirmAddRequest(@NonNull OnResultCallback<Void> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected Void onDumpData(JSONObject data) throws JSONException {
        return null;
    }

    @Override
    protected int getOpType() {
        return 100573;
    }

    @Override
    protected String getTaskId() {
        return "123";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("hospital_id", hospitalId);
        params.put("appointment_id", appointmentId);
        params.put("pay_value", payValue);
        params.put("file_name", fileName);
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPayValue() {
        return payValue;
    }

    public void setPayValue(String payValue) {
        this.payValue = payValue;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
