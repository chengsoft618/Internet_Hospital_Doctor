package cn.longmaster.hospital.doctor.ui.rounds;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.rounds.RoundsRefusalRequester;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.ui.main.MainActivity;

public class RoundsRefusalActivity extends BaseActivity {
    @FindViewById(R.id.activity_not_receive_tv)
    private TextView mRefusalTv;
    @FindViewById(R.id.activity_not_receive_feedback_et)
    private EditText mFeedbackEt;
    @FindViewById(R.id.activity_not_receive_img)
    private ImageView img;

    private int mRefusalType = -1;
    private int mOrderId;
    private ProgressDialog mProgressDialog;
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rounds_refusal);
        ViewInjecter.inject(this);
        initData();
    }

    private void initData() {
        mOrderId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_ID, 0);
    }

    @OnClick({R.id.activity_not_receive_reason_view,
            R.id.activity_not_receive_delete,
            R.id.activity_no_receive_time_determine})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_not_receive_delete:
                finish();
                break;

            case R.id.activity_no_receive_time_determine:
                refusal();
                break;

            case R.id.activity_not_receive_reason_view:
                img.setImageResource(R.drawable.ic_rounds_refusal_up);
                View contentView = LayoutInflater.from(this).inflate(R.layout.layout_refusal_list, null);
                final TextView inappropriate = (TextView) contentView.findViewById(R.id.layout_refusal_inappropriate);
                final TextView notInScope = (TextView) contentView.findViewById(R.id.layout_refusal_not_in_scope);
                final TextView callMe = (TextView) contentView.findViewById(R.id.layout_refusal_call_me);

                mPopupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                mPopupWindow.setTouchable(true);
                mPopupWindow.setBackgroundDrawable(new ColorDrawable(getCompatColor(R.color.color_transport)));
                mPopupWindow.showAsDropDown(view);
                inappropriate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRefusalType = 1;
                        mRefusalTv.setText(inappropriate.getText().toString());
                        mRefusalTv.setTextColor(getResColor(R.color.color_1a1a1a));
                        mPopupWindow.dismiss();
                    }
                });
                notInScope.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRefusalType = 2;
                        mRefusalTv.setText(notInScope.getText().toString());
                        mRefusalTv.setTextColor(getResColor(R.color.color_1a1a1a));
                        mPopupWindow.dismiss();
                    }
                });
                callMe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRefusalType = 3;
                        mRefusalTv.setText(callMe.getText().toString());
                        mRefusalTv.setTextColor(getResColor(R.color.color_1a1a1a));
                        mPopupWindow.dismiss();
                    }
                });
                mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        img.setImageResource(R.drawable.ic_rounds_refusal_lower);
                    }
                });
                break;
            default:
                break;
        }
    }

    private void refusal() {
        if (mRefusalType == -1) {
            showToast(getString(R.string.rounds_reasons_rejection));
            return;
        }
        showProgressDialog();
        RoundsRefusalRequester requester = new RoundsRefusalRequester(new OnResultListener<Void>() {
            @Override
            public void onResult(BaseResult baseResult, Void aVoid) {
                dismissProgressDialog();
                Logger.logI(Logger.COMMON, "RoundsRefusalRequester：baseResult" + baseResult);
                if (baseResult.getCode() == 0) {
                    Intent intent = new Intent(RoundsRefusalActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showToast(R.string.no_network_connection);
                }
            }
        });
        requester.orderId = mOrderId;
        requester.type = mRefusalType;
        requester.typeValue = mFeedbackEt.getText().toString();
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
