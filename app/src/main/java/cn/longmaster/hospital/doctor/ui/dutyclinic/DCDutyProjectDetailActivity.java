package cn.longmaster.hospital.doctor.ui.dutyclinic;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.allen.library.SuperTextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCProjectInfo;
import cn.longmaster.hospital.doctor.core.manager.dutyclinic.DCManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.dutyclinic.adapter.DCProjectChoiceAdapter;
import cn.longmaster.hospital.doctor.ui.dutyclinic.fragment.DCDutyProjectInfoFragment;
import cn.longmaster.hospital.doctor.ui.dutyclinic.fragment.DCDutyProjectPatientListFragment;
import cn.longmaster.hospital.doctor.util.RadioTabFragmentHelper;
import cn.longmaster.utils.DisplayUtil;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/12/16 11:06
 * @description: 门诊项目详情界面
 */
public class DCDutyProjectDetailActivity extends NewBaseActivity {
    @FindViewById(R.id.act_du_duty_project_details_title_ll)
    private FrameLayout actDuDutyProjectDetailsTitleLl;
    @FindViewById(R.id.act_dc_duty_details_back_iv)
    private ImageView actDcDutyDetailsBackIv;
    @FindViewById(R.id.act_dc_duty_details_title_stv)
    private SuperTextView actDcDutyDetailsTitleStv;
    @FindViewById(R.id.act_dc_duty_project_detail_rg)
    private RadioGroup actDcDutyProjectDetailRg;
    @FindViewById(R.id.act_dc_duty_project_info_rb)
    private RadioButton actDcDutyProjectInfoRb;
    @FindViewById(R.id.act_dc_duty_project_patient_list_rb)
    private RadioButton actDcDutyProjectPatientRb;
    @FindViewById(R.id.act_dc_duty_project_detail_fl)
    private FrameLayout actDcDutyProjectDetailFl;

    @AppApplication.Manager
    private DCManager dcManager;
    private RadioTabFragmentHelper radioTabFragmentHelper;
    private int currentProjectId;
    private DCProjectChoiceAdapter dcProjectChoiceAdapter;
    private List<DCProjectInfo> mDcProjectInfos;
    private DCDutyProjectInfoFragment dcDutyProjectInfoFragment;
    private DCDutyProjectPatientListFragment dcDutyProjectPatientListFragment;

    @Override
    protected void initDatas() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_dcduty_detail;
    }

    @Override
    protected void initViews() {
        initListener();
        dcDutyProjectInfoFragment = new DCDutyProjectInfoFragment();
        dcDutyProjectPatientListFragment = new DCDutyProjectPatientListFragment();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(dcDutyProjectInfoFragment);
        fragments.add(dcDutyProjectPatientListFragment);
        radioTabFragmentHelper = new RadioTabFragmentHelper.Builder()
                .setContainerViewId(R.id.act_dc_duty_project_detail_fl)
                .setFragmentList(fragments)
                .setFragmentManager(getSupportFragmentManager())
                .setCurrentTab(0)
                .build();
        radioTabFragmentHelper.initFragment();
        getProjectList();
    }

    private void initListener() {
        actDcDutyDetailsBackIv.setOnClickListener(v -> onBackPressed());
        actDcDutyDetailsTitleStv.setOnClickListener(v -> {
            if (LibCollections.isEmpty(mDcProjectInfos)) {
                getProjectList();
            } else {
                showChoiceProjectWindow(mDcProjectInfos);
            }
        });
        actDcDutyProjectDetailRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (null != radioTabFragmentHelper) {
                    switch (checkedId) {
                        case R.id.act_dc_duty_project_info_rb:
                            radioTabFragmentHelper.setFragment(0);
                            actDcDutyProjectInfoRb.setTextSize(18);
                            actDcDutyProjectInfoRb.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.color_049eff));
                            actDcDutyProjectPatientRb.setTextSize(16);
                            actDcDutyProjectPatientRb.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.color_1a1a1a));
                            break;
                        case R.id.act_dc_duty_project_patient_list_rb:
                            radioTabFragmentHelper.setFragment(1);
                            actDcDutyProjectPatientRb.setTextSize(18);
                            actDcDutyProjectPatientRb.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.color_049eff));
                            actDcDutyProjectInfoRb.setTextSize(16);
                            actDcDutyProjectInfoRb.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.color_1a1a1a));
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    private void getProjectList() {
        dcManager.getProjectList(1, new DefaultResultCallback<List<DCProjectInfo>>() {
            @Override
            public void onSuccess(List<DCProjectInfo> dcProjectInfos, BaseResult baseResult) {
                mDcProjectInfos = dcProjectInfos;
                if (LibCollections.isNotEmpty(dcProjectInfos)) {
                    showProjectDetails(dcProjectInfos.get(0));
                }
            }
        });
    }

    private void showChoiceProjectWindow(List<DCProjectInfo> dcProjectInfos) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.layout_dcproject_choice_window, null);
        PopupWindow popupWindow = new PopupWindow(contentView);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.setWidth(DisplayUtil.getDisplayMetrics().widthPixels - 50);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                actDcDutyDetailsTitleStv.setCenterTvDrawableRight(ContextCompat.getDrawable(getBaseContext(), R.mipmap.ic_dc_duty_details_arrow_bottom));
            }
        });
        actDcDutyDetailsTitleStv.setCenterTvDrawableRight(ContextCompat.getDrawable(getBaseContext(), R.mipmap.ic_dc_duty_details_arrow_up));
        dcProjectChoiceAdapter = new DCProjectChoiceAdapter(R.layout.item_dcproject_choice_window, dcProjectInfos);
        dcProjectChoiceAdapter.setOnItemClickListener((adapter, view, position) -> {
            popupWindow.dismiss();
            DCProjectInfo info = (DCProjectInfo) adapter.getItem(position);
            if (null != info) {
                showProjectDetails(info);
            }
        });
        RecyclerView projectChoiceRv = contentView.findViewById(R.id.layout_dcproject_choice_windown_rv);
        projectChoiceRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(this));
        projectChoiceRv.setAdapter(dcProjectChoiceAdapter);
        popupWindow.showAsDropDown(actDuDutyProjectDetailsTitleLl, 25, -15);
    }

    private void showProjectDetails(DCProjectInfo info) {
        actDcDutyDetailsTitleStv.setCenterString(info.getItemName());
        if (null != dcDutyProjectInfoFragment) {
            dcDutyProjectInfoFragment.refresh(info);
        }
        if (null != dcDutyProjectPatientListFragment) {
            dcDutyProjectPatientListFragment.refresh(info.getItemId());
        }
    }
}
