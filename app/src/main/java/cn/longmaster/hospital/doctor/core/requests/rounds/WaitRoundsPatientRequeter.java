package cn.longmaster.hospital.doctor.core.requests.rounds;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.rounds.WaitRoundsPatientInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.utils.StringUtils;

/**
 * Created by W·H·K on 2018/9/13.
 * 待查房患者request
 * Mod by biao on 2019/8/5
 */

public class WaitRoundsPatientRequeter extends BaseApiUrlRequester<List<WaitRoundsPatientInfo>> {
    public String keyWords;
    public int pageindex;
    public int pagesize;
    //类型：0为未关联就诊患者；1为全部患者
    public int dataType;

    public WaitRoundsPatientRequeter(@NonNull OnResultCallback<List<WaitRoundsPatientInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_GET_WAIT_ROUNDS_PATIENT;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected List<WaitRoundsPatientInfo> onDumpData(JSONObject data) throws JSONException {
        String datas = data.getString("data");
        if (StringUtils.isTrimEmpty(datas)) {
            return new ArrayList<>(0);
        }
        return JsonHelper.toList(data.getJSONArray("data"), WaitRoundsPatientInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("key_words", keyWords);
        params.put("pageindex", pageindex);
        params.put("pagesize", pagesize);
        params.put("data_type", dataType);
    }
}
