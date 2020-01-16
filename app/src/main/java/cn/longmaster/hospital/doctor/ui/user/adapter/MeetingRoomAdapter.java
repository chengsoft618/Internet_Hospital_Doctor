package cn.longmaster.hospital.doctor.ui.user.adapter;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.user.MeetingRoomInfo;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.util.AvatarUtils;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 会议室adapter
 * Created by W·H·K on 2018/12/19.
 */
public class MeetingRoomAdapter extends BaseQuickAdapter<MeetingRoomInfo, BaseViewHolder> {
    private Map<Integer, DoctorBaseInfo> mDoctorBaseInfoMap = new HashMap<>();

    public MeetingRoomAdapter(int layoutResId, @Nullable List<MeetingRoomInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MeetingRoomInfo item) {
        CircleImageView doctorAvatar = helper.getView(R.id.item_meeting_room_role_iv);
        AppApplication.getInstance().getManager(DoctorManager.class).getDoctorInfo(item.getUserId(), true, new DoctorManager.OnDoctorInfoLoadListener() {
            @Override
            public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                if (null != doctorBaseInfo) {
                    mDoctorBaseInfoMap.put(item.getUserId(), doctorBaseInfo);
                    helper.setText(R.id.item_meeting_room_role_name, doctorBaseInfo.getRealName());
                    GlideUtils.showDoctorAvatar(doctorAvatar, mContext, AvatarUtils.getAvatar(false, doctorBaseInfo.getUserId(), doctorBaseInfo.getAvaterToken()));
                }
            }

            @Override
            public void onFailed(int code, String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
        Logger.logD(Logger.ROOM, "MeetingRoomAdapter->onMemberSpeaking()-->isSpeak:" + item.isSpeak());
        if (item.isSpeak()) {
            helper.setBackgroundRes(R.id.item_meeting_room_ring_view, R.drawable.bg_red_ring_image);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helper.setBackgroundRes(R.id.item_meeting_room_ring_view, R.drawable.bg_white_ring_image);
                }
            }, 500);
        }
    }
}
