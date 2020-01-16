package cn.longmaster.hospital.doctor.core.requests.dutyclinic;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCProjectInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * 值班门诊-拉取项目列表接口
 * Created by yangyong on 2019-11-30.
 */
public class GetDCProjectListRequester extends BaseApiUrlRequester<List<DCProjectInfo>> {
    //0:拉取值班门诊项目 1：拉取慢病管理项目
    private int type;

    public GetDCProjectListRequester(@NonNull OnResultCallback<List<DCProjectInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected List<DCProjectInfo> onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toList(data.optJSONArray("data"), DCProjectInfo.class);
    }

    @Override
    protected int getOpType() {
        return 100577;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("type", type);
    }

    public void setType(int type) {
        this.type = type;
    }
}
