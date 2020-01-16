package cn.longmaster.hospital.doctor.core.entity.consult.record;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 医嘱上传信息实体类
 * Created by yangyong on 16/8/25.
 */
public class UploadDiagnosisInfo {
    @JsonField("diagnosis_picture")
    private String diagnosisPicture;//报告图文件名
    @JsonField("recure_num")
    private int recureNum;//复诊次数
    @JsonField("recure_desc")
    private String recureDesc;//医嘱描述
    @JsonField("audio_src")
    private String audioSrc;//语音文件路径名
    @JsonField("audio_dt")
    private int audioDt;//语音时长

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

    public String getRecureDesc() {
        return recureDesc;
    }

    public void setRecureDesc(String recureDesc) {
        this.recureDesc = recureDesc;
    }

    public String getAudioSrc() {
        return audioSrc;
    }

    public void setAudioSrc(String audioSrc) {
        this.audioSrc = audioSrc;
    }

    public int getAudioDt() {
        return audioDt;
    }

    public void setAudioDt(int audioDt) {
        this.audioDt = audioDt;
    }

    @Override
    public String toString() {
        return "UploadDiagnosisInfo{" +
                "diagnosisPicture='" + diagnosisPicture + '\'' +
                ", recureNum=" + recureNum +
                ", recureDesc='" + recureDesc + '\'' +
                ", audioSrc='" + audioSrc + '\'' +
                ", audioDt=" + audioDt +
                '}';
    }
}
