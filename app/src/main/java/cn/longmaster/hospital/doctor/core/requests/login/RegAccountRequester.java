package cn.longmaster.hospital.doctor.core.requests.login;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.common.AppUtil;
import cn.longmaster.doctorlibrary.util.common.MD5Util;
import cn.longmaster.doctorlibrary.util.common.OsUtil;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 账号注册请求
 * Created by yangyong on 16/7/25.
 */
public class RegAccountRequester extends SimpleHttpRequester<Integer> {
    public String account;
    public byte accountType;

    public RegAccountRequester(@NonNull OnResultListener<Integer> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getDuwsUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.DUWS_OPTYPE_REG_ACCOUNT;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected Integer onDumpData(JSONObject data) throws JSONException {
        int userID = data.getJSONObject("data").optInt("userID", 0);
        return userID;
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("user_name", account);
        params.put("accounttype", accountType);
        params.put("version", AppConfig.CLIENT_VERSION);
        params.put("userorigin", 2);
        params.put("phoneos", 1);
        params.put("phonetype", OsUtil.getPhoneType());
        params.put("phoneosversion", OsUtil.getPhoneOSVersion());
        params.put("iosdevicetoken", "");
        params.put("userfrom", AppUtil.getChannelCode());
        params.put("mac", OsUtil.getMacAddress());
        params.put("imei", OsUtil.getIMEI());
        params.put("sign", MD5Util.md5(OpTypeConfig.DUWS_OPTYPE_REG_ACCOUNT + "_" + account + "_" + AppConstant.DUWS_APP_KEY));
        params.put("romversion", OsUtil.getRomVersion());
    }
}
