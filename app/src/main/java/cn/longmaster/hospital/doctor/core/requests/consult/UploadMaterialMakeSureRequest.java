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
 * 提交材料信息确认请求
 * Created by YY on 17/9/11.
 */

public class UploadMaterialMakeSureRequest extends SimpleHttpRequester<Integer> {
    public int userId;//医生id
    public int appointmentId;//预约id
    public String materialPic;//文件名称
    public int uploadType;//材料上传类型 0:普通材料 1:病情描述图片
    public int checkState;//审核状态 0：未审核 1：审核成功

    public UploadMaterialMakeSureRequest(@NonNull OnResultListener<Integer> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getDwsUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.DWS_UPLOAD_MATERIAL_MAKE_SURE;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected Integer onDumpData(JSONObject data) throws JSONException {

        return data.optInt("appointment_id", 0);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("user_id", userId);
        params.put("appointment_id", appointmentId);
        params.put("material_pic", materialPic);
        params.put("upload_type", uploadType);
        params.put("check_state", checkState);
    }
}
