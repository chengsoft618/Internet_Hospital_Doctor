package cn.longmaster.hospital.doctor.core.requests.dutyclinic;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * @author ABiao_Abiao
 * @date 2020/1/3 16:12
 * @description: 更新值班门诊患者信息
 */
public class UpdateRoomPatientRequester extends BaseApiUrlRequester<String> {
    //订单ID
    private int orderId;
    //病历id
    private int medicalId;
    //患者姓名
    private String patientName;
    //身份证号码
    private String cardNo;
    //患者手机号码
    private String phoneNum;
    //就诊时间
    private String cureDt;
    //文件列表
    private JSONArray fileList;

    public UpdateRoomPatientRequester(@NonNull OnResultCallback<String> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String onDumpData(JSONObject data) throws JSONException {
        return null;
    }

    @Override
    protected int getOpType() {
        return 100608;
    }

    @Override
    protected String getTaskId() {
        return "100608";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("order_id", orderId);
        params.put("medical_id", medicalId);
        params.put("patient_name", patientName);
        params.put("card_no", cardNo);
        params.put("phone_num", phoneNum);
        params.put("cure_dt", cureDt);
        params.put("file_list", fileList);
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setMedicalId(int medicalId) {
        this.medicalId = medicalId;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setCureDt(String cureDt) {
        this.cureDt = cureDt;
    }

    public void setFileList(JSONArray fileList) {
        this.fileList = fileList;
    }
}
