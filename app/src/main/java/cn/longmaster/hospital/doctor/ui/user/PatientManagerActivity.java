package cn.longmaster.hospital.doctor.ui.user;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.rounds.BasicMedicalInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.WaitRoundsPatientInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.rounds.CorrelationVisitRequester;
import cn.longmaster.hospital.doctor.core.requests.rounds.WaitRoundsPatientRequeter;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.user.adapter.PatientManagerAdapter;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/6/29 13:47
 * @description: 患者管理
 */
public class PatientManagerActivity extends NewBaseActivity {
    @FindViewById(R.id.tool_bar_base)
    private Toolbar toolBarBase;
    @FindViewById(R.id.iv_tool_bar_back)
    private ImageView ivToolBarBack;
    @FindViewById(R.id.tv_tool_bar_title)
    private TextView tvToolBarTitle;
    @FindViewById(R.id.tv_tool_bar_sub)
    private TextView tvToolBarSub;
    @FindViewById(R.id.iv_tool_bar_sub)
    private ImageView ivToolBarSub;
    @FindViewById(R.id.include_search_title_et)
    private EditText includeSearchTitleEt;
    @FindViewById(R.id.include_search_operation_tv)
    private TextView includeSearchOperationTv;
    @FindViewById(R.id.include_search_clear_iv)
    private ImageView includeSearchClearIv;

    @FindViewById(R.id.activity_patient_data_manager_srl)
    private SmartRefreshLayout activityPatientDataManagerSrl;
    @FindViewById(R.id.activity_patient_data_manager_rv)
    private RecyclerView activityPatientDataManagerRv;
    @FindViewById(R.id.include_new_no_data_ll)
    private LinearLayout includeNoDataPatientLl;
    @FindViewById(R.id.include_new_no_data_iv)
    private ImageView includeNewNoDataIv;
    private PatientManagerAdapter patientManagerAdapter;
    private int mPageIndex = MIN_PAGE_INDEX_1;
    private final int REQUEST_CODE_FOR_PATIENT_CHANGE = 104;

    @Override
    protected void initDatas() {
        patientManagerAdapter = new PatientManagerAdapter(R.layout.item_patient_manager, new ArrayList<>(0));
        patientManagerAdapter.setOnItemClickListener((adapter, view, position) -> {
            WaitRoundsPatientInfo waitRoundsPatientInfo = (WaitRoundsPatientInfo) adapter.getItem(position);
            if (null != waitRoundsPatientInfo) {
                //查看详情
                BasicMedicalInfo info = new BasicMedicalInfo();
                info.setMedicalId(waitRoundsPatientInfo.getMedicalId());
                getDisplay().startRoundsPatientInfoActivity(info, REQUEST_CODE_FOR_PATIENT_CHANGE);
            }
        });
        patientManagerAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            WaitRoundsPatientInfo item = (WaitRoundsPatientInfo) adapter.getItem(position);
            if (null != item) {
                BasicMedicalInfo basicMedicalInfo = new BasicMedicalInfo();
                basicMedicalInfo.setMedicalId(item.getMedicalId());
                switch (view.getId()) {
                    case R.id.item_patient_manager_item_rl:
                        getDisplay().startRoundsPatientInfoActivity(basicMedicalInfo, 0);
                        break;
                    case R.id.item_wait_rounds_patient_relation:
                        if (!item.hasOrderId()) {
                            displayRelationWindow(item.getMedicalId());
                        }
                        break;
                    default:
                        break;
                }
            }
            //关联
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_patient_data_manager;
    }

