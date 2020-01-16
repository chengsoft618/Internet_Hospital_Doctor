package cn.longmaster.hospital.doctor.core.entity.rounds;

import java.io.Serializable;
import java.util.List;

/**
 * 材料分类实体类
 * Created by Tengshuxiang on 2016-06-13.
 */
public class RoundsMaterialClassify implements Serializable {
    public String classifyName;
    public List<AuxiliaryInspectInfo> materialCheckInfos;

    @Override
    public String toString() {
        return "MaterialClassify{" +
                "classifyName='" + classifyName + '\'' +
                ", materialCheckInfos=" + materialCheckInfos +
                '}';
    }
}
