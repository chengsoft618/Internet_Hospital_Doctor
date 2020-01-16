package cn.longmaster.hospital.doctor.core.requests.user;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.user.PlatformInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * @author ABiao_Abiao
 * @date 2019/7/15 9:42
 * @description: 获取平台服务费详情
 */
public class PlatformCostDetailGetRequest extends BaseApiUrlRequester<PlatformInfo> {
    private String id;

    public PlatformCostDetailGetRequest(@NonNull OnResultCallback<PlatformInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected PlatformInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data.getJSONObject("data"), PlatformInfo.class);
    }

    @Override
    protected int getOpType() {
        return 100564;
    }

    @Override
    protected String getTaskId() {
        return "123";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("id", id);
    }

    public void setId(String id) {
        this.id = id;
    }
}
