package cn.longmaster.hospital.doctor.core.requests.user;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * @author ABiao_Abiao
 * @date 2019/7/11 17:57
 * @description: 修改平台服务费
 */
public class PlatformCostModRequest extends BaseApiUrlRequester<Void> {
    private String id;
    private String advanceValue;
    private String amtDt;
    private String fileName;

    public PlatformCostModRequest(@NonNull OnResultCallback<Void> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected Void onDumpData(JSONObject data) throws JSONException {
        return null;
    }

    @Override
    protected int getOpType() {
        return 100566;
    }

    @Override
    protected String getTaskId() {
        return "123";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("id", id);
        params.put("advance_value", advanceValue);
        params.put("amt_dt", amtDt);
        params.put("file_name", fileName);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAdvanceValue(String advanceValue) {
        this.advanceValue = advanceValue;
    }

    public void setAmtDt(String amtDt) {
        this.amtDt = amtDt;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
