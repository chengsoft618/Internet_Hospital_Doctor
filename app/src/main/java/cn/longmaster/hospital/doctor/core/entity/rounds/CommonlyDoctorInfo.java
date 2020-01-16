package cn.longmaster.hospital.doctor.core.entity.rounds;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 常用医生信息
 * <p>
 * Created by W·H·K on 2018/5/14.
 */
public class CommonlyDoctorInfo implements Serializable {
    @JsonField("doctor_id")//
    private int doctorId;
    @JsonField("score")//
    private double score;

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "CommonlyDoctorInfo{" +
                "doctorId=" + doctorId +
                ", score=" + score +
                '}';
    }
}
