package cn.longmaster.hospital.doctor.core.requests.user;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 添加账户接口
 */
public class AddBankAccountRequester extends SimpleHttpRequester<Void> {
    public String cardNum;
    public String realName;
    public String bankName;
    public String idNumber;
    public int account;

    public AddBankAccountRequester(@NonNull OnResultListener<Void> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_ADD_BANK_ACCOUNT;
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
        params.put("card_num", cardNum);
        params.put("real_name", realName);
        params.put("bank_name", bankName);
        params.put("card_type", AppConstant.PayType.PAY_TYPE_BANK_CARD);
        params.put("id_number", idNumber);
        params.put("account", account);
    }
}
