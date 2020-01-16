package cn.longmaster.hospital.doctor.core.requests.consultant;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.consultant.SchedulePaymentInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 获取排班相关的支付确认单信息
 * Created by Yang² on 2017/12/28.
 */

public class GetSchedulePaymentRequester extends SimpleHttpRequester<SchedulePaymentInfo> {
    public int scheduingId;

    public GetSchedulePaymentRequester(@NonNull OnResultListener<SchedulePaymentInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_GET_SCHEDULE_PAYMENT;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected SchedulePaymentInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data.getJSONObject("data"), SchedulePaymentInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("scheduing_id", scheduingId);
    }
}
