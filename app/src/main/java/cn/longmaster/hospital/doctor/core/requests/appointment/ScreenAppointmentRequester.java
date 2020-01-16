package cn.longmaster.hospital.doctor.core.requests.appointment;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointmentItemInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseClientApiRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.utils.StringUtils;

/**
 * 根据条件获取筛选预约信息
 * Created by Yang² on 2016/7/28.
 */
public class ScreenAppointmentRequester extends BaseClientApiRequester<List<AppointmentItemInfo>> {
    private int doctorId;//医生ID
    private int isSale;//是否销售代码:1 是，2 不是
    private int promoter;//发起人类型 1：我收到的;2：我发起的;0：我收到和我发起的
    private int scheduingType;//排班服务类型 1：排班 2：影像服务 0：不使用该条件
    private int serviceType;//就诊类型 101001：远程会诊;101002：远程咨询;102001：影像会诊;0：不使用该条件
    private String statNum = "0";//我发起的预约状态 1：等待视频 2：等待医嘱;3：报告整理 4：就诊完;5：支付超时 6：就诊关闭;7：未审核 8：已审核;0：不使用该条件
    private String receiveStatNum = "0";//我收到的预约状态 1：等待视频 2：等待医嘱 3：报告整理 4：就诊完成 5：支付超时 6：就诊关闭 7：未审核 8：已审核 0：不使用该条件
    private int recure;//是否复诊 1：是 2：否;0：不使用该条件
    private String keyWords = "";//搜索关键字	不使用该条件请传空
    private int symbol;//分页参数
    private int pageSize = 10;//分页尺寸
    private int sameDep = 1;//分页尺寸

    public ScreenAppointmentRequester(@NonNull OnResultCallback<List<AppointmentItemInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_SCREEN_APPOINTMENT;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected List<AppointmentItemInfo> onDumpData(JSONObject data) throws JSONException {
        String datas = data.getString("data");
        if (StringUtils.isTrimEmpty(datas)) {
            return new ArrayList<>(0);
        }
        return JsonHelper.toList(data.getJSONArray("data"), AppointmentItemInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("doctor_id", doctorId);
        params.put("is_sale", isSale);
        params.put("promoter", promoter);
        params.put("scheduing_type", scheduingType);
        params.put("service_type", serviceType);
        params.put("stat_num", statNum);
        params.put("receive_stat_num", receiveStatNum);
        params.put("recure", recure);
        params.put("key_words", keyWords);
        params.put("symbol", symbol);
        params.put("page_size", pageSize);
        params.put("same_dep", sameDep);
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public void setIsSale(int isSale) {
        this.isSale = isSale;
    }

    public void setPromoter(int promoter) {
        this.promoter = promoter;
    }

    public void setScheduingType(int scheduingType) {
        this.scheduingType = scheduingType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public void setStatNum(String statNum) {
        this.statNum = statNum;
    }

    public void setReceiveStatNum(String receiveStatNum) {
        this.receiveStatNum = receiveStatNum;
    }

    public void setRecure(int recure) {
        this.recure = recure;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public void setSymbol(int symbol) {
        this.symbol = symbol;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setSameDep(int sameDep) {
        this.sameDep = sameDep;
    }
}
