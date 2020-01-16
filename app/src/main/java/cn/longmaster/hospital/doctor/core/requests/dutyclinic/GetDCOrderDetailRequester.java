package cn.longmaster.hospital.doctor.core.requests.dutyclinic;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCOrderDetailInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * 值班门诊-获取值班门诊订单详情
 * Created by yangyong on 2019-11-30.
 */
public class GetDCOrderDetailRequester extends BaseApiUrlRequester<DCOrderDetailInfo> {
    public int orderId;

    public GetDCOrderDetailRequester(@NonNull OnResultCallback<DCOrderDetailInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected DCOrderDetailInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data.optJSONObject("data"), DCOrderDetailInfo.class);
    }

    @Override
    protected int getOpType() {
        return 100582;
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
