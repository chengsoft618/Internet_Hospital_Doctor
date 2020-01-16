package cn.longmaster.hospital.doctor.ui.college;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.college.DepartmentInfo;
import cn.longmaster.hospital.doctor.core.entity.college.DiseaseInfo;
import cn.longmaster.hospital.doctor.core.entity.college.GuideDataInfo;
import cn.longmaster.hospital.doctor.core.entity.college.YearInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.college.DepartmentListRequester;
import cn.longmaster.hospital.doctor.core.requests.college.GetGuideDataRequester;
import cn.longmaster.hospital.doctor.core.requests.college.GetYearRequester;
import cn.longmaster.hospital.doctor.ui.PDFViewActivity;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.college.adapter.DepartmentAdapter;
import cn.longmaster.hospital.doctor.ui.college.adapter.DiseaseAdapter;
import cn.longmaster.hospital.doctor.ui.college.adapter.GuideLiteratureAdapter;
import cn.longmaster.hospital.doctor.ui.college.adapter.YearAdapter;
import cn.longmaster.hospital.doctor.ui.user.BrowserActivity;
import cn.longmaster.hospital.doctor.view.AppActionBar;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.ToastUtils;

/**
 * 指南文库activity
 * <p>
 * Created by W·H·K on 2018/3/23.
 */
public class GuideLiteratureActivity extends NewBaseActivity {
    @FindViewById(R.id.activity_guide_literature_bar)
    private AppActionBar mAppActionBar;
    @FindViewById(R.id.activity_guide_literature_srl)
    private SmartRefreshLayout activityGuideLiteratureSrl;
    @FindViewById(R.id.activity_guide_literature_rv)
    private RecyclerView mRecyclerView;
    @FindViewById(R.id.activity_guide_literature_department_img)
    private ImageView mDepartmentImg;
    @FindViewById(R.id.activity_guide_literature_disease_img)
    private ImageView mDiseaseImg;
    @FindViewById(R.id.activity_guide_literature_year_img)
    private ImageView mYearImg;
    @FindViewById(R.id.activity_guide_literature_view)
    private View mScreenView;
    @FindViewById(R.id.activity_guide_literature_department_tv)
    private TextView mDepartmentTv;
    @FindViewById(R.id.activity_guide_literature_disease_tv)
    private TextView mDiseaseTv;
    @FindViewById(R.id.activity_guide_literature_year_tv)
    private TextView mYearTv;
    @FindViewById(R.id.activity_guide_empty_view)
    private LinearLayout mEmptyView;

    private boolean mIsOpenScreen = false;
    private PopupWindow mPopupWindow;
    private List<GuideDataInfo> mGuideDataInfos = new ArrayList<>();
    private GuideLiteratureAdapter mAdapter;
    private int mDepartmentId = 0;
    private int mPageIndex = 1;
    private int mPageSize = 20;
    private String mDiseaseName = "疾病";
    private DepartmentAdapter mDepartmentAdapter;
    private List<DepartmentInfo> mDepartmentInfos = new ArrayList<>();
    private String mDepartmentStr = "科室";
    private String mYearStr = "年份";
    private DiseaseAdapter mDiseaseAdapter;
    private List<DiseaseInfo> mDiseaseInfos = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private List<YearInfo> mYearInfos = new ArrayList<>();
    private YearAdapter mYearAdapter;
    private int mDepartmentPosition = -1;

