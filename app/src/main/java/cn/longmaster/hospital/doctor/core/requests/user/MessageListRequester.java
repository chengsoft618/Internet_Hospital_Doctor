package cn.longmaster.hospital.doctor.core.requests.user;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.user.MessageListInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 根据用户拉取通知信息列表
 * Created by Yang² on 2016/11/2.
 */

public class MessageListRequester extends SimpleHttpRequester<List<MessageListInfo>> {
    public MessageListRequester(@NonNull OnResultListener<List<MessageListInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_MESSAGE_LIST;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected List<MessageListInfo> onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toList(data.getJSONArray("system_notice"), MessageListInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {

    }
}
