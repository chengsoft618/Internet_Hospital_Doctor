package cn.longmaster.hospital.doctor.core.entity.consult.record;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 医嘱语音和图片
 * Created by JinKe on 2016-07-27.
 */
public class DiagnosisFileInfo extends BaseDiagnosisInfo implements Serializable {
    @JsonField("diagnosis_picture")
    private String diagnosisPicture;//文件名称
    @JsonField("file_type")
    private int fileType;//文件类型 0：图片1：语音2：病历截图
    @JsonField("audio_time")
    private int audioTime;//语音时长
    @JsonField("remark")
    private String remark;//备注

    public String getDiagnosisPicture() {
        return diagnosisPicture;
    }

    public void setDiagnosisPicture(String diagnosisPicture) {
        this.diagnosisPicture = diagnosisPicture;
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

    @Override
    public String toString() {
        return "DiagnosisFileInfo{" +
                "diagnosisPicture='" + diagnosisPicture + '\'' +
                ", fileType=" + fileType +
                ", audioTime=" + audioTime +
                ", remark='" + remark + '\'' +
                '}';
    }
}
