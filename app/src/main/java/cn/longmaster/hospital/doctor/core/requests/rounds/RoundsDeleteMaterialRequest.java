package cn.longmaster.hospital.doctor.core.requests.rounds;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 删除材料 request
 * Created by Tengshuxiang on 2016-08-17.
 */
public class RoundsDeleteMaterialRequest extends SimpleHttpRequester<String> {
    public int medicalId;//病历id
    public int materialId;//病历资料id
    public String materialPic;//材料图片名称

    public RoundsDeleteMaterialRequest(@NonNull OnResultListener<String> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_DELETE_MEDICAL_RECORD_MATERIAL;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected String onDumpData(JSONObject data) throws JSONException {
        return "";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("medical_id", medicalId);
        params.put("material_id", materialId);
        params.put("material_pic", materialPic);
    }
}
