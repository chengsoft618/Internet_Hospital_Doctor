package cn.longmaster.hospital.doctor.core.entity.consult;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 患者基本信息
 * Created by JinKe on 2016-07-26.
 */
public class PatientBaseInfo implements Serializable {
    @JsonField("appointment_id")
    private int appointmentId;//预约id
    @JsonField("real_name")
    private String realName;//患者真实名字
    @JsonField("gender")
    private int gender;//性别
    @JsonField("birthday")
    private String birthday;//出生日期
    @JsonField("age")
    private String age;//患者年龄
    @JsonField("phone_num")
    private String phoneNum;//患者电话号码
    @JsonField("is_married")
    private int isMarried;//是否结婚
    @JsonField("nation")
    private int nation;//民族
    @JsonField("birth_place")
    private String birthPlace;//出生地址
    @JsonField("career")
    private String career;//职业
    @JsonField("city")
    private String city;//城市
    @JsonField("province")
    private String province;//省份
    @JsonField("address")
    private String address;//详细地址
    @JsonField("height")
    private int height;//患者身高
    @JsonField("weight")
    private float weight;//患者体重
    @JsonField("temperature")
    private float temperature;//患者体温
    @JsonField("hypertension")
    private int hypertension;//患者最高血压
    @JsonField("hypotension")
    private int hypotension;//患者最低血压
    @JsonField("pluse")
    private int pluse;//患者脉搏
    @JsonField("breath")
    private int breath;//患者心跳
    @JsonField("sign_desc")
    private String signDesc;//患者描述
    @JsonField("first_cure_dt")
    private String firstCureDt;//初诊时间
    @JsonField("first_cure_result")
    private String firstCureResult;//初诊结果
    @JsonField("first_cure_hosp")
    private String firstCureHosp;//初诊医院
    @JsonField("first_cure_desc")
    private String firstCureDesc;//初诊主诉
    @JsonField("first_disease_hist")
    private String firstDiseaseHist;//初诊现病史
    @JsonField("first_cure_symp")
    private String firstCureSymp;//初诊症状
    @JsonField("first_cure_situ")
    private int firstCureSitu;//治疗状况
    @JsonField("first_cure_illness")
    private String firstCureIllness;//初诊疾病
    @JsonField("remark")
    private String remark;//备注
    @JsonField("channel")
    private String channel;//渠道
    @JsonField("channel_remark")
    private String channelRemark;//渠道备注
    @JsonField("card_no")
    private String cardNo;//卡号
    @JsonField("record_dt")
    private String recordDt;//记录时间
    @JsonField("insert_dt")
    private String insertDt;//数据插入时间

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getIsMarried() {
        return isMarried;
    }

    public void setIsMarried(int isMarried) {
        this.isMarried = isMarried;
    }

    public int getNation() {
        return nation;
    }

    public void setNation(int nation) {
        this.nation = nation;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public int getHypertension() {
        return hypertension;
    }

    public void setHypertension(int hypertension) {
        this.hypertension = hypertension;
    }

    public int getHypotension() {
        return hypotension;
    }

    public void setHypotension(int hypotension) {
        this.hypotension = hypotension;
    }

    public int getPluse() {
        return pluse;
    }

    public void setPluse(int pluse) {
        this.pluse = pluse;
    }

    public int getBreath() {
        return breath;
    }

    public void setBreath(int breath) {
        this.breath = breath;
    }

    public String getSignDesc() {
        return signDesc;
    }

    public void setSignDesc(String signDesc) {
        this.signDesc = signDesc;
    }

    public String getFirstCureDt() {
        return firstCureDt;
    }

    public void setFirstCureDt(String firstCureDt) {
        this.firstCureDt = firstCureDt;
    }

    public String getFirstCureResult() {
        return firstCureResult;
    }

    public void setFirstCureResult(String firstCureResult) {
        this.firstCureResult = firstCureResult;
    }

    public String getFirstCureHosp() {
        return firstCureHosp;
    }

    public void setFirstCureHosp(String firstCureHosp) {
        this.firstCureHosp = firstCureHosp;
    }

    public String getFirstCureDesc() {
        return firstCureDesc;
    }

    public void setFirstCureDesc(String firstCureDesc) {
        this.firstCureDesc = firstCureDesc;
    }

    public String getFirstDiseaseHist() {
        return firstDiseaseHist;
    }

    public void setFirstDiseaseHist(String firstDiseaseHist) {
        this.firstDiseaseHist = firstDiseaseHist;
    }

    public String getFirstCureSymp() {
        return firstCureSymp;
    }

    public void setFirstCureSymp(String firstCureSymp) {
        this.firstCureSymp = firstCureSymp;
    }

    public int getFirstCureSitu() {
        return firstCureSitu;
    }

    public void setFirstCureSitu(int firstCureSitu) {
        this.firstCureSitu = firstCureSitu;
    }

    public String getFirstCureIllness() {
        return firstCureIllness;
    }

    public void setFirstCureIllness(String firstCureIllness) {
        this.firstCureIllness = firstCureIllness;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getChannelRemark() {
        return channelRemark;
    }

    public void setChannelRemark(String channelRemark) {
        this.channelRemark = channelRemark;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getRecordDt() {
        return recordDt;
    }

    public void setRecordDt(String recordDt) {
        this.recordDt = recordDt;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "PatientBaseInfo{" +
                "appointmentId=" + appointmentId +
                ", realName='" + realName + '\'' +
                ", gender=" + gender +
                ", birthday='" + birthday + '\'' +
                ", age='" + age + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", isMarried=" + isMarried +
                ", nation=" + nation +
                ", birthPlace='" + birthPlace + '\'' +
                ", career='" + career + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", address='" + address + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", temperature=" + temperature +
                ", hypertension=" + hypertension +
                ", hypotension=" + hypotension +
                ", pluse=" + pluse +
                ", breath=" + breath +
                ", signDesc='" + signDesc + '\'' +
                ", firstCureDt='" + firstCureDt + '\'' +
                ", firstCureResult='" + firstCureResult + '\'' +
                ", firstCureHosp='" + firstCureHosp + '\'' +
                ", firstCureDesc='" + firstCureDesc + '\'' +
                ", firstDiseaseHist='" + firstDiseaseHist + '\'' +
                ", firstCureSymp='" + firstCureSymp + '\'' +
                ", firstCureSitu=" + firstCureSitu +
                ", firstCureIllness='" + firstCureIllness + '\'' +
                ", remark='" + remark + '\'' +
                ", channel='" + channel + '\'' +
                ", channelRemark='" + channelRemark + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", recordDt='" + recordDt + '\'' +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}

