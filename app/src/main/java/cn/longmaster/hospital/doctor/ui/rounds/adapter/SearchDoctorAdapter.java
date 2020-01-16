package cn.longmaster.hospital.doctor.ui.rounds.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.SearchDoctorInfo;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.util.AvatarUtils;
import cn.longmaster.hospital.doctor.util.GlideUtils;

/**
 * 搜索医生信息结果适配
 * <p>
 * Created by W·H·K on 2018/5/14.
 */
public class SearchDoctorAdapter extends BaseQuickAdapter<SearchDoctorInfo, BaseViewHolder> {
    private Map<Integer, DoctorBaseInfo> mDoctorBaseInfos = new HashMap<>();
    private DoctorManager mDoctorManager;
    private OnChoiceDoctorBtnClickListener mOnChoiceDoctorBtnClickListener;
    private boolean mIsRounds = false;

    public SearchDoctorAdapter(int layoutResId, @Nullable List<SearchDoctorInfo> data) {
        super(layoutResId, data);
        mDoctorManager = AppApplication.getInstance().getManager(DoctorManager.class);
    }


    @Override
    protected void convert(BaseViewHolder helper, SearchDoctorInfo item) {
        int position = helper.getLayoutPosition();
        ImageView avatarAiv = helper.getView(R.id.item_doctor_list_photo_iv);
        TextView itemDoctorListChoiceBtnTv = helper.getView(R.id.item_doctor_list_choice_btn_tv);
        itemDoctorListChoiceBtnTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnChoiceDoctorBtnClickListener != null) {
                    mOnChoiceDoctorBtnClickListener.onChoiceDoctorBtnClickListener(v, item.getDoctorId(), position);
                }
            }
        });
        if (mIsRounds) {
            helper.setVisible(R.id.item_doctor_list_score_view, true);
        } else {
            helper.setGone(R.id.item_doctor_list_score_view, false);
        }
        DecimalFormat df = new DecimalFormat("#.0");
        helper.setText(R.id.item_doctor_list_score, df.format(item.getScore()) + "分");

        getDoctorBaseInfo(item.getDoctorId(), new DoctorManager.OnDoctorInfoLoadListener() {
            @Override
            public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                mDoctorBaseInfos.put(item.getDoctorId(), doctorBaseInfo);
                helper.setText(R.id.item_doctor_list_name_tv, doctorBaseInfo.getRealName());
                helper.setText(R.id.item_doctor_list_level_tv, doctorBaseInfo.getDoctorLevel());
                GlideUtils.showDoctorAvatar(avatarAiv, mContext, AvatarUtils.getAvatar(false, doctorBaseInfo.getUserId(), doctorBaseInfo.getAvaterToken()));
                helper.setText(R.id.item_doctor_list_department_tv, doctorBaseInfo.getDepartmentName());
                helper.setText(R.id.item_doctor_list_hospital_tv, doctorBaseInfo.getHospitalName());
            }

            @Override
            public void onFailed(int code, String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    public void setIsRounds(Boolean isRounds) {
        this.mIsRounds = isRounds;
    }


    public void setOnChoiceDoctorBtnClickListener(OnChoiceDoctorBtnClickListener onChoiceDoctorBtnClickListener) {
        mOnChoiceDoctorBtnClickListener = onChoiceDoctorBtnClickListener;
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

    public interface OnChoiceDoctorBtnClickListener {
        void onChoiceDoctorBtnClickListener(View view, Integer doctorId, int position);
    }
}
