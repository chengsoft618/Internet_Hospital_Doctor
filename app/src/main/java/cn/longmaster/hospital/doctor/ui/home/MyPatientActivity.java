package cn.longmaster.hospital.doctor.ui.home;

import android.support.v4.app.Fragment;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.util.RadioTabFragmentHelper;

/**
 * Created by Yang² on 2016/12/8.
 * Mod by biao on 2019/10/29
 * 预约列表Activity
 */

public class MyPatientActivity extends NewBaseActivity {
    @FindViewById(R.id.act_my_patient_action_bar)
    private LinearLayout actMyPatientActionBar;
    @FindViewById(R.id.act_my_patient_back_iv)
    private ImageView actMyPatientBackIv;
    @FindViewById(R.id.act_my_patient_title_tv)
    private TextView actMyPatientTitleTv;
    @FindViewById(R.id.act_my_patient_right_view)
    private LinearLayout actMyPatientRightView;
    @FindViewById(R.id.act_my_patient_show_same_dep_cb)
    private CheckBox actMyPatientShowSameDepCb;
    @FindViewById(R.id.act_my_patient_rg)
    private RadioGroup actMyPatientRg;
    @FindViewById(R.id.act_my_patient_rounds_rb)
    private RadioButton actMyPatientRoundsRb;
    @FindViewById(R.id.act_my_patient_consult_rb)
    private RadioButton actMyPatientConsultRb;

    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    private ConsultManager mPatientManager;

    private MyPatientConsultFragment mMyPatientConsultFragment;
    private MyPatientRoundsFragment mMyPatientRoundsFragment;
    private int mCurrentTab;
    private RadioTabFragmentHelper radioTabFragmentHelper;

    @Override
    protected void initDatas() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_my_patient;
    }

    @Override
    protected void initViews() {
        actMyPatientBackIv.setOnClickListener(v -> {
            onBackPressed();
        });
        actMyPatientShowSameDepCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (mCurrentTab == 0) {
                mMyPatientRoundsFragment.setSameDep(isChecked);
            } else {
                mMyPatientConsultFragment.setSameDep(isChecked);
            }
        });
        actMyPatientRg.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.act_my_patient_rounds_rb:
                    actMyPatientRoundsRb.setTextSize(18);
                    actMyPatientConsultRb.setTextSize(16);
                    mCurrentTab = 0;
                    break;
                case R.id.act_my_patient_consult_rb:
                    actMyPatientRoundsRb.setTextSize(16);
                    actMyPatientConsultRb.setTextSize(18);
                    mCurrentTab = 1;
                    break;
                default:
                    break;
            }
            radioTabFragmentHelper.setFragment(mCurrentTab);
        });
        actMyPatientRoundsRb.setChecked(true);
        List<Fragment> fragmentList = new ArrayList<>(2);
        mMyPatientRoundsFragment = new MyPatientRoundsFragment();
        mMyPatientConsultFragment = new MyPatientConsultFragment();
        fragmentList.add(mMyPatientRoundsFragment);
        fragmentList.add(mMyPatientConsultFragment);
        radioTabFragmentHelper = new RadioTabFragmentHelper.Builder()
                .setContainerViewId(R.id.act_my_patient_fl)
                .setCurrentTab(mCurrentTab)
                .setFragmentList(fragmentList)
                .setFragmentManager(getSupportFragmentManager())
                .build();
        radioTabFragmentHelper.initFragment();
    }
}
