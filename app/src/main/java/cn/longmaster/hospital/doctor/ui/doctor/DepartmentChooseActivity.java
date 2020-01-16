package cn.longmaster.hospital.doctor.ui.doctor;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allen.library.SuperTextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.department.DepartmentInfo;
import cn.longmaster.hospital.doctor.core.entity.department.DepartmentItemInfo;
import cn.longmaster.hospital.doctor.core.manager.common.HospitalManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.department.DepartmentSearchActivity;
import cn.longmaster.hospital.doctor.ui.doctor.adapter.DepartmentLeftAdapter;
import cn.longmaster.hospital.doctor.ui.doctor.adapter.DepartmentRightAdapter;
import cn.longmaster.hospital.doctor.view.MyStatusBar;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.StringUtils;

/**
 * @author lm_Abiao
 * @date 2019/5/29 20:00
 * @description: 通过不能筛选医生界面
 */
public class DepartmentChooseActivity extends NewBaseActivity {
    private DepartmentLeftAdapter leftAdapter;
    private DepartmentRightAdapter rightAdapter;
    private final int REQUEST_CODE_FOR_SEARCH_DEPARTMENT = 1024;
    @FindViewById(R.id.act_department_choose_msb)
    private MyStatusBar actDepartmentChooseMsb;
    @FindViewById(R.id.doctor_list_search_title_back_iv)
    private ImageView doctorListSearchTitleBackIv;
    @FindViewById(R.id.fragment_doctor_list_search_stv)
    private SuperTextView fragmentDoctorListSearchStv;
    @FindViewById(R.id.act_department_choose_result_ll)
    private LinearLayout actDepartmentChooseResultLl;
    @FindViewById(R.id.act_department_choose_first_tv)
    private TextView actDepartmentChooseFirstTv;
    @FindViewById(R.id.act_department_choose_second_tv)
    private TextView actDepartmentChooseSecondTv;
    @FindViewById(R.id.act_department_choose_result_indicator_v)
    private View actDepartmentChooseResultIndicatorV;
    @FindViewById(R.id.act_department_choose_first_rv)
    private RecyclerView actDepartmentChooseFirstRv;
    @FindViewById(R.id.act_department_choose_second_rv)
    private RecyclerView actDepartmentChooseSecondRv;

    @AppApplication.Manager
    HospitalManager hospitalManager;
    private final String ALL_DEPARTMENT = "全部";
    private final int TAB_INDEX_FIRST_DEPARTMENT = 0;
    private final int TAB_INDEX_SECOND_DEPARTMENT = 1;
    private int tabIndex = TAB_INDEX_FIRST_DEPARTMENT;

    @Override
    protected void initDatas() {
        leftAdapter = new DepartmentLeftAdapter(R.layout.item_double_click_branch_big, new ArrayList<>(0));
        rightAdapter = new DepartmentRightAdapter(R.layout.item_double_click_branch_small, new ArrayList<>());
        leftAdapter.setOnItemClickListener((adapter, view, position) -> {
            leftAdapter.setSelected(position);
            DepartmentItemInfo departmentItemInfo = (DepartmentItemInfo) adapter.getItem(position);
            clickFirstDepartment(departmentItemInfo);
        });
        rightAdapter.setOnItemClickListener((adapter, view, position) -> {
            DepartmentInfo department = (DepartmentInfo) adapter.getItem(position);
            if (null != department) {
                rightAdapter.setSelected(position);
                setBackData(department.getDepartmentName());
            }
        });
    }

