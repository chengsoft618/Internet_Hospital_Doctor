package cn.longmaster.hospital.doctor.core.requests.user;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.user.ConfirmInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * @author ABiao_Abiao
 * @date 2019/7/16 9:46
 * @description: 获取确认单费详情
 */
public class ConfirmDetailGetRequest extends BaseApiUrlRequester<ConfirmInfo> {
    private String id;
    //1会诊2查房
    private int type;

    public ConfirmDetailGetRequest(@NonNull OnResultCallback<ConfirmInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected ConfirmInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data.getJSONObject("data"), ConfirmInfo.class);
    }

    @Override
    protected int getOpType() {
        return 100570;
    }

    @Override
    protected String getTaskId() {
        return "123";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("id", id);
        params.put("type", type);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
