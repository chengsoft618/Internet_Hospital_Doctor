package cn.longmaster.hospital.doctor.core.requests.doctor;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseClientApiRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * @author ABiao_Abiao
 * @date 2019/6/12 9:16
 * @description:
 */
public class GetNoEnteringDoctorDetailRequester extends BaseClientApiRequester<DoctorBaseInfo> {
    private int doctorId;

    public GetNoEnteringDoctorDetailRequester(@NonNull OnResultCallback<DoctorBaseInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected DoctorBaseInfo onDumpData(JSONObject data) throws JSONException {
        if (null == JsonHelper.toObject(data.getJSONObject("data"), DoctorBaseInfo.class)) {
            return new DoctorBaseInfo();
        }
        return JsonHelper.toObject(data.getJSONObject("data"), DoctorBaseInfo.class);
    }

    @Override
    protected int getOpType() {
        return 100173;
    }

    @Override
    protected String getTaskId() {
        return "1001";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("token", 111);
        params.put("doctor_id", doctorId);
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }
}
