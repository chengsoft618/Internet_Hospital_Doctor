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
 * @date 2019/12/28 11:57
 * @description: 项目患者列表查询
 */
public class SearchDCProjectPatientListRequester extends BaseApiUrlRequester<List<DCDutyPatientItemInfo>> {
    //项目Id
    private int projectId;
    //0：全部患者 1：我的患者
    private int patientType;
    //0：按患者姓名 1：按社区医院
    private int searchType;
    private String keyWords;

    public SearchDCProjectPatientListRequester(@NonNull OnResultCallback<List<DCDutyPatientItemInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected List<DCDutyPatientItemInfo> onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toList(data.optJSONArray("data"), DCDutyPatientItemInfo.class);
    }

    @Override
    protected int getOpType() {
        return 100597;
    }

    @Override
    protected String getTaskId() {
        return "100597";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("item_id", projectId);
        params.put("patient_type", patientType);
        params.put("search_type", searchType);
        params.put("search_key", keyWords);
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public void setPatientType(int patientType) {
        this.patientType = patientType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }
}
