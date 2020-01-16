package cn.longmaster.hospital.doctor.core.requests.user;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.user.PlatformListItem;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.utils.StringUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/7/11 17:57
 * @description: 添加平台服务费
 */
public class PlatformCostListGetRequest extends BaseApiUrlRequester<List<PlatformListItem>> {
    private int pageIndex;
    private int pageSize;
    private String hospital;

    public PlatformCostListGetRequest(@NonNull OnResultCallback<List<PlatformListItem>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected List<PlatformListItem> onDumpData(JSONObject data) throws JSONException {
        String datas = data.getString("data");
        if (StringUtils.isTrimEmpty(datas)) {
            return new ArrayList<>(0);
        }
        return JsonHelper.toList(data.getJSONArray("data"), PlatformListItem.class);
    }

    @Override
    protected int getOpType() {
        return 100563;
    }

    @Override
    protected String getTaskId() {
        return "123";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("pageindex", pageIndex);
        params.put("pagesize", pageSize);
        params.put("key_words", hospital);
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }
}
