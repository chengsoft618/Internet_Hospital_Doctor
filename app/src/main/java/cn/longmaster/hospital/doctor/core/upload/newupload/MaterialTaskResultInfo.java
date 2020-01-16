package cn.longmaster.hospital.doctor.core.upload.newupload;

import java.io.Serializable;

/**
 * Created by YY on 17/9/30.
 */

public class MaterialTaskResultInfo implements Serializable {
    private int userId;
    private int appointmentId;
    private int successCount;
    private int failedCount;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }

    @Override
    public String toString() {
        return "MaterialTaskResultInfo{" +
                "userId=" + userId +
                ", appointmentId=" + appointmentId +
                ", successCount=" + successCount +
                ", failedCount=" + failedCount +
                '}';
    }
}
