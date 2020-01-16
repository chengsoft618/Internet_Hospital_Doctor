package cn.longmaster.hospital.doctor.core.entity.dutyclinic;

import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * @author ABiao_Abiao
 * @date 2019/12/28 15:15
 * @description:
 */
public class DCDutyPrognoteInfo {
    /**
     * type : 0
     * data_type : 2
     * data_val :
     * material_pic : 123.png
     * duration :
     */
    //类型 1疾病诊断 2病情摘要 3检查检验 4用药及处方 5资料图片 100：加入项目
    @JsonField("type")
    private int type;
    @JsonField("data_type_list")
    private List<DCDutyPrognoteTypeInfo> dataTypeList;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<DCDutyPrognoteTypeInfo> getDataTypeList() {
        return dataTypeList;
    }

    public void setDataTypeList(List<DCDutyPrognoteTypeInfo> dataTypeList) {
        this.dataTypeList = dataTypeList;
    }
}
