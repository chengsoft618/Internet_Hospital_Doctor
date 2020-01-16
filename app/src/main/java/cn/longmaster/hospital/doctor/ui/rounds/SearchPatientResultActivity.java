package cn.longmaster.hospital.doctor.ui.rounds;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.rounds.WaitRoundsPatientInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.rounds.CorrelationVisitRequester;
import cn.longmaster.hospital.doctor.core.requests.rounds.WaitRoundsPatientRequeter;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.ui.college.view.RefreshRecyclerView;
import cn.longmaster.hospital.doctor.ui.rounds.adapter.WaitRoundsPatientAdapter;
import cn.longmaster.hospital.doctor.view.AppActionBar;
import cn.longmaster.utils.StringUtils;

public class SearchPatientResultActivity extends BaseActivity {
    @FindViewById(R.id.activity_search_patient_result_bar)
    private AppActionBar mAppActionBar;
    @FindViewById(R.id.activity_search_patient_result_recycler_view)
    private RefreshRecyclerView mRecyclerView;
    @FindViewById(R.id.activity_search_patient_result_no_result_ll)
    private LinearLayout mNoResultLl;

    private WaitRoundsPatientAdapter mAadapter;
    String search;
    private int mPageIndex = 1;
    private int mPageSize = 10;
    private int mIsFinish;
    private int mOrderId;
    private boolean mIsAddPatient;
    private boolean mIsMouldAddPatient;
    private ProgressDialog mProgressDialog;
    private List<WaitRoundsPatientInfo> mWaitRoundsPatientInfos = new ArrayList<>();
    private Dialog mDialog;
    private List<Integer> mMedicalRecords = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_patient_result);
        ViewInjecter.inject(this);
        initData();
        initView();
        getWaitRoundsPatient(false);
    }

    private void initData() {
        search = getIntent().getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SEARCH_CONTENT);
        mOrderId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_ID, 0);
        mIsAddPatient = getIntent().getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ADD_PATIENT, false);
        mIsMouldAddPatient = getIntent().getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MOULD_ADD_PATIENT, false);
        mMedicalRecords = (List<Integer>) getIntent().getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_MEDICAL_LIST);
    }

    private void initView() {
        mAppActionBar.setTitle(search);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAadapter = new WaitRoundsPatientAdapter(getActivity(), mWaitRoundsPatientInfos);
        mRecyclerView.setAdapter(mAadapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setOnLoadMoreListener(new RefreshRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMoreListener() {
                if (mIsFinish == 1) {
                    mRecyclerView.setLoadMoreEnable(true);
                    mPageIndex++;
                    getWaitRoundsPatient(true);
                } else {
                    mRecyclerView.setLoadMoreEnable(false);
                }
            }
        });

    }

    private void getWaitRoundsPatient(final boolean isLoadMore) {
        WaitRoundsPatientRequeter requster = new WaitRoundsPatientRequeter(new DefaultResultCallback<List<WaitRoundsPatientInfo>>() {
            @Override
            public void onSuccess(List<WaitRoundsPatientInfo> waitRoundsPatientInfos, BaseResult baseResult) {
                Logger.logD(Logger.APPOINTMENT, "WaitRoundsPatientActivity->initData-->waitRoundsPatientInfos:" + waitRoundsPatientInfos);
                mRecyclerView.setLoadMoreEnable(true);
                mIsFinish = baseResult.getIsFinish();
                if (isLoadMore) {
                    mWaitRoundsPatientInfos.addAll(waitRoundsPatientInfos);
                    mAadapter.addData(waitRoundsPatientInfos);
                    mRecyclerView.notifyData();
                } else {
                    mWaitRoundsPatientInfos = waitRoundsPatientInfos;
                    mAadapter.setData(waitRoundsPatientInfos);
                    mRecyclerView.notifyData();
                }
                if (mWaitRoundsPatientInfos.size() == 0) {
                    mNoResultLl.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                } else {
                    mNoResultLl.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
        requster.keyWords = search;
        requster.pageindex = mPageIndex;
        requster.pagesize = mPageSize;
        requster.doPost();
    }

//    @Override
//    public void onItemClick(View view, int position) {
//        if (mIsAddPatient) {
//            showProgressDialog();
//            CorrelationVisitRequester requester = new CorrelationVisitRequester(new OnResultListener<Void>() {
//                @Override
//                public void onResult(BaseResult baseResult, Void aVoid) {
//                    dismissProgressDialog();
//                    if (baseResult.getCode() == RESULT_SUCCESS) {
//                        Intent intent = getIntent();
//                        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDICAL_ID, 0);
//                        setResult(RESULT_OK, intent);
//                        finish();
//                    } else {
//                        showToast(R.string.no_network_connection);
//                    }
//                }
//            });
//            requester.actType = 0;
//            requester.orderId = mOrderId;
//            requester.medicalId = mWaitRoundsPatientInfos.get(position).getMedicalId();
//            requester.doPost();
//        } else if (mIsMouldAddPatient) {
//            setRoundsMedicalInfo(position);
//        } else {
//            BasicMedicalInfo info = new BasicMedicalInfo();
//            info.setOrderState(1);
//            info.setMedicalId(mWaitRoundsPatientInfos.get(position).getMedicalId());
//            Intent intent = new Intent(SearchPatientResultActivity.this, RoundsPatientInfoActivity.class);
//            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_BASIC_MEDICAL_INFO, info);
//            startActivity(intent);
//        }
//    }

    private void setRoundsMedicalInfo(int position) {
        if (mMedicalRecords.contains(mWaitRoundsPatientInfos.get(position).getMedicalId())) {
            showToast("当前患者已添加");
            return;
        }
        Intent intent = getIntent();
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDICAL_ID, mWaitRoundsPatientInfos.get(position).getMedicalId());
        setResult(RESULT_OK, intent);
        finish();
    }

//    @Override
//    public void onRelationClick(View view, int position) {
//        displayRelationWindow(position);
//    }


    private void displayRelationWindow(int position) {
        View inputView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_wait_rounds_relation, null);
        EditText inputEdt = (EditText) inputView.findViewById(R.id.layout_wait_rounds_relation_edt);
        TextView submission = (TextView) inputView.findViewById(R.id.layout_wait_rounds_relation_submission);
        mDialog = new Dialog(getActivity(), R.style.custom_noActionbar_window_style);
        mDialog.show();
        mDialog.setContentView(inputView);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);

        Window win = mDialog.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        setInputViewListener(submission, inputEdt, position);
    }

    private void setInputViewListener(TextView submission, final EditText inputEdt, final int position) {
        submission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(inputEdt.getText().toString())) {
                    showToast(getString(R.string.activity_wait_rounds_patient_input_num));
                    return;
                }
                int orderId = Integer.valueOf(inputEdt.getText().toString()).intValue();
                relationVisitRequester(orderId, mWaitRoundsPatientInfos.get(position).getMedicalId());
            }
        });
    }

    private void relationVisitRequester(int orderId, int medicalId) {
        showProgressDialog();
        CorrelationVisitRequester requester = new CorrelationVisitRequester(new DefaultResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, BaseResult baseResult) {
                showToast(getString(R.string.relation_result_success));
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                mPageIndex = 1;
                getWaitRoundsPatient(false);

            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                if (result.getCode() == -102) {
                    showToast(getString(R.string.relation_resubmits_correct));
                } else {
                    showToast(getString(R.string.relation_resubmits));
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissProgressDialog();
            }
        });
        requester.actType = 0;
        requester.orderId = orderId;
        requester.medicalId = medicalId;
        requester.doPost();
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
