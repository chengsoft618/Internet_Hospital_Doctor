package cn.longmaster.hospital.doctor.core.requests.consult.record;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.record.UploadDiagnosisInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 出具医嘱 requester
 * Created by JinKe on 2016-07-27.
 */
public class IssueDIagnosisRequester extends SimpleHttpRequester<Void> {
    public int appointmentId;//预约id
    public int doctorId;//医生ID
    public String recureDt;//复诊提醒时间Y-m-d H:i:s格式
    /**
     * diagnosis_picture	报告图文件名
     * recure_num	复诊次数
     * recure_desc	医嘱描述
     * audio_src	语音文件路径名
     * audio_dt	语音时长
     */
    public List<UploadDiagnosisInfo> diagnosisInfoList;

    public IssueDIagnosisRequester(@NonNull OnResultListener<Void> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_ISSUE_DOCTOR_ADVICE;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected Void onDumpData(JSONObject data) throws JSONException {
        return null;
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("appointment_id", appointmentId);
        params.put("doctor_id", doctorId);
        params.put("recure_dt", recureDt);
        params.put("diagnosis_info", JsonHelper.toJSONArray(diagnosisInfoList));
    }
}
