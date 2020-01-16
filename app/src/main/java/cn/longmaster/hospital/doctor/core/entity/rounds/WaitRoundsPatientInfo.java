package cn.longmaster.hospital.doctor.core.entity.rounds;

import com.google.common.base.Objects;

import java.io.Serializable;

import javax.annotation.Nullable;

import cn.longmaster.doctorlibrary.util.json.JsonField;
import cn.longmaster.utils.StringUtils;

/**
 * Created by W·H·K on 2018/9/13.
 */

public class WaitRoundsPatientInfo implements Serializable {
    //病例号
    @JsonField("medical_id")
    private int medicalId;
    //患者姓名
    @JsonField("patient_name")//
    private String patientName;
    //患者年龄
    @JsonField("age")
    private String age;
    //病情描述
    @JsonField("patient_illness")
    private String patientIllness;
    //插入时间
    @JsonField("insert_dt")
    private String insertDt;
    //首诊id
    @JsonField("attdoc_id")
    private int attdocId;
    //首诊名字
    @JsonField("attdoc_name")
    private String attdocName;
    //性别
    @JsonField("gender")
    private int gender;
    //重点标注
    @JsonField("is_important")//
    private int important;
    //就诊编号
    @JsonField("order_id")
    private String orderId;
    //住院号
    @JsonField("hospitaliza_id")
    private String hospitalizaId;
    //诊断疾病
    @JsonField("attending_disease")
    private String attendingDisease;

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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPatientIllness() {
        return patientIllness;
    }

    public void setPatientIllness(String patientIllness) {
        this.patientIllness = patientIllness;
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

    public String getAttdocName() {
        return attdocName;
    }

    public void setAttdocName(String attdocName) {
        this.attdocName = attdocName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getImportant() {
        return important;
    }

    public void setImportant(int important) {
        this.important = important;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getHospitalizaId() {
        return hospitalizaId;
    }

    public void setHospitalizaId(String hospitalizaId) {
        this.hospitalizaId = hospitalizaId;
    }

    public String getAttendingDisease() {
        return attendingDisease;
    }

    public void setAttendingDisease(String attendingDisease) {
        this.attendingDisease = attendingDisease;
    }

    public boolean hasOrderId() {
        return !StringUtils.isTrimEmpty(orderId) && !StringUtils.equals(orderId, "0");
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(medicalId);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof WaitRoundsPatientInfo) {
            return medicalId == ((WaitRoundsPatientInfo) obj).getMedicalId();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "WaitRoundsPatientInfo{" +
                "medicalId=" + medicalId +
                ", patientName='" + patientName + '\'' +
                ", age='" + age + '\'' +
                ", patientIllness='" + patientIllness + '\'' +
                ", insertDt='" + insertDt + '\'' +
                ", attdocId=" + attdocId +
                ", attdocName='" + attdocName + '\'' +
                ", gender=" + gender +
                ", important=" + important +
                '}';
    }
}
