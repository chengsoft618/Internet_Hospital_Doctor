package cn.longmaster.hospital.doctor.core.requests.department;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.department.DepartmentItemInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseClientApiRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.utils.StringUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/6/10 15:49
 * @description:
 */
public class DepartmentListNewRequester extends BaseClientApiRequester<List<DepartmentItemInfo>> {
    public DepartmentListNewRequester(@NonNull OnResultCallback<List<DepartmentItemInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected List<DepartmentItemInfo> onDumpData(JSONObject data) throws JSONException {
        String datas = data.getString("data");
        if (StringUtils.isTrimEmpty(datas)) {
            return new ArrayList<>(0);
        }
        return JsonHelper.toList(data.getJSONArray("data"), DepartmentItemInfo.class);
    }

    @Override
    protected int getOpType() {
        return 100168;
    }

    @Override
    protected String getTaskId() {
        return "1001";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("token", 111);
    }
}
