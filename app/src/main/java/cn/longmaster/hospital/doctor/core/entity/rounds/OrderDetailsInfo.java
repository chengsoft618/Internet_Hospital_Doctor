package cn.longmaster.hospital.doctor.core.entity.rounds;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;
import cn.longmaster.hospital.doctor.core.entity.doctor.DepartmentListInfo;

/**
 * 订单详情
 * <p>
 * Created by W·H·K on 2018/5/15.
 */

public class OrderDetailsInfo implements Serializable {
    @JsonField("order_id")
    private int orderId;//订单id
    @JsonField("room_id")
    private int roomId;//房间id
    @JsonField("order_title")
    private String orderTitle;//订单主题
    @JsonField("order_type")
    private int orderType;//订单类型
    @JsonField("order_price")
    private float orderPrice;//订单价格
    @JsonField("order_state")
    private int orderState;//订单状态
    @JsonField("state_reason")
    private int stateReason;//状态原因
    @JsonField("pay_state")
    private int payState;//支付状态
    @JsonField("pay_dt")
    private String payDt;//支付时间
    @JsonField("source")
    private int source;//来源
    @JsonField("complete_dt")
    private String completeDt;//完成时间
    @JsonField("dgws_uid")
    private int dgwsUid;//导医id
    @JsonField("doctor_id")
    private int doctorId;//上级专家
    @JsonField("intention_duration")
    private String intentionDuration;//意向时长
    @JsonField("is_ppt")
    private int isPPT;//是否需要ppt
    @JsonField("launch_hospital")
    private int launchHospital;//订单发起医院id
    @JsonField("launch_hosdep_id")
    private int launchHosdepId;//订单发起医生科室id
    @JsonField("launch_doctor_id")
    private int launchDoctorId;//订单发起医生id
    @JsonField("launch_uid")
    private int launchUid;//发起人(销售)ID
    @JsonField("triage_require")
    private String triageRequire;//分诊要求
    @JsonField("visit_appeal")
    private String visitAppeal;//查房诉求
    @JsonField("attend_evaluate_id")
    private int attendEvaluateId;//评价模板id
    @JsonField("medical_summary_url")
    private String medicalSummaryUrl;//查房纪要
    @JsonField("medical_qrcode_url")
    private String medicalQrcodeUrl;//病历二维码
    @JsonField("order_share_url")
    private String orderShareUrl;//订单详情分享链接
    @JsonField("summary_share_url")
    private String summaryUrl;//查房纪要分享链接
    @JsonField("review_video_share_url")
    private String reviewVideoShareUrl;//查房纪要
    @JsonField("review_video_list")
    private List<ReviewVideoInfo> reviewVideoInfo;//视频回看
    @JsonField("order_medical_info")
    private List<RoundsPatientInfo> roundsPatientInfos;
    @JsonField("order_cure_dt")
    private List<OrderCureDtInfo> orderCureDtInfos;
    @JsonField("order_file")
    private List<OrderFileInfo> orderFileInfos;
    @JsonField("intention_department_list")
    private List<DepartmentListInfo> departmentListInfos;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public float getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(float orderPrice) {
        this.orderPrice = orderPrice;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public int getStateReason() {
        return stateReason;
    }

    public void setStateReason(int stateReason) {
        this.stateReason = stateReason;
    }

    public int getPayState() {
        return payState;
    }

    public void setPayState(int payState) {
        this.payState = payState;
    }

    public String getPayDt() {
        return payDt;
    }

    public void setPayDt(String payDt) {
        this.payDt = payDt;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getCompleteDt() {
        return completeDt;
    }

    public void setCompleteDt(String completeDt) {
        this.completeDt = completeDt;
    }

    public int getDgwsUid() {
        return dgwsUid;
    }

    public void setDgwsUid(int dgwsUid) {
        this.dgwsUid = dgwsUid;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getIntentionDuration() {
        return intentionDuration;
    }

    public void setIntentionDuration(String intentionDuration) {
        this.intentionDuration = intentionDuration;
    }

    public boolean isNeedPpt() {
        return isPPT == 1;
    }

    public int getIsPPT() {
        return isPPT;
    }

    public void setIsPPT(int isPPT) {
        this.isPPT = isPPT;
    }

    public int getLaunchHospital() {
        return launchHospital;
    }

    public void setLaunchHospital(int launchHospital) {
        this.launchHospital = launchHospital;
    }

    public int getLaunchHosdepId() {
        return launchHosdepId;
    }

    public void setLaunchHosdepId(int launchHosdepId) {
        this.launchHosdepId = launchHosdepId;
    }

    public int getLaunchDoctorId() {
        return launchDoctorId;
    }

    public void setLaunchDoctorId(int launchDoctorId) {
        this.launchDoctorId = launchDoctorId;
    }

    public int getLaunchUid() {
        return launchUid;
    }

    public void setLaunchUid(int launchUid) {
        this.launchUid = launchUid;
    }

    public String getTriageRequire() {
        return triageRequire;
    }

    public void setTriageRequire(String triageRequire) {
        this.triageRequire = triageRequire;
    }

    public String getVisitAppeal() {
        return visitAppeal;
    }

    public void setVisitAppeal(String visitAppeal) {
        this.visitAppeal = visitAppeal;
    }

    public int getAttendEvaluateId() {
        return attendEvaluateId;
    }

    public void setAttendEvaluateId(int attendEvaluateId) {
        this.attendEvaluateId = attendEvaluateId;
    }

    public String getMedicalSummaryUrl() {
        return medicalSummaryUrl;
    }

    public void setMedicalSummaryUrl(String medicalSummaryUrl) {
        this.medicalSummaryUrl = medicalSummaryUrl;
    }

    public String getMedicalQrcodeUrl() {
        return medicalQrcodeUrl;
    }

    public void setMedicalQrcodeUrl(String medicalQrcodeUrl) {
        this.medicalQrcodeUrl = medicalQrcodeUrl;
    }

    public String getOrderShareUrl() {
        return orderShareUrl;
    }

    public void setOrderShareUrl(String orderShareUrl) {
        this.orderShareUrl = orderShareUrl;
    }

    public String getSummaryUrl() {
        return summaryUrl;
    }

    public void setSummaryUrl(String summaryUrl) {
        this.summaryUrl = summaryUrl;
    }

    public String getReviewVideoShareUrl() {
        return reviewVideoShareUrl;
    }

    public void setReviewVideoShareUrl(String reviewVideoShareUrl) {
        this.reviewVideoShareUrl = reviewVideoShareUrl;
    }

    public List<ReviewVideoInfo> getReviewVideoInfo() {
        return reviewVideoInfo;
    }

    public void setReviewVideoInfo(List<ReviewVideoInfo> reviewVideoInfo) {
        this.reviewVideoInfo = reviewVideoInfo;
    }

    public List<RoundsPatientInfo> getRoundsPatientInfos() {
        return roundsPatientInfos;
    }

    public void setRoundsPatientInfos(List<RoundsPatientInfo> roundsPatientInfos) {
        this.roundsPatientInfos = roundsPatientInfos;
    }

    public List<OrderCureDtInfo> getOrderCureDtInfos() {
        return orderCureDtInfos;
    }

    public void setOrderCureDtInfos(List<OrderCureDtInfo> orderCureDtInfos) {
        this.orderCureDtInfos = orderCureDtInfos;
    }

    public List<OrderFileInfo> getOrderFileInfos() {
        return orderFileInfos;
    }

    public void setOrderFileInfos(List<OrderFileInfo> orderFileInfos) {
        this.orderFileInfos = orderFileInfos;
    }

    public List<DepartmentListInfo> getDepartmentListInfos() {
        return departmentListInfos;
    }

    public void setDepartmentListInfos(List<DepartmentListInfo> departmentListInfos) {
        this.departmentListInfos = departmentListInfos;
    }

    @Override
    public String toString() {
        return "OrderDetailsInfo{" +
                "orderId=" + orderId +
                ", roomId=" + roomId +
                ", orderTitle='" + orderTitle + '\'' +
                ", orderType=" + orderType +
                ", orderPrice=" + orderPrice +
                ", orderState=" + orderState +
                ", stateReason=" + stateReason +
                ", payState=" + payState +
                ", payDt='" + payDt + '\'' +
                ", source=" + source +
                ", completeDt='" + completeDt + '\'' +
                ", dgwsUid=" + dgwsUid +
                ", doctorId=" + doctorId +
                ", intentionDuration='" + intentionDuration + '\'' +
                ", isPPT=" + isPPT +
                ", launchHospital=" + launchHospital +
                ", launchHosdepId=" + launchHosdepId +
                ", launchDoctorId=" + launchDoctorId +
                ", launchUid=" + launchUid +
                ", triageRequire='" + triageRequire + '\'' +
                ", visitAppeal='" + visitAppeal + '\'' +
                ", attendEvaluateId=" + attendEvaluateId +
                ", medicalSummaryUrl='" + medicalSummaryUrl + '\'' +
                ", medicalQrcodeUrl='" + medicalQrcodeUrl + '\'' +
                ", orderShareUrl='" + orderShareUrl + '\'' +
                ", summaryUrl='" + summaryUrl + '\'' +
                ", reviewVideoShareUrl='" + reviewVideoShareUrl + '\'' +
                ", reviewVideoInfo=" + reviewVideoInfo +
                ", roundsPatientInfos=" + roundsPatientInfos +
                ", orderCureDtInfos=" + orderCureDtInfos +
                ", orderFileInfos=" + orderFileInfos +
                ", departmentListInfos=" + departmentListInfos +
                '}';
    }
}