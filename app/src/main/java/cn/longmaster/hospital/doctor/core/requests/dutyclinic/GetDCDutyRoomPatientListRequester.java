package cn.longmaster.hospital.doctor.core.requests.dutyclinic;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyRoomPatientItem;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * @author ABiao_Abiao
 * @date 2020/1/3 14:43
 * @description: 根据订单id拉取值班门诊患者列表
 */
public class GetDCDutyRoomPatientListRequester extends BaseApiUrlRequester<List<DCDutyRoomPatientItem>> {
    private int orderId;
    //非必传 传0时拉所有
    private int pageIndex;
    //非必传 传0时拉所有
    private int pageSize;

    public GetDCDutyRoomPatientListRequester(@NonNull OnResultCallback<List<DCDutyRoomPatientItem>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected List<DCDutyRoomPatientItem> onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toList(data.getJSONArray("data"), DCDutyRoomPatientItem.class);
    }

    @Override
    protected int getOpType() {
        return 100607;
    }

    @Override
    protected String getTaskId() {
        return "100607";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("order_id", orderId);
        params.put("page_index", pageIndex);
        params.put("page_size", pageSize);
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
