package cn.longmaster.hospital.doctor.core.requests.user;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 余额提现接口
 * Created by Yang² on 2016/7/28.
 */
public class BalanceWithdrawalRequester extends SimpleHttpRequester<Void> {
    public float opValue;//提现金额
    public String bankNo;//提现银行卡号
    public int payType;//银行账户类型 1：银行卡; 2：支付宝; 3：微信
    public int settlementType;//申请人身份类型 1：后台管理员; 2：医生; 3：患者

    public BalanceWithdrawalRequester(@NonNull OnResultListener<Void> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_BALANCE_WITHDRAWAL;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected Void onDumpData(JSONObject data) throws JSONException {
        return null;
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("op_value", opValue);
        params.put("bank_no", bankNo);
        params.put("pay_type", payType);
        params.put("settlement_type", settlementType);
    }
}
