package cn.longmaster.hospital.doctor.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.common.UserResultInfo;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.login.CheckCodeRequester;
import cn.longmaster.hospital.doctor.core.requests.login.RegCodeRequester;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.util.TimeCountUtil;
import cn.longmaster.utils.DisplayUtil;
import cn.longmaster.utils.SPUtils;
import cn.longmaster.utils.StringUtils;

public class AccountVerificationActivity extends BaseActivity {
    @FindViewById(R.id.activity_account_verification_get_code_view)
    private Button mButton;
    @FindViewById(R.id.activity_account_verification_edit_text)
    private EditText mEditText;
    @FindViewById(R.id.activity_account_verification_error_code)
    private TextView mErrorCodeView;
    @FindViewById(R.id.activity_account_verification_toast_content)
    private TextView mToastContentview;
    @FindViewById(R.id.activity_account_verification_toast_view)
    private LinearLayout mToastView;

    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    private boolean isShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_verification);
        ViewInjecter.inject(this);
        initView();
    }

    private void initView() {
        final TimeCountUtil util = new TimeCountUtil(this, 60 * 1000, 1000, mButton);
        mButton.setTransformationMethod(null);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToastView.setVisibility(View.VISIBLE);
                util.start();
                RegCodeRequester requester = new RegCodeRequester(new OnResultListener<Integer>() {
                    @Override
                    public void onResult(BaseResult baseResult, Integer integer) {
                        Logger.logI(Logger.USER, "AccountVerificationActivity--->RegCodeRequester-->baseResult:" + baseResult + ",integer:" + integer);
                        if (baseResult.getCode() == RESULT_SUCCESS) {
                            mToastContentview.setText(getString(R.string.account_send_code_suc));
                            showToastView();
                        } else {
                            mToastContentview.setText(getString(R.string.account_send_error));
                            showToastView();
                            util.cancel();
                            util.onFinish();
                        }
                    }
                });
                requester.requestType = 5;
                requester.account = mUserInfoManager.getCurrentUserInfo().getPhoneNum();
                requester.doPost();
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (temp.length() == 0) {
                    mErrorCodeView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void showToastView() {
        mToastView.setVisibility(View.VISIBLE);
        isShowToast(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isShowToast(false);
            }
        }, 3000);
    }

    @OnClick({R.id.activity_account_verification_bottom_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_account_verification_bottom_view:
                String code = mEditText.getText().toString();
                if (StringUtils.isEmpty(code)) {
                    showToast(getString(R.string.account_input_code));
                    return;
                }
                CheckCodeRequester requester = new CheckCodeRequester(new OnResultListener<UserResultInfo>() {
                    @Override
                    public void onResult(BaseResult baseResult, UserResultInfo userResultInfo) {
                        Logger.logI(Logger.USER, "AccountVerificationActivity--->CheckCodeRequester-->baseResult:" + baseResult + ",userResultInfo:" + userResultInfo);
                        if (baseResult.getCode() == RESULT_SUCCESS) {
                            SPUtils.getInstance().put(AppPreference.KEY_INQUIRY_ACCOUNT_INFO, true);
                            mErrorCodeView.setVisibility(View.GONE);
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), MyAccountActivity.class);
                            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ACCOUNT_ID, mUserInfoManager.getCurrentUserInfo().getUserId());
                            startActivity(intent);
                            finish();
                        } else {
                            mErrorCodeView.setVisibility(View.VISIBLE);
                        }
                    }
                });
                requester.account = mUserInfoManager.getCurrentUserInfo().getPhoneNum();
                requester.userId = mUserInfoManager.getCurrentUserInfo().getUserId();
                requester.verifyCode = code;
                requester.requestType = 5;
                requester.doPost();
                break;
            default:
                break;
        }
    }

    public void isShowToast(final boolean isShow) {
        final int marinTop = 0;
        final View view = mToastView;
        final int selfHeight = DisplayUtil.dip2px(30);//当前控件高度
        int yDeltaTemp = marinTop + selfHeight;
        if (!isShow) {
            if (view.getY() != marinTop) {
                return;
            }
            yDeltaTemp = -yDeltaTemp;
        } else {
            if (view.getY() != -selfHeight) {
                return;
            }
        }
        final int yDelta = yDeltaTemp;
        final TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, yDelta);
        translateAnimation.setDuration(200);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                if (isShow) {
                    view.setY(marinTop);
                    isShowing = true;
                } else {
                    view.setY(-selfHeight);
                    isShowing = false;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        AppHandlerProxy.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                view.startAnimation(translateAnimation);
            }
        });
    }

    public boolean isShowing() {
        return isShowing;
    }
}
