package cn.longmaster.hospital.doctor.core.requests.dutyclinic;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyPatientItemInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * @author ABiao_Abiao
 * @date 2019/12/28 11:23
 * @description: 根据项目id及患者类型拉取患者列表
 */
public class GetDCProjectPatientListRequester extends BaseApiUrlRequester<List<DCDutyPatientItemInfo>> {
    // 患者类型 必传 0：全部患者 1：我的患者
    private int type;
    //项目ID
    private int projectId;
    private int pageIndex;
    private int pageSize;

    public GetDCProjectPatientListRequester(@NonNull OnResultCallback<List<DCDutyPatientItemInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected List<DCDutyPatientItemInfo> onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toList(data.optJSONArray("data"), DCDutyPatientItemInfo.class);
    }

    @Override
    protected int getOpType() {
        return 100592;
    }

    @Override
    protected String getTaskId() {
        return "100592";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("patient_type", type);
        params.put("page_index", pageIndex);
        params.put("page_size", pageSize);
        params.put("item_id", projectId);
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
