package cn.longmaster.hospital.doctor.core.entity.doctor;

import java.io.Serializable;

/**
 * Created by W·H·K on 2018/11/26.
 */

public class MyDataDoctorInfo implements Serializable {
    int doctorId;
    boolean election;

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public boolean isElection() {
        return election;
    }

    public void setElection(boolean election) {
        this.election = election;
    }

    @Override
    public String toString() {
        return "MyDataDoctorInfo{" +
                "doctorId=" + doctorId +
                ", election=" + election +
                '}';
    }
}
