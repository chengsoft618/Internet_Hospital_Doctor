package cn.longmaster.hospital.doctor.core.requests.college;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.college.CourseListInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;
import cn.longmaster.utils.StringUtils;

/**
 * Created by W·H·K on 2018/3/26.
 */

public class CourseListRequester extends SimpleHttpRequester<List<CourseListInfo>> {
    public int labelId;//标签ID
    public int orderType;//0：全部;1：最新;2：最热
    public int moduleType;//1--标签模式；2--类型-video；3--分类-指南
    public int pageIndex;
    public int pageSize;

    public CourseListRequester(@NonNull OnResultListener<List<CourseListInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_COURSE_LIST;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected List<CourseListInfo> onDumpData(JSONObject data) throws JSONException {
        String datas = data.getString("data");
        if (StringUtils.isTrimEmpty(datas)) {
            return new ArrayList<>(0);
        }
        return JsonHelper.toList(data.getJSONArray("data"), CourseListInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("label_id", labelId);
        params.put("module_type", moduleType);
        params.put("order_type", orderType);
        params.put("page_index", pageIndex);
        params.put("page_size", pageSize);
    }
}
