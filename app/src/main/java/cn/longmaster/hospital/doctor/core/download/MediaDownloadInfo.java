package cn.longmaster.hospital.doctor.core.download;

import java.io.Serializable;

/**
 * 视频下载
 * Created by Yang² on 2017/12/7.
 */

public class MediaDownloadInfo implements Serializable {
    private int appointmentId;
    private String localFileName;
    private String localFilePath;
    private int state;
    private long currentSize;
    private long totalSize;

    public MediaDownloadInfo() {
    }

    public MediaDownloadInfo(int appointmentId, int state, String localFileName) {
        this.appointmentId = appointmentId;
        this.state = state;
        this.localFileName = localFileName;
    }

    public MediaDownloadInfo(int appointmentId, String localFileName, int state, long currentSize, long totalSize) {
        this.appointmentId = appointmentId;
        this.localFileName = localFileName;
        this.state = state;
        this.currentSize = currentSize;
        this.totalSize = totalSize;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getLocalFileName() {
        return localFileName;
    }

    public void setLocalFileName(String localFileName) {
        this.localFileName = localFileName;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    @Override
    public String toString() {
        return "MediaDownloadInfo{" +
                "appointmentId=" + appointmentId +
                ", localFileName='" + localFileName + '\'' +
                ", localFilePath='" + localFilePath + '\'' +
                ", state=" + state +
                ", currentSize=" + currentSize +
                ", totalSize=" + totalSize +
                '}';
    }
}
