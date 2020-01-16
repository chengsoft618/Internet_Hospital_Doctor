package cn.longmaster.hospital.doctor.core.requests.doctor;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorItemInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseClientApiRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.utils.StringUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/6/12 20:19
 * @description: 获取常用医生接口
 */
public class GetDoctorListRegularlyRequester extends BaseClientApiRequester<List<DoctorItemInfo>> {
    //1開始0或不传全拉 pageIndex++;
    private int pageIndex;
    private int pageSize;
    //基层医生科室 必传
    private int departmentId;

    public GetDoctorListRegularlyRequester(@NonNull OnResultCallback<List<DoctorItemInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected List<DoctorItemInfo> onDumpData(JSONObject data) throws JSONException {
        String datas = data.getString("data");
        if (StringUtils.isTrimEmpty(datas)) {
            return new ArrayList<>(0);
        }
        return JsonHelper.toList(data.getJSONArray("data"), DoctorItemInfo.class);
    }

    @Override
    protected int getOpType() {
        return 100176;
    }

    @Override
    protected String getTaskId() {
        return "1001";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("token", 111);
        params.put("department_id", 0);
        params.put("page_index", pageIndex);
        params.put("page_size", pageSize);
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

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
}
