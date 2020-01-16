package cn.longmaster.hospital.doctor.ui.consult.consultant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consultant.AdvanceResultInfo;
import cn.longmaster.hospital.doctor.core.entity.consultant.VisitDetailsInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.consultant.AdvanceRequester;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.ui.consult.FullyLinearLayoutManager;
import cn.longmaster.hospital.doctor.ui.consult.adapter.AdvanceAdapter;

/**
 * 垫付结果页面
 * Created by W·H·K on 2017/12/26.
 */
public class AdvanceResultActivity extends BaseActivity {
    @FindViewById(R.id.activity_advance_result_top_icn)
    private ImageView mTopIcn;
    @FindViewById(R.id.activity_advance_result_tv)
    private TextView mResultTv;
    @FindViewById(R.id.activity_advance_result_fail_view)
    private LinearLayout mResultFailView;
    @FindViewById(R.id.activity_advance_result_fail_num_tv)
    private TextView mResultFailNumTv;
    @FindViewById(R.id.activity_advance_result_fail_prv)
    private RecyclerView mResultFailPrv;
    @FindViewById(R.id.activity_advance_result_suc_view)
    private LinearLayout mResultSucView;
    @FindViewById(R.id.activity_advance_result_suc_num_tv)
    private TextView mResultSucNumTv;
    @FindViewById(R.id.activity_advance_result_suc_prv)
    private RecyclerView mResultSucPrv;

    private AdvanceResultInfo mAdvanceResultInfo;
    private List<VisitDetailsInfo> mAdvances;
    private List<Integer> mAdvanceFails;
    private List<Integer> mAdvanceSuccesss;
    private ProgressDialog mProgressDialog;

    public static void startAdvanceResultActivity(Activity activity, AdvanceResultInfo advanceResultInfo, List<VisitDetailsInfo> mAdvances) {
        Intent intent = new Intent(activity, AdvanceResultActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ADVANCE_RESULT, advanceResultInfo);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ADVANCE_LIST, (Serializable) mAdvances);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_result);
        ViewInjecter.inject(this);
        initData();
        initView();
    }

    private void initData() {
        mAdvanceResultInfo = (AdvanceResultInfo) getIntent().getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ADVANCE_RESULT);
        mAdvances = (List<VisitDetailsInfo>) getIntent().getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ADVANCE_LIST);
    }

    private void initView() {
        //失败适配
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(this);
        mResultFailPrv.setNestedScrollingEnabled(false);
        mResultFailPrv.setLayoutManager(linearLayoutManager);
        FullyLinearLayoutManager linearLayoutManagers = new FullyLinearLayoutManager(this);
        mResultSucPrv.setNestedScrollingEnabled(false);
        mResultSucPrv.setLayoutManager(linearLayoutManagers);
        mAdvanceSuccesss = mAdvanceResultInfo.getAdvanceSuccesss();
        mAdvanceFails = mAdvanceResultInfo.getAdvanceFails();
        displayAdvanceResult(mAdvanceSuccesss, mAdvanceFails);
    }

    private void displayAdvanceResult(List<Integer> mAdvanceSuccesss, List<Integer> mAdvanceFails) {
        if (mAdvanceFails.size() == 0) {
            mResultFailView.setVisibility(View.GONE);
            mTopIcn.setImageResource(R.drawable.ic_advance_result_top_suc);
            mResultTv.setText(getString(R.string.advance_result_suc));
            mResultTv.setTextColor(getResColor(R.color.color_00b700));
        } else {
            mResultFailView.setVisibility(View.VISIBLE);
            AdvanceAdapter failAdapter = new AdvanceAdapter(this, AppConstant.AdvanceResultType.RESULT_TYPE_FAIL, R.layout.item_advance_result, mAdvanceFails, mAdvances);
            mResultFailPrv.setAdapter(failAdapter);
            mResultFailNumTv.setText(getString(R.string.advance_result_fail_num, mAdvanceFails.size()));
            mTopIcn.setImageResource(R.drawable.ic_advance_result_fail_top);
            mResultTv.setText(getString(R.string.advance_result_fail));
            mResultTv.setTextColor(getResColor(R.color.color_ff2c2c));
        }

        //成功适配
        if (mAdvanceSuccesss.size() == 0) {
            mResultSucView.setVisibility(View.GONE);
        } else {
            mResultSucView.setVisibility(View.VISIBLE);
            AdvanceAdapter sucAdapter = new AdvanceAdapter(this, AppConstant.AdvanceResultType.RESULT_TYPE_SUC, R.layout.item_advance_result, mAdvanceSuccesss, mAdvances);
            mResultSucPrv.setAdapter(sucAdapter);
            mResultSucNumTv.setText(getString(R.string.advance_result_suc_num, mAdvanceSuccesss.size()));
        }
    }

    @OnClick({R.id.activity_advance_result_advance_again_tv,
            R.id.activity_advance_result_return_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_advance_result_advance_again_tv:
                advance();
                break;

            case R.id.activity_advance_result_return_tv:
                Intent intent = new Intent(getActivity(), SchedulingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

        }
    }

    private void advance() {
        showProgressDialog();
        AdvanceRequester requester = new AdvanceRequester(new OnResultListener<AdvanceResultInfo>() {
            @Override
            public void onResult(BaseResult baseResult, AdvanceResultInfo advanceResultInfo) {
                dismissProgressDialog();
                Logger.logD(Logger.COMMON, "->AdvanceActivity()->advance-->advanceResultInfo:" + advanceResultInfo + ", baseResult.getCode():" + baseResult.getCode());
                if (baseResult.getCode() == RESULT_SUCCESS) {
                    mAdvanceFails = advanceResultInfo.getAdvanceFails();
                    mAdvanceSuccesss = advanceResultInfo.getAdvanceSuccesss();
                    displayAdvanceResult(advanceResultInfo.getAdvanceSuccesss(), advanceResultInfo.getAdvanceFails());
                } else {
                    showToast(R.string.no_network_connection);
                }
            }
        });
        requester.appointmentIdList = mAdvanceFails;
        requester.doPost();
    }

    public void rightClick(View view) {
        Intent intent = new Intent(getActivity(), SchedulingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void showProgressDialog() {
        if (isFinishing()) {
            return;
        }
        if (mProgressDialog == null) {
            mProgressDialog = createProgressDialog(getString(R.string.loading));
        } else if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (isFinishing()) {
            return;
        }
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}

