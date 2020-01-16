package cn.longmaster.hospital.doctor.core.requests.im;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.im.ChatMsgList;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 根据不同条件获取相应的历史聊天记录
 * Created by Yang² on 2017/8/30.
 */

public class GetChatHistoryList extends SimpleHttpRequester<ChatMsgList> {
    public int groupId;//群组ID 必传。
    public int msgId;//消息ID 必传。
    public String msgDt = "";//消息时间 必传。
    public int pageSize;//消息条数 必传。

    public GetChatHistoryList(@NonNull OnResultListener<ChatMsgList> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_GET_CHAT_HISTORY_LIST;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected ChatMsgList onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data.getJSONObject("data"), ChatMsgList.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("group_id", groupId);
        params.put("msg_id", msgId);
        params.put("msg_dt", msgDt);
        params.put("page_size", pageSize);
    }
}
