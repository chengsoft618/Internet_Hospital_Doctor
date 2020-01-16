package cn.longmaster.hospital.doctor.core.entity.doctor;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * @author ABiao_Abiao
 * @date 2019/6/12 18:20
 * @description:
 */
public class DoctorVisitingItem {

    /**
     * id : 2
     * doctor_id : 1000040
     * week_num : 1
     * begin_dt : 13.50
     * end_dt : 15.00
     * type : 1
     * insert_dt : 2019-06-04 18:41:58
     */

    @JsonField("id")
    private String id;
    //医生ID
    @JsonField("doctor_id")
    private String doctorId;
    //星期，1代表星期一
    @JsonField("week_num")
    private int weekNum;
    //开始时间8.50代表8:30
    @JsonField("begin_dt")
    private String beginDt;
    //结束时间
    @JsonField("end_dt")
    private String endDt;
    //类型 1可以接诊时间 2不能接诊时间
    @JsonField("type")
    private int type;
    @JsonField("insert_dt")
    private String insertDt;
    //是否推荐 1是 0否
    @JsonField("is_recommend")
    private int isRecommend;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public int getWeekNum() {
        return weekNum;
    }

    public void setWeekNum(int weekNum) {
        this.weekNum = weekNum;
    }

    public String getBeginDt() {
        return beginDt;
    }

    public void setBeginDt(String beginDt) {
        this.beginDt = beginDt;
    }

    public String getEndDt() {
        return endDt;
    }

    public void setEndDt(String endDt) {
        this.endDt = endDt;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public int getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(int isRecommend) {
        this.isRecommend = isRecommend;
    }

    public boolean isRecommend() {
        return type == 1 && isRecommend == 1;
    }
}
