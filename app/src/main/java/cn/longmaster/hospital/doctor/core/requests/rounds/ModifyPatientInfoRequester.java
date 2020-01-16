package cn.longmaster.hospital.doctor.core.requests.rounds;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;

/**
 * 修改患者信息requester
 * <p>
 * Created by W·H·K on 2018/6/19.
 */

public class ModifyPatientInfoRequester extends BaseApiUrlRequester<Void> {
    private int medicalId;
    private String patientName;
    private int gender;
    private String age;
    private int attdocId;
    private String patientIllness;
    private String diagnose;
    private String phoneNum;
    private String cardNo;
    //1是，0否
    private int important;
    private JSONArray medicalFileList;
    private String hospitalizaId;

    public ModifyPatientInfoRequester(@NonNull OnResultCallback<Void> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_MODIFY_INFORMATION;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected Void onDumpData(JSONObject data) throws JSONException {
        return null;
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("medical_id", medicalId);
        params.put("patient_name", patientName);
        params.put("gender", gender);
        params.put("age", age);
        params.put("attdoc_id", attdocId);
        params.put("attending_disease", diagnose);
        params.put("patient_illness", patientIllness);
        params.put("phone_num", phoneNum);
        params.put("card_no", cardNo);
        params.put("medical_file_list", medicalFileList);
        params.put("is_important", important);
        params.put("hospitaliza_id", hospitalizaId);
    }

    public int getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(int medicalId) {
        this.medicalId = medicalId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getAttdocId() {
        return attdocId;
    }

    public void setAttdocId(int attdocId) {
        this.attdocId = attdocId;
    }

    public String getPatientIllness() {
        return patientIllness;
    }

    public void setPatientIllness(String patientIllness) {
        this.patientIllness = patientIllness;
    }

    public String getDiagnose() {
        return diagnose;
    }

    public void setDiagnose(String diagnose) {
        this.diagnose = diagnose;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public int getImportant() {
        return important;
    }

    public void setImportant(int important) {
        this.important = important;
    }

    public JSONArray getMedicalFileList() {
        return medicalFileList;
    }

    public void setMedicalFileList(JSONArray medicalFileList) {
        this.medicalFileList = medicalFileList;
    }

    public String getHospitalizaId() {
        return hospitalizaId;
    }

    public void setHospitalizaId(String hospitalizaId) {
        this.hospitalizaId = hospitalizaId;
    }
}
