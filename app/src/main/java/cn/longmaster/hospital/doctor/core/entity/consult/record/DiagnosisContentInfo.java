package cn.longmaster.hospital.doctor.core.entity.consult.record;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 医嘱内容
 * Created by JinKe on 2016-07-27.
 */
public class DiagnosisContentInfo extends BaseDiagnosisInfo implements Serializable {
    @JsonField("doctor_id")
    private int doctorId;//医生ID
    @JsonField("content")
    private String content;//医嘱内容
    @JsonField("ex_type")
    private int exType;//0:医嘱1:补充材料说明2:强制结束说明3：医嘱语音文件路径(附加说明:2: content(内容结构说明json):end_target:结束目标1:医生2:患者end_desc:取消原因)

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
        return "DiagnosisContentInfo{" +
                ", doctorId=" + doctorId +
                ", content='" + content + '\'' +
                ", exType=" + exType +
                '}';
    }
}
