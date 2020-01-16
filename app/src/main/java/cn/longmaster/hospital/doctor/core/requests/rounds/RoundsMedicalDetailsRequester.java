package cn.longmaster.hospital.doctor.core.requests.rounds;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.rounds.RoundsMedicalDetailsInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;

/**
 * 病历详情equester
 * <p>
 * Created by W·H·K on 2018/5/14.
 * Mod by biao on 2019/11/26
 */

public class RoundsMedicalDetailsRequester extends BaseApiUrlRequester<RoundsMedicalDetailsInfo> {
    private int medicalId;

    public RoundsMedicalDetailsRequester(@NonNull OnResultCallback<RoundsMedicalDetailsInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_GET_MEDICAL_RECORD;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected RoundsMedicalDetailsInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data.getJSONObject("data"), RoundsMedicalDetailsInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("medical_id", medicalId);
    }

    public void setMedicalId(int medicalId) {
        this.medicalId = medicalId;
    }
}
