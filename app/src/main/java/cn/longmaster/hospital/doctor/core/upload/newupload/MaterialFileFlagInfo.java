package cn.longmaster.hospital.doctor.core.upload.newupload;

import java.io.Serializable;

/**
 * 本地文件标记实体类
 * Created by YY on 17/10/17.
 */

public class MaterialFileFlagInfo implements Serializable {
    private int appointmentId;
    private String localFilePath;
    private String sessionId;
    private int uploadState;

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getUploadState() {
        return uploadState;
    }

    public void setUploadState(int uploadState) {
        this.uploadState = uploadState;
    }

    @Override
    public String toString() {
        return "MaterialFileFlagInfo{" +
                "appointmentId=" + appointmentId +
                ", localFilePath='" + localFilePath + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", uploadState=" + uploadState +
                '}';
    }
}
