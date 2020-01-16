package cn.longmaster.hospital.doctor.core.requests.im;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.im.PictureFileNameInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseClientApiRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;

/**
 * 添加IM图片到病历
 * Created by WangHaiKun on 2017/9/8.
 */

public class AddMedicalRecordRequester extends BaseClientApiRequester<PictureFileNameInfo> {
    public String fileName;
    public int appointmentId;

    public AddMedicalRecordRequester(@NonNull OnResultCallback<PictureFileNameInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_CHAT_ADD_MEDICAL_RECORD;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected PictureFileNameInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data.getJSONObject("data"), PictureFileNameInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("file_name", fileName);
        params.put("appointment_id", appointmentId);
    }
}
