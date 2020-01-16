package cn.longmaster.hospital.doctor.core.requests.appointment;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.consult.AppDialogInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.ApplyDescInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.ApplyDoctorInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointmentBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointmentExtendsInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointmentInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.ReviewVideoInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseClientApiRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;

/**
 * 根据预约ID获取预约信息
 * Created by Yang² on 2016/7/28.
 */
public class AppointmentByIdRequester extends BaseClientApiRequester<AppointmentInfo> {
    public int appointmentId;//预约ID

    public AppointmentByIdRequester(@NonNull OnResultCallback<AppointmentInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_APPOINTMENT_BY_ID;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected AppointmentInfo onDumpData(JSONObject data) throws JSONException {
        AppointmentInfo appointmentInfo = new AppointmentInfo();
        appointmentInfo.setBaseInfo(JsonHelper.toObject(data.getJSONObject("appointment"), AppointmentBaseInfo.class));
        String appointmentExtends = data.optString("appointment_extends").toString();
        if (!"".equals(appointmentExtends)) {
            appointmentInfo.setExtendsInfo(JsonHelper.toObject(data.getJSONObject("appointment_extends"), AppointmentExtendsInfo.class));
        }
        String applyDesc = data.optString("apply_desc").toString();
        if (!"".equals(applyDesc)) {
            appointmentInfo.setApplyDescInfo(JsonHelper.toObject(data.getJSONObject("apply_desc"), ApplyDescInfo.class));
        }
        appointmentInfo.setApplyDoctorInfoList(JsonHelper.toList(data.getJSONArray("apply_doctor"), ApplyDoctorInfo.class));
        appointmentInfo.setAppDialogInfo(JsonHelper.toObject(data.getJSONObject("app_diag"), AppDialogInfo.class));
        appointmentInfo.setReviewVideoInfo(JsonHelper.toList(data.getJSONArray("review_video_list"), ReviewVideoInfo.class));
        appointmentInfo.setVisitUrl(data.getString("visit_url").toString());
        appointmentInfo.setMedicalShareUrl(data.getString("medical_share_url").toString());
        appointmentInfo.setOpinionShareUrl(data.getString("opinion_share_url").toString());
        appointmentInfo.setReviewVideoShareUrl(data.getString("review_video_share_url").toString());
        appointmentInfo.setVisitShareUrl(data.getString("visit_share_url").toString());
        return appointmentInfo;
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("appointment_id", appointmentId);
    }
}
