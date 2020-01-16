package cn.longmaster.hospital.doctor.core.entity.rounds;

import com.google.common.base.MoreObjects;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.List;

/**
 * 病历信息
 * Created by W·H·K on 2018/5/9.
 */

public class RoundsMedicalInfo implements Serializable {
    //病历id
    private int medicalId;
    //患者姓名
    private String patientName;
    //患者性别 1:男，2：女
    private int patientNameSex;
    //患者年龄
    private String patientNameAge;
    //首诊医生
    private int attdocId;
    //病情摘要
    private String patientIllness;
    //诊断疾病
    private String diagnosis;
    //电话号码
    private String phoneNumber;
    //身份证号
    private String cardNumber;
    //所属医院
    private String hospitalName;
    //医院ID
    private int hospitalId;
    //住院号
    private String inHospitalNum;
    //首程图片本地
    private List<String> localPicUrl;
    //首程图片服务
    private List<String> serverPicUrl;
    //重点标记 1:重点标记
    private int important;
    //查房ID
    private int orderId;
    //首程图片
    private JSONArray medicalFileList;

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

    public int getPatientNameSex() {
        return patientNameSex;
    }

    public void setPatientNameSex(int patientNameSex) {
        this.patientNameSex = patientNameSex;
    }

    public String getPatientNameAge() {
        return patientNameAge;
    }

    public void setPatientNameAge(String patientNameAge) {
        this.patientNameAge = patientNameAge;
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

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getInHospitalNum() {
        return inHospitalNum;
    }

    public void setInHospitalNum(String inHospitalNum) {
        this.inHospitalNum = inHospitalNum;
    }

    public List<String> getLocalPicUrl() {
        return localPicUrl;
    }

    public void setLocalPicUrl(List<String> localPicUrl) {
        this.localPicUrl = localPicUrl;
    }

    public List<String> getServerPicUrl() {
        return serverPicUrl;
    }

    public void setServerPicUrl(List<String> serverPicUrl) {
        this.serverPicUrl = serverPicUrl;
    }

    public int getImportant() {
        return important;
    }

    public void setImportant(int important) {
        this.important = important;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public JSONArray getMedicalFileList() {
        return medicalFileList;
    }

    public void setMedicalFileList(JSONArray medicalFileList) {
        this.medicalFileList = medicalFileList;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("medicalId", medicalId)
                .add("patientName", patientName)
                .add("patientNameSex", patientNameSex)
                .add("patientNameAge", patientNameAge)
                .add("attdocId", attdocId)
                .add("patientIllness", patientIllness)
                .add("diagnosis", diagnosis)
                .add("phoneNumber", phoneNumber)
                .add("cardNumber", cardNumber)
                .add("hospitalName", hospitalName)
                .add("hospitalId", hospitalId)
                .add("inHospitalNum", inHospitalNum)
                .add("localPicUrl", localPicUrl)
                .add("serverPicUrl", serverPicUrl)
                .add("important", important)
                .toString();
    }
}
