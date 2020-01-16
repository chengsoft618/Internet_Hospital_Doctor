package cn.longmaster.hospital.doctor.core.entity.doctor;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;
import cn.longmaster.utils.StringUtils;

/**
 * 医生基本信息
 * Created by JinKe on 2016-07-26.
 */
public class DoctorBaseInfo implements Serializable {
    @JsonField("user_id")
    private int userId;//医生ID
    @JsonField("avater_token")
    private String avaterToken;//头像更新标识
    @JsonField("real_name")
    private String realName;//医生姓名
    @JsonField("birthday")
    private String birthday;//生日
    @JsonField("gender")
    private int gender;//性别
    @JsonField("phone_num")
    private String phoneNum;//电话号码
    @JsonField("id_card")
    private String idCard;//
    @JsonField("is_sign")
    private int isSign;//
    @JsonField("is_host")
    private int isHost;//
    @JsonField("hospital_id")
    private int hospitalId;//医院ID
    @JsonField("hospital_name")
    private String hospitalName;//医院名称
    @JsonField("doctor_level")
    private String doctorLevel;//医生职称
    @JsonField("doctor_title")
    private String doctorTitle;//医生头衔
    @JsonField("hosdep_id")
    private int hosdepId;
    @JsonField("department_id")
    private int departmentId;//科室ID
    @JsonField("department_name")
    private String departmentName;//科室名称
    @JsonField("first_grade_depm_id")
    private int firstGradeDepmId;//一级科室ID(已弃用)
    @JsonField("medical_expenses")
    private float medicalExpenses;//就诊金额
    @JsonField("doctor_skill")
    private String doctorSkill;//擅长疾病
    @JsonField("doctor_brief")
    private DoctorBriefInfo doctorBriefInfo;//医生信息扩展字段
    @JsonField("team_type")
    private int teamType;//团队类型
    @JsonField("is_cure")
    private int isCure;//是否接诊
    @JsonField("is_stick")
    private int isStick;//是否置顶 1:置顶0:不置顶
    @JsonField("stick_sort")
    private int stickSort;//排序
    @JsonField("is_led")
    private int isLed;//是否领衔
    @JsonField("region")
    private int region;//职称等级
    @JsonField("settled_type")
    private int settledType;//入驻类型
    @JsonField("referee_user_id")
    private int refereeUserId;//审阅人ID
    @JsonField("is_first_login")
    private int isFirstLogin;//是否第一次登陆
    @JsonField("is_display")
    private int isDisplay;//是否显示 1:显示 0:不显示
    @JsonField("doctor_honor")
    private String doctorHonor;//医生荣誉
    @JsonField("practise_year")
    private String practiseYear;//从业年限
    @JsonField("practise_date")
    private String practiseDate;//
    @JsonField("second_grade_id")
    private int secondGradeId;//区域等级ID
    @JsonField("recmd_user_id")
    private int recmdUserId;//推荐人ID
    @JsonField("area_id")
    private int areaId;//
    @JsonField("insert_dt")
    private String insertDt;//插入时间
    @JsonField("viewing_auth")
    private int viewingAuth;//实名病历权限
    @JsonField("web_doctor_url")
    private String webDoctorUrl;//医生详情地址
    @JsonField("doctor_picture")
    private List<DoctorPictureInfo> mDoctorPictureInfoList;//医生图片信息列表
    @JsonField("recmd_Info")
    private List<RecmdInfo> mRecmdInfoList;//医生推荐理由列表
    @JsonField("month_recommend")
    private int monthRecommend;//本月推荐标识 1-推荐， 0不推荐
    @JsonField("team_data")
    private List<DoctorTeamItemInfo> teamData;
    //1 审核成功 2 审核失敗   其他 未审核
    @JsonField("real_stat")
    private int realStat;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAvaterToken() {
        return avaterToken;
    }

