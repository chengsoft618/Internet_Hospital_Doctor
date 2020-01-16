package cn.longmaster.hospital.doctor.core.entity.user;

import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;
import cn.longmaster.hospital.doctor.core.entity.consult.AuxiliaryMaterialInfo;

/**
 * @author ABiao_Abiao
 * @date 2019/7/16 13:36
 * @description:
 */
public class PatientDataInfo {


    /**
     * medical_id : 10000008
     * patient_name : 张三
     * hospitaliza_id :
     * insert_dt : 2019-02-18 11:41:11
     * atthos_id : 178
     * hospital_name : 青海省互助土族自治县人民医院
     * data_list : [{"id":"700","medical_id":"10000008","material_id":"119","material_name":"CT超","material_result":"","material_hosp":"","material_pic":"201902.18.10000008.11-41-10_1550461270243958.jpg","list_data_id":"","list_data_show":"0","material_type":"0","material_dt":"2019-02-18 00:00:00","check_state":"1","check_name":"超级管理员","recure_num":"0","audit_desc":"","dicom":"","upload_type":"0","is_course":"0","win_width":"","win_pos":"","upload_uid":"0","upload_utype":"0","sort_num":"0","insert_dt":"2019-02-18 11:41:11","material_cfg_name":"CT超","material_ex":"0"}]
     */

    @JsonField("medical_id")
    private String medicalId;
    @JsonField("patient_name")
    private String patientName;
    @JsonField("hospitaliza_id")
    private String hospitalizaId;
    @JsonField("insert_dt")
    private String insertDt;
    @JsonField("atthos_id")
    private String atthosId;
    @JsonField("hospital_name")
    private String hospitalName;
    @JsonField("data_list")
    private List<AuxiliaryMaterialInfo> dataList;

    public String getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(String medicalId) {
        this.medicalId = medicalId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getHospitalizaId() {
        return hospitalizaId;
    }

    public void setHospitalizaId(String hospitalizaId) {
        this.hospitalizaId = hospitalizaId;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public String getAtthosId() {
        return atthosId;
    }

    public void setAtthosId(String atthosId) {
        this.atthosId = atthosId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public List<AuxiliaryMaterialInfo> getDataList() {
        return dataList;
    }

    public void setDataList(List<AuxiliaryMaterialInfo> dataList) {
        this.dataList = dataList;
    }

}