    private void clickFirstDepartment(DepartmentItemInfo departmentItemInfo) {
        tabIndex = TAB_INDEX_SECOND_DEPARTMENT;
        updateTabVisibility();
        updateContentVisibility();
        updateIndicator();
        if (null != departmentItemInfo) {
            actDepartmentChooseFirstTv.setText(departmentItemInfo.getParentDepartment());
            if (StringUtils.equals(ALL_DEPARTMENT, departmentItemInfo.getParentDepartment())) {
                setBackData(ALL_DEPARTMENT);
            } else {
                if (LibCollections.isNotEmpty(departmentItemInfo.getDepartmentList())) {
                    rightAdapter.setNewData(departmentItemInfo.getDepartmentList());
                    rightAdapter.setSelected(0);
                } else {
                    setBackData(departmentItemInfo.getParentDepartment());
                }
            }
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_department_choose;
    }

    @Override
    protected void initViews() {
        actDepartmentChooseFirstRv.setAdapter(leftAdapter);
        actDepartmentChooseFirstRv.setLayoutManager(new LinearLayoutManager(getThisActivity()));
        actDepartmentChooseSecondRv.setAdapter(rightAdapter);
        actDepartmentChooseSecondRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        fragmentDoctorListSearchStv.setCenterString("请输入科室名称");
        updateTabVisibility();
        updateContentVisibility();
        updateIndicator();
        initListener();
        getDepartmentList();
    }

    private void initListener() {
        doctorListSearchTitleBackIv.setOnClickListener(v -> onBackPressed());
        fragmentDoctorListSearchStv.setOnClickListener(v -> {
            Intent intent = new Intent(DepartmentChooseActivity.this, DepartmentSearchActivity.class);
            startActivityForResult(intent, REQUEST_CODE_FOR_SEARCH_DEPARTMENT);
        });
        actDepartmentChooseFirstTv.setOnClickListener(v -> {
            tabIndex = TAB_INDEX_FIRST_DEPARTMENT;
            updateTabVisibility();
            updateContentVisibility();
            updateIndicator();
        });
        actDepartmentChooseSecondTv.setOnClickListener(v -> {
            tabIndex = TAB_INDEX_SECOND_DEPARTMENT;
            updateTabVisibility();
            updateContentVisibility();
            updateIndicator();
        });
    }

    private void getDepartmentList() {
        hospitalManager.getDeparmentList(new DefaultResultCallback<List<DepartmentItemInfo>>() {
            @Override
            public void onSuccess(List<DepartmentItemInfo> departmentItemInfos, BaseResult baseResult) {
                leftAdapter.setNewData(departmentItemInfos);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            if (REQUEST_CODE_FOR_SEARCH_DEPARTMENT == requestCode) {
                setBackData(data.getStringExtra("department"));
            }
        }
    }

    private void setBackData(String departmentName) {
        Intent intent = new Intent();
        intent.putExtra("department", departmentName);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void updateTabVisibility() {
        int firstVisibility;
        int secondVisibility;
        int firstDepartmentSize;
        int secondDepartmentSize;
        if (tabIndex == TAB_INDEX_FIRST_DEPARTMENT) {
            actDepartmentChooseFirstTv.setText("请选择");
            firstVisibility = View.VISIBLE;
            secondVisibility = View.GONE;
            firstDepartmentSize = 18;
            secondDepartmentSize = 16;
        } else {
            firstVisibility = View.VISIBLE;
            secondVisibility = View.VISIBLE;
            firstDepartmentSize = 16;
            secondDepartmentSize = 18;
        }
        actDepartmentChooseFirstTv.setVisibility(firstVisibility);
        actDepartmentChooseFirstTv.setTextSize(firstDepartmentSize);
        actDepartmentChooseSecondTv.setVisibility(secondVisibility);
        actDepartmentChooseSecondTv.setTextSize(secondDepartmentSize);
    }

    private void updateContentVisibility() {
        int firstVisibility;
        int secondVisibility;
        if (tabIndex == TAB_INDEX_FIRST_DEPARTMENT) {
            firstVisibility = View.VISIBLE;
            secondVisibility = View.GONE;
        } else {
            firstVisibility = View.GONE;
            secondVisibility = View.VISIBLE;
        }
        actDepartmentChooseFirstRv.setVisibility(firstVisibility);
        actDepartmentChooseSecondRv.setVisibility(secondVisibility);
    }

    private void updateIndicator() {
        actDepartmentChooseResultIndicatorV.post(() -> {
            switch (tabIndex) {
                case TAB_INDEX_FIRST_DEPARTMENT: //省份
                    buildIndicatorAnimatorTowards(actDepartmentChooseFirstTv).start();
                    break;
                case TAB_INDEX_SECOND_DEPARTMENT: //城市
                    buildIndicatorAnimatorTowards(actDepartmentChooseSecondTv).start();
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * tab 来回切换的动画
     *
     * @param tab
     * @return
     */
    private AnimatorSet buildIndicatorAnimatorTowards(TextView tab) {
        ObjectAnimator xAnimator = ObjectAnimator.ofFloat(actDepartmentChooseResultIndicatorV, "X", actDepartmentChooseResultIndicatorV.getX(), tab.getX());
        final ViewGroup.LayoutParams params = actDepartmentChooseResultIndicatorV.getLayoutParams();
        ValueAnimator widthAnimator = ValueAnimator.ofInt(params.width, tab.getMeasuredWidth());
        widthAnimator.addUpdateListener(animation -> {
            params.width = (int) animation.getAnimatedValue();
            actDepartmentChooseResultIndicatorV.setLayoutParams(params);
        });

        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new FastOutSlowInInterpolator());
        set.playTogether(xAnimator, widthAnimator);
        return set;
    }
}
