package cn.longmaster.hospital.doctor.ui.rounds.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorItemInfo;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.doctor.GetDoctorListNewRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.doctor.adapter.DoctorListNewAdapter;
import cn.longmaster.hospital.doctor.util.PopupWindowHelper;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;


/**
 * 推荐专家fragemnt
 * Created by W·H·K on 2018/5/7.
 * mod by biao on2019/6/11
 */
public class MoreExpertFragment extends NewBaseFragment implements CompoundButton.OnCheckedChangeListener {
    private final int REQUEST_CODE_CHOICE_DOCTOR = 100;
    private final int REQUEST_CODE_FILTER_HOSPITAL = 102;
    private final int REQUEST_CODE_FILTER_DEPARTMENT = 104;
    //按名字搜索
    private final int REQUEST_CODE_SEARCH_CONTENT = 200;

    private int ROOT_FILTER_BY_NAME = 1234;
    private int ROOT_FILTER_BY_SICK = 1235;
    private int ROOT_FILTER_TYPE = ROOT_FILTER_BY_NAME;
    //接诊次数
    private final int SORT_BY_VISIT = 2;
    //医院排名
    private final int SORT_BY_HOSPITAL = 3;
    //评分
    private final int SORT_BY_RECOMMEND = 1;

    private int mSort = 0;
    private DoctorListNewAdapter mDoctorListAdapter;
    private int pageIndex = 0;
    private int pageSize = 20;
    //医院筛选
    private List<String> mHospitalName = new ArrayList<>();
    //部门筛选
    private List<String> mDepartmentName = new ArrayList<>();
    //接诊时间
    private List<String> mReceptionDateTime = new ArrayList<>();
    private int mFilterFudanSort;
    private int mFilterMinPrice;
    private int mFilterMaxPrice;
    //接诊类型
    private int mFilterReceptionType;

    private String mFilterName;
    private String mFilterSick;

    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    @FindViewById(R.id.fragment_doctor_list_main_ll)
    private LinearLayout fragmentDoctorListMainLl;
    @FindViewById(R.id.include_doctor_list_title_root_filter_stv)
    private SuperTextView includeDoctorListTitleRootFilterStv;
    @FindViewById(R.id.include_doctor_list_title_search_stv)
    private SuperTextView includeDoctorListTitleSearchStv;
    @FindViewById(R.id.include_doctor_list_title_search_cancel_tv)
    private TextView includeDoctorListTitleSearchCancelTv;
    @FindViewById(R.id.include_order_by_visit_cb)
    private CheckBox includeOrderByVisitCb;
    @FindViewById(R.id.include_order_by_evaluation_cb)
    private CheckBox includeOrderByEvaluationCb;
    @FindViewById(R.id.include_order_by_hospital_cb)
    private CheckBox includeOrderByHospitalCb;
    @FindViewById(R.id.include_filter_stv)
    private SuperTextView includeFilterStv;
    @FindViewById(R.id.fragment_rounds_more_expert_content_fl)
    private FrameLayout fragmentRoundsMoreExpertContentFl;
    @FindViewById(R.id.fragment_rounds_more_expert_srl)
    private SmartRefreshLayout fragmentRoundsMoreExpertSrl;
    @FindViewById(R.id.fragment_rounds_more_expert_rv)
    private RecyclerView fragmentRoundsMoreExpertRv;
    @FindViewById(R.id.include_new_no_data_ll)
    private LinearLayout includeNewNoDataLl;
    @FindViewById(R.id.include_new_no_data_tv)
    private TextView includeNewNoDataTv;
    private PopupWindowHelper.DoctorFilterBuilder doctorFilterPopWindowBuilder;

