package cn.longmaster.hospital.doctor.core.requests.appointment;

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
public class DeleteMaterialRequest extends SimpleHttpRequester<String> {
    public int appointmentId;//预约id
    public int materialId;//材料id
    public String materialPic;//材料图片名

    public DeleteMaterialRequest(@NonNull OnResultListener<String> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getNdwsUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.DWS_OPTYPE_DEL_MATERIAL;
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
        params.put("appointment_id", appointmentId);
        params.put("material_id", materialId);
        params.put("material_pic", materialPic);

    }
}
