package cn.longmaster.hospital.doctor.core.requests.consultant;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.consultant.VisitDoctorList;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 销售代表根据自己的账号获取自己所管理的医生列表
 * Created by Yang² on 2018/1/3.
 */

public class VisitDoctorListRequester extends SimpleHttpRequester<VisitDoctorList> {
    public int pageIndex;
    public int pageSize;

    public VisitDoctorListRequester(@NonNull OnResultListener<VisitDoctorList> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_VISIT_DOCTOR_LIST;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected VisitDoctorList onDumpData(JSONObject data) throws JSONException {
        JSONObject object = data.getJSONObject("is_finish");
        String is_finish = object.getString("is_finish");
        return JsonHelper.toObject(data.getJSONObject("data"), VisitDoctorList.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("pageindex", pageIndex);
        params.put("pagesize", pageSize);
    }
}
