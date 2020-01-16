package cn.longmaster.hospital.doctor.ui.rounds;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.rounds.fragment.CommonlyExpertFragment;
import cn.longmaster.hospital.doctor.ui.rounds.fragment.MoreExpertFragment;
import cn.longmaster.hospital.doctor.util.RadioTabFragmentHelper;

/**
 * 查房选择医生页面
 */
public class RoundsChoiceDoctorActivity extends NewBaseActivity {
    @FindViewById(R.id.iv_tool_bar_back)
    private ImageView ivToolBarBack;
    @FindViewById(R.id.act_rounds_choose_doctor_rg)
    private RadioGroup activityRoundsRadioGroup;
    @FindViewById(R.id.act_rounds_choose_commend_doctor_rb)
    private RadioButton activityRoundsRadioGroupCommonExpert;
    @FindViewById(R.id.act_rounds_choose_more_doctor_rb)
    private RadioButton activityRoundsRadioGroupMoreExpert;
    @FindViewById(R.id.act_rounds_choose_commend_doctor_v)
    private View actRoundsChooseCommendDoctorV;
    private List<Fragment> fragments = new ArrayList<>(2);
    private int mCurrentTab = 1;

    private RadioTabFragmentHelper radioTabFragmentHelper;

    @Override
    protected void initDatas() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_rounds_choice_doctor;
    }

    @Override
    protected void initViews() {
        initListener();
        initFragments();
    }

    private void initFragments() {
        CommonlyExpertFragment commonlyExpertFragment = CommonlyExpertFragment.getInstance();
        commonlyExpertFragment.setOnDoctorListLoadListener(hasDoctor -> {
            activityRoundsRadioGroupCommonExpert.setVisibility(hasDoctor ? View.VISIBLE : View.GONE);
            actRoundsChooseCommendDoctorV.setVisibility(!hasDoctor ? View.VISIBLE : View.GONE);
            if (hasDoctor) {
                activityRoundsRadioGroupMoreExpert.setBackgroundResource(R.drawable.bg_rounds_doctor_choose_tab);
            } else {
                activityRoundsRadioGroupMoreExpert.setBackgroundResource(R.color.color_049eff);
            }
            activityRoundsRadioGroupCommonExpert.setChecked(hasDoctor);
        });
        fragments.add(commonlyExpertFragment);
        fragments.add(MoreExpertFragment.getInstance());
        radioTabFragmentHelper = new RadioTabFragmentHelper.Builder()
                .setContainerViewId(R.id.act_rounds_choose_doctor_fl)
                .setCurrentTab(mCurrentTab)
                .setFragmentList(fragments)
                .setFragmentManager(getSupportFragmentManager())
                .build();
        radioTabFragmentHelper.initFragment();
    }

    private void initListener() {
        ivToolBarBack.setOnClickListener(v -> {
            onBackPressed();
        });
        activityRoundsRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.act_rounds_choose_commend_doctor_rb:
                    activityRoundsRadioGroupCommonExpert.setTextSize(18);
                    activityRoundsRadioGroupMoreExpert.setTextSize(16);
                    mCurrentTab = 0;
                    break;
                case R.id.act_rounds_choose_more_doctor_rb:
                    activityRoundsRadioGroupCommonExpert.setTextSize(16);
                    activityRoundsRadioGroupMoreExpert.setTextSize(18);
                    mCurrentTab = 1;
                    break;
                default:
                    break;
            }
            radioTabFragmentHelper.setFragment(mCurrentTab);
        });
    }
}
