package cn.longmaster.hospital.doctor.core.entity.dutyclinic;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * @author ABiao_Abiao
 * @date 2020/1/3 15:03
 * @description:
 */
public class DCDutyRoomPatientMedical {

    /**
     * id : 8
     * medical_id : 10002186
     * type : 0
     * file_name : 201805.09.500070.141_99_201612060238_1525867779228436-1.jpg
     * insert_dt : 2018-05-09 20:09:40
     */
    //检查资料id
    @JsonField("id")
    private int id;
    //病历id
    @JsonField("medical_id")
    private int medicalId;
    //检查资料类型
    @JsonField("type")
    private int type;
    //检查资料文件名
    @JsonField("file_name")
    private String fileName;
    //检查资料插入时间
    @JsonField("insert_dt")
    private String insertDt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(int medicalId) {
        this.medicalId = medicalId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }
}
