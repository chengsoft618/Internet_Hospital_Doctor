package cn.longmaster.hospital.doctor.core.requests.college;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.college.InteractionInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;
import cn.longmaster.utils.StringUtils;

/**
 * 拉取评论列表
 * Created by Yang² on 2018/3/27.
 */

public class GetInteractionListRequester extends SimpleHttpRequester<List<InteractionInfo>> {
    public int courseId;
    public int appointmentId;
    public int msgId;
    public String insertDt;
    public int pageSize;

    public GetInteractionListRequester(@NonNull OnResultListener<List<InteractionInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_GET_INTERACTION_LIST;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }


    @Override
    protected List<InteractionInfo> onDumpData(JSONObject data) throws JSONException {
        String datas = data.getString("data");
        if (StringUtils.isTrimEmpty(datas)) {
            return new ArrayList<>(0);
        }
        return JsonHelper.toList(data.getJSONArray("data"), InteractionInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("course_id", courseId);
        params.put("appointment_id", appointmentId);
        params.put("msg_id", msgId);
        params.put("insert_dt", insertDt);
        params.put("page_size", pageSize);
    }
}
