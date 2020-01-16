package cn.longmaster.hospital.doctor.core.requests.dutyclinic;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCMedicalInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * 值班门诊-根据订单id拉取病历列表
 * Created by yangyong on 2019-12-05.
 */
public class GetDcMedicalListRequester extends BaseApiUrlRequester<List<DCMedicalInfo>> {
    public int orderId;

    public GetDcMedicalListRequester(@NonNull OnResultCallback<List<DCMedicalInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected List<DCMedicalInfo> onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toList(data.optJSONArray("data"), DCMedicalInfo.class);
    }

    @Override
    protected int getOpType() {
        return 100591;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("order_id", orderId);
    }
}
