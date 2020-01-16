package cn.longmaster.hospital.doctor.core.requests.user;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 提交个人资料文献
 * <p>
 * Created by W·H·K on 2018/7/31.
 */
public class SubmissionPersonalDataRequester extends SimpleHttpRequester<Void> {
    public String fileName;
    public String materialName;
    public int fileType;//file_type：1、pdf，2、链接地址，3、ppt
    public int doctorId;

    public SubmissionPersonalDataRequester(@NonNull OnResultListener<Void> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_SUBMISSION_PERSONAL_DATA;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected Void onDumpData(JSONObject data) throws JSONException {
        return null;
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("file_name", fileName);
        params.put("material_name", materialName);
        params.put("doctor_id", doctorId);
        params.put("file_type", fileType);
    }
}
