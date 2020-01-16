package cn.longmaster.hospital.doctor.ui.doctor.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.customview.listview.SimpleBaseAdapter;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.consult.remote.ScheduleOrImageInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.PackageInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.appointment.ScheduleServiceRequester;

/**
 * 升窗选择服务适配器
 * Created by Yang² on 2016/7/2.
 */
public class ConsultServiceAdapter extends SimpleBaseAdapter<Integer, ConsultServiceAdapter.ViewHolder> {
    private int mPosition = -1;
    private OnConsultServiceClickListener mOnConsultServiceClickListener;
    private SparseArray<ScheduleOrImageInfo> mSchedules;//排班缓存
    private SparseArray<String> mServiceName;//服务名称
    private List<PackageInfo> mPackageInfoList = new ArrayList<>();//套餐列表

    public void setOnConsultServiceClickListener(OnConsultServiceClickListener mOnConsultServiceClickListener) {
        this.mOnConsultServiceClickListener = mOnConsultServiceClickListener;
    }

    public ConsultServiceAdapter(Context context, List<PackageInfo> packageInfos) {
        super(context);
        mSchedules = new SparseArray<>();
        mServiceName = new SparseArray<>();
        mPackageInfoList = packageInfos;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_imaging_consult_service;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, final Integer id, final int position) {
        if (mPosition == position) {
            viewHolder.mServiceTv.setTextColor(ContextCompat.getColor(context, R.color.color_white));
            viewHolder.mServiceTv.setBackgroundResource(R.drawable.bg_btn_up_window_orange);
        } else {
            viewHolder.mServiceTv.setTextColor(ContextCompat.getColor(context, R.color.color_666666));
            viewHolder.mServiceTv.setBackgroundResource(R.drawable.bg_btn_up_window_white);
        }
        reqScheduleDetailFromNet(id, viewHolder);
        viewHolder.mServiceTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPosition == position) {
                    mPosition = -1;
                } else {
                    mPosition = position;
                }
                notifyDataSetChanged();
                mOnConsultServiceClickListener.onServiceSelect(mPosition, mSchedules.get(id));
            }
        });
    }

    /**
     * 拉取排班详情
     *
     * @param id
     * @param viewHolder
     */
    private void reqScheduleDetailFromNet(final int id, final ViewHolder viewHolder) {
        if (mSchedules.get(id) != null) {
            viewHolder.mServiceTv.setText(mServiceName.get(id));
            return;
        }
        ScheduleServiceRequester requester = new ScheduleServiceRequester(new OnResultListener<ScheduleOrImageInfo>() {
            @Override
            public void onResult(BaseResult baseResult, ScheduleOrImageInfo scheduleOrImageInfo) {
                if (baseResult.getCode() != RESULT_SUCCESS) {
                    return;
                }
                mSchedules.append(id, scheduleOrImageInfo);
                for (PackageInfo info : mPackageInfoList) {
                    if (scheduleOrImageInfo.getScheduingRelationList().get(0).getPackageId() == info.getPackageId()) {
                        viewHolder.mServiceTv.setText(info.getPackageName());
                        mServiceName.append(id, info.getPackageName());
                    }
                }
            }
        });
        requester.scheduingId = id;
        requester.doPost();
    }

    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    public class ViewHolder {
        @FindViewById(R.id.item_imagine_consult_service_tv)
        private TextView mServiceTv;

    }

    public interface OnConsultServiceClickListener {
        void onServiceSelect(int position, ScheduleOrImageInfo info);
    }
}
