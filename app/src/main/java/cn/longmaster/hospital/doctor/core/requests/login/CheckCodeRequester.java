package cn.longmaster.hospital.doctor.core.requests.login;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.common.MD5Util;
import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.DwpOpType;
import cn.longmaster.hospital.doctor.core.entity.common.UserResultInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 校验验证码请求
 * Created by yangyong on 16/7/28.
 */
public class CheckCodeRequester extends SimpleHttpRequester<UserResultInfo> {
    public int userId;
    public String account;
    public String verifyCode;
    public int requestType;
    public String password;

    public CheckCodeRequester(@NonNull OnResultListener<UserResultInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getDuwsUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.DUWS_OPTYPE_CHECK_VERIFY_CODE;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected UserResultInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data, UserResultInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("user_id", userId);
        params.put("user_name", account);
        params.put("verify_code", verifyCode);
        params.put("request_type", requestType);
        params.put("password", password);
        params.put("userorigin", 2);
        params.put("sign", MD5Util.md5(DwpOpType.CHECK_VERIFY_CODE + "_" + userId + "_" + AppConstant.DUWS_APP_KEY));
    }
}
