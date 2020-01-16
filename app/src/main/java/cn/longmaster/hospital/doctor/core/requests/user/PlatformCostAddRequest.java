package cn.longmaster.hospital.doctor.core.requests.user;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * @author ABiao_Abiao
 * @date 2019/7/11 17:57
 * @description: 添加平台服务费
 */
public class PlatformCostAddRequest extends BaseApiUrlRequester<Void> {
    private String hospitalId;
    private String departmentId;
    private String advanceValue;
    private String amtDt;
    private String fileName;

    public PlatformCostAddRequest(@NonNull OnResultCallback<Void> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected Void onDumpData(JSONObject data) throws JSONException {
        return null;
    }

    @Override
    protected int getOpType() {
        return 100567;
    }

    @Override
    protected String getTaskId() {
        return "123";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("hospital_id", hospitalId);
        params.put("department_id", departmentId);
        params.put("advance_value", advanceValue);
        params.put("amt_dt", amtDt);
        params.put("file_name", fileName);
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public void setAdvanceValue(String advanceValue) {
        this.advanceValue = advanceValue;
    }

    public void setAmtDt(String amtDt) {
        this.amtDt = amtDt;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
