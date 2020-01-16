package cn.longmaster.hospital.doctor.core.requests.user;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.doctor.FinanceStatisticInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 医生财务统计 requester
 * Created by JinKe on 2016-07-28.
 */
public class FinanceStatisticRequester extends SimpleHttpRequester<FinanceStatisticInfo> {
    public int reqType;
    public int account;

    public FinanceStatisticRequester(@NonNull OnResultListener<FinanceStatisticInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_DOCTOR_FINANCE_STATISTIC;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected FinanceStatisticInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data, FinanceStatisticInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("req_type", reqType);
        params.put("account", account);
    }
}
