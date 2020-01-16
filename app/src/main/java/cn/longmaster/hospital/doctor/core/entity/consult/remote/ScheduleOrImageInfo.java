package cn.longmaster.hospital.doctor.core.entity.consult.remote;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;
import cn.longmaster.utils.StringUtils;

/**
 * 排班或服务信息
 * Created by JinKe on 2016-07-28.
 */
public class ScheduleOrImageInfo implements Serializable {
    @JsonField("scheduing_id")
    private int scheduingId;//排班/影像服务ID
    @JsonField("scheduing_type")
    private int scheduingType;//类型 1：排班 2：影像服务
    @JsonField("doctor_id")
    private int doctorId;//医生ID
    @JsonField("begin_dt")
    private String beginDt;//开始时间
    @JsonField("end_dt")
    private String endDt;//结束时间
    @JsonField("admission_num")
    private int admissionNum;//接诊人数
    @JsonField("now_num")
    private int nowNum;//当前编号
    @JsonField("op_type")
    private int opType;//操作类型
    @JsonField("is_new_appointment")
    private int isNewAppointment;//是否接收新的预约
    @JsonField("insert_dt")
    private String insertDt;//插入时间
    @JsonField("consult_num")
    private int consultNum;//排班或者影像服务会诊人数
    @JsonField("advice_num")
    private int adviceNum;//咨询人数[此参数为排班时输出]
    @JsonField("recure_num")
    private int recureNum;//复诊人数[此参数为排班时输出]
    @JsonField("scheduing_relation")
    private List<ScheduleRelateInfo> scheduingRelationList;//关联信息

    public int getScheduingId() {
        return scheduingId;
    }

    public void setScheduingId(int scheduingId) {
        this.scheduingId = scheduingId;
    }

    public List<ScheduleRelateInfo> getScheduingRelationList() {
        return scheduingRelationList;
    }

    public void setScheduingRelationList(List<ScheduleRelateInfo> scheduingRelationList) {
        this.scheduingRelationList = scheduingRelationList;
    }

    public int getScheduingType() {
        return scheduingType;
    }

    public void setScheduingType(int scheduingType) {
        this.scheduingType = scheduingType;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
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

    public int getAdmissionNum() {
        return admissionNum;
    }

    public void setAdmissionNum(int admissionNum) {
        this.admissionNum = admissionNum;
    }

    public int getNowNum() {
        return nowNum;
    }

    public void setNowNum(int nowNum) {
        this.nowNum = nowNum;
    }

    public int getOpType() {
        return opType;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public int getIsNewAppointment() {
        return isNewAppointment;
    }

    public void setIsNewAppointment(int isNewAppointment) {
        this.isNewAppointment = isNewAppointment;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public int getConsultNum() {
        return consultNum;
    }

    public void setConsultNum(int consultNum) {
        this.consultNum = consultNum;
    }

    public int getAdviceNum() {
        return adviceNum;
    }

    public void setAdviceNum(int adviceNum) {
        this.adviceNum = adviceNum;
    }

    public int getRecureNum() {
        return recureNum;
    }

    public void setRecureNum(int recureNum) {
        this.recureNum = recureNum;
    }

    public String getAdmissionPrice(int serviceType) {
        if (null != scheduingRelationList) {
            for (ScheduleRelateInfo scheduleRelateInfo : scheduingRelationList) {
                if (scheduleRelateInfo.getServiceType() == serviceType) {
                    return StringUtils.float2Str(scheduleRelateInfo.getAdmissionPrice());
                }
            }
        }
        return "待定";
    }

    @Override
    public String toString() {
        return "ScheduleOrImageInfo{" +
                "scheduingId=" + scheduingId +
                ", scheduingType=" + scheduingType +
                ", doctorId=" + doctorId +
                ", beginDt='" + beginDt + '\'' +
                ", endDt='" + endDt + '\'' +
                ", admissionNum=" + admissionNum +
                ", nowNum=" + nowNum +
                ", opType=" + opType +
                ", isNewAppointment=" + isNewAppointment +
                ", insertDt='" + insertDt + '\'' +
                ", consultNum=" + consultNum +
                ", adviceNum=" + adviceNum +
                ", recureNum=" + recureNum +
                ", scheduingRelationList=" + scheduingRelationList +
                '}';
    }
}