    @Override
    protected void initViews() {
        tvToolBarTitle.setText("患者列表");
        tvToolBarSub.setText("添加患者");
        includeSearchTitleEt.setHint("输入患者姓名/住院号");
        tvToolBarSub.setVisibility(View.VISIBLE);
        includeSearchOperationTv.setText("搜索");
        activityPatientDataManagerRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getThisActivity()));
        activityPatientDataManagerRv.setAdapter(patientManagerAdapter);
        initListener();
        activityPatientDataManagerSrl.autoRefresh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            if (requestCode == REQUEST_CODE_FOR_PATIENT_CHANGE) {
                activityPatientDataManagerSrl.autoRefresh();
            }
        }
    }

    private void initListener() {
        tvToolBarSub.setOnClickListener(v -> {
            getDisplay().startRoundsPatientAddActivity(0, 0, false, REQUEST_CODE_FOR_PATIENT_CHANGE);
        });
        ivToolBarBack.setOnClickListener(v -> onBackPressed());
        includeSearchOperationTv.setOnClickListener(v -> {
            activityPatientDataManagerSrl.autoRefresh();
        });
        includeSearchClearIv.setOnClickListener(v -> {
            includeSearchTitleEt.setText(null);
            activityPatientDataManagerSrl.autoRefresh();
        });
        includeSearchTitleEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtils.isTrimEmpty(s)) {
                    includeSearchClearIv.setVisibility(View.GONE);
                } else {
                    includeSearchClearIv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        includeSearchTitleEt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                activityPatientDataManagerSrl.autoRefresh();
                return true;
            }
            return false;
        });
        activityPatientDataManagerSrl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPageIndex++;
                getWaitRoundsPatient(getString(includeSearchTitleEt), false, refreshLayout);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPageIndex = MIN_PAGE_INDEX_1;
                getWaitRoundsPatient(getString(includeSearchTitleEt), true, refreshLayout);
            }
        });
    }

    /**
     * @param filterName
     */
    private void getWaitRoundsPatient(String filterName, boolean isRefresh, RefreshLayout refreshLayout) {
        WaitRoundsPatientRequeter requeter = new WaitRoundsPatientRequeter(new DefaultResultCallback<List<WaitRoundsPatientInfo>>() {
            @Override
            public void onSuccess(List<WaitRoundsPatientInfo> waitRoundsPatientInfos, BaseResult baseResult) {
                Logger.logD(Logger.APPOINTMENT, "WaitRoundsPatientActivity->initData-->waitRoundsPatientInfos:" + waitRoundsPatientInfos);
                if (baseResult.isFinish()) {
                    activityPatientDataManagerSrl.finishLoadMoreWithNoMoreData();
                }
                if (mPageIndex == MIN_PAGE_INDEX_1) {
                    if (LibCollections.isEmpty(waitRoundsPatientInfos)) {
                        includeNoDataPatientLl.setVisibility(View.VISIBLE);
                        activityPatientDataManagerSrl.setVisibility(View.GONE);
                    } else {
                        includeNoDataPatientLl.setVisibility(View.GONE);
                        activityPatientDataManagerSrl.setVisibility(View.VISIBLE);
                    }
                    patientManagerAdapter.setNewData(waitRoundsPatientInfos);
                } else {
                    patientManagerAdapter.addData(waitRoundsPatientInfos);
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
        requeter.keyWords = filterName;
        requeter.pageindex = mPageIndex;
        requeter.pagesize = PAGE_SIZE;
        requeter.dataType = 1;
        requeter.start();
    }

    private void displayRelationWindow(int medicalId) {
        View inputView = LayoutInflater.from(getThisActivity()).inflate(R.layout.layout_wait_rounds_relation, null);
        EditText inputEdt = (EditText) inputView.findViewById(R.id.layout_wait_rounds_relation_edt);
        TextView submission = (TextView) inputView.findViewById(R.id.layout_wait_rounds_relation_submission);
        Dialog mDialog = new Dialog(getThisActivity(), R.style.custom_noActionbar_window_style);
        mDialog.show();
        mDialog.setContentView(inputView);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);

        Window win = mDialog.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        submission.setOnClickListener(v -> {
            if (StringUtils.isEmpty(inputEdt.getText().toString())) {
                ToastUtils.showShort(R.string.activity_wait_rounds_patient_input_num);
                return;
            }
            int orderId = StringUtils.str2Integer(inputEdt.getText().toString().trim());
            relationVisitRequester(orderId, medicalId, mDialog);
        });
    }

    private void relationVisitRequester(int orderId, int medicalId, Dialog dialog) {
        CorrelationVisitRequester requester = new CorrelationVisitRequester(new OnResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, BaseResult baseResult) {
                ToastUtils.showShort(R.string.relation_result_success);
                activityPatientDataManagerSrl.autoRefresh();
            }

            @Override
            public void onFail(BaseResult baseResult) {
                if (baseResult.getCode() == -102) {
                    ToastUtils.showShort(R.string.relation_resubmits_correct);
                } else {
                    ToastUtils.showShort(R.string.relation_resubmits);
                }
            }

            @Override
            public void onFinish() {
                dialog.dismiss();
            }
        });
        requester.actType = 0;
        requester.orderId = orderId;
        requester.medicalId = medicalId;
        requester.doPost();
    }
}
