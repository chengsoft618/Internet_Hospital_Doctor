package cn.longmaster.hospital.doctor.ui.rounds;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.rounds.OrderCureDtInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.rounds.RoundsReceiveRequester;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.ui.rounds.adapter.ChoiceReceiveTimeAdapter;
import cn.longmaster.hospital.doctor.util.CommonlyUtil;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.utils.ToastUtils;

public class ReceiveActivity extends BaseActivity {
    @FindViewById(R.id.layout_choice_receive_time_recycler_view)
    private RecyclerView mRecyclerView;

    private List<OrderCureDtInfo> mReceiveTimeList;
    private ChoiceReceiveTimeAdapter mChoiceReceiveTimeAdapter;
    private ProgressDialog mProgressDialog;
    private int mAtthosId;
    private int mOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_choice_receive_time_window);
        ViewInjecter.inject(this);
        initData();
        initView();
    }

    private void initData() {
        mReceiveTimeList = (List<OrderCureDtInfo>) getIntent().getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_RECEIVE_TIME_LIST);
        mAtthosId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ATTHOS_ID, 0);
        mOrderId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_ID, 0);
    }

    private void initView() {
        initChoiceTimeRecyclerView();
    }

    private void initChoiceTimeRecyclerView() {
        mRecyclerView.setLayoutManager(new FullyLayoutManager(getActivity()));
        mChoiceReceiveTimeAdapter = new ChoiceReceiveTimeAdapter(R.layout.item_receive_time, mReceiveTimeList);
        mChoiceReceiveTimeAdapter.setOnItemClickListener((adapter, view, position) -> {
            OrderCureDtInfo cureDtInfo = (OrderCureDtInfo) adapter.getItem(position);
            if (null != cureDtInfo) {
                String timeStr = cureDtInfo.getCureDt();
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Date date = format.parse(timeStr);//有异常要捕获
                    SimpleDateFormat yearF = new SimpleDateFormat("MM月dd");
                    String newD = yearF.format(date) + "日 ";
                    SimpleDateFormat yearD = new SimpleDateFormat("HH:mm");
                    timeStr = newD + CommonlyUtil.getWeek(date, ReceiveActivity.this) + " " + yearD.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new CommonDialog.Builder(ReceiveActivity.this)
                        .setMessage(timeStr)
                        .setTitle("请确认是否选择该时间段？")
                        .setPositiveButton(R.string.fill_consult_dialog_cancel, () -> {

                        })
                        .setNegativeButton(R.string.fill_consult_dialog_ok, () -> determine(mOrderId, cureDtInfo.getCureDt()))
                        .show();
            }

        });
        mRecyclerView.setAdapter(mChoiceReceiveTimeAdapter);
    }

    @OnClick({R.id.layout_choice_receive_time_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_choice_receive_time_delete:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * @param orderId
     * @param selectTime
     */
    private void determine(final int orderId, String selectTime) {
        showProgressDialog();
        RoundsReceiveRequester roundsReceiveRequester = new RoundsReceiveRequester(new DefaultResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, BaseResult baseResult) {
                Logger.logI(Logger.COMMON, "RoundsReceiveRequester：baseResult" + baseResult);
                Intent intent = new Intent(ReceiveActivity.this, ReceiveSuccessActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_RECEPTION_TIME, selectTime);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ATTHOS_ID, mAtthosId);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_ID, mOrderId);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                ToastUtils.showShort(R.string.rounds_receive_fail);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissProgressDialog();
            }
        });
        roundsReceiveRequester.orderId = orderId;
        roundsReceiveRequester.cureDt = selectTime;
        roundsReceiveRequester.doPost();
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
