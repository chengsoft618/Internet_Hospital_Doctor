package cn.longmaster.hospital.doctor.core.upload.newupload;

/**
 * Created by YY on 17/9/21.
 */

public class MaterialFileInfo {
    private String taskId;
    private String sessionId;
    private int state;
    private int progress;
    private String materialDt;
    private String localFilePath;
    private String localFileName;
    private String filePostfix;
    private String serverFileName;
    private int appointmentId;
    private boolean isDelete;//是否需要删除
    private String newSessionId = "";//新的sessionid，为了解决未找到文件流问题

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getMaterialDt() {
        return materialDt;
    }

    public void setMaterialDt(String materialDt) {
        this.materialDt = materialDt;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public String getLocalFileName() {
        return localFileName;
    }

    public void setLocalFileName(String localFileName) {
        this.localFileName = localFileName;
    }

    public String getFilePostfix() {
        return filePostfix;
    }

    public void setFilePostfix(String filePostfix) {
        this.filePostfix = filePostfix;
    }

    public String getServerFileName() {
        return serverFileName;
    }

    public void setServerFileName(String serverFileName) {
        this.serverFileName = serverFileName;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public String getNewSessionId() {
        return newSessionId;
    }

    public void setNewSessionId(String newSessionId) {
        this.newSessionId = newSessionId;
    }

    @Override
    public String toString() {
        return "MaterialFileInfo{" +
                "taskId='" + taskId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", state=" + state +
                ", progress=" + progress +
                ", materialDt='" + materialDt + '\'' +
                ", localFilePath='" + localFilePath + '\'' +
                ", localFileName='" + localFileName + '\'' +
                ", filePostfix='" + filePostfix + '\'' +
                ", serverFileName='" + serverFileName + '\'' +
                ", appointmentId=" + appointmentId +
                ", isDelete=" + isDelete +
                ", newSessionId='" + newSessionId + '\'' +
                '}';
    }
}
