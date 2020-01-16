package cn.longmaster.hospital.doctor.ui.user.adapter;

import android.support.annotation.Nullable;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.rounds.WaitRoundsPatientInfo;
import cn.longmaster.hospital.doctor.view.SlidingButtonView;
import cn.longmaster.utils.DisplayUtil;
import cn.longmaster.utils.StringUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/7/3 15:14
 * @description: 患者管理
 */
public class PatientManagerAdapter extends BaseQuickAdapter<WaitRoundsPatientInfo, BaseViewHolder> {
    private SlidingButtonView mSlidingButtonView;

    public PatientManagerAdapter(int layoutResId, @Nullable List<WaitRoundsPatientInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WaitRoundsPatientInfo item) {
        SlidingButtonView itemPatientManagerContentSbv = helper.getView(R.id.item_patient_manager_content_sbv);
        itemPatientManagerContentSbv.setSlidingButtonListener(new SlidingButtonView.IonSlidingButtonListener() {
            @Override
            public void onMenuIsOpen(SlidingButtonView view) {
                mSlidingButtonView = view;
            }

            @Override
            public void onDownOrMove(SlidingButtonView slidingButtonView) {
                if (mSlidingButtonView != null) {
                    if (mSlidingButtonView != slidingButtonView) {
                        mSlidingButtonView.closeMenu();
                        mSlidingButtonView = null;
                    }
                }
            }
        });
        //是否已经关联
        if (!item.hasOrderId()) {
            helper.setGone(R.id.item_patient_manager_association_num_tv, false);
            helper.setGone(R.id.item_patient_manager_association_tv, false);
            helper.setVisible(R.id.item_patient_manager_no_association_tv, true);
            helper.setText(R.id.item_wait_rounds_patient_relation, "关联就诊");
        } else {
            helper.setVisible(R.id.item_patient_manager_association_num_tv, true);
            helper.setVisible(R.id.item_patient_manager_association_tv, true);
            helper.setGone(R.id.item_patient_manager_no_association_tv, false);
            helper.setText(R.id.item_patient_manager_association_num_tv, item.getOrderId());
            helper.setText(R.id.item_wait_rounds_patient_relation, "已关联");
        }

        RelativeLayout itemPatientManagerItemRl = helper.getView(R.id.item_patient_manager_item_rl);
        itemPatientManagerItemRl.getLayoutParams().width = DisplayUtil.getDisplayMetrics().widthPixels - DisplayUtil.dp2px(20);
        helper.setText(R.id.item_patient_manager_name_tv, item.getPatientName());
        helper.setText(R.id.item_patient_manager_gender_tv, item.getGender() == 1 ? "男" : "女");
        helper.setText(R.id.item_patient_manager_age_tv, item.getAge());
        helper.setText(R.id.item_patient_manager_in_hospital_num_desc_tv, StringUtils.isTrimEmpty(item.getHospitalizaId()) ? "--" : item.getHospitalizaId());
        helper.setText(R.id.item_patient_manager_patient_num_desc_tv, item.getMedicalId() + "");
        helper.setText(R.id.item_patient_manager_confirm_sick_desc_tv, item.getAttendingDisease());
        helper.addOnClickListener(R.id.item_wait_rounds_patient_relation)
                .addOnClickListener(R.id.item_patient_manager_item_rl);
    }
}
