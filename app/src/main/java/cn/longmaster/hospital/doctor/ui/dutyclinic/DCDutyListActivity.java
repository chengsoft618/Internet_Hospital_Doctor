package cn.longmaster.hospital.doctor.ui.dutyclinic;

import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCProjectInfo;
import cn.longmaster.hospital.doctor.core.manager.dutyclinic.DCManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.dutyclinic.SetDCProjectDutyStateRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.dutyclinic.adapter.DCDutyListAdapter;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * 值班门诊-值班列表界面
 * Created by yangyong on 2019-11-27.
 */
public class DCDutyListActivity extends NewBaseActivity {
    @FindViewById(R.id.activity_dcduty_list_rv)
    private RecyclerView recyclerView;

    private DCDutyListAdapter adapter;
    private List<DCProjectInfo> projectInfos;
    @AppApplication.Manager
    DCManager dcManager;

    @Override
    protected void initDatas() {
        projectInfos = new ArrayList<>();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_dcduty_list;
    }

    @Override
    protected void initViews() {
        adapter = new DCDutyListAdapter(R.layout.item_dcduty_list, projectInfos);
        recyclerView.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setDutySwitchIconClickListener(new DCDutyListAdapter.OnDutySwitchIconClickListener() {
            @Override
            public void onDutySwitchIconClickListener(DCProjectInfo projectInfo) {
                showTipDialog(projectInfo);
            }
        });

        getProjectList();
    }

    private void getProjectList() {
        ProgressDialog dialog = createProgressDialog(getString(R.string.loading_data));
        dialog.show();
        dcManager.getProjectList(0, new DefaultResultCallback<List<DCProjectInfo>>() {
            @Override
            public void onSuccess(List<DCProjectInfo> dcProjectInfos, BaseResult baseResult) {
                projectInfos = dcProjectInfos;
                adapter.setNewData(projectInfos);
            }

            @Override
            public void onFinish() {
                dialog.dismiss();
            }
        });
    }

    private void showTipDialog(DCProjectInfo info) {
        if (info.getDutyState() == 0) {
            setProjectDytyState(info);
        } else {
            new CommonDialog.Builder(getThisActivity())
                    .setMessage("是否结束值班")
                    .setPositiveButton(R.string.cancel, () -> {
                    })
                    .setNegativeButton(R.string.confirm, () -> setProjectDytyState(info))
                    .show();
        }
    }

    private void setProjectDytyState(DCProjectInfo info) {
        ProgressDialog dialog = createProgressDialog(getString(R.string.loading_data));
        dialog.show();
        SetDCProjectDutyStateRequester requester = new SetDCProjectDutyStateRequester(new OnResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, BaseResult baseResult) {
                info.setDutyState(info.getDutyState() == 0 ? 1 : 0);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(BaseResult baseResult) {
                ToastUtils.showShort(R.string.no_network_connection);
            }

            @Override
            public void onFinish() {
                dialog.dismiss();
            }
        });
        requester.itemId = info.getItemId();
        requester.dutyState = info.getDutyState() == 0 ? 1 : 0;
        requester.start();
    }
}
