package cn.longmaster.hospital.doctor.core.requests.consult.remote;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.doctor.ServiceAuthorityInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;
import cn.longmaster.utils.StringUtils;

/**
 * 根据医生ID获取服务权限 requester
 * Created by JinKe on 2016-07-27.
 */
public class ServiceAuthorityRequester extends SimpleHttpRequester<List<ServiceAuthorityInfo>> {
    private int doctorId;//医生ID

    public ServiceAuthorityRequester(@NonNull OnResultListener<List<ServiceAuthorityInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_SERVICE_AUTHORITY;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected List<ServiceAuthorityInfo> onDumpData(JSONObject data) throws JSONException {
        String datas = data.getString("data");
        if (StringUtils.isTrimEmpty(datas)) {
            return new ArrayList<>(0);
        }
        return JsonHelper.toList(data.getJSONArray("data"), ServiceAuthorityInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("doctor_id", doctorId);
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }
}
