package cn.longmaster.hospital.doctor.core.requests.account;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.account.AccountListInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;
import cn.longmaster.utils.StringUtils;

/**
 * 获取就诊清单
 * Created by W·H·K on 2019/1/24.
 */
public class AccountListRequester extends SimpleHttpRequester<List<AccountListInfo>> {
    public int account;
    public int state;//101未结算 102已打款 103提现中 104待提现 201欠款未处理 202欠款处理中 203欠款已处理
    public int pageindex;
    public int pageSize;

    public AccountListRequester(@NonNull OnResultListener<List<AccountListInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_GET_ACCOUNT_LIST;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected List<AccountListInfo> onDumpData(JSONObject data) throws JSONException {
        String datas = data.getString("data");
        if (StringUtils.isTrimEmpty(datas)) {
            return new ArrayList<>(0);
        }
        return JsonHelper.toList(data.getJSONArray("data"), AccountListInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("account", account);
        params.put("state", state);
        params.put("pageindex", pageindex);
        params.put("pagesize", pageSize);
    }
}
