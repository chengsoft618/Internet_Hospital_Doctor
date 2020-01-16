package cn.longmaster.hospital.doctor.core.upload.newupload;

import java.util.List;

/**
 * Created by YY on 17/9/21.
 */

public class MaterialTaskInfo {
    private int userId;
    private String taskId;
    private int state;
    private int appointmentId;
    private int materialId;
    private String materialDt;
    private int recureNum;
    private int doctorId;
    private List<MaterialFileInfo> materialFileInfos;

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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getMaterialDt() {
        return materialDt;
    }

    public void setMaterialDt(String materialDt) {
        this.materialDt = materialDt;
    }

    public int getRecureNum() {
        return recureNum;
    }

    public void setRecureNum(int recureNum) {
        this.recureNum = recureNum;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public List<MaterialFileInfo> getMaterialFileInfos() {
        return materialFileInfos;
    }

    public void setMaterialFileInfos(List<MaterialFileInfo> materialFileInfos) {
        this.materialFileInfos = materialFileInfos;
    }

    @Override
    public String toString() {
        return "MaterialTaskInfo{" +
                "userId=" + userId +
                ", taskId='" + taskId + '\'' +
                ", state=" + state +
                ", appointmentId=" + appointmentId +
                ", materialId=" + materialId +
                ", materialDt='" + materialDt + '\'' +
                ", recureNum=" + recureNum +
                ", doctorId=" + doctorId +
                ", materialFileInfos=" + materialFileInfos +
                '}';
    }
}
