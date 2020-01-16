package cn.longmaster.hospital.doctor.core.requests.dutyclinic;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCAdviceEditorInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 值班门诊-订单开具医嘱单锁定/解除锁定
 * Created by yangyong on 2019-11-30.
 */
public class SetDCAdviceEditStateRequester extends SimpleHttpRequester<DCAdviceEditorInfo> {
    public int orderId;
    //
    public int editState;

    public SetDCAdviceEditStateRequester(@NonNull OnResultListener<DCAdviceEditorInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getApiUrl();
    }

    @Override
    protected int getOpType() {
        return 100583;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected DCAdviceEditorInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data.optJSONObject("data"), DCAdviceEditorInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("order_id", orderId);
        params.put("edit_state", editState);
    }
}
