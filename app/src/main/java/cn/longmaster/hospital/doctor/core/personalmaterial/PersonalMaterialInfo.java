package cn.longmaster.hospital.doctor.core.personalmaterial;

import java.io.Serializable;

/**
 * 个人资料info
 * <p>
 * Created by W·H·K on 2018/8/2.
 */

public class PersonalMaterialInfo implements Serializable {
    private int userId;
    private String taskId;
    private String materialName;
    private String localFileName;
    private String svrFileName;
    private float uploadProgress;
    private int uploadState;
    private int fileType;
    private String insertDt;
    private int doctorId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getLocalFileName() {
        return localFileName;
    }

    public void setLocalFileName(String localFileName) {
        this.localFileName = localFileName;
    }

    public String getSvrFileName() {
        return svrFileName;
    }

    public void setSvrFileName(String svrFileName) {
        this.svrFileName = svrFileName;
    }

    public float getUploadProgress() {
        return uploadProgress;
    }

    public void setUploadProgress(float uploadProgress) {
        this.uploadProgress = uploadProgress;
    }

    public int getUploadState() {
        return uploadState;
    }

    public void setUploadState(int uploadState) {
        this.uploadState = uploadState;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public String toString() {
        return "PersonalMaterialInfo{" +
                "userId=" + userId +
                ", taskId='" + taskId + '\'' +
                ", materialName='" + materialName + '\'' +
                ", localFileName='" + localFileName + '\'' +
                ", svrFileName='" + svrFileName + '\'' +
                ", uploadProgress=" + uploadProgress +
                ", uploadState=" + uploadState +
                ", fileType=" + fileType +
                ", insertDt='" + insertDt + '\'' +
                ", doctorId=" + doctorId +
                '}';
    }
}
