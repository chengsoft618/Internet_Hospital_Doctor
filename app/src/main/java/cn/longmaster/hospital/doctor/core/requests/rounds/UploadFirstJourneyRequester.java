package cn.longmaster.hospital.doctor.core.requests.rounds;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.rounds.FirstJourneyInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;

/**
 * 上传首程requester
 * <p>
 * Created by W·H·K on 2018/5/14.
 */

public class UploadFirstJourneyRequester extends BaseApiUrlRequester<FirstJourneyInfo> {
    public String fileName;

    public UploadFirstJourneyRequester(@NonNull OnResultCallback<FirstJourneyInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_UPLOAD_FIRST_JOURNEY;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected FirstJourneyInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data.getJSONObject("data"), FirstJourneyInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("file_name", fileName);
    }
}
