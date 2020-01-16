package cn.longmaster.hospital.doctor.core.requests.user;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.doctor.HospitalInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.utils.StringUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/9/20 15:35
 * @description: 按病历分类获取上传人患者材料
 */
public class PatientHospitalListGetRequest extends BaseApiUrlRequester<List<HospitalInfo>> {
    private String patientName;
    private String hospitalizaId;

    public PatientHospitalListGetRequest(@NonNull OnResultCallback<List<HospitalInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected List<HospitalInfo> onDumpData(JSONObject data) throws JSONException {
        String datas = data.getString("data");
        if (StringUtils.isTrimEmpty(datas)) {
            return new ArrayList<>(0);
        }
        return JsonHelper.toList(data.getJSONArray("data"), HospitalInfo.class);
    }

    @Override
    protected int getOpType() {
        return 100574;
    }

    @Override
    protected String getTaskId() {
        return "123";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("patient_name", patientName);
        params.put("hospitaliza_id", hospitalizaId);
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public void setHospitalizaId(String hospitalizaId) {
        this.hospitalizaId = hospitalizaId;
    }
}
