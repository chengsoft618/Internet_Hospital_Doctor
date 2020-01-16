package cn.longmaster.hospital.doctor.core.entity.consult.record;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 系统材料配置
 * Created by Yang² on 2016/7/26.
 */
public class MaterialInfo implements Serializable {
    @JsonField("material_id")
    private int materialId;//材料id
    @JsonField("material_name")
    private String materialName;//材料名称
    @JsonField("material_desc")
    private String materialDesc;//材料信息描述
    @JsonField("material_func")
    private String materialFunc;//材料功能
    @JsonField("insert_dt")
    private String insertDt;//材料插入时间

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

    public String getMaterialDesc() {
        return materialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        this.materialDesc = materialDesc;
    }

    public String getMaterialFunc() {
        return materialFunc;
    }

    public void setMaterialFunc(String materialFunc) {
        this.materialFunc = materialFunc;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "MaterialInfo{" +
                "materialId=" + materialId +
                ", materialName=" + materialName +
                ", materialDesc=" + materialDesc +
                ", materialFunc=" + materialFunc +
                ", insertDt=" + insertDt +
                '}';
    }
}
