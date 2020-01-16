package cn.longmaster.hospital.doctor.ui.rounds;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
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
import cn.longmaster.hospital.doctor.Display;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.rounds.WaitRoundsPatientInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.rounds.CorrelationVisitRequester;
import cn.longmaster.hospital.doctor.core.requests.rounds.WaitRoundsPatientRequeter;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.rounds.adapter.WaitRoundsPatientInfoAdapter;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * 待查房患者Activity
 */
public class WaitRoundsPatientActivity extends NewBaseActivity {
    private final int REQUEST_CODE_WRITE_INFO = 100; //页面请求码:新建患者

    @FindViewById(R.id.iv_tool_bar_back)
    private ImageView ivToolBarBack;
    @FindViewById(R.id.tv_tool_bar_title)
    private TextView tvToolBarTitle;
    @FindViewById(R.id.tv_tool_bar_sub)
    private TextView tvToolBarSub;

    @FindViewById(R.id.include_search_title_et)
    private EditText includeSearchTitleEt;
    @FindViewById(R.id.include_search_clear_iv)
    private ImageView includeSearchClearIv;
    @FindViewById(R.id.include_search_operation_tv)
    private TextView includeSearchOperationTv;
    @FindViewById(R.id.activity_wait_rounds_patient_srl)
    private SmartRefreshLayout activityWaitRoundsPatientSrl;
    @FindViewById(R.id.activity_wait_rounds_patient_rv)
    private RecyclerView activityWaitRoundsPatientRv;
    @FindViewById(R.id.include_new_no_data_ll)
    private LinearLayout includeNoDataPatientLl;
    @FindViewById(R.id.include_new_no_data_iv)
    private ImageView includeNewNoDataIv;
    @FindViewById(R.id.include_new_no_data_tv)
    private TextView includeNewNoDataTv;
    @FindViewById(R.id.activity_wait_rounds_patient_selected_count_tv)
    private TextView activityWaitRoundsPatientSelectedCountTv;
    @FindViewById(R.id.activity_wait_rounds_patient_btn)
    private Button activityWaitRoundsPatientBtn;

    private WaitRoundsPatientInfoAdapter patientManagerAdapter;
    private int mPageIndex = 1;
    private int mOrderId;
    //已选择患者
    private List<WaitRoundsPatientInfo> mMedicalRecords;

