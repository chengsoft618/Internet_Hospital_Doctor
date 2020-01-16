package cn.longmaster.hospital.doctor.core.requests.rounds;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.rounds.WaitRoundsPatientInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;

/**
 * 添加患者requester
 * <p>
 * Created by W·H·K on 2018/6/19.
 */

public class PatientAddRequester extends BaseApiUrlRequester<WaitRoundsPatientInfo> {
    private int orderId;
    private String patientName;
    private int gender;
    private String age;
    private int attdocId;
    private String patientIllness;
    private String diagnose;
    private String phoneNum;
    private String cardNo;
    private JSONArray medicalFileList;
    private int important;
    private String hospitalizaId;

    public PatientAddRequester(@NonNull OnResultCallback<WaitRoundsPatientInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_ADD_PATIENT;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected WaitRoundsPatientInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data.getJSONObject("data"), WaitRoundsPatientInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("order_id", orderId);
        params.put("patient_name", patientName);
        params.put("gender", gender);
        params.put("age", age);
        params.put("attdoc_id", attdocId);
        params.put("attending_disease", diagnose);
        params.put("patient_illness", patientIllness);
        params.put("medical_file_list", medicalFileList);
        params.put("phone_num", phoneNum);
        params.put("card_no", cardNo);
        params.put("is_important", important);
        params.put("hospitaliza_id", hospitalizaId);
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setAttdocId(int attdocId) {
        this.attdocId = attdocId;
    }

    public void setPatientIllness(String patientIllness) {
        this.patientIllness = patientIllness;
    }

    public void setDiagnose(String diagnose) {
        this.diagnose = diagnose;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public void setMedicalFileList(JSONArray medicalFileList) {
        this.medicalFileList = medicalFileList;
    }

    public void setImportant(int important) {
        this.important = important;
    }

    public void setHospitalizaId(String hospitalizaId) {
        this.hospitalizaId = hospitalizaId;
    }
}
