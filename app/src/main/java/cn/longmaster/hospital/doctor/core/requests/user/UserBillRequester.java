package cn.longmaster.hospital.doctor.core.requests.user;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.user.UserBillInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;
import cn.longmaster.utils.StringUtils;

/**
 * 根据用户ID拉取用户账单明细 requester
 * Created by JinKe on 2016-07-27.
 */
public class UserBillRequester extends SimpleHttpRequester<List<UserBillInfo>> {
    public int reqType;

    public UserBillRequester(@NonNull OnResultListener<List<UserBillInfo>> onResultListener) {
        super(onResultListener);
    }


    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_USER_BILL_DETAIL;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected List<UserBillInfo> onDumpData(JSONObject data) throws JSONException {
        String datas = data.getString("data");
        if (StringUtils.isTrimEmpty(datas)) {
            return new ArrayList<>(0);
        }
        return JsonHelper.toList(data.getJSONArray("data"), UserBillInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("req_type", reqType);
    }
}
