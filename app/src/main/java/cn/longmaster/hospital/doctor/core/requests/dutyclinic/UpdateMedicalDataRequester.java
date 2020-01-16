package cn.longmaster.hospital.doctor.core.requests.dutyclinic;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * @author ABiao_Abiao
 * @date 2019/12/28 15:05
 * @description: 根据病历id及病程id更新患者病程（当病程id为0时为新增病程）
 */
public class UpdateMedicalDataRequester extends BaseApiUrlRequester<Void> {
    //病历ID
    private int medicalId;
    //病程记录id
    private int recordId;
    //病程记录时间
    private String recordDt;
    //病程记录类型 必传 1：首诊 2：复诊 3：处方医嘱 4：病例 5：影像 6：化验 7：体征  8：入院 9：出院 10：手术 11：随访记录
    private int recordType;
    //病程记录详情
    private JSONArray prognoteList;

    public UpdateMedicalDataRequester(@NonNull OnResultCallback<Void> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected Void onDumpData(JSONObject data) throws JSONException {
        return null;
    }

    @Override
    protected int getOpType() {
        return 100595;
    }

    @Override
    protected String getTaskId() {
        return "100595";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("medical_id", medicalId);
        params.put("record_id", recordId);
        params.put("record_dt", recordDt);
        params.put("attr", recordType);
        params.put("prognote_list", prognoteList);
    }

    public void setMedicalId(int medicalId) {
        this.medicalId = medicalId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public void setRecordDt(String recordDt) {
        this.recordDt = recordDt;
    }

    public void setRecordType(int recordType) {
        this.recordType = recordType;
    }

    public void setPrognoteList(JSONArray prognoteList) {
        this.prognoteList = prognoteList;
    }
}
