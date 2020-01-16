package cn.longmaster.hospital.doctor.core.requests.rounds;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.rounds.HospitalAndDepartmentInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * Created by W·H·K on 2019/2/19.
 * 根据医院id及医院所属科室id拉取医院及科室信息
 */

public class GetHospitalAndDepartmentInfoRequester extends SimpleHttpRequester<HospitalAndDepartmentInfo> {
    public int hospitalId;
    public int hosdepId;

    public GetHospitalAndDepartmentInfoRequester(@NonNull OnResultListener<HospitalAndDepartmentInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_GET_HOSPITAL_DEPARTMENT_INFO;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected HospitalAndDepartmentInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data.getJSONObject("data"), HospitalAndDepartmentInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("hospital_id", hospitalId);
        params.put("hosdep_id", hosdepId);
    }
}
