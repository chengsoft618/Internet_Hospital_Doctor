package cn.longmaster.hospital.doctor.core.entity.rounds;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2018/5/24.
 */

public class RecommendDoctorInfo implements Serializable {
    @JsonField("user_id")//
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
        return "RecommendDoctorInfo{" +
                "doctorId=" + doctorId +
                ", score=" + score +
                '}';
    }
}
