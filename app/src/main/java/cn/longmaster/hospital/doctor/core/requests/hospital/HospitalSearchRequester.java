package cn.longmaster.hospital.doctor.core.requests.hospital;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.doctor.HospitalInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseClientApiRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.utils.StringUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/6/11 19:53
 * @description:
 */
public class HospitalSearchRequester extends BaseClientApiRequester<List<HospitalInfo>> {
    private String hospitalName;
    private int pageIndex;
    private int pageSize;

    public HospitalSearchRequester(@NonNull OnResultCallback<List<HospitalInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected List<HospitalInfo> onDumpData(JSONObject data) throws JSONException {
        String datas = data.getString("data");
        if (StringUtils.isTrimEmpty(datas)) {
            return new ArrayList<>(0);
        }
        return JsonHelper.toList(data.getJSONArray("data"), HospitalInfo.class);
    }

    @Override
    protected int getOpType() {
        return 100174;
    }

    @Override
    protected String getTaskId() {
        return "133";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("hospital_name", hospitalName);
        params.put("page_index", pageIndex);
        params.put("page_size", pageSize);
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
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
