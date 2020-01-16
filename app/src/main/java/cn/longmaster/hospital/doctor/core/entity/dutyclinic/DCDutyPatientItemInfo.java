package cn.longmaster.hospital.doctor.core.entity.dutyclinic;

import cn.longmaster.doctorlibrary.util.json.JsonField;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.TimeUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/12/16 21:27
 * @description: 患者列表信息
 */
public class DCDutyPatientItemInfo {

    /**
     * order_id : 501333
     * medical_id : 10002165
     * user_id : 1000004
     * patient_name : 张三峰
     * state : 1
     * attr : 2001
     * avatar : www.baidu.com
     * gender : 0
     * birthday : 2008-03-21
     * hospital_name : 贵阳医学院
     */

    @JsonField("order_id")
    private int orderId;
    @JsonField("medical_id")
    private int medicalId;
    @JsonField("user_id")
    private int userId;
    @JsonField("patient_name")
    private String patientName;
    @JsonField("insert_dt")
    private String insertDt;
    //未读状态 0：未读 1：已读
    @JsonField("state")
    private int state;
    //未读类型 1001病程更新 1002用药提醒等，2001病人新加入 2002病程更新
    @JsonField("attr")
    private int attr;
    @JsonField("avatar")
    private String avatar;
    @JsonField("gender")
    private int gender;
    @JsonField("birthday")
    private String birthday;
    @JsonField("hospital_name")
    private String hospitalName;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(int medicalId) {
        this.medicalId = medicalId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public boolean isReaded() {
        return state == 1;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getAttr() {
        return attr;
    }

    public void setAttr(int attr) {
        this.attr = attr;
    }

    //1001病程更新 1002用药提醒等，2001病人新加入 2002病程更新
    public String getReadType() {
        if (attr == 1001) {
            return "病程更新";
        }
        if (attr == 1002) {
            return "用药提醒";
        }
        if (attr == 2001) {
            return "病人新加入";
        }
        if (attr == 2002) {
            return "病程更新";
        }
        return "";
    }
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getGenderStr() {
        return gender == 1 ? "男" : "女";
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAge() {
        if (StringUtils.isEmpty(this.birthday)){
            return "";
        }else {
            String birthYear = TimeUtils.string2String(this.birthday, "yyyy-MM-dd", "yyyy");
            String nowYear = TimeUtils.getNowYearString();
            return StringUtils.str2Integer(nowYear) - StringUtils.str2Integer(birthYear) + "岁";
        }
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }
}