    @Override
    protected void handleIntent(Intent intent, Display display) {
        mOrderId = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_ID, 0);
        mMedicalRecords = (ArrayList<WaitRoundsPatientInfo>) intent.getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_MEDICAL_LIST);
    }

    @Override
    protected void initDatas() {
        patientManagerAdapter = new WaitRoundsPatientInfoAdapter(R.layout.item_patient_wait_raounds, new ArrayList<>(0));
        patientManagerAdapter.setOnItemClickListener((adapter, view, position) -> {
            patientManagerAdapter.toggleSelected(position);
            activityWaitRoundsPatientSelectedCountTv.setText(StringUtils.integer2Str(LibCollections.size(patientManagerAdapter.getSelectedDatas())));
        });
        patientManagerAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            patientManagerAdapter.toggleSelected(position);
            activityWaitRoundsPatientSelectedCountTv.setText(StringUtils.integer2Str(LibCollections.size(patientManagerAdapter.getSelectedDatas())));
        });
        patientManagerAdapter.setSelectedMedicals(mMedicalRecords);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_wait_rounds_patient;
    }

    @Override
    protected void initViews() {
        tvToolBarTitle.setText("待查房患者");
        tvToolBarSub.setVisibility(View.VISIBLE);
        tvToolBarSub.setText("新建患者");
        includeSearchOperationTv.setText("查找");
        includeSearchTitleEt.setHint("输入患者姓名/住院号");
        includeNewNoDataTv.setText("暂无相关数据");
        activityWaitRoundsPatientSelectedCountTv.setText(LibCollections.size(mMedicalRecords) + "");
        includeSearchOperationTv.setTextColor(getCompatColor(R.color.color_666666));
        activityWaitRoundsPatientRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getThisActivity()));
        activityWaitRoundsPatientRv.setAdapter(patientManagerAdapter);
        activityWaitRoundsPatientRv.setItemAnimator(new DefaultItemAnimator());
        initListener();
        activityWaitRoundsPatientSrl.autoRefresh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.logD(Logger.APPOINTMENT, "->onActivityResult()->requestCode:" + requestCode + ", resultCode:" + resultCode + ", data:" + data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_WRITE_INFO:
                    ArrayList<WaitRoundsPatientInfo> waitRoundsPatientInfos = (ArrayList<WaitRoundsPatientInfo>) patientManagerAdapter.getSelectedDatas();
                    if (null != data) {
                        ArrayList<WaitRoundsPatientInfo> patientInfos = (ArrayList<WaitRoundsPatientInfo>) data.getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_WAIT_ROUNDS_PATIENT_INFO);
                        if (waitRoundsPatientInfos != null && LibCollections.isNotEmpty(patientInfos)) {
                            for (WaitRoundsPatientInfo patientInfo : patientInfos) {
                                waitRoundsPatientInfos.add(patientInfo);
                            }
                        }
                    }
                    Intent intent = new Intent();
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_WAIT_ROUNDS_PATIENT_INFO, waitRoundsPatientInfos);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    }

    private void initListener() {
        ivToolBarBack.setOnClickListener(v -> {
            onBackPressed();
        });
        tvToolBarSub.setOnClickListener(v -> {
            getDisplay().startRoundsPatientAddActivity(0, mOrderId, isFromRounds(), REQUEST_CODE_WRITE_INFO);
        });
        activityWaitRoundsPatientSrl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
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
                activityWaitRoundsPatientSrl.autoRefresh();
                return true;
            }
            return false;
        });
        includeSearchClearIv.setOnClickListener(v -> {
            includeSearchTitleEt.setText(null);
            activityWaitRoundsPatientSrl.autoRefresh();
        });
        includeSearchOperationTv.setOnClickListener(v -> {
            activityWaitRoundsPatientSrl.autoRefresh();
        });
        activityWaitRoundsPatientBtn.setOnClickListener(v -> {
            ArrayList<WaitRoundsPatientInfo> waitRoundsPatientInfoHashMap = (ArrayList<WaitRoundsPatientInfo>) patientManagerAdapter.getSelectedDatas();
            if (mOrderId == 0) {
                Intent intent = new Intent();
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_WAIT_ROUNDS_PATIENT_INFO, waitRoundsPatientInfoHashMap);
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                if (LibCollections.isNotEmpty(waitRoundsPatientInfoHashMap)) {
                    for (WaitRoundsPatientInfo infoEntry : waitRoundsPatientInfoHashMap) {
                        addPatientToOrder(mOrderId, infoEntry.getMedicalId());
                    }
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }


    private void getWaitRoundsPatient(String filterName, boolean isRefresh, RefreshLayout refreshLayout) {
        WaitRoundsPatientRequeter requster = new WaitRoundsPatientRequeter(new DefaultResultCallback<List<WaitRoundsPatientInfo>>() {
            @Override
            public void onSuccess(List<WaitRoundsPatientInfo> waitRoundsPatientInfos, BaseResult baseResult) {
                Logger.logD(Logger.APPOINTMENT, "WaitRoundsPatientActivity->initData-->waitRoundsPatientInfos:" + waitRoundsPatientInfos);
                if (baseResult.isFinish()) {
                    activityWaitRoundsPatientSrl.finishLoadMoreWithNoMoreData();
                }
                if (mPageIndex != MIN_PAGE_INDEX_1) {
                    if (LibCollections.isNotEmpty(waitRoundsPatientInfos)) {
                        patientManagerAdapter.addData(waitRoundsPatientInfos);
                    }
                } else {
                    if (LibCollections.isEmpty(waitRoundsPatientInfos)) {
                        includeNoDataPatientLl.setVisibility(View.VISIBLE);
                        activityWaitRoundsPatientRv.setVisibility(View.GONE);
                    } else {
                        includeNoDataPatientLl.setVisibility(View.GONE);
                        activityWaitRoundsPatientRv.setVisibility(View.VISIBLE);
                    }
                    patientManagerAdapter.setNewData(waitRoundsPatientInfos);
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
        requster.keyWords = filterName;
        requster.pageindex = mPageIndex;
        requster.pagesize = PAGE_SIZE;
        requster.dataType = 0;
        requster.doPost();
    }

    /**
     * 查房详情添加患者
     *
     * @param orderId
     * @param medicalId
     */
    private void addPatientToOrder(int orderId, int medicalId) {
        CorrelationVisitRequester requester = new CorrelationVisitRequester(new DefaultResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, BaseResult baseResult) {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                ToastUtils.showShort(R.string.no_network_connection);
            }
        });
        requester.actType = 0;
        requester.orderId = orderId;
        requester.medicalId = medicalId;
        requester.doPost();
    }

    /**
     * 判断是都是从查房预约信息填写进来
     *
     * @return
     */
    private boolean isFromRounds() {
        return mMedicalRecords != null;
    }
}
