package cn.longmaster.hospital.doctor.core.entity.consultant;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2017/12/28.
 */

public class AttDoctorInfo implements Serializable {
    @JsonField("attending_doctor_user_id")
    private int attDoctorId;
    @JsonField("att_doctor_real_name")
    private String attDoctorName;

    public int getAttDoctorId() {
        return attDoctorId;
    }

    public void setAttDoctorId(int attDoctorId) {
        this.attDoctorId = attDoctorId;
    }

    public String getAttDoctorName() {
        return attDoctorName;
    }

    public void setAttDoctorName(String attDoctorName) {
        this.attDoctorName = attDoctorName;
    }

    @Override
    public String toString() {
        return "AttDoctorInfo{" +
                "attDoctorId=" + attDoctorId +
                ", attDoctorName='" + attDoctorName + '\'' +
                '}';
    }
}
