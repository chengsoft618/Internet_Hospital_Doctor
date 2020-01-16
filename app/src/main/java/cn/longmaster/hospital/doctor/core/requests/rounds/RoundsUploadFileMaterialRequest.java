package cn.longmaster.hospital.doctor.core.requests.rounds;


import android.support.annotation.NonNull;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 查房上传辅助材料 request
 * Created by Tengshuxiang on 2016-08-15.
 */
public class RoundsUploadFileMaterialRequest extends SimpleHttpRequester<String> {
    public String fileName;//file_name	文件名
    public int medicalId;//medicalId 病历号

    public RoundsUploadFileMaterialRequest(@NonNull OnResultListener<String> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_UPLOAD_MEDICAL_RECORD_MATERIAL;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }


    @Override
    protected String onDumpData(JSONObject data) throws JSONException {
        return "";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("medical_id", medicalId);
        params.put("material_pic", fileName);
    }

    public String getCompleteUrl() {
        String url = getUrl();
        Map<String, Object> params = new HashMap<>();
        initBaseParams(params);
        putParams(params);

        JSONObject jsonObject = new JSONObject(params);
        RequestParams requestParams = new RequestParams();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            requestParams.put("json", jsonObject);
        }
        return url + "?" + requestParams.toString();
    }

}
