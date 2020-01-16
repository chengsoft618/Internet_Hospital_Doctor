package cn.longmaster.hospital.doctor.core.entity.consult.record;

import java.io.Serializable;

/**
 * 语音、图片医嘱和文字医嘱的合体
 * Created by Yang² on 2016/8/16.
 */
public class DoctorDiagnosisAllInfo implements Serializable {
    private int appointmentId;//预约ID
    private String diagnosisPicture;//文件名称
    private int recureNum;//复诊次数
    private int fileType;//文件类型 0：图片1：语音2：病历截图
    private int audioTime;//语音时长
    private String remark;//备注
    private String insertDt;//插入时间

    private int doctorId;//医生ID
    private String content;//医嘱内容
    private int exType;//0:医嘱1:补充材料说明2:强制结束说明3：医嘱语音文件路径(附加说明:2: content(内容结构说明json):end_target:结束目标1:医生2:患者end_desc:取消原因)

    public DoctorDiagnosisAllInfo() {

    }

    public DoctorDiagnosisAllInfo(DiagnosisFileInfo diagnosisFileInfo) {
        this.appointmentId = diagnosisFileInfo.getAppointmentId();
        this.diagnosisPicture = diagnosisFileInfo.getDiagnosisPicture();
        this.recureNum = diagnosisFileInfo.getRecureNum();
        this.fileType = diagnosisFileInfo.getFileType();
        this.audioTime = diagnosisFileInfo.getAudioTime();
        this.remark = diagnosisFileInfo.getRemark();
        this.insertDt = diagnosisFileInfo.getInsertDt();
    }

    public DoctorDiagnosisAllInfo(DiagnosisContentInfo diagnosisContentInfo) {
        this.appointmentId = diagnosisContentInfo.getAppointmentId();
        this.doctorId = diagnosisContentInfo.getDoctorId();
        this.recureNum = diagnosisContentInfo.getRecureNum();
        this.content = diagnosisContentInfo.getContent();
        this.exType = diagnosisContentInfo.getExType();
        this.insertDt = diagnosisContentInfo.getInsertDt();
        this.fileType = -1;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDiagnosisPicture() {
        return diagnosisPicture;
    }

    public void setDiagnosisPicture(String diagnosisPicture) {
        this.diagnosisPicture = diagnosisPicture;
    }

    public int getRecureNum() {
        return recureNum;
    }

    public void setRecureNum(int recureNum) {
        this.recureNum = recureNum;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public int getAudioTime() {
        return audioTime;
    }

    public void setAudioTime(int audioTime) {
        this.audioTime = audioTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getExType() {
        return exType;
    }

    public void setExType(int exType) {
        this.exType = exType;
    }

    @Override
    public String toString() {
        return "DoctorDiagnosisAllInfo{" +
                "appointmentId=" + appointmentId +
                ", diagnosisPicture='" + diagnosisPicture + '\'' +
                ", recureNum=" + recureNum +
                ", fileType=" + fileType +
                ", remark='" + remark + '\'' +
                ", insertDt='" + insertDt + '\'' +
                ", doctorId=" + doctorId +
                ", content='" + content + '\'' +
                ", exType=" + exType +
                '}';
    }
}
