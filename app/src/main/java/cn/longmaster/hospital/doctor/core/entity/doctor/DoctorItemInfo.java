package cn.longmaster.hospital.doctor.core.entity.doctor;

import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * @author ABiao_Abiao
 * @date 2019/6/10 10:36
 * @description:
 */
public class DoctorItemInfo {

    /**
     * doctor_id : 1001758
     * real_name : 赵鸿志(销售）离职
     * recmd_user_id : 0
     * doctor_level : 主任医师
     * recommend_meet :
     * reception_price1 : 0
     * reception_price3 : 0
     * reception_price5 : 0
     * reception_dt :
     * reception_num : 0
     * hospital_id : 30
     * hospital_name : 39互联网医院
     * hospital_sort : 20
     * department_id : 85
     * department_name : 病理科
     * doctor_skill :
     * total_score :
     * insert_dt : 1463642603
     * avater_token :
     * label_data : []
     */

    @JsonField("doctor_id")
    private int doctorId;
    @JsonField("real_name")
    private String realName;
    @JsonField("recmd_user_id")
    private String recmdUserId;
    @JsonField("doctor_level")
    private String doctorLevel;
    @JsonField("recommend_meet")
    private String recommendMeet;
    @JsonField("reception_price1")
    private String receptionPrice1;
    @JsonField("reception_price3")
    private String receptionPrice3;
    @JsonField("reception_price5")
    private String receptionPrice5;
    @JsonField("reception_dt")
    private String receptionDt;
    @JsonField("is_recommend")
    private String isRecommend;
    @JsonField("reception_num")
    private int receptionNum;
    @JsonField("hospital_id")
    private String hospitalId;
    @JsonField("hospital_name")
    private String hospitalName;
    @JsonField("hospital_sort")
    private String hospitalSort;
    @JsonField("department_id")
    private String departmentId;
    @JsonField("department_name")
    private String departmentName;
    @JsonField("doctor_skill")
    private String doctorSkill;
    @JsonField("total_score")
    private float totalScore;
    @JsonField("insert_dt")
    private String insertDt;
    @JsonField("avater_token")
    private String avaterToken;
    @JsonField("label_data")
    private List<LabeData> labelData;

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRecmdUserId() {
        return recmdUserId;
    }

    public void setRecmdUserId(String recmdUserId) {
        this.recmdUserId = recmdUserId;
    }

    public String getDoctorLevel() {
        return doctorLevel;
    }

    public void setDoctorLevel(String doctorLevel) {
        this.doctorLevel = doctorLevel;
    }

    public String getRecommendMeet() {
        return recommendMeet;
    }

    public void setRecommendMeet(String recommendMeet) {
        this.recommendMeet = recommendMeet;
    }

    public String getReceptionPrice1() {
        return receptionPrice1;
    }

    public void setReceptionPrice1(String receptionPrice1) {
        this.receptionPrice1 = receptionPrice1;
    }

    public String getReceptionPrice3() {
        return receptionPrice3;
    }

    public void setReceptionPrice3(String receptionPrice3) {
        this.receptionPrice3 = receptionPrice3;
    }

    public String getReceptionPrice5() {
        return receptionPrice5;
    }

    public void setReceptionPrice5(String receptionPrice5) {
        this.receptionPrice5 = receptionPrice5;
    }

    public String getReceptionDt() {
        return receptionDt;
    }

    public void setReceptionDt(String receptionDt) {
        this.receptionDt = receptionDt;
    }

    public String getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(String isRecommend) {
        this.isRecommend = isRecommend;
    }

    public int getReceptionNum() {
        return receptionNum;
    }

    public void setReceptionNum(int receptionNum) {
        this.receptionNum = receptionNum;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalSort() {
        return hospitalSort;
    }

    public void setHospitalSort(String hospitalSort) {
        this.hospitalSort = hospitalSort;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDoctorSkill() {
        return doctorSkill;
    }

    public void setDoctorSkill(String doctorSkill) {
        this.doctorSkill = doctorSkill;
    }

    public float getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(float totalScore) {
        this.totalScore = totalScore;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public String getAvaterToken() {
        return avaterToken;
    }

    public void setAvaterToken(String avaterToken) {
        this.avaterToken = avaterToken;
    }

    public List<LabeData> getLabelData() {
        return labelData;
    }

    public void setLabelData(List<LabeData> labelData) {
        this.labelData = labelData;
    }

    public static class LabeData {
        @JsonField("label_id")
        private String labelId;
        @JsonField("label_name")
        private String labelName;
        @JsonField("insert_dt")
        private String insertDt;

        public String getLabelId() {
            return labelId;
        }

        public void setLabelId(String labelId) {
            this.labelId = labelId;
        }

        public String getLabelName() {
            return labelName;
        }

        public void setLabelName(String labelName) {
            this.labelName = labelName;
        }

        public String getInsertDt() {
            return insertDt;
        }

        public void setInsertDt(String insertDt) {
            this.insertDt = insertDt;
        }
    }
}
