package cn.longmaster.hospital.doctor.core.requests.user;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;

/**
 * 预约课程
 * Created by W·H·K on 2019/3/19.
 */

public class ReservationCourseRequester extends BaseApiUrlRequester<Void> {
    public int courseId;
    public String appointTime;

    public ReservationCourseRequester(@NonNull OnResultCallback<Void> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_RESERVATION_COURSE;
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
        params.put("appoint_time", appointTime);
    }
}
