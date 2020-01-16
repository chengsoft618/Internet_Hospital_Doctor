package cn.longmaster.hospital.doctor.core.requests.im;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.im.MemberListInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseClientApiRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.utils.StringUtils;

/**
 * 获取im成员列表
 * Created by WangHaiKun on 2017/8/29.
 */

public class GetChatMemberListRequester extends BaseClientApiRequester<List<MemberListInfo>> {
    public int appointmentId;

    public GetChatMemberListRequester(@NonNull OnResultCallback<List<MemberListInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_CHAT_GET_MEMBER_LIST;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected List<MemberListInfo> onDumpData(JSONObject data) throws JSONException {
        String datas = data.getString("data");
        if (StringUtils.isTrimEmpty(datas)) {
            return new ArrayList<>(0);
        }
        return JsonHelper.toList(data.getJSONArray("data"), MemberListInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("appointment_id", appointmentId);
    }
}
