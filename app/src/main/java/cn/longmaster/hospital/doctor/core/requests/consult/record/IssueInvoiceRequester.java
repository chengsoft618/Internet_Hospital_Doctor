package cn.longmaster.hospital.doctor.core.requests.consult.record;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 开具发票
 * Created by Yang² on 2016/7/27.
 */
public class IssueInvoiceRequester extends SimpleHttpRequester<Void> {
    public String appointmentId;//预约编号，多个预约使用英文逗号隔开
    public int invoiceHeader;//发票抬头，1个人、 2单位
    public String invoiceHeaderInfo;//抬头内容
    public String phoneNum;//电话
    public String address;//地址
    public String postcode;//邮政编码

    public IssueInvoiceRequester(@NonNull OnResultListener<Void> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_ISSUE_INVOICE;
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
        params.put("invoice_header", invoiceHeader);
        params.put("invoice_header_info", invoiceHeaderInfo);
        params.put("phone_num", phoneNum);
        params.put("address", address);
        params.put("postcode", postcode);
    }
}
