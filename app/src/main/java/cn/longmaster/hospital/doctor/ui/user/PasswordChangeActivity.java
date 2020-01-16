package cn.longmaster.hospital.doctor.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.DcpErrorcodeDef;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.user.CheckAccountInfo;
import cn.longmaster.hospital.doctor.core.manager.room.AudioAdapterManager;
import cn.longmaster.hospital.doctor.core.manager.user.LoginStateChangeListener;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.receiver.NetStateReceiver;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.login.CheckAccountExistRequester;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.ui.main.MainActivity;
import cn.longmaster.hospital.doctor.view.AppActionBar;
import cn.longmaster.hospital.doctor.view.LoadingButton;
import cn.longmaster.utils.PhoneUtil;
import cn.longmaster.utils.StringUtils;

/**
 * 修改密码
 * Created by Yang² on 2016/6/3.
 */
public class PasswordChangeActivity extends BaseActivity {
    private final String TAG = PasswordChangeActivity.class.getSimpleName();

    @FindViewById(R.id.activity_password_change_action_bar)
    private AppActionBar mActionBar;
    @FindViewById(R.id.activity_password_change_phonenum_et)
    private EditText mPhoneNumberEt;
    @FindViewById(R.id.activity_password_change_verification_code_et)
    private EditText mIdentifyingCodeEt;
    @FindViewById(R.id.activity_password_change_get_verification_code_btn)
    private Button mGetIdentifyCodeBt;
    @FindViewById(R.id.activity_password_change_pwd_et)
    private EditText mPasswordEt;
    @FindViewById(R.id.activity_password_change_pwd_sure_et)
    private EditText mCheckPasswordEt;
    @FindViewById(R.id.activity_password_change_confirm_btn)
    private LoadingButton mConfirmBtn;

    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;

