package cn.longmaster.hospital.doctor.core.requests.user;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.core.entity.user.PersonalDataInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseClientApiRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.utils.StringUtils;

/**
 * 拉取个人资料
 * <p>
 * Created by W·H·K on 2018/7/31.
 */
public class PersonalDataRequester extends BaseClientApiRequester<List<PersonalDataInfo>> {
    public int pageindex;
    public int pagesize;

    public PersonalDataRequester(@NonNull OnResultCallback<List<PersonalDataInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_GET_PERSONAL_DATA;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected List<PersonalDataInfo> onDumpData(JSONObject data) throws JSONException {
        Logger.logD(Logger.COMMON, "PersonalDataRequester()->data:" + data);
        String datas = data.getString("data");
        if (StringUtils.isTrimEmpty(datas)) {
            return new ArrayList<>(0);
        }
        return JsonHelper.toList(data.getJSONArray("data"), PersonalDataInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("pageindex", pageindex);
        params.put("pagesize", pagesize);
    }
}
