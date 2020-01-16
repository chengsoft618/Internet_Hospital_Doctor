package cn.longmaster.hospital.doctor.ui.rounds;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.doctor.HospitalInfo;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.ui.BaseActivity;

/**
 * 接诊成功
 */
public class ReceiveSuccessActivity extends BaseActivity {
    @FindViewById(R.id.activity_receive_success_time)
    private TextView mTimeTv;
    @FindViewById(R.id.activity_receive_success_tip)
    private TextView mTipTv;

    @AppApplication.Manager
    private DoctorManager mDoctorManager;

    private String mReceptionTime;
    private int mAtthosId;
    private int mOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_success);
        ViewInjecter.inject(this);
        initData();
        initView();
    }

    private void initData() {
        mReceptionTime = getIntent().getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_RECEPTION_TIME);
        mAtthosId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ATTHOS_ID, 0);
        mOrderId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_ID, 0);
    }

    private void initView() {
        displayView();
        mDoctorManager.getHospitalInfo(mAtthosId, true, new DoctorManager.OnHospitalInfoLoadListener() {
            @Override
            public void onSuccess(HospitalInfo hospitalInfo) {
                if (hospitalInfo != null) {
                    mTipTv.setText(getString(R.string.rounds_receive_suss, hospitalInfo.getHospitalName(), mOrderId + ""));
                } else {
                    mTipTv.setText(getString(R.string.rounds_receive_suss, "", mOrderId + ""));
                }
            }

            @Override
            public void onFailed(int code, String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void displayView() {
        mTimeTv.setText(mReceptionTime);
    }

    @OnClick({R.id.activity_receive_success_determine})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_receive_success_determine:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
