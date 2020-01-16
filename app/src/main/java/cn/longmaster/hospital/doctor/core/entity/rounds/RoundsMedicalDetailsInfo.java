package cn.longmaster.hospital.doctor.core.entity.rounds;

import android.support.annotation.Nullable;

import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2018/5/16.
 */

public class RoundsMedicalDetailsInfo implements Serializable {
    @JsonField("medical_id")
    private int medicalId;
    @JsonField("order_id")
    private int orderId;
    @JsonField("patient_name")
    private String patientName;
    @JsonField("gender")
    private int gender;
    @JsonField("age")
    private String age;
    @JsonField("birthday")
    private String birthday;
    @JsonField("phone_num")
    private String phoneNum;
    @JsonField("marital_state")
    private int maritalState;
    @JsonField("national")
    private int national;
    @JsonField("birth_place")
    private String birthPlace;
    @JsonField("province")
    private String province;
    @JsonField("city")
    private String city;
    @JsonField("address")
    private String address;
    @JsonField("attending_hospital")
    private String attendingHospital;
    @JsonField("attending_dt")
    private String attendingDt;
    @JsonField("attending_disease")
    private String attendingDisease;
    @JsonField("patient_illness")
    private String patientIllness;
    @JsonField("attending_complaint")
    private String attendingComplaint;
    @JsonField("record_dt")
    private String recordDt;
    @JsonField("insert_dt")
    private String insertDt;
    @JsonField("attdoc_id")
    private int attdocId;
    @JsonField("atthosdep_id")
    private int atthosdepId;
    @JsonField("atthos_id")
    private int atthosId;
    @JsonField("launch_area")
    private int launchArea;
    @JsonField("weight")
    private int weight;
    @JsonField("temperature")
    private float temperature;
    @JsonField("career")
    private String career;
    @JsonField("hypertension")
    private int hypertension;
    @JsonField("hypotension")
    private int hypotension;
    @JsonField("pulse")
    private int pulse;
    @JsonField("breath")
    private int breath;
    @JsonField("now_history")
    private String nowHistory;
    @JsonField("family_history")
    private String familyHistory;
    @JsonField("marriage_history")
    private String marriageHistory;
    @JsonField("past_history")
    private String pastHistory;
    @JsonField("personal_history")
    private String personalHistory;
    @JsonField("sign_desc")
    private String signDesc;
    @JsonField("medical_finish_dt")
    private String medicalFinishDt;
    @JsonField("channel")
    private int channel;
    @JsonField("channel_remark")
    private String channelRemark;
    @JsonField("hosp_record_number")
    private String hospRecordNumber;
    @JsonField("first_cure_situ")
    private int firstCureSitu;
    @JsonField("drug_plan")
    private List<DrugPlanInfo> drugPlanInfos;
    @JsonField("allergy_history")
    private String allergyHistory;
    @JsonField("surgery_history")
    private String surgeryHistory;
    @JsonField("menstrual_history")
    private String menstrualHistory;
    @JsonField("height")
    private int height;
    @JsonField("surgery_plan")
    private String surgeryPlan;
    @JsonField("physical_plan")
    private String physicalPlan;
    @JsonField("card_no")
    private String cardNo;
    //1：重点 0：非重点
    @JsonField("is_important")
    private int important;
    @JsonField("visit_url")
    private String visitUrl;
    @JsonField("medical_share_url")
    private String medicalShareUrl;
    @JsonField("relation_share_url")
    private String relationShareUrl;
    @JsonField("visit_share_url")
    private String visitShareUrl;
    @JsonField("medical_file")
    private List<MedicalFileInfo> medicalFileInfos;
    //医院住院号
    @JsonField("hospitaliza_id")
    private String hospitalizaId;
    //首程图片
    @JsonField("medical_file_list")
    private List<MedicalFileInfo> medicalFileList;

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

    public String getRecordDt() {
        return recordDt;
    }