    public void setAvaterToken(String avaterToken) {
        this.avaterToken = avaterToken;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public int getIsSign() {
        return isSign;
    }

    public void setIsSign(int isSign) {
        this.isSign = isSign;
    }

    public int getIsHost() {
        return isHost;
    }

    public void setIsHost(int isHost) {
        this.isHost = isHost;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getDoctorLevel() {
        return doctorLevel;
    }

    public void setDoctorLevel(String doctorLevel) {
        this.doctorLevel = doctorLevel;
    }

    public String getDoctorTitle() {
        return doctorTitle;
    }

    public void setDoctorTitle(String doctorTitle) {
        this.doctorTitle = doctorTitle;
    }

    public int getHosdepId() {
        return hosdepId;
    }

    public void setHosdepId(int hosdepId) {
        this.hosdepId = hosdepId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getFirstGradeDepmId() {
        return firstGradeDepmId;
    }

    public void setFirstGradeDepmId(int firstGradeDepmId) {
        this.firstGradeDepmId = firstGradeDepmId;
    }

    public float getMedicalExpenses() {
        return medicalExpenses;
    }

    public void setMedicalExpenses(float medicalExpenses) {
        this.medicalExpenses = medicalExpenses;
    }

    public String getDoctorSkill() {
        if (StringUtils.isTrimEmpty(doctorSkill)) {
            return "该医生暂未上传资料";
        }
        return doctorSkill;
    }

    public void setDoctorSkill(String doctorSkill) {
        this.doctorSkill = doctorSkill;
    }

    public DoctorBriefInfo getDoctorBriefInfo() {
        return doctorBriefInfo;
    }

    public void setDoctorBriefInfo(DoctorBriefInfo doctorBriefInfo) {
        this.doctorBriefInfo = doctorBriefInfo;
    }

    public int getTeamType() {
        return teamType;
    }

    public void setTeamType(int teamType) {
        this.teamType = teamType;
    }

    public int getIsCure() {
        return isCure;
    }

    public void setIsCure(int isCure) {
        this.isCure = isCure;
    }

    public int getIsStick() {
        return isStick;
    }

    public void setIsStick(int isStick) {
        this.isStick = isStick;
    }

    public int getStickSort() {
        return stickSort;
    }

    public void setStickSort(int stickSort) {
        this.stickSort = stickSort;
    }

    public int getIsLed() {
        return isLed;
    }

    public void setIsLed(int isLed) {
        this.isLed = isLed;
    }

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public int getSettledType() {
        return settledType;
    }

    public void setSettledType(int settledType) {
        this.settledType = settledType;
    }

    public int getRefereeUserId() {
        return refereeUserId;
    }

    public void setRefereeUserId(int refereeUserId) {
        this.refereeUserId = refereeUserId;
    }

    public int getIsFirstLogin() {
        return isFirstLogin;
    }

    public void setIsFirstLogin(int isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    public int getIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(int isDisplay) {
        this.isDisplay = isDisplay;
    }

    public String getDoctorHonor() {
        return doctorHonor;
    }

    public void setDoctorHonor(String doctorHonor) {
        this.doctorHonor = doctorHonor;
    }

    public String getPractiseYear() {
        return practiseYear;
    }

    public void setPractiseYear(String practiseYear) {
        this.practiseYear = practiseYear;
    }

    public String getPractiseDate() {
        return practiseDate;
    }

    public void setPractiseDate(String practiseDate) {
        this.practiseDate = practiseDate;
    }

    public int getSecondGradeId() {
        return secondGradeId;
    }

    public void setSecondGradeId(int secondGradeId) {
        this.secondGradeId = secondGradeId;
    }

    public int getRecmdUserId() {
        return recmdUserId;
    }

    public void setRecmdUserId(int recmdUserId) {
        this.recmdUserId = recmdUserId;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public String getWebDoctorUrl() {
        return webDoctorUrl;
    }

    public void setWebDoctorUrl(String webDoctorUrl) {
        this.webDoctorUrl = webDoctorUrl;
    }

    public List<DoctorPictureInfo> getDoctorPictureInfoList() {
        return mDoctorPictureInfoList;
    }

    public void setDoctorPictureInfoList(List<DoctorPictureInfo> doctorPictureInfoList) {
        this.mDoctorPictureInfoList = doctorPictureInfoList;
    }

    public List<RecmdInfo> getRecmdInfoList() {
        return mRecmdInfoList;
    }

    public void setRecmdInfoList(List<RecmdInfo> recmdInfoList) {
        this.mRecmdInfoList = recmdInfoList;
    }

    public int getViewingAuth() {
        return viewingAuth;
    }

    public void setViewingAuth(int viewingAuth) {
        this.viewingAuth = viewingAuth;
    }

    public List<DoctorTeamItemInfo> getTeamData() {
        return teamData;
    }

    public void setTeamData(List<DoctorTeamItemInfo> teamData) {
        this.teamData = teamData;
    }

    public int getMonthRecommend() {
        return monthRecommend;
    }

    public void setMonthRecommend(int monthRecommend) {
        this.monthRecommend = monthRecommend;
    }

    public boolean isMonthRecommend() {
        return monthRecommend == 1;
    }

    public List<DoctorPictureInfo> getmDoctorPictureInfoList() {
        return mDoctorPictureInfoList;
    }

    public void setmDoctorPictureInfoList(List<DoctorPictureInfo> mDoctorPictureInfoList) {
        this.mDoctorPictureInfoList = mDoctorPictureInfoList;
    }

    public List<RecmdInfo> getmRecmdInfoList() {
        return mRecmdInfoList;
    }

    public void setmRecmdInfoList(List<RecmdInfo> mRecmdInfoList) {
        this.mRecmdInfoList = mRecmdInfoList;
    }

    public int getRealStat() {
        return realStat;
    }

    public boolean isReal() {
        return realStat == 1;
    }

    public void setRealStat(int realStat) {
        this.realStat = realStat;
    }

    @Override
    public String toString() {
        return "DoctorBaseInfo{" +
                "userId=" + userId +
                ", avaterToken='" + avaterToken + '\'' +
                ", realName='" + realName + '\'' +
                ", birthday='" + birthday + '\'' +
                ", gender=" + gender +
                ", phoneNum='" + phoneNum + '\'' +
                ", idCard='" + idCard + '\'' +
                ", isSign=" + isSign +
                ", isHost=" + isHost +
                ", hospitalId=" + hospitalId +
                ", doctorLevel='" + doctorLevel + '\'' +
                ", doctorTitle='" + doctorTitle + '\'' +
                ", departmentId=" + departmentId +
                ", firstGradeDepmId=" + firstGradeDepmId +
                ", medicalExpenses=" + medicalExpenses +
                ", doctorSkill='" + doctorSkill + '\'' +
                ", doctorBriefInfo=" + doctorBriefInfo +
                ", teamType=" + teamType +
                ", isCure=" + isCure +
                ", isStick=" + isStick +
                ", stickSort=" + stickSort +
                ", isLed=" + isLed +
                ", region=" + region +
                ", settledType=" + settledType +
                ", refereeUserId=" + refereeUserId +
                ", isFirstLogin=" + isFirstLogin +
                ", isDisplay=" + isDisplay +
                ", doctorHonor='" + doctorHonor + '\'' +
                ", practiseYear='" + practiseYear + '\'' +
                ", practiseDate='" + practiseDate + '\'' +
                ", secondGradeId=" + secondGradeId +
                ", recmdUserId=" + recmdUserId +
                ", areaId=" + areaId +
                ", insertDt='" + insertDt + '\'' +
                ", webDoctorUrl='" + webDoctorUrl + '\'' +
                ", mDoctorPictureInfoList=" + mDoctorPictureInfoList +
                ", mRecmdInfoList=" + mRecmdInfoList +
                '}';
    }
}
