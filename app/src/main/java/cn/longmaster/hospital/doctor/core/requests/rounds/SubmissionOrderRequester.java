package cn.longmaster.hospital.doctor.core.requests.rounds;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.rounds.OrderInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;

/**
 * 提交订单requester
 * <p>
 * Created by W·H·K on 2018/5/14.
 */

public class SubmissionOrderRequester extends BaseApiUrlRequester<OrderInfo> {
    public int doctorId;
    public String orderTitle;
    public String triageRequire;
    public String visitAppeal;
    public String intentionDuration;
    public int isPpt;
    public int orderType;
    public JSONArray patientList;
    public JSONArray cureDtList;
    public JSONArray medicalList;
    public JSONArray departmentList;

    public SubmissionOrderRequester(@NonNull OnResultCallback<OrderInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_SUBMISSION_ORDER;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected OrderInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data.getJSONObject("data"), OrderInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("doctor_id", doctorId);
        params.put("order_title", orderTitle);
        params.put("triage_require", triageRequire);
        params.put("intention_duration", intentionDuration);
        params.put("is_ppt", isPpt);
        params.put("order_type", orderType);
        params.put("patient_list", patientList);
        params.put("cure_dt_list", cureDtList);
        params.put("visit_appeal", visitAppeal);
        params.put("medical_list", medicalList);
        params.put("intention_department_list", departmentList);
    }
}
