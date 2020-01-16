package cn.longmaster.hospital.doctor.core.requests.appointment;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.AuxiliaryMaterialInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.AuxiliaryMaterialResult;
import cn.longmaster.hospital.doctor.core.requests.BaseClientApiRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;

/**
 * 获取辅助资料列表 requester
 * Created by JinKe on 2016-07-27.
 */
public class AuxiliaryMaterialRequester extends BaseClientApiRequester<AuxiliaryMaterialResult> {
    public int appointmentId;

    public AuxiliaryMaterialRequester(@NonNull OnResultCallback<AuxiliaryMaterialResult> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_AUXILIARY_MATERIAL_LIST;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected AuxiliaryMaterialResult onDumpData(JSONObject data) throws JSONException {
        AuxiliaryMaterialResult auxiliaryMaterialResult = new AuxiliaryMaterialResult();
        auxiliaryMaterialResult.setMaterialRemindDesc(data.optString("material_remind_desc", ""));
        auxiliaryMaterialResult.setAuxiliaryMaterialInfos(JsonHelper.toList(data.getJSONArray("data"), AuxiliaryMaterialInfo.class));
        return auxiliaryMaterialResult;
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("appointment_id", appointmentId);
    }
}
