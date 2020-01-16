package cn.longmaster.hospital.doctor.core.requests.user;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.user.PatientDataItem;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.utils.StringUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/7/16 13:35
 * @description: 按病历分类获取上传人患者材料
 */
public class PatientDataListGetRequest extends BaseApiUrlRequester<List<PatientDataItem>> {
    private String keyWords;
    private int pageIndex;
    private int pageSize;

    public PatientDataListGetRequest(@NonNull OnResultCallback<List<PatientDataItem>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected List<PatientDataItem> onDumpData(JSONObject data) throws JSONException {
        String datas = data.getString("data");
        if (StringUtils.isTrimEmpty(datas)) {
            return new ArrayList<>(0);
        }
        return JsonHelper.toList(data.getJSONArray("data"), PatientDataItem.class);
    }

    @Override
    protected int getOpType() {
        return 100561;
    }

    @Override
    protected String getTaskId() {
        return "123";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("key_words", keyWords);
        params.put("pageindex", pageIndex);
        params.put("pagesize", pageSize);
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
