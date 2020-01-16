package cn.longmaster.hospital.doctor.core.entity.dutyclinic;

import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * @author ABiao_Abiao
 * @date 2019/12/31 15:07
 * @description:
 */
public class DCDutyPrognoteTypeInfo {
    //材料类型 1文字 2语音 3图片
    @JsonField("data_type")
    private int dataType;
    //材料列表
    @JsonField("data_list")
    private List<DCDutyPrognoteDataInfo> dataList;

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public List<DCDutyPrognoteDataInfo> getDataList() {
        return dataList;
    }

    public void setDataList(List<DCDutyPrognoteDataInfo> dataList) {
        this.dataList = dataList;
    }
}