    public static MoreExpertFragment getInstance() {
        MoreExpertFragment moreExpertFragment = new MoreExpertFragment();
        return moreExpertFragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_rounds_more_expert;
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        mDoctorListAdapter = new DoctorListNewAdapter(R.layout.item_doctor_list_layout_test, new ArrayList<>(0));
        mDoctorListAdapter.setOnItemClickListener((adapter, view, position) -> {
            DoctorItemInfo doctorItemInfo = (DoctorItemInfo) adapter.getItem(position);
            if (null != doctorItemInfo) {
                if (mUserInfoManager.getCurrentUserInfo().getUserId() == doctorItemInfo.getDoctorId()) {
                    ToastUtils.showShort(getString(R.string.rounds_info_not_to_oneself));
                    return;
                }
                if (doctorItemInfo.getDoctorId() > 0) {
                    getDisplay().startSelectionTimeActivity(doctorItemInfo.getDoctorId(), false, null, REQUEST_CODE_CHOICE_DOCTOR);
                } else {
                    finishWithResult(doctorItemInfo.getDoctorId(), null);
                }
            }
        });
        mDoctorListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            DoctorItemInfo doctorItemInfo = (DoctorItemInfo) adapter.getItem(position);
            if (null != doctorItemInfo) {
                getDisplay().startDoctorDetailActivity(doctorItemInfo.getDoctorId(), true, true, REQUEST_CODE_CHOICE_DOCTOR);
            }
        });
    }

    @Override
    public void initViews(View rootView) {
        fragmentRoundsMoreExpertRv.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        fragmentRoundsMoreExpertRv.setAdapter(mDoctorListAdapter);
        initListener();
        fragmentRoundsMoreExpertSrl.autoRefresh();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_FILTER_HOSPITAL:
                    String hospitalName = data.getStringExtra("hospital");
                    if (null != doctorFilterPopWindowBuilder) {
                        doctorFilterPopWindowBuilder.addHospital(hospitalName);
                    }
                    break;
                case REQUEST_CODE_FILTER_DEPARTMENT:
                    String departmentName = data.getStringExtra("department");
                    if (null != doctorFilterPopWindowBuilder) {
                        doctorFilterPopWindowBuilder.addDepartment(departmentName);
                    }
                    break;
                case REQUEST_CODE_SEARCH_CONTENT:
                    String result = data.getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SEARCH_CONTENT);
                    if (ROOT_FILTER_TYPE == ROOT_FILTER_BY_NAME) {
                        mFilterName = result;
                        mFilterSick = null;
                    } else {
                        mFilterName = null;
                        mFilterSick = result;
                    }
                    initRootFilter();
                    includeDoctorListTitleSearchStv.setLeftString(result);
                    fragmentRoundsMoreExpertSrl.autoRefresh();
                    break;
                case REQUEST_CODE_CHOICE_DOCTOR:
                    finishWithResult(data.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_DOCTOR_ID, 0), data.getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_INTENTION_TIME_LIST));
                    break;
                default:
                    break;
            }
        }
    }

    private void finishWithResult(int doctorId, ArrayList<String> timeList) {
        Intent intent = new Intent();
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_DOCTOR_ID, doctorId);
        intent.putStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_INTENTION_TIME_LIST, timeList);
        getBaseActivity().setResult(Activity.RESULT_OK, intent);
        getBaseActivity().finish();
    }

    private void initListener() {
        includeOrderByVisitCb.setOnCheckedChangeListener(this);
        includeOrderByEvaluationCb.setOnCheckedChangeListener(this);
        includeOrderByHospitalCb.setOnCheckedChangeListener(this);
        includeDoctorListTitleRootFilterStv.setOnClickListener(v -> showRootFilterPopupWindow());
        includeDoctorListTitleSearchStv.setOnClickListener(v -> {
            getDisplay().startDoctorSearchActivity(ROOT_FILTER_TYPE, REQUEST_CODE_SEARCH_CONTENT);
        });
        includeDoctorListTitleSearchCancelTv.setOnClickListener(v -> {
            initRootFilter();
            initSearch();
            fragmentRoundsMoreExpertSrl.autoRefresh();
        });

        includeFilterStv.setOnClickListener(view -> {
            includeFilterStv.setRightTextColor(getCompatColor(R.color.color_049eff));
            includeFilterStv.setRightTvDrawableRight(ContextCompat.getDrawable(getBaseActivity(), R.mipmap.ic_doctor_list_filter_sel));
            int identity = AppApplication.getInstance().getCurrentDoctorIdentity();
            if (doctorFilterPopWindowBuilder == null) {
                doctorFilterPopWindowBuilder = new PopupWindowHelper.DoctorFilterBuilder()
                        .setActivity(getBaseActivity())
                        .setFilterReceptionType(mFilterReceptionType)
                        .setFilterFudanSort(mFilterFudanSort)
                        .setFilterMinPrice(mFilterMinPrice)
                        .setFilterMaxPrice(mFilterMaxPrice)
                        .setFilterDepartments(mDepartmentName)
                        .setFilterReceptionTime(mReceptionDateTime)
                        .setFilterHospitals(mHospitalName)
                        .setParent(fragmentRoundsMoreExpertContentFl)
                        .setHeight(fragmentRoundsMoreExpertContentFl.getHeight())
                        .setIdentity((identity & 32) != 0)
                        .setOnDismissListener(() -> updateFilterState())
                        .setOnDepartmentChooseListener(v -> {
                            getDisplay().startDoctorFilterByDepartmentActivity(REQUEST_CODE_FILTER_DEPARTMENT);
                        })
                        .setOnHospitalChooseListener(v -> {
                            getDisplay().startHospitalFilterByCityActivity(REQUEST_CODE_FILTER_HOSPITAL);
                        })
                        .setOnTimeChooseListener(v -> {
                            showTimeFilterPopupWindow(doctorFilterPopWindowBuilder);
                        })
                        .setOnFilterChangeListener((filterMinPrice, filterMaxPrice, filterReceptionType, filterFudanSort, hospitals, departments, receptionTime) -> {
                            mFilterMinPrice = filterMinPrice;
                            mFilterMaxPrice = filterMaxPrice;
                            mFilterReceptionType = filterReceptionType;
                            mFilterFudanSort = filterFudanSort;
                            mDepartmentName = departments;
                            mHospitalName = hospitals;
                            mReceptionDateTime = receptionTime;
                            mDoctorListAdapter.setNewData(null);
                            fragmentRoundsMoreExpertSrl.autoRefresh();
                        });
            }
            doctorFilterPopWindowBuilder.build();
        });

        fragmentRoundsMoreExpertSrl.setOnRefreshListener(refreshLayout -> {
            pageIndex = 0;
            fragmentRoundsMoreExpertRv.scrollToPosition(0);
            getDoctorList(true, refreshLayout);
        });
        fragmentRoundsMoreExpertSrl.setOnLoadMoreListener(refreshLayout -> {
            pageIndex = LibCollections.size(mDoctorListAdapter.getData());
            getDoctorList(false, refreshLayout);
        });
        includeNewNoDataTv.setOnClickListener(v -> {
            getBaseActivity().setResult(Activity.RESULT_OK);
            getBaseActivity().finish();
        });
    }

    /**
     * 获取全部医生列表
     */
    private void getDoctorList(boolean isRefresh, RefreshLayout refreshLayout) {
        GetDoctorListNewRequester requester = new GetDoctorListNewRequester(new DefaultResultCallback<List<DoctorItemInfo>>() {
            @Override
            public void onSuccess(List<DoctorItemInfo> doctorItemInfos, BaseResult baseResult) {
                if (LibCollections.isNotEmpty(doctorItemInfos)) {
                    includeNewNoDataLl.setVisibility(View.GONE);
                    if (pageIndex != 0) {
                        mDoctorListAdapter.addData(doctorItemInfos);
                    } else {
                        mDoctorListAdapter.setNewData(doctorItemInfos);
                    }
                } else {
                    if (pageIndex == 0) {
                        includeNewNoDataLl.setVisibility(View.VISIBLE);
                        mDoctorListAdapter.setNewData(null);
                    }
                }
                if (baseResult.isFinish()) {
                    fragmentRoundsMoreExpertSrl.finishLoadMoreWithNoMoreData();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (isRefresh) {
                    refreshLayout.finishRefresh();
                } else {
                    refreshLayout.finishLoadMore();
                }
            }
        });
        requester.setReceptionDt(mReceptionDateTime);
        requester.setDepartment(mDepartmentName);
        requester.setHospital(mHospitalName);
        if (mFilterMinPrice != 0) {
            requester.setMinPrice(mFilterMinPrice);
        }
        if (mFilterMaxPrice != 0) {
            requester.setMaxPrice(mFilterMaxPrice);
        }
        if (mFilterReceptionType != 0) {
            requester.setReceptionType(mFilterReceptionType);
        }
        if (!StringUtils.isTrimEmpty(mFilterSick)) {
            requester.setIllness(mFilterSick);
        }
        if (!StringUtils.isTrimEmpty(mFilterName)) {
            requester.setRealName(mFilterName);
        }
        if (0 != mFilterFudanSort) {
            requester.setHospitalSort(mFilterFudanSort);
        }
        requester.setPageIndex(pageIndex);
        requester.setPageSize(pageSize);
        requester.setSort(mSort);
        requester.start();
    }

    private void showRootFilterPopupWindow() {
        View contentView = LayoutInflater.from(getBaseActivity()).inflate(R.layout.pop_win_doctor_list_filter_root, null);
        final PopupWindow mPopWindow = new PopupWindow(contentView);
        mPopWindow.setFocusable(true);
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.update();
        mPopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tvFilterByName = contentView.findViewById(R.id.dialog_doctor_list_filter_by_name);
        TextView tvFilterBySick = contentView.findViewById(R.id.dialog_doctor_list_filter_by_sick);
        tvFilterByName.setOnClickListener(v -> {
            includeDoctorListTitleRootFilterStv.setLeftString("按姓名");
            includeDoctorListTitleSearchStv.setLeftString("输入医生姓名查找");
            ROOT_FILTER_TYPE = ROOT_FILTER_BY_NAME;
            mPopWindow.dismiss();
        });
        tvFilterBySick.setOnClickListener(v -> {
            includeDoctorListTitleRootFilterStv.setLeftString("按疾病");
            includeDoctorListTitleSearchStv.setLeftString("输入疾病名称查找");
            ROOT_FILTER_TYPE = ROOT_FILTER_BY_SICK;
            mPopWindow.dismiss();
        });
        mPopWindow.showAsDropDown(includeDoctorListTitleRootFilterStv);
    }

    private void showTimeFilterPopupWindow(PopupWindowHelper.DoctorFilterBuilder doctorFilterPopWindowBuilder) {
        new PopupWindowHelper.DateSelectBuilder()
                .setActivity(getBaseActivity())
                .setDayItems(Arrays.asList(getBaseActivity().getResources().getStringArray(R.array.week_no_weekend)))
                .setTimeItems(Arrays.asList(getBaseActivity().getResources().getStringArray(R.array.all_day_desc)))
                .setOnDateSelectListener(new PopupWindowHelper.OnDateSelectListener() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onCommit(String selectDate, String selectTime) {
                        doctorFilterPopWindowBuilder.addReceptionTime(selectDate + " " + selectTime);
                    }
                })
                .build();
    }

    private void initRootFilter() {
        initFilter();
        initSortType();
    }

    private void initFilter() {
        mDepartmentName.clear();
        mHospitalName.clear();
        mReceptionDateTime.clear();
        mFilterReceptionType = 0;
        mFilterMinPrice = 0;
        mFilterMaxPrice = 0;
        mFilterFudanSort = 0;
        updateFilterState();
    }

    private void initSortType() {
        includeOrderByVisitCb.setChecked(false);
        includeOrderByEvaluationCb.setChecked(false);
        includeOrderByHospitalCb.setChecked(false);
        mSort = 0;
    }

    private void initSearch() {
        mFilterName = null;
        mFilterSick = null;
        if (ROOT_FILTER_TYPE == ROOT_FILTER_BY_NAME) {
            includeDoctorListTitleSearchStv.setLeftString("请输入医生姓名查找");
        } else {
            includeDoctorListTitleSearchStv.setLeftString("请输入疾病名称查找");
        }
    }

    private boolean hasFilter() {
        return mFilterReceptionType != 0 || mFilterFudanSort != 0 || mFilterMinPrice != 0 || mFilterMaxPrice != 0
                || !LibCollections.isEmpty(mHospitalName) || !LibCollections.isEmpty(mDepartmentName) || !LibCollections.isEmpty(mReceptionDateTime);
    }

    /**
     * 已有筛选
     */
    private void updateFilterState() {
        if (hasFilter()) {
            includeFilterStv.setRightTextColor(getCompatColor(R.color.color_049eff));
            includeFilterStv.setRightTvDrawableRight(ContextCompat.getDrawable(getBaseActivity(), R.mipmap.ic_doctor_list_filter_sel));
        } else {
            includeFilterStv.setRightTextColor(getCompatColor(R.color.color_1a1a1a));
            includeFilterStv.setRightTvDrawableRight(ContextCompat.getDrawable(getBaseActivity(), R.mipmap.ic_doctor_list_filter_pre));
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.include_order_by_visit_cb:
                    if (includeOrderByEvaluationCb.isChecked()) {
                        includeOrderByEvaluationCb.setChecked(false);
                    }
                    if (includeOrderByHospitalCb.isChecked()) {
                        includeOrderByHospitalCb.setChecked(false);
                    }
                    mSort = SORT_BY_VISIT;
                    break;
                case R.id.include_order_by_evaluation_cb:
                    if (includeOrderByVisitCb.isChecked()) {
                        includeOrderByVisitCb.setChecked(false);
                    }
                    if (includeOrderByHospitalCb.isChecked()) {
                        includeOrderByHospitalCb.setChecked(false);
                    }
                    mSort = SORT_BY_RECOMMEND;
                    break;
                case R.id.include_order_by_hospital_cb:
                    if (includeOrderByEvaluationCb.isChecked()) {
                        includeOrderByEvaluationCb.setChecked(false);
                    }
                    if (includeOrderByVisitCb.isChecked()) {
                        includeOrderByVisitCb.setChecked(false);
                    }
                    mSort = SORT_BY_HOSPITAL;
                    break;
                default:
                    break;
            }
        } else {
            mSort = 0;
        }
        fragmentRoundsMoreExpertSrl.autoRefresh();
    }
}
