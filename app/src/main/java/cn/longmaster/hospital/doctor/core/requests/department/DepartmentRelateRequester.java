package cn.longmaster.hospital.doctor.core.requests.department;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.department.DepartmentRelateInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;
import cn.longmaster.utils.StringUtils;

/**
 * 根据科室ID拉取科室关联信息 requester
 * Created by JinKe on 2016-07-27.
 */
public class DepartmentRelateRequester extends SimpleHttpRequester<List<DepartmentRelateInfo>> {
    public int departmentId;//科室ID

    public DepartmentRelateRequester(@NonNull OnResultListener<List<DepartmentRelateInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_DEPARTMENT_RELATE;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected List<DepartmentRelateInfo> onDumpData(JSONObject data) throws JSONException {
        String datas = data.getString("data");
        if (StringUtils.isTrimEmpty(datas)) {
            return new ArrayList<>(0);
        }
        return JsonHelper.toList(data.getJSONArray("data"), DepartmentRelateInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("department_id", departmentId);
    }
}
