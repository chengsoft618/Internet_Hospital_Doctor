package cn.longmaster.hospital.doctor.core.requests.appointment;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.AssessInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * Created by yangyong on 2019-07-16.
 */
public class GetAssessInfoRequester extends SimpleHttpRequester<AssessInfo> {
    public int orderId;
    public int attDocId;

    public GetAssessInfoRequester(@NonNull OnResultListener<AssessInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_GET_ASSESS_INFO;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected AssessInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data.getJSONObject("data"), AssessInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("order_id", orderId);
        params.put("attdoc_id", attDocId);
    }
}
