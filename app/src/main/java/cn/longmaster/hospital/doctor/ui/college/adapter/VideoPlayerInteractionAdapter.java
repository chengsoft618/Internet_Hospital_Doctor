package cn.longmaster.hospital.doctor.ui.college.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.common.DateUtil;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.college.InteractionInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.util.AvatarUtils;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 互动适配器
 * <p>
 * Created by W·H·K on 2018/3/19.
 */
public class VideoPlayerInteractionAdapter extends BaseQuickAdapter<InteractionInfo, BaseViewHolder> {
    private Map<Integer, DoctorBaseInfo> mDoctorBaseInfos = new HashMap<>();
    private DoctorManager mDoctorManager;

    public VideoPlayerInteractionAdapter(int layoutResId, @Nullable List<InteractionInfo> data) {
        super(layoutResId, data);
        mDoctorManager = AppApplication.getInstance().getManager(DoctorManager.class);
    }

    @Override
    protected void convert(BaseViewHolder helper, InteractionInfo item) {
        helper.setText(R.id.item_video_player_insert_dt, TextUtils.isEmpty(item.getInsertDt()) ? "" : DateUtil.getTime(item.getInsertDt(), "yyyy.MM.dd"));
        helper.setText(R.id.item_video_player_msg_content, item.getMsgContent());
        int doctorId = item.getUserId();
        if (!mDoctorBaseInfos.containsKey(doctorId)) {
            mDoctorManager.getDoctorInfo(doctorId, false, new DoctorManager.OnDoctorInfoLoadListener() {
                @Override
                public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                    mDoctorBaseInfos.put(doctorId, doctorBaseInfo);
                    displayInfo(helper, doctorBaseInfo);
                }

                @Override
                public void onFailed(int code, String msg) {

                }

                @Override
                public void onFinish() {

                }
            });
        } else {
            displayInfo(helper, mDoctorBaseInfos.get(doctorId));
        }
    }

    private void displayInfo(BaseViewHolder helper, DoctorBaseInfo doctorBaseInfo) {
        helper.setText(R.id.item_video_player_name, TextUtils.isEmpty(doctorBaseInfo.getRealName())
                ? mContext.getString(R.string.medical_college_student) : doctorBaseInfo.getRealName());
        CircleImageView avatar = helper.getView(R.id.item_video_player_civ);
        GlideUtils.showDoctorAvatar(avatar, mContext, AvatarUtils.getAvatar(false, doctorBaseInfo.getUserId(), doctorBaseInfo.getAvaterToken()));
    }
}