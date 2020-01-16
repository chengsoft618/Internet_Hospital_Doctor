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
 * @date 2019/6/10 14:46
 * @description: 根据省份城市分页获取医院
 */
public class GetHospitalByCityRequester extends BaseClientApiRequester<List<HospitalInfo>> {
    private String province;
    private String city;
    private int pageIndex;
    private int pageSize;

    public GetHospitalByCityRequester(@NonNull OnResultCallback<List<HospitalInfo>> onResultListener) {
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
        return 100167;
    }

    @Override
    protected String getTaskId() {
        return "1001";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("token", 111);
        params.put("province", province);
        params.put("city", city);
        params.put("page_index", pageIndex);
        params.put("page_size", pageSize);
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
