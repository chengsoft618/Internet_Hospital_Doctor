package cn.longmaster.hospital.doctor.ui.rounds.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.common.BaseConfigInfo;
import cn.longmaster.hospital.doctor.core.entity.department.DepartmentInfo;
import cn.longmaster.hospital.doctor.core.manager.common.BaseConfigManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.config.RequestParams;

/**
 * Created by W·H·K on 2018/5/11.
 */

public class CommonDepartmentAdapter extends BaseAdapter {
    private Context mContext;
    private List<Integer> mList;

    public CommonDepartmentAdapter(Context context, List<Integer> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_common_department_layout, null);
            vh = new ViewHolder();
            vh.timeTv = (TextView) convertView.findViewById(R.id.item_common_department_name_tv);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        getDepartmentInfo(mList.get(position), vh);
        return convertView;
    }

    class ViewHolder {
        TextView timeTv;
    }

    private void getDepartmentInfo(final int departmentId, final ViewHolder viewHolder) {
        AppApplication.getInstance().getManager(BaseConfigManager.class).getBaseConfigFromDB(OpTypeConfig.CLIENTAPI_OPTYE_DEPARTMENT_INFO, departmentId, new BaseConfigManager.OnGetBaseConfigStateChangeListener() {
            @Override
            public void onGetBaseConfigStateChanged(BaseConfigInfo baseConfigInfo) {
                if (baseConfigInfo != null) {
                    displayInfo(baseConfigInfo, viewHolder);
                }
                RequestParams requestParams = new RequestParams();
                requestParams.setDepartmentId(departmentId);
                requestParams.setToken(baseConfigInfo == null ? "0" : baseConfigInfo.getToken());
                requestParams.setType(OpTypeConfig.CLIENTAPI_OPTYE_DEPARTMENT_INFO);
                AppApplication.getInstance().getManager(BaseConfigManager.class).getBaseConfigFromNet(requestParams, new OnResultListener() {
                    @Override
                    public void onResult(BaseResult baseResult, Object o) {
                        if (baseResult.getCode() == RESULT_SUCCESS) {
                            DepartmentInfo departmentInfo = (DepartmentInfo) o;
                            BaseConfigInfo configInfo = new BaseConfigInfo();
                            configInfo.setToken(baseResult.getToken());
                            configInfo.setData(departmentInfo);
                            displayInfo(configInfo, viewHolder);
                        }
                    }
                });
            }
        });
    }

    private void displayInfo(BaseConfigInfo baseConfigInfo, ViewHolder viewHolder) {
        DepartmentInfo departmentInfo = (DepartmentInfo) baseConfigInfo.getData();
        viewHolder.timeTv.setText(departmentInfo.getDepartmentName());
    }
}
