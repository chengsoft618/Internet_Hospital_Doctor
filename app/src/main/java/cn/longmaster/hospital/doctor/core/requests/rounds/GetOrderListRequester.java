package cn.longmaster.hospital.doctor.core.requests.rounds;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.rounds.OrderListInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;

/**
 * 获取订单列表requester
 * <p>
 * Created by W·H·K on 2018/5/14.
 */

public class GetOrderListRequester extends BaseApiUrlRequester<OrderListInfo> {
    /**
     * order_state订单状态 state_reason 订单状态原因
     * 1、等待分诊：预约发起后尚无指定接诊医生时
     * 1-0 未分诊
     * 1-1 已推荐上级专家、但首诊医生未确认
     * 4、等待接诊：首诊医生已经确认上级专家，等待上级专家确认接诊
     * 4-0 等待接诊
     * 8、等待就诊：预约发起后已经选择指定就诊医生，且已配置查房时间，尚未点击完成问诊
     * 8-0 等待就诊
     * 10、就诊完成：上级专家点击了完成问诊
     * 10-0 就诊完成：上级专家点击了完成问诊
     * 15
     * 15-2 就诊关闭：导医后台点击了就诊关闭按钮
     * 15-3 全部完成：就诊完成并已结算
     */
    private String orderState;
    private String stateReason;
    private int pageindex;
    private int pagesize;
    //0不拉取同科室
    //1拉取同科室
    private int sameDep;
    private String keyWords;

    public GetOrderListRequester(@NonNull OnResultCallback<OrderListInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_GET_ORDER_LIST;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected OrderListInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data.getJSONObject("data"), OrderListInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("order_state", orderState);
        params.put("state_reason", stateReason);
        params.put("pageindex", pageindex);
        params.put("pagesize", pagesize);
        params.put("same_dep", sameDep);
        params.put("key_words", keyWords);
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public void setStateReason(String stateReason) {
        this.stateReason = stateReason;
    }

    public void setPageindex(int pageindex) {
        this.pageindex = pageindex;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public void setSameDep(int sameDep) {
        this.sameDep = sameDep;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }
}