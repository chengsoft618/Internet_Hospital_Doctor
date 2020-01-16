package cn.longmaster.hospital.doctor.core.requests.consult;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 上传辅助材料文件名
 * Created by Tengshuxiang on 2016-08-15.
 */
public class UploadMaterialRequest extends SimpleHttpRequester<String> {
    public int appointmentId;//	病历号
    public int materialId;//辅助检查项ID
    public String materialPic;//资料图片 多张图片请用分号(;)隔开
    public String materialDt;//资料时间
    public int recureNum;//复诊次数	初诊传0
    public int docUserId;//医生ID

    public UploadMaterialRequest(@NonNull OnResultListener<String> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getNdwsUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.DWS_OPTYPE_UPLOAD_MATERIAL;
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
        params.put("material_dt", materialDt);
        params.put("recure_num", recureNum);
        params.put("doc_user_id", docUserId);
    }
}
