package cn.longmaster.hospital.doctor.core.entity.consult;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.hospital.doctor.core.entity.rounds.ReviewVideoInfo;

/**
 * 预约信息
 * Created by JinKe on 2016-08-03.
 */
public class AppointmentInfo implements Serializable {
    private AppointmentBaseInfo mBaseInfo;
    private AppDialogInfo mAppDialogInfo;
    private AppointmentExtendsInfo mExtendsInfo;
    private ApplyDescInfo mApplyDescInfo;
    private List<ApplyDoctorInfo> mApplyDoctorInfoList;
    private List<ReviewVideoInfo> reviewVideoInfo;//视频回看
    private String visitUrl;//回访信息
    private String medicalShareUrl;//病历分享链接
    private String opinionShareUrl;//会诊意见分享链接
    private String reviewVideoShareUrl;//视频回访分享链接
    private String visitShareUrl;//回访信息分享链接

    public AppointmentBaseInfo getBaseInfo() {
        return mBaseInfo;
    }

    public void setBaseInfo(AppointmentBaseInfo mBaseInfo) {
        this.mBaseInfo = mBaseInfo;
    }

    public AppDialogInfo getAppDialogInfo() {
        return mAppDialogInfo;
    }

    public void setAppDialogInfo(AppDialogInfo mAppDialogInfo) {
        this.mAppDialogInfo = mAppDialogInfo;
    }

    public AppointmentExtendsInfo getExtendsInfo() {
        return mExtendsInfo;
    }

    public void setExtendsInfo(AppointmentExtendsInfo mExtendsInfo) {
        this.mExtendsInfo = mExtendsInfo;
    }

    public ApplyDescInfo getApplyDescInfo() {
        return mApplyDescInfo;
    }

    public void setApplyDescInfo(ApplyDescInfo mApplyDescInfo) {
        this.mApplyDescInfo = mApplyDescInfo;
    }

    public List<ApplyDoctorInfo> getApplyDoctorInfoList() {
        return mApplyDoctorInfoList;
    }

    public void setApplyDoctorInfoList(List<ApplyDoctorInfo> mApplyDoctorInfoList) {
        this.mApplyDoctorInfoList = mApplyDoctorInfoList;
    }

    public List<ReviewVideoInfo> getReviewVideoInfo() {
        return reviewVideoInfo;
    }

    public void setReviewVideoInfo(List<ReviewVideoInfo> reviewVideoInfo) {
        this.reviewVideoInfo = reviewVideoInfo;
    }

    public String getVisitUrl() {
        return visitUrl;
    }

    public void setVisitUrl(String visitUrl) {
        this.visitUrl = visitUrl;
    }

    public String getVisitShareUrl() {
        return visitShareUrl;
    }

    public void setVisitShareUrl(String visitShareUrl) {
        this.visitShareUrl = visitShareUrl;
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

    @Override
    public String toString() {
        return "AppointmentInfo{" +
                "mBaseInfo=" + mBaseInfo +
                ", mAppDialogInfo=" + mAppDialogInfo +
                ", mExtendsInfo=" + mExtendsInfo +
                ", mApplyDescInfo=" + mApplyDescInfo +
                ", mApplyDoctorInfoList=" + mApplyDoctorInfoList +
                ", reviewVideoInfo=" + reviewVideoInfo +
                ", visitUrl='" + visitUrl + '\'' +
                ", medicalShareUrl='" + medicalShareUrl + '\'' +
                ", opinionShareUrl='" + opinionShareUrl + '\'' +
                ", reviewVideoShareUrl='" + reviewVideoShareUrl + '\'' +
                ", visitShareUrl='" + visitShareUrl + '\'' +
                '}';
    }
}
