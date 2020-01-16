package cn.longmaster.hospital.doctor.core.requests.doctor;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorItemInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseClientApiRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.StringUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/6/10 15:34
 * @description: 拉取医生列表
 */
public class GetDoctorListNewRequester extends BaseClientApiRequester<List<DoctorItemInfo>> {
    //真名
    private String realName;
    //查询疾病
    private String illness;
    private String level;
    private String department;
    private String hospital;
    private int receptionType;
    private int minPrice;
    private int maxPrice;
    private String receptionDt;
    //排名类型
    private int sort;
    private int pageIndex;
    private int pageSize;
    private int hospitalSort;

    public GetDoctorListNewRequester(@NonNull OnResultCallback<List<DoctorItemInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected List<DoctorItemInfo> onDumpData(JSONObject data) throws JSONException {
        String datas = data.getString("data");
        if (StringUtils.isTrimEmpty(datas)) {
            return new ArrayList<>(0);
        }
        return JsonHelper.toList(data.getJSONArray("data"), DoctorItemInfo.class);
    }

    @Override
    protected int getOpType() {
        return 100169;
    }

    @Override
    protected String getTaskId() {
        return "1001";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("real_name", realName);
        params.put("illness", illness);
        params.put("level", level);
        params.put("department", department);
        params.put("hospital", hospital);
        params.put("reception_type", receptionType);
        params.put("min_price", minPrice);
        params.put("max_price", maxPrice);
        params.put("reception_dt", receptionDt);
        params.put("sort", sort);
        params.put("page_index", pageIndex);
        params.put("page_size", pageSize);
        params.put("hospital_sort", hospitalSort);
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIllness() {
        return illness;
    }

    public void setIllness(String illness) {
        this.illness = illness;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(List<String> department) {
        this.department = createDepartmentNames(department);
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(List<String> hospital) {
        this.hospital = createHospitalName(hospital);
    }

    public int getReceptionType() {
        return receptionType;
    }

    public void setReceptionType(int receptionType) {
        this.receptionType = receptionType;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getReceptionDt() {
        return receptionDt;
    }

    public void setReceptionDt(List<String> receptionDt) {
        this.receptionDt = createTimeForFilter(receptionDt);
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getHospitalSort() {
        return hospitalSort;
    }

    public void setHospitalSort(int hospitalSort) {
        this.hospitalSort = hospitalSort;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    private String createDepartmentNames(List<String> departments) {
        return StringUtils.strList2Str(departments);
    }

    private String createHospitalName(List<String> hospitals) {
        return StringUtils.strList2Str(hospitals);
    }

    private String createTimeForFilter(List<String> receptionTime) {
        if (LibCollections.isEmpty(receptionTime)) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String time : receptionTime) {
            if (time.contains("一")) {
                stringBuilder.append(1);
            } else if (time.contains("二")) {
                stringBuilder.append(2);
            } else if (time.contains("三")) {
                stringBuilder.append(3);
            } else if (time.contains("四")) {
                stringBuilder.append(4);
            } else if (time.contains("五")) {
                stringBuilder.append(5);
            } else if (time.contains("六")) {
                stringBuilder.append(6);
            } else if (time.contains("日")) {
                stringBuilder.append(7);
            }
            if (time.contains("上午")) {
                stringBuilder.append("a");
            } else {
                stringBuilder.append("p");
            }
            stringBuilder.append(",");
        }
        return StringUtils.substringBeforeLast(stringBuilder.toString(), ",");
    }
}
