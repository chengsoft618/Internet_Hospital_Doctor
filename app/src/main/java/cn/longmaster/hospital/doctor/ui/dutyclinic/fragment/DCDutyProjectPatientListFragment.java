package cn.longmaster.hospital.doctor.ui.dutyclinic.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyPatientItemInfo;
import cn.longmaster.hospital.doctor.core.manager.dutyclinic.DCManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.dutyclinic.adapter.DCDutyPatientAdapter;
import cn.longmaster.utils.KeyboardUtils;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.StringUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/12/17 18:44
 * @description: 慢病项目我的患者
 */
public class DCDutyProjectPatientListFragment extends NewBaseFragment {

    private final int ROOT_FILTER_BY_NAME = 0;
    private final int ROOT_FILTER_BY_HOSPITAL = 1;
    private int ROOT_FILTER_TYPE = ROOT_FILTER_BY_NAME;
    private static String KEY_TO_QUERY_PROJECT_ID = "_KEY_TO_QUERY_PROJECT_ID_";
    @FindViewById(R.id.fg_dc_duty_project_patient_search_type_tv)
    private TextView fgDcDutyProjectPatientSearchTypeTv;
    @FindViewById(R.id.fg_dc_duty_project_patient_search_ll)
    private LinearLayout fgDcDutyProjectPatientSearchLl;
    @FindViewById(R.id.fg_dc_duty_project_patient_search_et)
    private EditText fgDcDutyProjectPatientSearchEt;
    @FindViewById(R.id.fg_dc_duty_project_patient_search_clear_iv)
    private ImageView fgDcDutyProjectPatientSearchClearIv;
    @FindViewById(R.id.fg_my_patient_consult_search_tv)
    private TextView fgMyPatientConsultSearchTv;
    @FindViewById(R.id.fg_dc_duty_project_patient_num_tv)
    private TextView fgDcDutyProjectPatientNumTv;
    @FindViewById(R.id.fg_dc_duty_project_patient_list_srl)
    private SmartRefreshLayout fgDcDutyProjectPatientListSrl;
    @FindViewById(R.id.fg_dc_duty_project_patient_list_rv)
    private RecyclerView fgDcDutyProjectPatientListRv;
    @AppApplication.Manager
    DCManager dcManager;
    private DCDutyPatientAdapter dcDutyPatientAdapter;
    private int mProjectId;

    public static DCDutyProjectPatientListFragment getInstance(int projectId) {
        DCDutyProjectPatientListFragment fragment = new DCDutyProjectPatientListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TO_QUERY_PROJECT_ID, projectId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void refresh(int projectId) {
        mProjectId = projectId;
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TO_QUERY_PROJECT_ID, projectId);
        setArguments(bundle);
        fgDcDutyProjectPatientListSrl.autoRefresh();
    }

