package cn.longmaster.hospital.doctor.ui.dutyclinic;

import android.support.annotation.NonNull;
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
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDoctorInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDoctorSectionInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCProjectDoctorListInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCProjectInfo;
import cn.longmaster.hospital.doctor.core.manager.dutyclinic.DCManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.dutyclinic.adapter.DCDoctorListAdapter;
import cn.longmaster.hospital.doctor.ui.dutyclinic.adapter.DCProjectChoiceAdapter;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;

/**
 * 值班门诊-发起门诊-选择医生
 * Created by yangyong on 2019-11-26.
 */
public class DCDoctorListActivity extends NewBaseActivity {
    @FindViewById(R.id.act_dcdoctor_list_srl)
    private SmartRefreshLayout actDcdoctorListSrl;
    @FindViewById(R.id.activity_dcdoctor_list_rv)
    private RecyclerView recyclerView;
    @FindViewById(R.id.activity_dcdoctor_list_project_choice_ll)
    private LinearLayout projectChoiceLl;
    @FindViewById(R.id.activity_dcdoctor_list_project_title_tv)
    private TextView titleTv;
    @FindViewById(R.id.activity_dcdoctor_list_project_title_icon_iv)
    private ImageView choiceProjectIconIv;
    @FindViewById(R.id.activity_dcdoctor_list_empty_ll)
    private LinearLayout empttLl;

    private PopupWindow popupWindow;
    private DCProjectChoiceAdapter projectChoiceAdapter;
    private DCDoctorListAdapter dcDoctorListAdapter;
    private List<DCProjectInfo> projectInfos;
    private List<DCDoctorSectionInfo> dcDoctorSectionInfos;
    private DCProjectInfo currentProjectInfo;
    @AppApplication.Manager
    private DCManager mDCManager;

    @Override
    protected void initDatas() {
        dcDoctorSectionInfos = new ArrayList<>();
        projectInfos = new ArrayList<>();
        dcDoctorListAdapter = new DCDoctorListAdapter(R.layout.item_dcdcotor_list, R.layout.layout_dcdoctor_list_header, dcDoctorSectionInfos);
        dcDoctorListAdapter.setOnItemClickListener((adapter, view, position) -> {
            DCDoctorSectionInfo dcDoctorSessionInfo = (DCDoctorSectionInfo) adapter.getItem(position);
            if (null != dcDoctorSessionInfo && !dcDoctorSessionInfo.isHeader) {
                getDisplay().startDCInputPatientInfoActivity(currentProjectInfo.getItemId(), new DCDoctorInfo().initWithDoctorSessionInfo(dcDoctorSessionInfo));
            }
        });
        projectChoiceAdapter = new DCProjectChoiceAdapter(R.layout.item_dcproject_choice_window, projectInfos);
        projectChoiceAdapter.setOnItemClickListener((adapter, view, position) -> {
            popupWindow.dismiss();
            currentProjectInfo = (DCProjectInfo) adapter.getData().get(position);
            titleTv.setText(currentProjectInfo.getItemName());
            actDcdoctorListSrl.autoRefresh();
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_dcdoctor_list;
    }

    @Override
    protected void initViews() {
        actDcdoctorListSrl.setEnableLoadMore(false);
        actDcdoctorListSrl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getDoctorList(refreshLayout, currentProjectInfo.getItemId());
            }
        });
        recyclerView.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(this));
        recyclerView.setAdapter(dcDoctorListAdapter);
        getProjectList();
    }

    @OnClick({R.id.activity_dcdoctor_list_project_choice_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_dcdoctor_list_project_choice_ll:
                showChoiceProjectWindow();
                break;

            default:
                break;
        }
    }

    private void getProjectList() {
        mDCManager.getProjectList(0, new DefaultResultCallback<List<DCProjectInfo>>() {
            @Override
            public void onSuccess(List<DCProjectInfo> dcProjectInfos, BaseResult baseResult) {
                if (LibCollections.isNotEmpty(dcProjectInfos)) {
                    projectInfos = dcProjectInfos;
                    projectChoiceAdapter.setNewData(projectInfos);
                    currentProjectInfo = projectInfos.get(0);
                    titleTv.setText(currentProjectInfo.getItemName());
                    actDcdoctorListSrl.autoRefresh();
                }
            }
        });
    }

    private void getDoctorList(RefreshLayout refreshLayout, int itemId) {
        dcDoctorSectionInfos.clear();
        mDCManager.getDoctorList(itemId, 0, new DefaultResultCallback<List<DCProjectDoctorListInfo>>() {
            @Override
            public void onSuccess(List<DCProjectDoctorListInfo> dcProjectDoctorListInfos, BaseResult baseResult) {
                if (LibCollections.isEmpty(dcProjectDoctorListInfos)) {
                    recyclerView.setVisibility(View.GONE);
                    empttLl.setVisibility(View.VISIBLE);
                    return;
                }
                empttLl.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                dcDoctorListAdapter.setNewData(mDCManager.createDoctorSectionInfoList(dcProjectDoctorListInfos));
            }

            @Override
            public void onFinish() {
                refreshLayout.finishRefresh();
            }
        });
    }

    private void showChoiceProjectWindow() {
        if (popupWindow == null) {
            View contentView = LayoutInflater.from(this).inflate(R.layout.layout_dcproject_choice_window, null);
            popupWindow = new PopupWindow(contentView);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.update();
            popupWindow.setWidth(projectChoiceLl.getMeasuredWidth() - 50);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    choiceProjectIconIv.setImageResource(R.mipmap.ic_blue_down_arrow);
                }
            });
            RecyclerView projectChoiceRv = contentView.findViewById(R.id.layout_dcproject_choice_windown_rv);
            projectChoiceRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(this));
            projectChoiceRv.setAdapter(projectChoiceAdapter);
        }
        choiceProjectIconIv.setImageResource(R.mipmap.ic_blue_up_arrow);
        popupWindow.showAsDropDown(projectChoiceLl, 25, -15);
    }
}
