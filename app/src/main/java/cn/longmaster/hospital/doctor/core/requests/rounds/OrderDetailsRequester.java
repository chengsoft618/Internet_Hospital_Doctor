package cn.longmaster.hospital.doctor.core.requests.rounds;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.rounds.OrderDetailsInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;

/**
 * 订单详情requester
 * <p>
 * Created by W·H·K on 2018/5/14.
 */

public class OrderDetailsRequester extends BaseApiUrlRequester<OrderDetailsInfo> {
    public int orderId;

    public OrderDetailsRequester(@NonNull OnResultCallback<OrderDetailsInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_GET_ORDER_DETAILS;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected OrderDetailsInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data.getJSONObject("data"), OrderDetailsInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("order_id", orderId);
    }
}
