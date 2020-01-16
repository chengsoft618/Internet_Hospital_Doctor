package cn.longmaster.hospital.doctor.core.requests.account;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.account.CashListInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 根据账户id拉取账户可提现列表
 * Created by W·H·K on 2019/1/24.
 */
public class CashListRequester extends SimpleHttpRequester<CashListInfo> {
    public int account;

    public CashListRequester(@NonNull OnResultListener<CashListInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_GET_ACCOUNT_CAN_CASH_LIST;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected CashListInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data.getJSONObject("data"), CashListInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("account", account);
    }
}
