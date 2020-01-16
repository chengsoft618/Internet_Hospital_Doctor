package cn.longmaster.hospital.doctor.core.requests.doctor;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.doctor.AssistantDoctorInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseClientApiRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;

/**
 * 拉取助理医师信息
 * Created by Yang² on 2016/7/27.
 */
public class AssistantDoctorRequester extends BaseClientApiRequester<AssistantDoctorInfo> {
    public int assistantId;//助理医师ID

    public AssistantDoctorRequester(@NonNull OnResultCallback<AssistantDoctorInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_ASSISTANT_DOCTOR;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected AssistantDoctorInfo onDumpData(JSONObject data) throws JSONException {
        String JSONData = data.optString("data").toString();
        if ("".equals(JSONData)) {
            return new AssistantDoctorInfo();
        }
        return JsonHelper.toObject(data.getJSONObject("data"), AssistantDoctorInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("assistant_doctor_id", assistantId);
    }
}
