package cn.longmaster.hospital.doctor.core.requests.consult;


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
 * 上传辅助材料 request
 * Created by Tengshuxiang on 2016-08-15.
 */
public class UploadFileMaterialRequest extends SimpleHttpRequester<String> {
    public String actType = "1";//act_type	操作类型
    public String ext;// ext	文件扩展名
    public String fileName;//file_name	文件名
    public int appointmentId;//appointment_id 预约号

    public UploadFileMaterialRequest(@NonNull OnResultListener<String> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getNginxUploadUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.NGINX_OPTYPE_UPLOAD_MATERIAL;
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
        params.put("act_type", actType);
        params.put("ext", ext);
        params.put("file_name", fileName);
        params.put("appointment_id", appointmentId);
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
