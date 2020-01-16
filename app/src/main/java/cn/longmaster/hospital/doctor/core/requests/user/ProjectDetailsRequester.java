package cn.longmaster.hospital.doctor.core.requests.user;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.user.ProjectDetailsInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * Created by W·H·K on 2019/3/18.
 */
public class ProjectDetailsRequester extends SimpleHttpRequester<ProjectDetailsInfo> {
    //建设项目ID
    public int itemId;

    public ProjectDetailsRequester(@NonNull OnResultListener<ProjectDetailsInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_GET_PROJECT_DETAILS;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected ProjectDetailsInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data.getJSONObject("data"), ProjectDetailsInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("item_id", itemId);
    }
}
