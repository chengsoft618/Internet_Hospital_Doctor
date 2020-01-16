package cn.longmaster.hospital.doctor.core.entity.rounds;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;
import cn.longmaster.hospital.doctor.core.download.MediaDownloadInfo;

/**
 * Created by W·H·K on 2018/5/17.
 */

public class AuxiliaryInspectInfo implements Serializable {
    @JsonField("medical_id")//
    private int medicalId;
    @JsonField("material_id")//
    private int materialId;
    @JsonField("material_name")//
    private String materialName;
    @JsonField("material_result")//
    private String materialResult;
    @JsonField("material_hosp")//
    private String materialHosp;
    @JsonField("material_pic")//
    private String materialPic;
    @JsonField("list_data_id")//
    private int listDataId;
    @JsonField("list_data_show")//
    private int listDataShow;
    @JsonField("material_type")//
    private int materialType;
    @JsonField("material_dt")//
    private String materialDt;
    @JsonField("check_state")//
    private int checkState;
    @JsonField("check_name")//
    private String checkName;
    @JsonField("audit_desc")//
    private String auditDesc;
    @JsonField("dicom")//
    private String dicom;
    @JsonField("upload_type")//
    private int uploadType;
    @JsonField("win_width")//
    private int winWidth;
    @JsonField("win_pos")//
    private int winPos;
    @JsonField("material_cfg_name")//
    private String materialCfgName;

    private MediaDownloadInfo mediaDownloadInfo;

    public MediaDownloadInfo getMediaDownloadInfo() {
        return mediaDownloadInfo;
    }

    public void setMediaDownloadInfo(MediaDownloadInfo mediaDownloadInfo) {
        this.mediaDownloadInfo = mediaDownloadInfo;
    }

    public int getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(int medicalId) {
        this.medicalId = medicalId;
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

    public int getListDataId() {
        return listDataId;
    }

    public void setListDataId(int listDataId) {
        this.listDataId = listDataId;
    }

    public int getListDataShow() {
        return listDataShow;
    }

    public void setListDataShow(int listDataShow) {
        this.listDataShow = listDataShow;
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

    public int getWinWidth() {
        return winWidth;
    }

    public void setWinWidth(int winWidth) {
        this.winWidth = winWidth;
    }

    public int getWinPos() {
        return winPos;
    }

    public void setWinPos(int winPos) {
        this.winPos = winPos;
    }

    public String getMaterialCfgName() {
        return materialCfgName;
    }

    public void setMaterialCfgName(String materialCfgName) {
        this.materialCfgName = materialCfgName;
    }

    @Override
    public String toString() {
        return "AuxiliaryInspectInfo{" +
                "medicalId=" + medicalId +
                ", materialId=" + materialId +
                ", materialName='" + materialName + '\'' +
                ", materialResult='" + materialResult + '\'' +
                ", materialHosp='" + materialHosp + '\'' +
                ", materialPic='" + materialPic + '\'' +
                ", listDataId=" + listDataId +
                ", listDataShow=" + listDataShow +
                ", materialType=" + materialType +
                ", materialDt=" + materialDt +
                ", checkState=" + checkState +
                ", checkName='" + checkName + '\'' +
                ", auditDesc='" + auditDesc + '\'' +
                ", dicom='" + dicom + '\'' +
                ", uploadType=" + uploadType +
                ", winWidth=" + winWidth +
                ", winPos=" + winPos +
                ", materialCfgName='" + materialCfgName + '\'' +
                '}';
    }
}
