package cn.longmaster.hospital.doctor.core.requests.college;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.college.GuideDataInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseClientApiRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.utils.StringUtils;

/**
 * Created by W·H·K on 2018/3/27.
 */

public class GetGuideDataRequester extends BaseClientApiRequester<List<GuideDataInfo>> {
    public int departmentId;
    public String diseaseName;
    public String year;
    public int ownType = 1;
    public int pageIndex;
    public int pageSize;

    public GetGuideDataRequester(@NonNull OnResultCallback<List<GuideDataInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_GET_GUIDE_DATA;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected List<GuideDataInfo> onDumpData(JSONObject data) throws JSONException {
        String datas = data.getString("data");
        if (StringUtils.isTrimEmpty(datas)) {
            return new ArrayList<>(0);
        }
        return JsonHelper.toList(data.getJSONArray("data"), GuideDataInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("department_id", departmentId);
        params.put("disease_name", diseaseName);
        params.put("year", year);
        params.put("own_type", ownType);
        params.put("pageindex", pageIndex);
        params.put("pagesize", pageSize);
    }
}
