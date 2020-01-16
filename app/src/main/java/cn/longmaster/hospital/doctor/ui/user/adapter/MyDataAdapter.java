package cn.longmaster.hospital.doctor.ui.user.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.daimajia.swipe.SwipeLayout;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.user.PersonalDataInfo;
import cn.longmaster.utils.TimeUtils;

/**
 * @author W·H·K
 * @date 2018/5/9
 * Mod by biao on 2019/11/15
 */
public class MyDataAdapter extends BaseQuickAdapter<PersonalDataInfo, BaseViewHolder> {
    private OnItemClickListener listener;

    public MyDataAdapter(int layoutResId, @Nullable List<PersonalDataInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PersonalDataInfo item) {
        int position = helper.getLayoutPosition();
        SwipeLayout swipeLayout = helper.getView(R.id.item_my_data_sl);
        swipeLayout.getSurfaceView().setOnClickListener(v -> listener.onItemClick(MyDataAdapter.this, v, position));
        swipeLayout.setOnClickListener(v -> listener.onItemClick(MyDataAdapter.this, v, position));
        helper.setText(R.id.item_my_data_visible_tv, item.isSelfVisible() ? "可看" : "不可看");
        helper.setText(R.id.item_my_data_name_tv, item.getMaterialName());
        helper.setText(R.id.item_my_data_time_tv, getFriendlyTimeSpanByNow(item.getInsertDt()));
        helper.addOnClickListener(R.id.item_my_data_visible_tv);
        helper.addOnClickListener(R.id.item_my_data_edit_name_tv);
        helper.addOnClickListener(R.id.item_my_data_relation_tv);
        helper.addOnClickListener(R.id.item_my_data_delete_tv);
        helper.addOnClickListener(R.id.item_my_data_sl);
    }

    @Override
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setSelfVisible(int position, int selfVisible) {
        getData().get(position).setSelfVisible(selfVisible);
        notifyItemChanged(position);
    }

    public void setNewName(int position, String newName) {
        getData().get(position).setMaterialName(newName);
        notifyItemChanged(position);
    }

    private String getFriendlyTimeSpanByNow(String time) {
        long now = System.currentTimeMillis();
        long millis = TimeUtils.string2Millis(time);
        long span = now - millis;
        if (span < 0) {
            return TimeUtils.string2String(time, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()), new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()));
        }
        return TimeUtils.getFriendlyTimeSpanByNow(time);
    }
}
