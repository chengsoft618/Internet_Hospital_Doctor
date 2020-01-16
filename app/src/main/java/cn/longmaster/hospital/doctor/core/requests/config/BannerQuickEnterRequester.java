package cn.longmaster.hospital.doctor.core.requests.config;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.common.BannerAndQuickEnterInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;
import cn.longmaster.utils.StringUtils;

/**
 * 客户端Banner与快捷入口配置 requester
 * Created by JinKe on 2016-07-26.
 */
public class BannerQuickEnterRequester extends SimpleHttpRequester<List<BannerAndQuickEnterInfo>> {
    public String token;//同步token标识
    public int bannerType;//banner类型

    public BannerQuickEnterRequester(@NonNull OnResultListener<List<BannerAndQuickEnterInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_BANNER_AND_QUICK_ENTRY;
    }

    @Override
    protected String getTaskId() {
        return super.getTaskId();
    }

    @Override
    protected List<BannerAndQuickEnterInfo> onDumpData(JSONObject data) throws JSONException {
        String datas = data.getString("data");
        if (StringUtils.isTrimEmpty(datas)) {
            return new ArrayList<>(0);
        }
        return JsonHelper.toList(data.getJSONArray("data"), BannerAndQuickEnterInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("token", token);
        params.put("banner_type", bannerType);
    }
}
