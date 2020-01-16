package cn.longmaster.hospital.doctor.core.requests.dutyclinic;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyVisitPlantItem;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * @author ABiao_Abiao
 * @date 2019/12/30 14:48
 * @description: 根据项目id拉取项目随访计划模板列表
 */
public class GetDCDutyVisitPlantListRequester extends BaseApiUrlRequester<List<DCDutyVisitPlantItem>> {
    private int projectId;

    public GetDCDutyVisitPlantListRequester(@NonNull OnResultCallback<List<DCDutyVisitPlantItem>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected List<DCDutyVisitPlantItem> onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toList(data.getJSONArray("data"), DCDutyVisitPlantItem.class);
    }

    @Override
    protected int getOpType() {
        return 100598;
    }

    @Override
    protected String getTaskId() {
        return "100598";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("item_id", projectId);
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
