package cn.longmaster.hospital.doctor.core.requests.user;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.user.ProjectListInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;
import cn.longmaster.utils.StringUtils;

/**
 * Created by W·H·K on 2019/3/18.
 */
public class GetProjectListRequester extends SimpleHttpRequester<List<ProjectListInfo>> {
    public int medicalId;

    public GetProjectListRequester(@NonNull OnResultListener<List<ProjectListInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_GET_SPECIALTY_CONSTRUCTION_PROJECT_LIST;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected List<ProjectListInfo> onDumpData(JSONObject data) throws JSONException {
        String datas = data.getString("data");
        if (StringUtils.isTrimEmpty(datas)) {
            return new ArrayList<>(0);
        }
        return JsonHelper.toList(data.getJSONArray("data"), ProjectListInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        // params.put("medical_id", medicalId);
    }
}
