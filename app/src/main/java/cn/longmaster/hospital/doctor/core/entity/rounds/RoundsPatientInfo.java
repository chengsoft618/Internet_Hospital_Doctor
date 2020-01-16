package cn.longmaster.hospital.doctor.core.entity.rounds;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2018/5/14.
 */

public class RoundsPatientInfo implements Serializable {
    @JsonField("medical_id")
    private int medicalId;//病历id
    @JsonField("order_id")
    private int orderId;//订单id
    @JsonField("patient_name")
    private String patientName;
    @JsonField("gender")
    private int gender;//性别
    @JsonField("age")
    private String age;//年龄
    @JsonField("birthday")
    private String birthday;//出生日期
    @JsonField("phone_num")
    private String phoneNum;//电话
    @JsonField("marital_state")
    private int maritalState;//H婚姻状态
    @JsonField("national")
    private int national;//民族
    @JsonField("birth_place")
    private String birthPlace;//籍贯
    @JsonField("province")
    private String province;//省份
    @JsonField("city")
    private String city;//城市
    @JsonField("address")
    private String address;//详细地址
    @JsonField("attending_hospital")
    private String attendingHospital;//首诊医生
    @JsonField("attending_dt")
    private String attendingDt;//首诊时间
    @JsonField("attending_disease")
    private String attendingDisease;//初步诊断
    @JsonField("patient_illness")
    private String patientIllness;//病情述求
    @JsonField("attending_complaint")
    private String attendingComplaint;//主诉
    @JsonField("is_important")
    private int important;//重点病历
    @JsonField("visit_url")
    private String visitUrl;//回访信息
    //病历资料整理状态 0未整理 1 已整理
    @JsonField("medical_finish_state")
    private int medicalFinishState;
    @JsonField("medical_file")
    private List<MedicalFileInfo> MedicalFiles;

    public int getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(int medicalId) {
        this.medicalId = medicalId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getMaritalState() {
        return maritalState;
    }

    public void setMaritalState(int maritalState) {
        this.maritalState = maritalState;
    }

    public int getNational() {
        return national;
    }

    public void setNational(int national) {
        this.national = national;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAttendingHospital() {
        return attendingHospital;
    }

    public void setAttendingHospital(String attendingHospital) {
        this.attendingHospital = attendingHospital;
    }

    public String getAttendingDt() {
        return attendingDt;
    }

    public void setAttendingDt(String attendingDt) {
        this.attendingDt = attendingDt;
    }

    public String getAttendingDisease() {
        return attendingDisease;
    }

    public void setAttendingDisease(String attendingDisease) {
        this.attendingDisease = attendingDisease;
    }

    public String getPatientIllness() {
        return patientIllness;
    }

    public void setPatientIllness(String patientIllness) {
        this.patientIllness = patientIllness;
    }

    public String getAttendingComplaint() {
        return attendingComplaint;
    }

    public void setAttendingComplaint(String attendingComplaint) {
        this.attendingComplaint = attendingComplaint;
    }

    public boolean isImportant() {
        return important == 1;
    }

    public int getImportant() {
        return important;
    }

    public void setImportant(int important) {
        this.important = important;
    }

    public String getVisitUrl() {
        return visitUrl;
    }

    public void setVisitUrl(String visitUrl) {
        this.visitUrl = visitUrl;
    }

    public boolean medicalIsFinish() {
        return medicalFinishState == 1;
    }

    public int getMedicalFinishState() {
        return medicalFinishState;
    }

    public void setMedicalFinishState(int medicalFinishState) {
        this.medicalFinishState = medicalFinishState;
    }

    public List<MedicalFileInfo> getMedicalFiles() {
        return MedicalFiles;
    }

    public void setMedicalFiles(List<MedicalFileInfo> medicalFiles) {
        MedicalFiles = medicalFiles;
    }

    @Override
    public String toString() {
        return "RoundsPatientInfo{" +
                "medicalId=" + medicalId +
                ", orderId=" + orderId +
                ", patientName='" + patientName + '\'' +
                ", gender=" + gender +
                ", age='" + age + '\'' +
                ", birthday='" + birthday + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", maritalState=" + maritalState +
                ", national=" + national +
                ", birthPlace='" + birthPlace + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", attendingHospital='" + attendingHospital + '\'' +
                ", attendingDt='" + attendingDt + '\'' +
                ", attendingDisease='" + attendingDisease + '\'' +
                ", patientIllness='" + patientIllness + '\'' +
                ", attendingComplaint='" + attendingComplaint + '\'' +
                ", important=" + important +
                ", visitUrl='" + visitUrl + '\'' +
                ", medicalFinishState=" + medicalFinishState +
                ", MedicalFiles=" + MedicalFiles +
                '}';
    }
}
