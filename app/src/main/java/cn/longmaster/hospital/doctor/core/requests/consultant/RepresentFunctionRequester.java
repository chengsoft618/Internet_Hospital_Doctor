package cn.longmaster.hospital.doctor.core.requests.consultant;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.consultant.RepresentFunctionList;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 获取销售代表功能列表
 * Created by Yang² on 2017/11/27.
 */

public class RepresentFunctionRequester extends SimpleHttpRequester<RepresentFunctionList> {
    public String token = "0";// 就诊编号

    public RepresentFunctionRequester(@NonNull OnResultListener<RepresentFunctionList> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_REPRESENT_FUNCTION;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected RepresentFunctionList onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data.getJSONObject("data"), RepresentFunctionList.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("token", token);
    }
}