    @Override
    protected void initDatas() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_guide_literature;
    }

    @Override
    protected void initViews() {
        initView();
        initData();
        initListener();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getThisActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new GuideLiteratureAdapter(R.layout.item_guide_literature, mGuideDataInfos);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        String moduleTitle = getIntent().getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_COLLEGE_TAB_NAME);
        mAppActionBar.setTitle(moduleTitle);
        showProgressDialog();
        activityGuideLiteratureSrl.autoRefresh();
    }

    private void initListener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            GuideDataInfo guideDataInfo = (GuideDataInfo) adapter.getItem(position);
            if (null == guideDataInfo) {
                return;
            }
            if (guideDataInfo.getContentUrl().endsWith(".pdf")) {
                PDFViewActivity.startActivity(getThisActivity(), guideDataInfo.getContentUrl(), guideDataInfo.getMaterialName());
            } else {
                Intent intent = new Intent(getThisActivity(), BrowserActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TITLE, guideDataInfo.getMaterialName());
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TITLE_NAME, true);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, guideDataInfo.getContentUrl());
                startActivity(intent);
            }
        });
        activityGuideLiteratureSrl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPageIndex++;
                getGuideData(mDepartmentId, mDiseaseName, mYearStr, mPageIndex, mPageSize, true, refreshLayout);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPageIndex = MIN_PAGE_INDEX_1;
                getGuideData(mDepartmentId, mDiseaseName, mYearStr, mPageIndex, mPageSize, false, refreshLayout);
            }
        });
    }

    private void getGuideData(int departmentId, String diseaseName, String year, int pageIndex, int pageSize, final boolean isLoadMore, RefreshLayout refreshLayout) {
        GetGuideDataRequester requester = new GetGuideDataRequester(new DefaultResultCallback<List<GuideDataInfo>>() {
            @Override
            public void onSuccess(List<GuideDataInfo> guideDataInfos, BaseResult baseResult) {
                if (baseResult.isFinish()) {
                    activityGuideLiteratureSrl.finishLoadMoreWithNoMoreData();
                }
                if (isLoadMore) {
                    mAdapter.addData(guideDataInfos);
                } else {
                    if (LibCollections.isEmpty(guideDataInfos)) {
                        mEmptyView.setVisibility(View.VISIBLE);
                    } else {
                        mEmptyView.setVisibility(View.GONE);
                    }
                    mGuideDataInfos = guideDataInfos;
                    mAdapter.setNewData(guideDataInfos);
                }
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                ToastUtils.showShort(R.string.no_network_connection);
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
                if (isLoadMore) {
                    refreshLayout.finishLoadMore();
                } else {
                    refreshLayout.finishRefresh();
                }
            }
        });
        requester.departmentId = departmentId;
        requester.diseaseName = diseaseName.contains("疾病") ? "" : mDiseaseName;
        requester.year = year.contains("年份") ? "" : mYearStr;
        requester.pageIndex = pageIndex;
        requester.pageSize = pageSize;
        requester.doPost();
    }

    @OnClick({R.id.activity_guide_literature_screen_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_guide_literature_screen_view:
                setScreenArrowIcon();
                showDialog();
                break;
        }
    }

    private void showDialog() {
        View contentView = LayoutInflater.from(getThisActivity()).inflate(R.layout.dialog_guide_literature_screen, null);
        mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        displayView(contentView, mPopupWindow);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setScreenArrowIcon();
            }
        });
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.showAsDropDown(mScreenView);
    }

    private void displayView(View contentView, final PopupWindow popupWindow) {
        final RecyclerView departmentRecyclerView = contentView.findViewById(R.id.dialog_guide_literature_department_recycler_view);
        final RecyclerView diseaseRecyclerView = contentView.findViewById(R.id.dialog_guide_literature_disease_recycler_view);
        final RecyclerView yearRecyclerView = contentView.findViewById(R.id.dialog_guide_literature_year_recycler_view);
        final TextView determineVtn = contentView.findViewById(R.id.dialog_guide_literature_determine_tv);
        final TextView departmentAllTv = contentView.findViewById(R.id.dialog_guide_literature_department_all_tv);
        final TextView diseaseAllTv = contentView.findViewById(R.id.dialog_guide_literature_disease_all_tv);
        final TextView yearAllTv = contentView.findViewById(R.id.dialog_guide_literature_year_all_tv);

        departmentRecyclerView.setLayoutManager(new LinearLayoutManager(getThisActivity()));
        diseaseRecyclerView.setLayoutManager(new LinearLayoutManager(getThisActivity()));
        yearRecyclerView.setLayoutManager(new LinearLayoutManager(getThisActivity()));
        setViewBackground(departmentAllTv, diseaseAllTv, yearAllTv);
        if (mDepartmentInfos.size() > 0) {
            displayScreenView(departmentRecyclerView, diseaseRecyclerView, yearRecyclerView, mDepartmentInfos, departmentAllTv, diseaseAllTv, yearAllTv);
        } else {
            final DepartmentListRequester requester = new DepartmentListRequester((baseResult, departmentInfos) -> {
                Logger.logD(Logger.COMMON, "GuideLiteratureActivity->DepartmentListRequester()->baseResult:" + baseResult + ",guideDataInfos:" + departmentInfos);
                if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS && departmentInfos.size() > 0) {
                    mDepartmentInfos = departmentInfos;
                    displayScreenView(departmentRecyclerView, diseaseRecyclerView, yearRecyclerView, departmentInfos, departmentAllTv, diseaseAllTv, yearAllTv);
                }
            });
            requester.ownType = 0;
            requester.doPost();
        }
        if (mYearInfos.size() > 0) {
            displayYearView(mYearInfos, yearRecyclerView, yearAllTv);
        } else {
            GetYearRequester yearRequester = new GetYearRequester((baseResult, yearInfos) -> {
                Logger.logD(Logger.COMMON, "GuideLiteratureActivity->DepartmentListRequester()->baseResult:" + baseResult + ",yearInfos:" + yearInfos);
                if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS && yearInfos.size() > 0) {
                    mYearInfos = yearInfos;
                    displayYearView(yearInfos, yearRecyclerView, yearAllTv);
                }
            });
            yearRequester.doPost();
        }
        determineVtn.setOnClickListener(v -> {
            showProgressDialog();
            boolean isselectDepartment = false;
            for (int i = 0; i < mDepartmentInfos.size(); i++) {
                if (mDepartmentInfos.get(i).isSelected()) {
                    isselectDepartment = true;
                    mDepartmentStr = mDepartmentInfos.get(i).getDepartmentName();
                    mDepartmentId = mDepartmentInfos.get(i).getDepartmentId();
                }
            }
            if (!isselectDepartment) {
                mDepartmentStr = getString(R.string.medical_college_department);
                mDepartmentId = 0;
            }
            boolean isselectDisease = false;
            for (int i = 0; i < mDiseaseInfos.size(); i++) {
                if (mDiseaseInfos.get(i).isSelected()) {
                    isselectDisease = true;
                    mDiseaseName = mDiseaseInfos.get(i).getDiseaseName();
                }
            }
            if (!isselectDisease) {
                mDiseaseName = getString(R.string.medical_college_disease);
            }
            boolean isselectYea = false;
            for (int i = 0; i < mYearInfos.size(); i++) {
                if (mYearInfos.get(i).isSelected()) {
                    isselectYea = true;
                    mYearStr = mYearInfos.get(i).getMatYear();
                }
            }
            if (!isselectYea) {
                mYearStr = getString(R.string.medical_college_year);
            }
            mDepartmentTv.setText(mDepartmentStr);
            mDiseaseTv.setText(mDiseaseName);
            mYearTv.setText(mYearStr);
            popupWindow.dismiss();
            activityGuideLiteratureSrl.autoRefresh();
        });
    }

    private void displayDiseaseView(RecyclerView diseaseRecyclerView, List<DepartmentInfo> departmentInfos) {
        if (mDiseaseAdapter != null && mDepartmentPosition != -1 && mDiseaseInfos != null) {
            mDiseaseInfos.clear();
            for (String str : departmentInfos.get(mDepartmentPosition).getDiseases()) {
                DiseaseInfo diseaseInfo = new DiseaseInfo();
                diseaseInfo.setDiseaseName(str);
                if (mDiseaseName.equals(str)) {
                    diseaseInfo.setSelected(true);
                }
                mDiseaseInfos.add(diseaseInfo);
            }
            mDiseaseAdapter.notifyDataSetChanged();
        }
    }

    private void setViewBackground(TextView departmentAllTv, TextView diseaseAllTv, TextView yearAllTv) {
        mDepartmentTv.setText(mDepartmentStr);
        mDiseaseTv.setText(mDiseaseName);
        mYearTv.setText(mYearStr);
        if (mDepartmentStr.contains(getString(R.string.medical_college_department))) {
            departmentAllTv.setBackgroundResource(R.color.color_ebeff0);
            departmentAllTv.setTextColor(getCompatColor(R.color.color_45aef8));
        } else {
            departmentAllTv.setBackgroundResource(R.color.white);
            departmentAllTv.setTextColor(getCompatColor(R.color.color_666666));
        }
        if (mDiseaseName.contains(getString(R.string.medical_college_disease))) {
            diseaseAllTv.setBackgroundResource(R.color.color_ebeff0);
            diseaseAllTv.setTextColor(getCompatColor(R.color.color_45aef8));
        } else {
            diseaseAllTv.setBackgroundResource(R.color.white);
            diseaseAllTv.setTextColor(getCompatColor(R.color.color_666666));
        }
        if (mYearStr.contains(getString(R.string.medical_college_year))) {
            yearAllTv.setBackgroundResource(R.color.color_ebeff0);
            yearAllTv.setTextColor(getCompatColor(R.color.color_45aef8));
        } else {
            yearAllTv.setBackgroundResource(R.color.white);
            yearAllTv.setTextColor(getCompatColor(R.color.color_666666));
        }
    }

    private void displayYearView(final List<YearInfo> yearInfos, RecyclerView yearRecyclerView, final TextView yearAllTv) {
        mYearAdapter = new YearAdapter(getThisActivity(), yearInfos);
        yearRecyclerView.setAdapter(mYearAdapter);
        mYearAdapter.setOnItemClickListener((view, position) -> {
            for (int i = 0; i < yearInfos.size(); i++) {
                if (i == position) {
                    yearInfos.get(i).setSelected(true);
                } else {
                    yearInfos.get(i).setSelected(false);
                }
            }
            yearAllTv.setBackgroundResource(R.color.color_white);
            yearAllTv.setTextColor(getCompatColor(R.color.color_666666));
            mYearAdapter.notifyDataSetChanged();
        });
    }

    private void displayScreenView(RecyclerView departmentRecyclerView, final RecyclerView diseaseRecyclerView, RecyclerView yearRecyclerView, final List<DepartmentInfo> departmentInfos, final TextView departmentAllTv, final TextView diseaseAllTv, final TextView yearAllTv) {
        mDepartmentAdapter = new DepartmentAdapter(getThisActivity(), departmentInfos);
        departmentRecyclerView.setAdapter(mDepartmentAdapter);
        mDiseaseAdapter = new DiseaseAdapter(getThisActivity(), mDiseaseInfos);
        diseaseRecyclerView.setAdapter(mDiseaseAdapter);
        displayDiseaseView(diseaseRecyclerView, departmentInfos);
        mDepartmentAdapter.setOnItemClickListener((view, position) -> {
            mDiseaseInfos.clear();
            for (String str : departmentInfos.get(position).getDiseases()) {
                DiseaseInfo diseaseInfo = new DiseaseInfo();
                diseaseInfo.setDiseaseName(str);
                mDiseaseInfos.add(diseaseInfo);
            }
            mDiseaseAdapter.notifyDataSetChanged();
            for (int i = 0; i < departmentInfos.size(); i++) {
                if (i == position) {
                    departmentInfos.get(i).setSelected(true);
                } else {
                    departmentInfos.get(i).setSelected(false);
                }
            }
            departmentAllTv.setBackgroundResource(R.color.color_white);
            departmentAllTv.setTextColor(getCompatColor(R.color.color_666666));
            mDepartmentAdapter.notifyDataSetChanged();
        });
        mDiseaseAdapter.setOnItemClickListener((view, position) -> {
            for (int i = 0; i < mDiseaseInfos.size(); i++) {
                if (i == position) {
                    mDiseaseInfos.get(i).setSelected(true);
                } else {
                    mDiseaseInfos.get(i).setSelected(false);
                }
            }
            diseaseAllTv.setBackgroundResource(R.color.color_white);
            diseaseAllTv.setTextColor(getCompatColor(R.color.color_666666));
            mDiseaseAdapter.notifyDataSetChanged();
        });

        departmentAllTv.setOnClickListener(v -> {
            departmentAllTv.setBackgroundResource(R.color.color_ebeff0);
            departmentAllTv.setTextColor(getCompatColor(R.color.color_45aef8));
            for (int i = 0; i < departmentInfos.size(); i++) {
                departmentInfos.get(i).setSelected(false);
            }
            mDepartmentAdapter.notifyDataSetChanged();
            if (mDiseaseAdapter != null) {
                mDiseaseInfos.clear();
                mDiseaseAdapter.notifyDataSetChanged();
            }
        });
        diseaseAllTv.setOnClickListener(v -> {
            diseaseAllTv.setBackgroundResource(R.color.color_ebeff0);
            diseaseAllTv.setTextColor(getCompatColor(R.color.color_45aef8));
            if (mDiseaseInfos.size() > 0) {
                for (int i = 0; i < mDiseaseInfos.size(); i++) {
                    mDiseaseInfos.get(i).setSelected(false);
                }
                mDiseaseAdapter.notifyDataSetChanged();
            }
        });
        yearAllTv.setOnClickListener(v -> {
            yearAllTv.setBackgroundResource(R.color.color_ebeff0);
            yearAllTv.setTextColor(getCompatColor(R.color.color_45aef8));
            if (mYearInfos.size() > 0) {
                for (int i = 0; i < mYearInfos.size(); i++) {
                    mYearInfos.get(i).setSelected(false);
                }
                mYearAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setScreenArrowIcon() {
        if (mIsOpenScreen) {
            mIsOpenScreen = false;
            mDepartmentImg.setImageResource(R.drawable.ic_recommend_expert_down);
            mDiseaseImg.setImageResource(R.drawable.ic_recommend_expert_down);
            mYearImg.setImageResource(R.drawable.ic_recommend_expert_down);
        } else {
            mIsOpenScreen = true;
            mDepartmentImg.setImageResource(R.drawable.ic_recommend_expert_up);
            mDiseaseImg.setImageResource(R.drawable.ic_recommend_expert_up);
            mYearImg.setImageResource(R.drawable.ic_recommend_expert_up);
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = createProgressDialog(getString(R.string.loading));
        } else if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
