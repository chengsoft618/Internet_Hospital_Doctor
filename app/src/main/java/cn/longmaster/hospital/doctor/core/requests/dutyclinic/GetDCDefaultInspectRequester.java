package cn.longmaster.hospital.doctor.core.requests.dutyclinic;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCInspectInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * 值班门诊-拉取默认配置检查项
 * Created by yangyong on 2019-12-02.
 */
public class GetDCDefaultInspectRequester extends BaseApiUrlRequester<List<DCInspectInfo>> {
    public GetDCDefaultInspectRequester(@NonNull OnResultCallback<List<DCInspectInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected List<DCInspectInfo> onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toList(data.optJSONArray("data"), DCInspectInfo.class);
    }

    @Override
    protected int getOpType() {
        return 100589;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {

    }
}
