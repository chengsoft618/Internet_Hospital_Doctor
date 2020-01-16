package cn.longmaster.hospital.doctor.core.requests.login;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.common.AppUtil;
import cn.longmaster.doctorlibrary.util.common.MD5Util;
import cn.longmaster.doctorlibrary.util.common.OsUtil;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.DwpOpType;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 获取验证码请求
 * Created by yangyong on 16/7/28.
 */
public class RegCodeRequester extends SimpleHttpRequester<Integer> {
    public String account;
    public byte requestType;

    public RegCodeRequester(@NonNull OnResultListener<Integer> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getDuwsUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.DUWS_OPTYPE_REG_CODE;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected Integer onDumpData(JSONObject data) throws JSONException {
        int userId = data.optInt("userID", 0);
        return userId;
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("user_name", account);
        params.put("request_type", requestType);
        params.put("userorigin", 2);
        params.put("phoneos", 1);
        params.put("phonetype", OsUtil.getPhoneType());
        params.put("phoneosversion", OsUtil.getPhoneOSVersion());
        params.put("iosdevicetoken", "");
        params.put("romversion", OsUtil.getRomVersion());
        params.put("userfrom", AppUtil.getChannelCode());
        params.put("sign", MD5Util.md5(DwpOpType.REG_CODE + "_" + account + "_" + requestType + "_" + AppApplication.getInstance().getAppKey()));
    }
}
