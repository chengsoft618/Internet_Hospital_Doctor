package cn.longmaster.hospital.doctor.core.requests.hospital;


import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.user.DepartmentItem;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.utils.StringUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/7/11 18:12
 * @description: 获取医院的科室列表
 */
public class GetDepartmentByHospitalRequest extends BaseApiUrlRequester<List<DepartmentItem>> {
    private int hospitalId;
    //0一级科室；大于0为二级科室；-1为全部科室不分级
    private String departmentId;

    public GetDepartmentByHospitalRequest(@NonNull OnResultCallback<List<DepartmentItem>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected List<DepartmentItem> onDumpData(JSONObject data) throws JSONException {
        String datas = data.getString("data");
        if (StringUtils.isTrimEmpty(datas)) {
            return new ArrayList<>(0);
        }
        return JsonHelper.toList(data.getJSONArray("data"), DepartmentItem.class);
    }

    @Override
    protected int getOpType() {
        return 100568;
    }

    @Override
    protected String getTaskId() {
        return "123";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("hospital_id", hospitalId);
        if (StringUtils.isEmpty(departmentId)) {
            departmentId = "0";
        }
        params.put("hosdep_id", departmentId);
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
}
