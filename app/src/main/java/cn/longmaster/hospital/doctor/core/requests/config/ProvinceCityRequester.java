package cn.longmaster.hospital.doctor.core.requests.config;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.doctor.ProvinceInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseClientApiRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.utils.StringUtils;

/**
 * 省份城市信息配置
 * Created by JinKe on 2016-07-26.
 */
public class ProvinceCityRequester extends BaseClientApiRequester<List<ProvinceInfo>> {
    public String token;//同步token标识

    public ProvinceCityRequester(@NonNull OnResultCallback<List<ProvinceInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_GET_PROVINCE_LIST;
    }

    @Override
    protected String getTaskId() {
        return "133";
    }

    @Override
    protected List<ProvinceInfo> onDumpData(JSONObject data) throws JSONException {
        String datas = data.getString("data");
        if (StringUtils.isTrimEmpty(datas)) {
            return new ArrayList<>(0);
        }
        return JsonHelper.toList(data.getJSONArray("data"), ProvinceInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("token", token);
    }
}
