package cn.longmaster.hospital.doctor.core.requests.config;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.doctor.PackageInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;
import cn.longmaster.utils.StringUtils;

/**
 * 套餐信息配置Requester
 * Created by Yang² on 2016/7/26.
 */
public class PackageRequester extends SimpleHttpRequester<List<PackageInfo>> {
    public String token;//同步token标识

    public PackageRequester(@NonNull OnResultListener<List<PackageInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_PACKAGE_INFO;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected List<PackageInfo> onDumpData(JSONObject data) throws JSONException {
        String datas = data.getString("data");
        if (StringUtils.isTrimEmpty(datas)) {
            return new ArrayList<>(0);
        }
        return JsonHelper.toList(data.getJSONArray("data"), PackageInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("token", token);
    }
}
