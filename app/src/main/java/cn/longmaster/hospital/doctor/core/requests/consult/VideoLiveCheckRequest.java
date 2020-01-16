package cn.longmaster.hospital.doctor.core.requests.consult;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.GetOrderInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * Created by W·H·K on 2018/3/8.
 * 根据病历号拉取指定订单的详细信息
 */

public class VideoLiveCheckRequest extends SimpleHttpRequester<GetOrderInfo> {
    public int appointmentId;//	病历号

    public VideoLiveCheckRequest(@NonNull OnResultListener<GetOrderInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getNdwsUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.DWS_OPTYPE_VIDEO_LIVE_CHECK;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected GetOrderInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data, GetOrderInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("appointment_id", appointmentId);
    }
}
