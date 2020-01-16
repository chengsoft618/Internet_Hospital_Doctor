package cn.longmaster.hospital.doctor.ui.main;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.consult.RoomListInfo;

/**
 * Created by W·H·K on 2018/12/18.
 * Mod by biao on 2019/7/1
 */
public class MeetingViewAdapter extends BaseQuickAdapter<RoomListInfo, BaseViewHolder> {

    public MeetingViewAdapter(int layoutResId, @Nullable List<RoomListInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RoomListInfo item) {
        helper.setText(R.id.item_meeting_view_text, item.getRoomName());
        helper.addOnClickListener(R.id.item_meeting_view_join_btn);
    }
}
