package cn.longmaster.hospital.doctor.core.requests.user;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.requests.BaseClientApiRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * @author ABiao_Abiao
 * @date 2019/11/15 15:11
 * @description:
 */
public class PersonDataModRequest extends BaseClientApiRequester<Void> {
    //材料id
    private int materialId;
    //资料名称update_type=2时必填
    private String materialName;
    //修改类型  必传 1修改可看，2修改标题
    private int updateType;
    //可看状态update_type=1时必填
    private int selfVisible;

    public PersonDataModRequest(@NonNull OnResultCallback<Void> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected Void onDumpData(JSONObject data) throws JSONException {
        return null;
    }

    @Override
    protected int getOpType() {
        return 100296;
    }

    @Override
    protected String getTaskId() {
        return "100296";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("material_id", materialId);
        params.put("update_type", updateType);
        params.put("self_visible", selfVisible);
        params.put("material_name", materialName);
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public void setUpdateType(int updateType) {
        this.updateType = updateType;
    }

    public void setSelfVisible(int selfVisible) {
        this.selfVisible = selfVisible;
    }
}
