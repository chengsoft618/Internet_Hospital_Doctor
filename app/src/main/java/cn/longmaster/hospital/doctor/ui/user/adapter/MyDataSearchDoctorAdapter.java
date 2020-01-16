package cn.longmaster.hospital.doctor.ui.user.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.MyDataDoctorInfo;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.util.AvatarUtils;
import cn.longmaster.hospital.doctor.util.GlideUtils;

/**
 * 搜索医生信息结果适配
 * <p>
 * Created by W·H·K on 2018/5/14.
 */
public class MyDataSearchDoctorAdapter extends BaseQuickAdapter<MyDataDoctorInfo, BaseViewHolder> {
    private Map<Integer, DoctorBaseInfo> mDoctorBaseInfos = new HashMap<>();
    private DoctorManager mDoctorManager;

    public MyDataSearchDoctorAdapter(int layoutResId, @Nullable List<MyDataDoctorInfo> data) {
        super(layoutResId, data);
        mDoctorManager = AppApplication.getInstance().getManager(DoctorManager.class);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyDataDoctorInfo item) {
        int position = helper.getLayoutPosition();
        if (position == 0) {
            helper.setGone(R.id.item_line, false);
        } else {
            helper.setVisible(R.id.item_line, true);
        }
        if (item.isElection()) {
            helper.setVisible(R.id.item_election_view, true);
        } else {
            helper.setGone(R.id.item_election_view, false);
        }
        ImageView avatarAiv = helper.getView(R.id.item_photo_iv);

        getDoctorBaseInfo(item.getDoctorId(), new DoctorManager.OnDoctorInfoLoadListener() {
            @Override
            public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                mDoctorBaseInfos.put(item.getDoctorId(), doctorBaseInfo);

                GlideUtils.showDoctorAvatar(avatarAiv, mContext, AvatarUtils.getAvatar(false, doctorBaseInfo.getUserId(), doctorBaseInfo.getAvaterToken()));
                helper.setText(R.id.item_doctor_name_tv, doctorBaseInfo.getRealName());
                helper.setText(R.id.item_level_tv, doctorBaseInfo.getDoctorLevel());
                GlideUtils.showDoctorAvatar(avatarAiv, mContext, AvatarUtils.getAvatar(false, doctorBaseInfo.getUserId(), doctorBaseInfo.getAvaterToken()));
                helper.setText(R.id.item_department_tv, doctorBaseInfo.getDepartmentName());
                helper.setText(R.id.item_hospital_tv, doctorBaseInfo.getHospitalName());
            }

            @Override
            public void onFailed(int code, String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void getDoctorBaseInfo(int doctorId, DoctorManager.OnDoctorInfoLoadListener listener) {
        if (mDoctorBaseInfos.containsKey(doctorId)) {
            listener.onSuccess(mDoctorBaseInfos.get(doctorId));
        } else {
            mDoctorManager.getDoctorInfo(doctorId, false, listener);
        }
    }

    public DoctorBaseInfo getDoctorBaseInfo(int doctorId) {
        return mDoctorBaseInfos.get(doctorId);
    }
}