    public void setRecordDt(String recordDt) {
        this.recordDt = recordDt;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public int getAttdocId() {
        return attdocId;
    }

    public void setAttdocId(int attdocId) {
        this.attdocId = attdocId;
    }

    public int getAtthosdepId() {
        return atthosdepId;
    }

    public void setAtthosdepId(int atthosdepId) {
        this.atthosdepId = atthosdepId;
    }

    public int getAtthosId() {
        return atthosId;
    }

    public void setAtthosId(int atthosId) {
        this.atthosId = atthosId;
    }

    public int getLaunchArea() {
        return launchArea;
    }

    public void setLaunchArea(int launchArea) {
        this.launchArea = launchArea;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public int getHypertension() {
        return hypertension;
    }

    public void setHypertension(int hypertension) {
        this.hypertension = hypertension;
    }

    public int getHypotension() {
        return hypotension;
    }

    public void setHypotension(int hypotension) {
        this.hypotension = hypotension;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public int getBreath() {
        return breath;
    }

    public void setBreath(int breath) {
        this.breath = breath;
    }

    public String getNowHistory() {
        return nowHistory;
    }

    public void setNowHistory(String nowHistory) {
        this.nowHistory = nowHistory;
    }

    public String getFamilyHistory() {
        return familyHistory;
    }

    public void setFamilyHistory(String familyHistory) {
        this.familyHistory = familyHistory;
    }

    public String getMarriageHistory() {
        return marriageHistory;
    }

    public void setMarriageHistory(String marriageHistory) {
        this.marriageHistory = marriageHistory;
    }

    public String getPastHistory() {
        return pastHistory;
    }

    public void setPastHistory(String pastHistory) {
        this.pastHistory = pastHistory;
    }

    public String getPersonalHistory() {
        return personalHistory;
    }

    public void setPersonalHistory(String personalHistory) {
        this.personalHistory = personalHistory;
    }

    public String getSignDesc() {
        return signDesc;
    }

    public void setSignDesc(String signDesc) {
        this.signDesc = signDesc;
    }

    public String getMedicalFinishDt() {
        return medicalFinishDt;
    }

    public void setMedicalFinishDt(String medicalFinishDt) {
        this.medicalFinishDt = medicalFinishDt;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getChannelRemark() {
        return channelRemark;
    }

    public void setChannelRemark(String channelRemark) {
        this.channelRemark = channelRemark;
    }

    public String getHospRecordNumber() {
        return hospRecordNumber;
    }

    public void setHospRecordNumber(String hospRecordNumber) {
        this.hospRecordNumber = hospRecordNumber;
    }

    public int getFirstCureSitu() {
        return firstCureSitu;
    }

    public void setFirstCureSitu(int firstCureSitu) {
        this.firstCureSitu = firstCureSitu;
    }

    public List<DrugPlanInfo> getDrugPlanInfos() {
        return drugPlanInfos;
    }

    public void setDrugPlanInfos(List<DrugPlanInfo> drugPlanInfos) {
        this.drugPlanInfos = drugPlanInfos;
    }

    public String getAllergyHistory() {
        return allergyHistory;
    }

    public void setAllergyHistory(String allergyHistory) {
        this.allergyHistory = allergyHistory;
    }

    public String getSurgeryHistory() {
        return surgeryHistory;
    }

    public void setSurgeryHistory(String surgeryHistory) {
        this.surgeryHistory = surgeryHistory;
    }

    public String getMenstrualHistory() {
        return menstrualHistory;
    }

    public void setMenstrualHistory(String menstrualHistory) {
        this.menstrualHistory = menstrualHistory;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getSurgeryPlan() {
        return surgeryPlan;
    }

    public void setSurgeryPlan(String surgeryPlan) {
        this.surgeryPlan = surgeryPlan;
    }

    public String getPhysicalPlan() {
        return physicalPlan;
    }

    public void setPhysicalPlan(String physicalPlan) {
        this.physicalPlan = physicalPlan;
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

    public String getVisitUrl() {
        return visitUrl;
    }

    public void setVisitUrl(String visitUrl) {
        this.visitUrl = visitUrl;
    }

    public String getMedicalShareUrl() {
        return medicalShareUrl;
    }

    public void setMedicalShareUrl(String medicalShareUrl) {
        this.medicalShareUrl = medicalShareUrl;
    }

    public String getRelationShareUrl() {
        return relationShareUrl;
    }

    public void setRelationShareUrl(String relationShareUrl) {
        this.relationShareUrl = relationShareUrl;
    }

    public String getVisitShareUrl() {
        return visitShareUrl;
    }

    public void setVisitShareUrl(String visitShareUrl) {
        this.visitShareUrl = visitShareUrl;
    }

    public List<MedicalFileInfo> getMedicalFileInfos() {
        return medicalFileInfos;
    }

    public void setMedicalFileInfos(List<MedicalFileInfo> medicalFileInfos) {
        this.medicalFileInfos = medicalFileInfos;
    }

    public String getHospitalizaId() {
        return hospitalizaId;
    }

    public void setHospitalizaId(String hospitalizaId) {
        this.hospitalizaId = hospitalizaId;
    }

    public List<MedicalFileInfo> getMedicalFileList() {
        return medicalFileList;
    }

    public void setMedicalFileList(List<MedicalFileInfo> medicalFileList) {
        this.medicalFileList = medicalFileList;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof RoundsMedicalDetailsInfo) {
            return medicalId == ((RoundsMedicalDetailsInfo) obj).getMedicalId();
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(medicalId);
    }

    @Override
    public String toString() {
        return "RoundsMedicalDetailsInfo{" +
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
                ", recordDt='" + recordDt + '\'' +
                ", insertDt='" + insertDt + '\'' +
                ", attdocId=" + attdocId +
                ", atthosdepId=" + atthosdepId +
                ", atthosId=" + atthosId +
                ", launchArea=" + launchArea +
                ", weight=" + weight +
                ", temperature=" + temperature +
                ", career='" + career + '\'' +
                ", hypertension=" + hypertension +
                ", hypotension=" + hypotension +
                ", pulse=" + pulse +
                ", breath=" + breath +
                ", nowHistory='" + nowHistory + '\'' +
                ", familyHistory='" + familyHistory + '\'' +
                ", marriageHistory='" + marriageHistory + '\'' +
                ", pastHistory='" + pastHistory + '\'' +
                ", personalHistory='" + personalHistory + '\'' +
                ", signDesc='" + signDesc + '\'' +
                ", medicalFinishDt='" + medicalFinishDt + '\'' +
                ", channel=" + channel +
                ", channelRemark='" + channelRemark + '\'' +
                ", hospRecordNumber='" + hospRecordNumber + '\'' +
                ", firstCureSitu=" + firstCureSitu +
                ", drugPlanInfos=" + drugPlanInfos +
                ", allergyHistory='" + allergyHistory + '\'' +
                ", surgeryHistory='" + surgeryHistory + '\'' +
                ", menstrualHistory='" + menstrualHistory + '\'' +
                ", height=" + height +
                ", surgeryPlan='" + surgeryPlan + '\'' +
                ", physicalPlan='" + physicalPlan + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", important=" + important +
                ", visitUrl='" + visitUrl + '\'' +
                ", medicalShareUrl='" + medicalShareUrl + '\'' +
                ", relationShareUrl=" + relationShareUrl +
                ", visitShareUrl=" + visitShareUrl +
                ", medicalFileInfos=" + medicalFileInfos +
                '}';
    }
}
