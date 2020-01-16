package cn.longmaster.hospital.doctor.core.entity.dutyclinic;

import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * @author ABiao_Abiao
 * @date 2019/12/26 10:29
 * @description: 值班门诊患者列表
 */
public class DCDutyRoomPatientItem {

    /**
     * medical_id : 10002186
     * order_id : 501354
     * item_id : 1
     * user_id : 1000004
     * patient_name : Viki
     * card_no : 150102199003070613
     * phone_num : 11111111111
     * cure_dt :
     * age : 50
     * temp_state : 1
     * dgws_uid : 0
     * is_delete : 0
     * insert_dt : 2019-12-27 10:25:53
     * check_list : [{"id":"6","medical_id":"10002186","type":"0","file_name":"201805.09.500070.141_99_201612060236_1525867777784952-1.jpg","insert_dt":"2018-05-09 20:09:37"},{"id":"8","medical_id":"10002186","type":"0","file_name":"201805.09.500070.141_99_201612060238_1525867779228436-1.jpg","insert_dt":"2018-05-09 20:09:40"},{"id":"12","medical_id":"10002186","type":"0","file_name":"201805.09.500070.141_99_201612070121_1525867783615502-1.jpg","insert_dt":"2018-05-09 20:09:44"}]
     */
    //病历id
    @JsonField("medical_id")
    private int medicalId;
    //订单id
    @JsonField("order_id")
    private int orderId;
    //项目id
    @JsonField("item_id")
    private int itemId;
    //患者id
    @JsonField("user_id")
    private int userId;
    //患者姓名
    @JsonField("patient_name")
    private String patientName;
    //患者身份证号
    @JsonField("card_no")
    private String cardNo;
    //患者手机号码
    @JsonField("phone_num")
    private String phoneNum;
    //就诊时间
    @JsonField("cure_dt")
    private String cureDt;
    //患者年龄
    @JsonField("age")
    private int age;
    @JsonField("temp_state")
    private int tempState;
    @JsonField("dgws_uid")
    private int dgwsUid;
    @JsonField("is_delete")
    private int isDelete;
    @JsonField("insert_dt")
    private String insertDt;
    @JsonField("check_list")
    private List<DCDutyRoomPatientMedical> checkList;

    public int getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(int medicalId) {
        this.medicalId = medicalId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
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

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getCureDt() {
        return cureDt;
    }

    public void setCureDt(String cureDt) {
        this.cureDt = cureDt;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getTempState() {
        return tempState;
    }

    public void setTempState(int tempState) {
        this.tempState = tempState;
    }

    public int getDgwsUid() {
        return dgwsUid;
    }

    public void setDgwsUid(int dgwsUid) {
        this.dgwsUid = dgwsUid;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public List<DCDutyRoomPatientMedical> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<DCDutyRoomPatientMedical> checkList) {
        this.checkList = checkList;
    }
}
