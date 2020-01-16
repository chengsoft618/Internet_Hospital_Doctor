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
 * 新增影像服务
 * Created by Yang² on 2016/7/28.
 */
public class AddServiceRequester extends SimpleHttpRequester<Integer> {
    public int doctorId;//医生ID
    public int gradePriceId;//档位价格ID
    public int admissionNum;//服务人数

    public AddServiceRequester(@NonNull OnResultListener<Integer> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_ADD_SERVICE;
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
        params.put("doctor_id", doctorId);
        params.put("grade_price_id", gradePriceId);
        params.put("admission_num", admissionNum);
    }
}
