package cn.longmaster.hospital.doctor.core.requests.rounds;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.consult.AuxiliaryMaterialInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;

/**
 * 根据病历id获取辅助检查requester
 * <p>
 * Created by W·H·K on 2018/5/14.
 */

public class MedicalAuxiliaryInspectRequester extends BaseApiUrlRequester<List<AuxiliaryMaterialInfo>> {
    private int medicalId;

    public MedicalAuxiliaryInspectRequester(@NonNull OnResultCallback<List<AuxiliaryMaterialInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_GET_MEDICAL_RECORD_MATERIAL;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected List<AuxiliaryMaterialInfo> onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toList(data.getJSONArray("data"), AuxiliaryMaterialInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("medical_id", medicalId);
    }

    public void setMedicalId(int medicalId) {
        this.medicalId = medicalId;
    }
}