    @Override
    protected void initDatas() {
        mProjectId = getProjectId();
        dcDutyPatientAdapter = new DCDutyPatientAdapter(R.layout.item_dc_duty_patient, new ArrayList<>(0));
        dcDutyPatientAdapter.setMyPatient(true);
        dcDutyPatientAdapter.setOnItemClickListener((adapter, view, position) -> {
            DCDutyPatientItemInfo info = (DCDutyPatientItemInfo) adapter.getItem(position);
            if (null != info) {
                if (info.isReaded()) {
                    getDisplay().startDCDutyPatientDetailActivity(info.getUserId(), getProjectId(), 0);
                } else {
                    dcManager.updatePatientDataState(info.getMedicalId(), new DefaultResultCallback<Void>() {
                        @Override
                        public void onSuccess(Void aVoid, BaseResult baseResult) {
                            getDisplay().startDCDutyPatientDetailActivity(info.getUserId(), getProjectId(), 0);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fg_dc_duty_project_patient_list;
    }

    @Override
    public void initViews(View rootView) {
        fgDcDutyProjectPatientListRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getBaseActivity()));
        fgDcDutyProjectPatientListRv.setAdapter(dcDutyPatientAdapter);
        initListener();
        fgDcDutyProjectPatientListSrl.autoRefresh();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            KeyboardUtils.hideSoftInput(getBaseActivity());
        }
    }

    private void initListener() {
        fgDcDutyProjectPatientSearchEt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                fgDcDutyProjectPatientListSrl.autoRefresh();
                KeyboardUtils.hideSoftInput(v);
                return true;
            }
            return false;
        });
        fgDcDutyProjectPatientSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fgDcDutyProjectPatientSearchClearIv.setVisibility(View.VISIBLE);
                } else {
                    fgDcDutyProjectPatientSearchClearIv.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fgMyPatientConsultSearchTv.setOnClickListener(v -> {
            KeyboardUtils.hideSoftInput(v);
            fgDcDutyProjectPatientListSrl.autoRefresh();
        });
        fgDcDutyProjectPatientSearchClearIv.setOnClickListener(v -> {
            fgDcDutyProjectPatientSearchEt.setText(null);
            fgDcDutyProjectPatientListSrl.autoRefresh();
        });
        fgDcDutyProjectPatientListSrl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                PAGE_INDEX++;
                getPatientList(mProjectId, refreshLayout);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                PAGE_INDEX = MIN_PAGE_INDEX_1;
                getPatientList(mProjectId, refreshLayout);
            }
        });
        fgDcDutyProjectPatientSearchTypeTv.setOnClickListener(v -> {
            showRootFilterPopupWindow();
        });
    }

    private void getPatientList(int projectId, RefreshLayout refreshLayout) {
        boolean isRefresh = PAGE_INDEX == MIN_PAGE_INDEX_1;
        if (StringUtils.isEmpty(getString(fgDcDutyProjectPatientSearchEt))) {
            dcManager.getPatientList(projectId, 1, PAGE_INDEX, PAGE_SIZE, new DefaultResultCallback<List<DCDutyPatientItemInfo>>() {
                @Override
                public void onSuccess(List<DCDutyPatientItemInfo> dcDutyPatientItemInfos, BaseResult baseResult) {
                    fgDcDutyProjectPatientNumTv.setText("总" + baseResult.getCount());
                    if (baseResult.isFinish()) {
                        fgDcDutyProjectPatientListSrl.finishLoadMoreWithNoMoreData();
                    }
                    if (isRefresh) {
                        if (LibCollections.isEmpty(dcDutyPatientItemInfos)) {
                            dcDutyPatientAdapter.setNewData(null);
                            dcDutyPatientAdapter.setEmptyView(createEmptyListView());
                        } else {
                            dcDutyPatientAdapter.setNewData(dcDutyPatientItemInfos);
                        }
                    } else {
                        dcDutyPatientAdapter.addData(dcDutyPatientItemInfos);
                    }
                }

                @Override
                public void onFinish() {
                    if (isRefresh) {
                        refreshLayout.finishRefresh();
                    } else {
                        refreshLayout.finishLoadMore();
                    }
                }
            });
        } else {
            dcManager.searchPatientList(projectId, 1, ROOT_FILTER_TYPE, getString(fgDcDutyProjectPatientSearchEt), new DefaultResultCallback<List<DCDutyPatientItemInfo>>() {
                @Override
                public void onSuccess(List<DCDutyPatientItemInfo> dcDutyPatientItemInfos, BaseResult baseResult) {
                    fgDcDutyProjectPatientNumTv.setText("总" + LibCollections.size(dcDutyPatientItemInfos));
                    if (LibCollections.isEmpty(dcDutyPatientItemInfos)) {
                        dcDutyPatientAdapter.setNewData(null);
                        dcDutyPatientAdapter.setEmptyView(createEmptyListView());
                    } else {
                        dcDutyPatientAdapter.setNewData(dcDutyPatientItemInfos);
                    }
                }

                @Override
                public void onFinish() {
                    refreshLayout.finishRefresh();
                    fgDcDutyProjectPatientListSrl.setEnableLoadMore(false);
                }
            });
        }
    }

    private int getProjectId() {
        return getArguments() == null ? 0 : getArguments().getInt(KEY_TO_QUERY_PROJECT_ID);
    }

    @SuppressLint("NewApi")
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
        tvFilterByName.setText("按姓名");
        tvFilterBySick.setText("按医院");
        tvFilterByName.setOnClickListener(v -> {
            fgDcDutyProjectPatientSearchTypeTv.setText("按姓名");
            fgDcDutyProjectPatientSearchEt.setHint("请输入患者姓名");
            ROOT_FILTER_TYPE = ROOT_FILTER_BY_NAME;
            mPopWindow.dismiss();
        });
        tvFilterBySick.setOnClickListener(v -> {
            fgDcDutyProjectPatientSearchTypeTv.setText("按医院");
            fgDcDutyProjectPatientSearchEt.setHint("请输入医院名称");
            ROOT_FILTER_TYPE = ROOT_FILTER_BY_HOSPITAL;
            mPopWindow.dismiss();
        });
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDismiss() {
                fgDcDutyProjectPatientSearchTypeTv.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.mipmap.ic_gray_arrow_down,0);
            }
        });
        fgDcDutyProjectPatientSearchTypeTv.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.mipmap.ic_gray_arrow_up,0);
        mPopWindow.showAsDropDown(fgDcDutyProjectPatientSearchTypeTv, 15, 0);
    }
}
