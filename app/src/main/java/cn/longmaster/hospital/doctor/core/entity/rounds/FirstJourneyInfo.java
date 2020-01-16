package cn.longmaster.hospital.doctor.core.entity.rounds;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2018/5/17.
 */

public class FirstJourneyInfo implements Serializable {
    @JsonField("patient_name")
    private String patientName;//
    @JsonField("gender")
    private String gender;//
    @JsonField("age")
    private String age;//
    @JsonField("patient_illness")
    private String patientIllness;//

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    @Override
    public String toString() {
        return "FirstJourneyInfo{" +
                "patientName='" + patientName + '\'' +
                ", gender=" + gender +
                ", age='" + age + '\'' +
                ", patientIllness='" + patientIllness + '\'' +
                '}';
    }
}
