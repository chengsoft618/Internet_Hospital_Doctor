package cn.longmaster.hospital.doctor.ui.user;

import android.support.v4.app.Fragment;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.user.fragment.MineDataListFragment;
import cn.longmaster.hospital.doctor.ui.user.fragment.PatientDataAddFragment;
import cn.longmaster.hospital.doctor.util.RadioTabFragmentHelper;

/**
 * @author ABiao_Abiao
 * @date 2019/6/29 11:18
 * @description: 材料管理列表Activity
 */
public class PatientMaterialManagerActivity extends NewBaseActivity {
    @FindViewById(R.id.iv_tool_bar_back)
    private ImageView ivToolBarBack;
    @FindViewById(R.id.tv_tool_bar_title)
    private TextView tvToolBarTitle;
    @FindViewById(R.id.activity_data_manager_radio_group)
    private RadioGroup activityDataManagerRadioGroup;
    @FindViewById(R.id.activity_data_manager_fl)
    private FrameLayout activityDataManagerFl;
    private RadioTabFragmentHelper radioTabFragmentHelper;
    private PatientDataAddFragment patientDataListFragment;
    private MineDataListFragment mineDataListFragment;
    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;

    @Override
    protected void initDatas() {
        patientDataListFragment = new PatientDataAddFragment();
        mineDataListFragment = MineDataListFragment.getInstance(getIntent().getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOCAL_PATH));

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(patientDataListFragment);
        fragments.add(mineDataListFragment);
        radioTabFragmentHelper = new RadioTabFragmentHelper.Builder()
                .setContainerViewId(R.id.activity_data_manager_fl)
                .setFragmentList(fragments)
                .setFragmentManager(getSupportFragmentManager())
                .setCurrentTab(0)
                .build();
        radioTabFragmentHelper.initFragment();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_data_manager;
    }

    @Override
    protected void initViews() {
        tvToolBarTitle.setText("材料管理");
        ivToolBarBack.setOnClickListener(v -> {
            onBackPressed();
        });
        activityDataManagerRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (null != radioTabFragmentHelper) {
                switch (checkedId) {
                    case R.id.activity_data_manager_patient_info:
                        radioTabFragmentHelper.setFragment(0);
                        break;
                    case R.id.activity_data_manager_my_info:
                        radioTabFragmentHelper.setFragment(1);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
