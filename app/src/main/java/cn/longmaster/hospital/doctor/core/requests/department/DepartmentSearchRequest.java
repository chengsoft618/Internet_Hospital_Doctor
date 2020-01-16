package cn.longmaster.hospital.doctor.core.requests.department;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.department.DepartmentInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseClientApiRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.utils.StringUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/6/11 17:50
 * @description:
 */
public class DepartmentSearchRequest extends BaseClientApiRequester<List<DepartmentInfo>> {
    private String departmentName;
    private int pageIndex;
    private int pageSize;

    public DepartmentSearchRequest(@NonNull OnResultCallback<List<DepartmentInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected List<DepartmentInfo> onDumpData(JSONObject data) throws JSONException {
        String datas = data.getString("data");
        if (StringUtils.isTrimEmpty(datas)) {
            return new ArrayList<>(0);
        }
        return JsonHelper.toList(data.getJSONArray("data"), DepartmentInfo.class);
    }

    @Override
    protected int getOpType() {
        return 100175;
    }

    @Override
    protected String getTaskId() {
        return "1001";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("token", 111);
        params.put("department_name", departmentName);
        params.put("page_index", pageIndex);
        params.put("page_size", pageSize);
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
