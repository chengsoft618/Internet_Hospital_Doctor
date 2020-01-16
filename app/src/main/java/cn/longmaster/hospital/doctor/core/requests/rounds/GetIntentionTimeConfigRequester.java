package cn.longmaster.hospital.doctor.core.requests.rounds;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.rounds.IntentionTimeInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;

/**
 * 查房意向时间配置列表拉取。
 * Created by W·H·K on 2018/7/16.
 */

public class GetIntentionTimeConfigRequester extends BaseApiUrlRequester<List<IntentionTimeInfo>> {

    public GetIntentionTimeConfigRequester(@NonNull OnResultCallback<List<IntentionTimeInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_INTENTION_TIME_CONFIGURE;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected List<IntentionTimeInfo> onDumpData(JSONObject data) throws JSONException {
        List<IntentionTimeInfo> list = new ArrayList<>();
        JSONArray jsonArray = data.getJSONArray("data");
        if (jsonArray != null && jsonArray.length() != 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                IntentionTimeInfo info = new IntentionTimeInfo();
                info.setDuration(jsonObject.optDouble("duration", 0));
                info.setUnit(jsonObject.optString("unit"));
                list.add(info);
            }
        }
        return list;
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
    }
}
