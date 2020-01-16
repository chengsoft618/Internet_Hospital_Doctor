package cn.longmaster.hospital.doctor.core.entity.rounds;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2018/5/17.
 */

public class MedicalFileInfo implements Serializable {
    @JsonField("id")
    private String id;
    @JsonField("file_name")
    private String fileName;
    @JsonField("medical_id")
    private String medicalId;
    @JsonField("type")
    private int type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(String medicalId) {
        this.medicalId = medicalId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MedicalFileInfo{" +
                "fileName='" + fileName + '\'' +
                ", medicalId='" + medicalId + '\'' +
                ", type=" + type +
                '}';
    }
}
