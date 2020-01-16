package cn.longmaster.hospital.doctor.core.requests.consultant;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 编辑排班相关的支付确认单信息
 * Created by Yang² on 2017/12/28.
 */

public class ModifySchedulePaymentRequester extends SimpleHttpRequester<Void> {
    public int scheduingId; //排班ID
    public int paymentType; //方式	1：病例、2：时长
    public String beginDt; //开始时间
    public String endDt; //结束时间
    public String dtLength; //时长
    public String payValue; //金额
    public List<String> picList; //图片地址列表	格式：["201712.20171226165302253902l5t3.jpg", "201712.2017122617125325391Pzjms.jpg"]

    public ModifySchedulePaymentRequester(@NonNull OnResultListener<Void> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_MODIFY_SCHEDULE_PAYMENT;
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
        params.put("scheduing_id", scheduingId);
        params.put("payment_type", paymentType);
        params.put("begin_dt", beginDt);
        params.put("end_dt", endDt);
        params.put("dt_length", dtLength);
        params.put("pay_value", payValue);
        params.put("pic_list", picList == null ? "" : JsonHelper.toJSONArray(picList));
    }
}
