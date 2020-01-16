package cn.longmaster.hospital.doctor.core.requests.dutyclinic;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCProjectDoctorListInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * 值班门诊-拉取项目列表接口
 * Created by yangyong on 2019-11-30.
 */
public class GetDCProjectDoctorListRequester extends BaseApiUrlRequester<List<DCProjectDoctorListInfo>> {
    private int itemId;
    //0:拉取正在值班医生列表 1：拉取项目所有医生列表
    private int type;

    public GetDCProjectDoctorListRequester(@NonNull OnResultCallback<List<DCProjectDoctorListInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected List<DCProjectDoctorListInfo> onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toList(data.optJSONArray("data"), DCProjectDoctorListInfo.class);
    }

    @Override
    protected int getOpType() {
        return 100579;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("item_id", itemId);
        params.put("type", type);
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setType(int type) {
        this.type = type;
    }
}
