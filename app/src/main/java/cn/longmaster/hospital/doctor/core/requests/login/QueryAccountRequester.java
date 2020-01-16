package cn.longmaster.hospital.doctor.core.requests.login;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.common.AppUtil;
import cn.longmaster.doctorlibrary.util.common.MD5Util;
import cn.longmaster.doctorlibrary.util.common.OsUtil;
import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.entity.common.UserResultInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 查询账号请求
 * Created by yangyong on 16/9/8.
 */
public class QueryAccountRequester extends SimpleHttpRequester<UserResultInfo> {
    public String account;
    public String pwd;
    public byte accountType;

    public QueryAccountRequester(@NonNull OnResultListener<UserResultInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getDuwsUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.DUWS_OPTYPE_QUERY_ACCOUNT;
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
        params.put("user_name", account);
        params.put("accounttype", accountType);
        params.put("user_pwd", pwd);
        params.put("phoneos", 1);
        params.put("phonetype", OsUtil.getPhoneType());
        params.put("phoneosversion", OsUtil.getPhoneOSVersion());
        params.put("romversion", OsUtil.getRomVersion());
        params.put("clientversion", AppConfig.CLIENT_VERSION);
        params.put("isdoctor", 0);
        params.put("userorigin", 2);
        params.put("devtoken", "");
        params.put("userfrom", AppUtil.getChannelCode());
        params.put("mac", OsUtil.getMacAddress());
        params.put("imei", OsUtil.getIMEI());
        params.put("sign", MD5Util.md5(account + "_" + pwd + "_" + AppConstant.DUWS_APP_KEY));


    }
}
