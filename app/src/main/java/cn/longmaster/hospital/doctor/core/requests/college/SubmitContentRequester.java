package cn.longmaster.hospital.doctor.core.requests.college;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 提交评论
 * Created by Yang² on 2018/3/27.
 */

public class SubmitContentRequester extends SimpleHttpRequester<Void> {
    public int courseId;
    public int appointmentId;
    public String msgContent;
    public int msgType = 301;

    public SubmitContentRequester(@NonNull OnResultListener<Void> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_SUBMIT_CONTENT;
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
        params.put("course_id", courseId);
        params.put("appointment_id", appointmentId);
        params.put("msg_content", msgContent);
        params.put("msg_type", msgType);
    }
}
