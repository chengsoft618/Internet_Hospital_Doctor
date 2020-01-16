package cn.longmaster.hospital.doctor.core.entity.dutyclinic;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * @author ABiao_Abiao
 * @date 2020/1/2 11:07
 * @description:
 */
public class DCDutyPrognoteDataInfo {
    /**
     * id : 24
     * record_id : 12
     * type : 1
     * data_type : 1
     * mat_relid : 0
     * data_val : 文字类型
     * insert_dt : 2019-12-26 11:06:46
     * material_pic : 123.png
     * duration : 30
     */
    //病程记录详情id
    @JsonField("id")
    private int id;
    //所属病程记录id
    @JsonField("record_id")
    private int recordId;
    //类型 1疾病诊断 2病情摘要 3检查检验 4用药及处方 5资料图片
    @JsonField("type")
    private int type;
    //材料类型 1文字 2语音 3图片
    @JsonField("data_type")
    private int dataType;
    //关联辅助检查id
    @JsonField("mat_relid")
    private int matRelid;
    //当是文字类型时文字内容
    @JsonField("data_val")
    private String dataVal;
    //插入时间
    @JsonField("insert_dt")
    private String insertDt;
    //附件地址
    @JsonField("material_pic")
    private String materialPic;
    //语音时长 单位秒
    @JsonField("duration")
    private int duration;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getMatRelid() {
        return matRelid;
    }

    public void setMatRelid(int matRelid) {
        this.matRelid = matRelid;
    }

    public String getDataVal() {
        return dataVal;
    }

    public void setDataVal(String dataVal) {
        this.dataVal = dataVal;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public String getMaterialPic() {
        return materialPic;
    }

    public void setMaterialPic(String materialPic) {
        this.materialPic = materialPic;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
