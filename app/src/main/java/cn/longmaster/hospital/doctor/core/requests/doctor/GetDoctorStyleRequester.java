package cn.longmaster.hospital.doctor.core.requests.doctor;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorStyleInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseClientApiRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * @author ABiao_Abiao
 * @date 2019/6/12 15:30
 * @description: 获取专家风采
 */
public class GetDoctorStyleRequester extends BaseClientApiRequester<DoctorStyleInfo> {
    private int doctorId;

    public GetDoctorStyleRequester(@NonNull OnResultCallback<DoctorStyleInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected DoctorStyleInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data.getJSONObject("data"), DoctorStyleInfo.class);
    }

    @Override
    protected int getOpType() {
        return 100170;
    }

    @Override
    protected String getTaskId() {
        return "111";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("doctor_id", doctorId);
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }
}
