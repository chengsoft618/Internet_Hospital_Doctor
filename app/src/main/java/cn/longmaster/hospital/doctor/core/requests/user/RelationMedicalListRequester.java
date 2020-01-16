package cn.longmaster.hospital.doctor.core.requests.user;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.user.RelationMedicalInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;
import cn.longmaster.utils.StringUtils;

/**
 * 3.3.82	拉取资料关联病历列表
 * <p>
 * Created by W·H·K on 2018/7/31.
 */

public class RelationMedicalListRequester extends SimpleHttpRequester<List<RelationMedicalInfo>> {
    public int materialId;
    public int pageindex;
    public int pagesize;

    public RelationMedicalListRequester(@NonNull OnResultListener<List<RelationMedicalInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_GET_RELATION_MEDICAL_LIST;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected List<RelationMedicalInfo> onDumpData(JSONObject data) throws JSONException {
        String datas = data.getString("data");
        if (StringUtils.isTrimEmpty(datas)) {
            return new ArrayList<>(0);
        }
        return JsonHelper.toList(data.getJSONArray("data"), RelationMedicalInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("material_id", materialId);
        params.put("pageindex", pageindex);
        params.put("pagesize", pagesize);
    }
}
