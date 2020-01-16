package cn.longmaster.hospital.doctor.core.entity.consult;

import java.util.List;

/**
 * 辅助检查材料
 * Created by Tengshuxiang on 2016-09-12.
 */
public class AuxiliaryMaterialResult {
    private String materialRemindDesc;
    private List<AuxiliaryMaterialInfo> auxiliaryMaterialInfos;

    public String getMaterialRemindDesc() {
        return materialRemindDesc;
    }

    public void setMaterialRemindDesc(String materialRemindDesc) {
        this.materialRemindDesc = materialRemindDesc;
    }

    public List<AuxiliaryMaterialInfo> getAuxiliaryMaterialInfos() {
        return auxiliaryMaterialInfos;
    }

    public void setAuxiliaryMaterialInfos(List<AuxiliaryMaterialInfo> auxiliaryMaterialInfos) {
        this.auxiliaryMaterialInfos = auxiliaryMaterialInfos;
    }

    @Override
    public String toString() {
        return "AuxiliaryMaterialResult{" +
                "materialRemindDesc='" + materialRemindDesc + '\'' +
                ", auxiliaryMaterialInfos=" + auxiliaryMaterialInfos +
                '}';
    }
}
