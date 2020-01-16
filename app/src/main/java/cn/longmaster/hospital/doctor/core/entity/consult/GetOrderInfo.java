package cn.longmaster.hospital.doctor.core.entity.consult;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;
import cn.longmaster.hospital.doctor.core.entity.rounds.ReviewVideoInfo;

/**
 * Created by W·H·K on 2018/3/8.
 */

public class GetOrderInfo implements Serializable {
    @JsonField("user_id")//患者id
    private int patientId;
    @JsonField("doctor_user_id")//大医生id
    private int doctorUserId;
    @JsonField("attending_doctor_user_id")//首诊医生id
    private int attendingDoctorUserId;
    @JsonField("appointment_extends")
    private ServiceTypeInfo serviceTypeInfo;
    @JsonField("clinic_info")
    private ClinicInfo clinicInfo;
    @JsonField("visit_url")
    private String visitUrl;//回访信息
    @JsonField("medical_share_url")
    private String medicalShareUrl;//病历分享链接
    @JsonField("opinion_share_url")
    private String opinionShareUrl;//会诊分享链接
    @JsonField("review_video_share_url")
    private String reviewVideoShareUrl;//视频回看分享链接
    @JsonField("visit_share_url")
    private String visitShareUrl;//回访分享链接
    @JsonField("attending_doctor_hosdep_id")
    private int attendingDoctorHosdepId;//首诊所属医院科室id
    @JsonField("review_video_list")
    private List<ReviewVideoInfo> reviewVideoInfos;//视频回看

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDoctorUserId() {
        return doctorUserId;
    }

    public void setDoctorUserId(int doctorUserId) {
        this.doctorUserId = doctorUserId;
    }

    public int getAttendingDoctorUserId() {
        return attendingDoctorUserId;
    }

    public void setAttendingDoctorUserId(int attendingDoctorUserId) {
        this.attendingDoctorUserId = attendingDoctorUserId;
    }

    public ServiceTypeInfo getServiceTypeInfo() {
        return serviceTypeInfo;
    }

    public void setServiceTypeInfo(ServiceTypeInfo serviceTypeInfo) {
        this.serviceTypeInfo = serviceTypeInfo;
    }

    public ClinicInfo getClinicInfo() {
        return clinicInfo;
    }

    public void setClinicInfo(ClinicInfo clinicInfo) {
        this.clinicInfo = clinicInfo;
    }

    public String getVisitUrl() {
        return visitUrl;
    }

    public void setVisitUrl(String visitUrl) {
        this.visitUrl = visitUrl;
    }

    public List<ReviewVideoInfo> getReviewVideoInfos() {
        return reviewVideoInfos;
    }

    public void setReviewVideoInfos(List<ReviewVideoInfo> reviewVideoInfos) {
        this.reviewVideoInfos = reviewVideoInfos;
    }

    public String getMedicalShareUrl() {
        return medicalShareUrl;
    }

    public void setMedicalShareUrl(String medicalShareUrl) {
        this.medicalShareUrl = medicalShareUrl;
    }

    public String getOpinionShareUrl() {
        return opinionShareUrl;
    }

    public void setOpinionShareUrl(String opinionShareUrl) {
        this.opinionShareUrl = opinionShareUrl;
    }

    public String getReviewVideoShareUrl() {
        return reviewVideoShareUrl;
    }

    public void setReviewVideoShareUrl(String reviewVideoShareUrl) {
        this.reviewVideoShareUrl = reviewVideoShareUrl;
    }

    public String getVisitShareUrl() {
        return visitShareUrl;
    }

    public void setVisitShareUrl(String visitShareUrl) {
        this.visitShareUrl = visitShareUrl;
    }

    public int getAttendingDoctorHosdepId() {
        return attendingDoctorHosdepId;
    }

    public void setAttendingDoctorHosdepId(int attendingDoctorHosdepId) {
        this.attendingDoctorHosdepId = attendingDoctorHosdepId;
    }

    @Override
    public String toString() {
        return "GetOrderInfo{" +
                "patientId=" + patientId +
                ", doctorUserId=" + doctorUserId +
                ", attendingDoctorUserId=" + attendingDoctorUserId +
                ", serviceTypeInfo=" + serviceTypeInfo +
                ", clinicInfo=" + clinicInfo +
                ", visitUrl='" + visitUrl + '\'' +
                ", medicalShareUrl='" + medicalShareUrl + '\'' +
                ", opinionShareUrl='" + opinionShareUrl + '\'' +
                ", reviewVideoShareUrl='" + reviewVideoShareUrl + '\'' +
                ", visitShareUrl='" + visitShareUrl + '\'' +
                ", attendingDoctorHosdepId=" + attendingDoctorHosdepId +
                ", reviewVideoInfos=" + reviewVideoInfos +
                '}';
    }
}
