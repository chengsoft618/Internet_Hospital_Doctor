package cn.longmaster.hospital.doctor.ui.consult.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.SparseBooleanArray;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.consult.remote.ScheduleOrImageInfo;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.TimeUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/11/5 10:20
 * @description: 会诊就诊排班时间
 */
public class ConsultScheduleAdapter extends BaseQuickAdapter<ScheduleOrImageInfo, BaseViewHolder> {
    private SparseBooleanArray checkCache;
    private OnItemCheckedListener onItemCheckedListener;

    public ConsultScheduleAdapter(int layoutResId, @Nullable List<ScheduleOrImageInfo> data) {
        super(layoutResId, data);
        checkCache = new SparseBooleanArray(LibCollections.size(data));
    }

    @Override
    protected void convert(BaseViewHolder helper, ScheduleOrImageInfo item) {
        int position = helper.getLayoutPosition();
        if (!StringUtils.isEmpty(item.getBeginDt()) && item.getBeginDt().contains("2099")) {
            helper.setText(R.id.item_consult_schedule_time_tv, "时间待定");
        } else {
            StringBuilder time = new StringBuilder();
            time.append(TimeUtils.string2String(item.getBeginDt(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()), new SimpleDateFormat("yyyy年 MM月dd日 E HH:mm", Locale.getDefault())));
            time.append("至");
            time.append(TimeUtils.string2String(item.getEndDt(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()), new SimpleDateFormat("HH:mm", Locale.getDefault())));
            helper.setText(R.id.item_consult_schedule_time_tv, time);
        }

        if (checkCache.get(position)) {
            helper.setTextColor(R.id.item_consult_schedule_time_tv, ContextCompat.getColor(mContext, R.color.color_049eff));
            helper.setBackgroundRes(R.id.item_consult_schedule_time_tv, R.drawable.bg_solid_edf1fd_radius_8);
        } else {
            helper.setTextColor(R.id.item_consult_schedule_time_tv, ContextCompat.getColor(mContext, R.color.color_1a1a1a));
            helper.setBackgroundRes(R.id.item_consult_schedule_time_tv, R.drawable.bg_solid_faf9f9_radius_8);
        }

    }

    public void setOnItemCheckedListener(OnItemCheckedListener onItemCheckedListener) {
        this.onItemCheckedListener = onItemCheckedListener;
    }

    public void setCheck(int position) {
        checkCache = new SparseBooleanArray(getItemCount());
        if (position < getItemCount()) {
            for (int i = 0; i < getItemCount(); i++) {
                checkCache.put(i, i == position);
            }
        }
        onItemCheckedListener.onItemChecked(this, position);
        notifyDataSetChanged();
    }

    public ScheduleOrImageInfo getChecked() {
        for (int i = 0; i < getItemCount(); i++) {
            if (checkCache.get(i)) {
                return getData().get(i);
            }
        }
        return null;
    }

    public interface OnItemCheckedListener {
        void onItemChecked(BaseQuickAdapter adapter, int position);
    }
}
