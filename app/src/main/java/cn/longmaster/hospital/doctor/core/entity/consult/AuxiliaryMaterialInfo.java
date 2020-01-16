package cn.longmaster.hospital.doctor.core.entity.consult;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;
import cn.longmaster.hospital.doctor.core.download.MediaDownloadInfo;

/**
 * 获取辅助资料列表
 *
 * @author JinKe
 * @date 2016-07-27
 */
public class AuxiliaryMaterialInfo implements Serializable {
    //
    @JsonField("appointment_id")
    private int appointmentId;
    //病历ID
    @JsonField("medical_id")
    private int medicalId;
    //材料类型ID 如CT等
    @JsonField("material_id")
    private int materialId;
    //材料名称
    @JsonField("material_name")
    private String materialName;
    //材料结果
    @JsonField("material_result")
    private String materialResult;
    //材料医院
    @JsonField("material_hosp")
    private String materialHosp;
    //材料文件名称/文件路径
    @JsonField("material_pic")
    private String materialPic;
    //材料类型 0-图片，1-dicom文件，2-切片，3-多媒体材料（视频和语音）
    @JsonField("material_type")
    private int materialType;
    //材料时间
    @JsonField("material_dt")
    private String materialDt;
    //材料审核状态 -1未上传；0-未审核；1-审核成功；2-审核不通过;3-客户端已审核提交
    @JsonField("check_state")
    private int checkState;
    //审核人ID
    @JsonField("check_name")
    private String checkName;
    //复诊次数（已弃用）
    @JsonField("recure_num")
    private int recureNum;
    //审核说明
    @JsonField("audit_desc")
    private String auditDesc;
    //dicom地址
    @JsonField("dicom")
    private String dicom;
    //上传类型 0:普通材料[包括补充资料],1:病情描述
    @JsonField("upload_type")
    private int uploadType;
    //
    @JsonField("insert_dt")
    private String insertDt;
    //
    @JsonField("material_cfg_name")
    private String materialCfgName;
    //
    @JsonField("material_ex")
    private int mediaType;
    //1为病案首页，0为其他类型,2出院小结
    @JsonField("data_type")
    private int dataType;

    public int getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(int medicalId) {
        this.medicalId = medicalId;
    }

    private MediaDownloadInfo mediaDownloadInfo;

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

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialResult() {
        return materialResult;
    }

    public void setMaterialResult(String materialResult) {
        this.materialResult = materialResult;
    }

    public String getMaterialHosp() {
        return materialHosp;
    }

    public void setMaterialHosp(String materialHosp) {
        this.materialHosp = materialHosp;
    }

    public String getMaterialPic() {
        return materialPic;
    }

    public void setMaterialPic(String materialPic) {
        this.materialPic = materialPic;
    }

    public int getMaterialType() {
        return materialType;
    }

    public void setMaterialType(int materialType) {
        this.materialType = materialType;
    }

    public String getMaterialDt() {
        return materialDt;
    }

    public void setMaterialDt(String materialDt) {
        this.materialDt = materialDt;
    }

    public int getCheckState() {
        return checkState;
    }

    public void setCheckState(int checkState) {
        this.checkState = checkState;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public int getRecureNum() {
        return recureNum;
    }

    public void setRecureNum(int recureNum) {
        this.recureNum = recureNum;
    }

    public String getAuditDesc() {
        return auditDesc;
    }

    public void setAuditDesc(String auditDesc) {
        this.auditDesc = auditDesc;
    }

    public String getDicom() {
        return dicom;
    }

    public void setDicom(String dicom) {
        this.dicom = dicom;
    }

    public int getUploadType() {
        return uploadType;
    }

    public void setUploadType(int uploadType) {
        this.uploadType = uploadType;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public String getMaterialCfgName() {
        return materialCfgName;
    }

    public void setMaterialCfgName(String materialCfgName) {
        this.materialCfgName = materialCfgName;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public MediaDownloadInfo getMediaDownloadInfo() {
        return mediaDownloadInfo;
    }

    public void setMediaDownloadInfo(MediaDownloadInfo mediaDownloadInfo) {
        this.mediaDownloadInfo = mediaDownloadInfo;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    @Override
    public String toString() {
        return "AuxiliaryMaterialInfo{" +
                "appointmentId=" + appointmentId +
                ", medicalId=" + medicalId +
                ", materialId=" + materialId +
                ", materialName='" + materialName + '\'' +
                ", materialResult='" + materialResult + '\'' +
                ", materialHosp='" + materialHosp + '\'' +
                ", materialPic='" + materialPic + '\'' +
                ", materialType=" + materialType +
                ", materialDt='" + materialDt + '\'' +
                ", checkState=" + checkState +
                ", checkName='" + checkName + '\'' +
                ", recureNum=" + recureNum +
                ", auditDesc='" + auditDesc + '\'' +
                ", dicom='" + dicom + '\'' +
                ", uploadType=" + uploadType +
                ", insertDt='" + insertDt + '\'' +
                ", materialCfgName='" + materialCfgName + '\'' +
                ", mediaType=" + mediaType +
                ", dataType=" + dataType +
                ", mediaDownloadInfo=" + mediaDownloadInfo +
                '}';
    }
}

