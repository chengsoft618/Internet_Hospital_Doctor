package cn.longmaster.hospital.doctor.core.requests.login;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.common.MD5Util;
import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.entity.user.CheckAccountInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 查询账号是否存在
 * Created by yangyong on 16/9/5.
 */
public class CheckAccountExistRequester extends SimpleHttpRequester<CheckAccountInfo> {
    public String userName;// 用户账号
    public int accountType;// 账号类型

    public CheckAccountExistRequester(@NonNull OnResultListener<CheckAccountInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getDuwsUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.DUWS_OPTYPE_CHECK_ACCOUNT_EXIST;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected CheckAccountInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data, CheckAccountInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("user_name", userName);
        params.put("accounttype", accountType);
        params.put("sign", MD5Util.md5(userName + "_" + accountType + "_" + AppConstant.DUWS_APP_KEY));
    }
}
