package cn.longmaster.hospital.doctor.core.requests.consultant;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.consultant.ConsultantSchedulingInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 获取顾问排班列表requester
 * <p>
 * Created by W·H·K on 2017/12/28.
 */

public class ConsultantSchedulingRequester extends SimpleHttpRequester<ConsultantSchedulingInfo> {
    public String beginDt;//预约开始时间
    public String endDt;//预约结束时间
    public int doctorId;//首诊ID
    public String searchType;//搜索关键字类型1-预约编号；2-患者姓名；3-首诊姓名
    public String searchWord;//搜索关键字内容
    public int statNum;//预约状态类型0-全部；1-分诊中；2-进行；3-完成；
    public int pageindex;//当前页码
    public int pagesize;//每页显示的条数

    public ConsultantSchedulingRequester(@NonNull OnResultListener<ConsultantSchedulingInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_GET_CONSULTAN_LIST;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected ConsultantSchedulingInfo onDumpData(JSONObject data) throws JSONException {
        Logger.logI(Logger.COMMON, "ConsultantSchedulingRequester-->data:" + data);
        return JsonHelper.toObject(data.getJSONObject("data"), ConsultantSchedulingInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("begin_dt", beginDt);
        params.put("end_dt", endDt);
        params.put("doctor_id", doctorId);
        params.put("search_type", searchType);
        params.put("search_word", searchWord);
        params.put("stat_num", statNum);
        params.put("pageindex", pageindex);
        params.put("pagesize", pagesize);
    }
}
