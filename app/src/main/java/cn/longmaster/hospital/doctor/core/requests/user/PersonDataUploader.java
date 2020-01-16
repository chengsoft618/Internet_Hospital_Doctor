package cn.longmaster.hospital.doctor.core.requests.user;

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
 * Created by W·H·K on 2018/5/16.
 */

public class PersonDataUploader extends SimpleHttpRequester<String> {
    public String filePath;
    public int actType = 1;
    public String ext;

    public PersonDataUploader(@NonNull OnResultListener<String> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getNginxUploadUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.NGINX_OPTYPE_UPLOAD_PERSON_DATA;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected String onDumpData(JSONObject data) throws JSONException {
        return null;
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("act_type", actType);
        params.put("ext", ext);
        params.put("file_name", filePath.substring(filePath.lastIndexOf("/") + 1));
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
