package cn.longmaster.hospital.doctor.core.entity.consult;

import java.util.List;

/**
 * 材料分类实体类
 * Created by Tengshuxiang on 2016-06-13.
 */
public class MaterialClassify {
    public String classifyName;
    public List<AuxiliaryMaterialInfo> materialCheckInfos;

    @Override
    public String toString() {
        return "MaterialClassify{" +
                "classifyName='" + classifyName + '\'' +
                ", materialCheckInfos=" + materialCheckInfos +
                '}';
    }
}
