package cn.longmaster.hospital.doctor.core.requests.account;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.account.AccountFlowInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseClientApiRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.utils.StringUtils;

/**
 * 获取账户流水明细
 * Created by W·H·K on 2019/1/24.
 */
public class AccountFlowRequester extends BaseClientApiRequester<List<AccountFlowInfo>> {
    public int account;
    public int pageindex;
    public int pageSize;

    public AccountFlowRequester(@NonNull OnResultCallback<List<AccountFlowInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_GET_ACCOUNT_FLOW;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected List<AccountFlowInfo> onDumpData(JSONObject data) throws JSONException {
        String datas = data.getString("data");
        if (StringUtils.isTrimEmpty(datas)) {
            return new ArrayList<>(0);
        }
        return JsonHelper.toList(data.getJSONArray("data"), AccountFlowInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("account", account);
        params.put("pageindex", pageindex);
        params.put("pagesize", pageSize);
    }
}
