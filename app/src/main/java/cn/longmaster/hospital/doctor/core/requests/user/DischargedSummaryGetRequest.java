package cn.longmaster.hospital.doctor.core.requests.user;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.consult.DischargedSummaryInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.utils.StringUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/9/20 14:24
 * @description: 查看当前病历下的住院小结
 */
public class DischargedSummaryGetRequest extends BaseApiUrlRequester<List<DischargedSummaryInfo>> {
    private String medicalId;

    public DischargedSummaryGetRequest(@NonNull OnResultCallback<List<DischargedSummaryInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected List<DischargedSummaryInfo> onDumpData(JSONObject data) throws JSONException {
        String datas = data.getString("data");
        if (StringUtils.isTrimEmpty(datas)) {
            return new ArrayList<>(0);
        }
        return JsonHelper.toList(data.getJSONArray("data"), DischargedSummaryInfo.class);
    }

    @Override
    protected int getOpType() {
        return 100575;
    }

    @Override
    protected String getTaskId() {
        return "123";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("medical_id", medicalId);
    }

    public void setMedicalId(String medicalId) {
        this.medicalId = medicalId;
    }
}