    private String mPhoneNumber;
    private String mPassword;
    private String mIdentifyingCode;
    private int mUserId;
    private int mPageType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        ViewInjecter.inject(this);
        initData();
        initView();
    }

    private void initData() {
        mPageType = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PAGE_TYPE, AppConstant.PAGE_TYPE_CHANGE);
    }

    private void initView() {
        switch (mPageType) {
            case AppConstant.PAGE_TYPE_CHANGE:
                mActionBar.setTitle(getString(R.string.user_change_password));
                mConfirmBtn.setText(getString(R.string.change_submit_change));
                mConfirmBtn.setLoadingText(getString(R.string.change_submit_changing));
                break;

            case AppConstant.PAGE_TYPE_FOUND:
                mActionBar.setTitle(getString(R.string.login_found_password));
                mConfirmBtn.setText(getString(R.string.reset_and_register));
                mConfirmBtn.setLoadingText(getString(R.string.change_submit_changing));
                break;
        }
    }

    @OnClick({R.id.activity_password_change_get_verification_code_btn,
            R.id.activity_password_change_confirm_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_password_change_get_verification_code_btn:
                regCode();
                break;

            case R.id.activity_password_change_confirm_btn:
                checkAccountExit();
                break;
        }

    }

    /**
     * 判断号码是否存在及账号类型
     */
    private void checkAccountExit() {
        if (!checkInput()) {
            return;
        }
        CheckAccountExistRequester requester = new CheckAccountExistRequester(new OnResultListener<CheckAccountInfo>() {
            @Override
            public void onResult(BaseResult baseResult, CheckAccountInfo info) {
                if (baseResult.getCode() == RESULT_SUCCESS) {
                    if (info != null && info.getIsDoctor() == 1) {
                        checkVerifyCode();
                    } else {
                        showToast(R.string.ret_account_not_is_doctor);
                    }
                } else {
                    showToast(DcpErrorcodeDef.buildErrorMsg(baseResult.getReason()));
                }
            }
        });
        requester.userName = "86" + mPhoneNumberEt.getText().toString().trim();
        requester.accountType = AppConstant.UserAccountType.ACCOUNT_PHONE_NUMBER;
        requester.doPost();
    }

    /**
     * 2.重置密码流程:发送验证码(regCode)--> 验证码验证(checkVerifyCode)-->设置PES的IP和端口(setPesInfo)-->登录(login)
     */
    private void checkVerifyCode() {
        if (mConfirmBtn.isLoadingShowing()) {
            return;
        }

        if (mPhoneNumber == null) {
            mPhoneNumber = mPhoneNumberEt.getText().toString().trim();
        } else if (!mPhoneNumber.equals(mPhoneNumberEt.getText().toString().trim())) {
            showToast(R.string.ret_login_failed);
            return;
        }

        if (!checkInput()) {
            return;
        }

        if (!NetStateReceiver.hasNetConnected(this)) {//无网络
            showToast(R.string.no_network_connection);
            return;
        }

        mConfirmBtn.showLoading();
        mUserInfoManager.checkVerifyCode(mUserId, mPhoneNumber, mIdentifyingCode, AppConstant.RegCodeType.RESET_PASSWORD, mPassword, new LoginStateChangeListener() {
            @Override
            public void onLoginStateChanged(int code, int msg) {
                if (code == OnResultListener.RESULT_SUCCESS) {
                    switch (mPageType) {
                        case AppConstant.PAGE_TYPE_CHANGE:
                            showToast(R.string.change_password_success);
                            break;

                        case AppConstant.PAGE_TYPE_FOUND:
                            showToast(R.string.reset_password_success);
                            break;
                    }
                    AppApplication.getInstance().getManager(AudioAdapterManager.class).clearModeConfig();
                    AppApplication.getInstance().getManager(AudioAdapterManager.class).refreshData(true);
                    Intent mIntent = new Intent(getActivity(), MainActivity.class);
                    startActivity(mIntent);
                    finish();
                } else {
                    showToast(msg);
                    mConfirmBtn.showButtonText();
                }
            }
        });
    }

    /**
     * 校验手机号码是否有效
     *
     * @return
     */
    private boolean checkPhoneNum(String phoneNumber) {
        if (StringUtils.isEmpty(phoneNumber)) {
            showToast(R.string.user_phone_number_empty_tip);
            return false;
        }
        if (!PhoneUtil.isPhoneNumber(phoneNumber)) {
            showToast(R.string.user_phone_number_error_tip);
            return false;
        }
        if (mPageType == AppConstant.PAGE_TYPE_CHANGE) {
            String phoneNum = mUserInfoManager.getCurrentUserInfo().getPhoneNum();
            if (phoneNum.startsWith("86")) {
                phoneNum = phoneNum.substring(2);
            }
            if (!mPhoneNumber.equals(phoneNum)) {
                showToast(R.string.cant_change_others_pwd);
                return false;
            }
        }
        return true;
    }

    /**
     * 计时重发验证码
     *
     * @return
     */
    private CountDownTimer countTimer() {
        return new CountDownTimer(AppConstant.REG_CODE_MILLIS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mGetIdentifyCodeBt.setText(String.format(getString(R.string.login_message_send_code_count_down), millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                mGetIdentifyCodeBt.setEnabled(true);
                mGetIdentifyCodeBt.setText(R.string.login_message_again_send_code);
            }
        };
    }

    /**
     * 重发验证码
     */
    private void regCode() {
        mPhoneNumber = mPhoneNumberEt.getText().toString().trim();
        if (!checkPhoneNum(mPhoneNumber)) {
            return;
        }

        if (!NetStateReceiver.hasNetConnected(this)) {//无网络
            showToast(R.string.no_network_connection);
            return;
        }

        mGetIdentifyCodeBt.setEnabled(false);
        mGetIdentifyCodeBt.setText(String.format(getString(R.string.login_message_send_code_count_down), 60));

        mUserInfoManager.regCode(mPhoneNumber, AppConstant.RegCodeType.RESET_PASSWORD, new OnResultListener() {
            @Override
            public void onResult(BaseResult baseResult, Object userId) {
                if (baseResult.getCode() == RESULT_SUCCESS) {
                    showToast(R.string.login_message_send_code_success);
                    mUserId = (int) userId;
                    CountDownTimer countDownTimer = countTimer();
                    countDownTimer.start();
                } else {
                    showToast(DcpErrorcodeDef.buildErrorMsg(baseResult.getReson()));
                    mGetIdentifyCodeBt.setEnabled(true);
                    mGetIdentifyCodeBt.setText(R.string.login_message_again_send_code);
                }
            }
        });
    }

    /**
     * 判断输入合法性
     */
    private boolean checkInput() {
        //校验手机号码
        mPhoneNumber = mPhoneNumberEt.getText().toString().trim();
        if (!checkPhoneNum(mPhoneNumber)) {
            return false;
        }

        //校验验证
        mIdentifyingCode = mIdentifyingCodeEt.getText().toString();
        if (StringUtils.isEmpty(mIdentifyingCode)) {
            showToast(R.string.change_password_code_empty_tip);
            return false;
        }
        if (!StringUtils.isDigitsOnly(mIdentifyingCode) || StringUtils.length(mIdentifyingCode) != 4) {
            showToast(R.string.login_message_code_error_tip);
            return false;
        }

        //校验密码
        mPassword = mPasswordEt.getText().toString().trim();
        if (StringUtils.isEmpty(mPassword)) {
            showToast(R.string.change_password_input_new_password);
            return false;
        }
        if (mPassword.length() < 6) {
            showToast(R.string.change_password_password_too_short);
            return false;
        }

        //校验 确认密码
        String passwordCheck = mCheckPasswordEt.getText().toString().trim();
        if (StringUtils.isEmpty(passwordCheck)) {
            showToast(R.string.change_password_again_input_password);
            return false;
        }
        if (!mPassword.equals(passwordCheck)) {
            showToast(R.string.change_password_diff_password);
            return false;
        }
        return true;
    }

}
