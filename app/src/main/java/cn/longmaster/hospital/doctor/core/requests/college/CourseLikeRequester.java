package cn.longmaster.hospital.doctor.core.requests.college;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.college.LikeInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 点赞
 * Created by Yang² on 2018/3/27.
 */

public class CourseLikeRequester extends SimpleHttpRequester<LikeInfo> {
    public int courseId;

    public CourseLikeRequester(@NonNull OnResultListener<LikeInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_COURSE_LIKE;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected LikeInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data.getJSONObject("data"), LikeInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("course_id", courseId);
    }
}
