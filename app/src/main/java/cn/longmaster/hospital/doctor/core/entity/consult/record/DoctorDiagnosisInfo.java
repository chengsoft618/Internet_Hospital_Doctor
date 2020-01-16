package cn.longmaster.hospital.doctor.core.entity.consult.record;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 医嘱信息
 * Created by JinKe on 2016-07-27.
 */
public class DoctorDiagnosisInfo implements Serializable {
    @JsonField("diagnosis_file")
    private List<DiagnosisFileInfo> diagnosisFileList;//医嘱语音和图片列表
    @JsonField("diagnosis_content")
    private List<DiagnosisContentInfo> diagnosisContentList;//医嘱内容列表

    public List<DiagnosisFileInfo> getDiagnosisFileList() {
        return diagnosisFileList;
    }

    public void setDiagnosisFileList(List<DiagnosisFileInfo> diagnosisFileList) {
        this.diagnosisFileList = diagnosisFileList;
    }

    public List<DiagnosisContentInfo> getDiagnosisContentList() {
        return diagnosisContentList;
    }

    public void setDiagnosisContentList(List<DiagnosisContentInfo> diagnosisContentList) {
        this.diagnosisContentList = diagnosisContentList;
    }

    @Override
    public String toString() {
        return "DoctorDiagnosisInfo{" +
                "diagnosisFileList=" + diagnosisFileList +
                ", diagnosisContentList=" + diagnosisContentList +
                '}';
    }
}
