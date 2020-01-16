package cn.longmaster.hospital.doctor.core.requests.consult;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 视频诊室上传ip地址及经纬度信息
 * Created by Yang² on 2018/4/13.
 */

public class UploadLocationNetworkRequester extends SimpleHttpRequester<Void> {
    public int appointmentId;
    public String ipHome;//IP地址
    public String latitude;//经度
    public String longitude;//纬度
    public String address;//地址

    public UploadLocationNetworkRequester(@NonNull OnResultListener<Void> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_UPLOAD_LOCATION_NETWORK;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected Void onDumpData(JSONObject data) throws JSONException {
        return null;
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("appointment_id", appointmentId);
        params.put("ip_home", ipHome);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("address", address);
    }
}
