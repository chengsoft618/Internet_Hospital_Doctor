package cn.longmaster.hospital.doctor.core.requests.im;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.im.ChatGroupList;
import cn.longmaster.hospital.doctor.core.requests.BaseClientApiRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;


/**
 * 分页获取相应的群组列表
 * Created by Yang² on 2017/8/28.
 */

public class GetChatGroupListRequester extends BaseClientApiRequester<ChatGroupList> {
    public String status;//群组状态	必传。如："0,1"
    public int mode;//获取方式	必传1：根据ID获取，2：获取列表
    public int groupMarkId;//群组标识ID	mode为1时必传。mode为2时不传则拉最新
    public String groupDt;//群组标识时间	mode为2时有效，获取时间之前的数据。
    public int pageSize;//分页大小	mode为2时有效、必传。
    public int orderType;//排序方式	必传。0：更新时间降序，1：创建时间降序
    public String keyWords = "";

    public GetChatGroupListRequester(@NonNull OnResultCallback<ChatGroupList> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_GET_GROUP_LIST;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected ChatGroupList onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data.getJSONObject("data"), ChatGroupList.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
//        params.put("user_id", 1003675);
//        params.put("user_id", 1003325);
        params.put("status", status);
        params.put("mode", mode);
        params.put("group_mark_id", groupMarkId);
        params.put("group_dt", groupDt);
        params.put("page_size", pageSize);
        params.put("order_type", orderType);
        params.put("key_words", keyWords);
    }
}
