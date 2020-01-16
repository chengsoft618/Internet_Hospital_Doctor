package cn.longmaster.hospital.doctor.ui.account;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.account.AccountListInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.account.MergerListRequester;
import cn.longmaster.hospital.doctor.ui.BaseActivity;

public class FlowDetailActivity extends BaseActivity {
    @FindViewById(R.id.activity_flow_detail_recycler_view)
    private RecyclerView mRecyclerView;
    @FindViewById(R.id.activity_flow_detail_amount_tv)
    private TextView mAmountTv;

    private List<AccountListInfo> mFlowDetailList = new ArrayList<>();
    private FlowDetailAdapter mAdapter;
    private String mSerialId;
    private float mOrderValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_detail);
        ViewInjecter.inject(this);
        initData();
        initView();
        getMergerList();
    }

    private void initData() {
        mSerialId = getIntent().getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SERIAL_ID);
        //mOrderValue = getIntent().getFloatExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_VALUE, 0);
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FlowDetailAdapter(this, mFlowDetailList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getMergerList() {
        MergerListRequester requester = new MergerListRequester(new OnResultListener<List<AccountListInfo>>() {
            @Override
            public void onResult(BaseResult baseResult, List<AccountListInfo> accountListInfos) {
                Logger.logD(Logger.COMMON, "FlowDetailActivity->MergerListRequester-->baseResult：" + baseResult + ",accountListInfos：" + accountListInfos);
                if (baseResult.getCode() == RESULT_SUCCESS && accountListInfos != null) {
                    mOrderValue = 0;
                    mAdapter.setData(accountListInfos);
                    for (int i = 0; i < accountListInfos.size(); i++) {
                        mOrderValue += accountListInfos.get(i).getIncomeValue();
                    }
                    mAmountTv.setText(getString(R.string.my_account_decimal, mOrderValue));
                } else {
                    showToast(R.string.no_network_connection);
                }
            }
        });
        requester.serialId = mSerialId;
        requester.doPost();
    }
}
