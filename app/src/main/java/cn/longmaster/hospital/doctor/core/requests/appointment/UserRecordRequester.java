package cn.longmaster.hospital.doctor.core.requests.appointment;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.consult.record.AppointmentItemForSelectInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseClientApiRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.utils.StringUtils;

/**
 * 关联病历请求
 * Created by JinKe on 2016-08-30.
 */
public class UserRecordRequester extends BaseClientApiRequester<List<AppointmentItemForSelectInfo>> {
    public int userNumId;//ID
    public int userType;//用户身份类型
    public int symbol;//分页参数
    public int pageSize;//分页尺寸

    public UserRecordRequester(@NonNull OnResultCallback<List<AppointmentItemForSelectInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_RELATE_RECORDER;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected List<AppointmentItemForSelectInfo> onDumpData(JSONObject data) throws JSONException {
        if (StringUtils.isEmpty(data.getString("data"))) {
            return new ArrayList<>();
        }
        return JsonHelper.toList(data.getJSONArray("data"), AppointmentItemForSelectInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("user_num_id", userNumId);
        params.put("user_type", userType);
        params.put("symbol", symbol);
        params.put("page_size", pageSize);
    }
}
