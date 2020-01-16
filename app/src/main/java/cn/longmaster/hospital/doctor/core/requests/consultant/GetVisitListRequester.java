package cn.longmaster.hospital.doctor.core.requests.consultant;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.consultant.VisitInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 拉取预约列表
 * Created by W·H·K on 2017/12/28.
 */

public class GetVisitListRequester extends SimpleHttpRequester<VisitInfo> {
    public String beginDt = "";//预约开始时间
    public String endDt = "";//预约结束时间
    public int doctorId = 0;//首诊ID
    public String searchType = "";//搜索关键字类型1-预约编号；2-患者姓名；3-首诊姓名
    public String searchWord = "";//搜索关键字内容
    public int statNum;//预约状态类型0-全部；1-分诊中；2-进行；3-完成；
    public int scheduingId;//排班ID
    public int pageindex;//当前页码
    public int pagesize;//每页显示的条数

    public GetVisitListRequester(@NonNull OnResultListener<VisitInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_GET_VISIT_LIST;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected VisitInfo onDumpData(JSONObject data) throws JSONException {
        Logger.logI(Logger.COMMON, "GetVisitListRequester-->data:" + data);
        return JsonHelper.toObject(data.getJSONObject("data"), VisitInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("begin_dt", beginDt);
        params.put("end_dt", endDt);
        params.put("doctor_id", doctorId);
        params.put("search_type", searchType);
        params.put("search_word", searchWord);
        params.put("stat_num", statNum);
        params.put("scheduing_id", scheduingId);
        params.put("pageindex", pageindex);
        params.put("pagesize", pagesize);
    }
}
