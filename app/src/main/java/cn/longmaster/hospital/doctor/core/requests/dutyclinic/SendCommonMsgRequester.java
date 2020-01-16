package cn.longmaster.hospital.doctor.core.requests.dutyclinic;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * @author ABiao_Abiao
 * @date 2020/1/6 10:39
 * @description: 通用短信发送接口
 */
public class SendCommonMsgRequester extends BaseApiUrlRequester<Void> {
    //接收人用户ID
    private int receiverId;
    //短信内容
    private String content;

    public SendCommonMsgRequester(@NonNull OnResultCallback<Void> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected Void onDumpData(JSONObject data) throws JSONException {
        return null;
    }

    @Override
    protected int getOpType() {
        return 100602;
    }

    @Override
    protected String getTaskId() {
        return "100602";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("receiver_id", receiverId);
        params.put("content", content);
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
