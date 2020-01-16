package cn.longmaster.hospital.doctor.core.requests.consult.imaging;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 修改影像服务 requester
 * Created by JinKe on 2016-07-28.
 */
public class ReviseImageServiceRequester extends SimpleHttpRequester<Integer> {
    public int scheduingId;//服务ID
    public int doctorId;//医生ID
    public int gradePriceId;//档位价格ID
    public int admissionNum;//服务人数

    public ReviseImageServiceRequester(@NonNull OnResultListener<Integer> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_REVISE_IMAGE_SERVICE;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected Integer onDumpData(JSONObject data) throws JSONException {
        return data.optInt("scheduing_id", 0);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("scheduing_id", scheduingId);
        params.put("doctor_id", doctorId);
        params.put("grade_price_id", gradePriceId);
        params.put("admission_num", admissionNum);
    }
}
