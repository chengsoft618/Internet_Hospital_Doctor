package cn.longmaster.hospital.doctor.core.requests.consultant;


import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.consultant.AdvanceResultInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 垫付请求接口
 * Created by W·H·K on 2018/1/2.
 */

public class AdvanceRequester extends SimpleHttpRequester<AdvanceResultInfo> {

    public List<Integer> appointmentIdList;

    public AdvanceRequester(@NonNull OnResultListener<AdvanceResultInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_ADVANCE;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected AdvanceResultInfo onDumpData(JSONObject data) throws JSONException {
        Logger.logI(Logger.COMMON, "AdvanceRequester-->data:" + data);
        return JsonHelper.toObject(data.getJSONObject("data"), AdvanceResultInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("appointment_id_list", appointmentIdList);

    }
}
