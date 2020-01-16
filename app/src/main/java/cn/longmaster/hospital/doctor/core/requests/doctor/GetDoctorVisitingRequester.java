package cn.longmaster.hospital.doctor.core.requests.doctor;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorVisitingItem;
import cn.longmaster.hospital.doctor.core.requests.BaseClientApiRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.utils.StringUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/6/12 18:23
 * @description:
 */
public class GetDoctorVisitingRequester extends BaseClientApiRequester<List<DoctorVisitingItem>> {
    private int doctorId;

    public GetDoctorVisitingRequester(@NonNull OnResultCallback<List<DoctorVisitingItem>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected List<DoctorVisitingItem> onDumpData(JSONObject data) throws JSONException {
        String datas = data.getString("data");
        if (StringUtils.isTrimEmpty(datas)) {
            return new ArrayList<>(0);
        }
        return JsonHelper.toList(data.getJSONArray("data"), DoctorVisitingItem.class);
    }

    @Override
    protected int getOpType() {
        return 100171;
    }

    @Override
    protected String getTaskId() {
        return "1001";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("token", 1111);
        params.put("doctor_id", doctorId);
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }
}
