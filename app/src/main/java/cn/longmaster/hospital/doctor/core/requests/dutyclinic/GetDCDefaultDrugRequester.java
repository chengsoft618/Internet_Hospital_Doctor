package cn.longmaster.hospital.doctor.core.requests.dutyclinic;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDrugInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * 值班门诊-获取默认处方
 * Created by yangyong on 2019-12-04.
 */
public class GetDCDefaultDrugRequester extends BaseApiUrlRequester<List<DCDrugInfo>> {
    public GetDCDefaultDrugRequester(@NonNull OnResultCallback<List<DCDrugInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected List<DCDrugInfo> onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toList(data.optJSONArray("data"), DCDrugInfo.class);
    }

    @Override
    protected int getOpType() {
        return 100590;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {

    }
}
