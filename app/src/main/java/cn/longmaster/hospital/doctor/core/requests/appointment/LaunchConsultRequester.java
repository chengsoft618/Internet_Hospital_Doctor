package cn.longmaster.hospital.doctor.core.requests.appointment;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.consult.FormForConsult;
import cn.longmaster.hospital.doctor.core.entity.consult.LaunchConsultInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseClientApiRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;

/**
 * 发起会诊 requester
 * Created by JinKe on 2016-07-28.
 */
public class LaunchConsultRequester extends BaseClientApiRequester<LaunchConsultInfo> {
    private FormForConsult formForConsult;

    public LaunchConsultRequester(@NonNull OnResultCallback<LaunchConsultInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_LAUNCH_CONSULT;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected LaunchConsultInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data, LaunchConsultInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("patient_desc", formForConsult.getPatientDesc());
        params.put("disease_name", formForConsult.getDiseaseName());
        params.put("file_path", formForConsult.getFilePath());
        params.put("real_name", formForConsult.getRealName());
        params.put("phone_num", formForConsult.getPhoneNum());
        params.put("scheduing_id", formForConsult.getScheduleOrImageInfo() == null ? 0 : formForConsult.getScheduleOrImageInfo().getScheduingId());
        params.put("attdoc_id", formForConsult.getAttdocId());
        params.put("source", formForConsult.getSource());
        params.put("card_no", formForConsult.getCardNo());
        params.put("scheduing_type", formForConsult.getScheduingType());
        params.put("service_type", formForConsult.getServiceType());
        params.put("doctor_id", formForConsult.getDoctorBaseInfo() == null ? 0 : formForConsult.getDoctorBaseInfo().getUserId());
        params.put("top_appointment_id", formForConsult.getTopAppointId());
        params.put("superior_appointment_id", formForConsult.getSuperiorAppointId());
        params.put("patient_user_id", formForConsult.getPatientUserId());
    }

    public void setFormForConsult(FormForConsult formForConsult) {
        this.formForConsult = formForConsult;
    }
}
