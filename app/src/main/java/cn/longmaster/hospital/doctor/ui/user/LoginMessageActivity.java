package cn.longmaster.hospital.doctor.ui.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.DcpErrorcodeDef;
import cn.longmaster.hospital.doctor.core.entity.user.CheckAccountInfo;
import cn.longmaster.hospital.doctor.core.manager.room.AudioAdapterManager;
import cn.longmaster.hospital.doctor.core.manager.user.LoginStateChangeListener;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.receiver.NetStateReceiver;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.login.CheckAccountExistRequester;
import cn.longmaster.hospital.doctor.core.upload.newupload.MaterialTaskManager;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.ui.main.MainActivity;
import cn.longmaster.hospital.doctor.view.LoadingButton;
import cn.longmaster.utils.PhoneUtil;
import cn.longmaster.utils.StringUtils;

/**
 * 短信登录
 *
 * @author Tengshuxiang
 */
public class LoginMessageActivity extends BaseActivity {
    @FindViewById(R.id.activity_login_message_phonenum_et)
    private EditText mPhoneEt;
    @FindViewById(R.id.activity_login_message_verification_code_et)
    private EditText mCodeEt;
    @FindViewById(R.id.activity_login_message_get_verification_code_btn)
    private Button mSendCodeBtn;
    @FindViewById(R.id.activity_login_message_confirm_btn)
    private LoadingButton mLogin;

    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;

    private String mPhone;
    private String mCode;
    private int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_message);
        ViewInjecter.inject(this);
    }

    @OnClick({R.id.activity_login_message_get_verification_code_btn,
            R.id.activity_login_message_account_login_tv,
            R.id.activity_login_message_register_tv,
            R.id.activity_login_message_confirm_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_login_message_get_verification_code_btn:
                regIdentifyingCode();
                break;

            case R.id.activity_login_message_account_login_tv:
                finish();
                break;

            case R.id.activity_login_message_register_tv:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(AppConfig.getAdwsUrl() + "settled/index.html");
                intent.setData(content_url);
                startActivity(intent);
//                Intent intent = new Intent(this, BrowserActivity.class);
//                intent.putExtra(BrowserActivity.EXTRA_DATA_KEY_LOADING_URL, AppConfig.getAdwsUrl() + "settled/index.html");
//                intent.putExtra(BrowserActivity.EXTRA_DATA_KEY_IS_SETTLED, true);
//                startActivity(intent);
                break;

            case R.id.activity_login_message_confirm_btn:
                if (!checkInput()) {
                    return;
                }
                checkAccountExit();
                break;

            default:
                break;
        }
    }

    /**
     * 判断号码是否存在及账号类型
     */
    private void checkAccountExit() {
        CheckAccountExistRequester requester = new CheckAccountExistRequester(new OnResultListener<CheckAccountInfo>() {
            @Override
            public void onResult(BaseResult baseResult, CheckAccountInfo info) {
                if (baseResult.getCode() == RESULT_SUCCESS) {
                    if (info != null && info.getIsDoctor() == 1) {
                        login();
                    } else {
                        showToast(R.string.ret_account_not_is_doctor);
                    }
                } else {
                    showToast(DcpErrorcodeDef.buildErrorMsg(baseResult.getReason()));
                }
            }
        });
        requester.userName = "86" + mPhoneEt.getText().toString().trim();
        requester.accountType = AppConstant.UserAccountType.ACCOUNT_PHONE_NUMBER;
        requester.doPost();
    }

    /**
     * 获取验证码
     */
    private void regIdentifyingCode() {
        mPhone = mPhoneEt.getText().toString().trim();
        if (!checkPhoneNum(mPhone)) {
            return;
        }
        mSendCodeBtn.setEnabled(false);
        mSendCodeBtn.setText(String.format(getString(R.string.login_message_send_code_count_down), 60));

        sendCode();
    }

    /**
     * 短信登录，短信登录流程:发送验证码(regCode)--> 验证码验证(checkVerifyCode)-->设置PES的IP和端口(setPesInfo)-->登录(login)
     */
    private void login() {
        if (mPhone == null) {
            mPhone = mPhoneEt.getText().toString().trim();
        } else if (!mPhone.equals(mPhoneEt.getText().toString().trim())) {
            showToast(R.string.ret_login_failed);
            return;
        }

        if (mLogin.isLoadingShowing()) {
            return;
        }

        if (!checkInput()) {
            return;
        }

        if (!NetStateReceiver.hasNetConnected(this)) {//无网络
            showToast(R.string.no_network_connection);
            return;
        }

        mLogin.showLoading();
        checkVerifyCode();
    }

    /******************************************这是一条分隔线、割线、线！**********************************************************************/

    /**
     * 校验手机号码是否有效
     *
     * @return
     */
    private boolean checkPhoneNum(String phone) {
        if (TextUtils.isEmpty(phone)) {
            showToast(R.string.login_phone_number_empty_tip);
            return false;
        }
        if (!PhoneUtil.isPhoneNumber(phone)) {
            showToast(R.string.user_phone_number_error_tip);
            return false;
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
                mSendCodeBtn.setText(String.format(getString(R.string.login_message_send_code_count_down), millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                mSendCodeBtn.setEnabled(true);
                mSendCodeBtn.setText(R.string.login_message_again_send_code);
            }
        }.start();
    }

    /**
     * 重发验证码
     */
    private void sendCode() {
        mUserInfoManager.regCode(mPhone, AppConstant.RegCodeType.RESET_PASSWORD, new OnResultListener() {
            @Override
            public void onResult(BaseResult baseResult, Object userId) {
                if (baseResult.getCode() == RESULT_SUCCESS) {
                    showToast(R.string.login_message_send_code_success);
                    mUserId = (int) userId;
                    CountDownTimer countDownTimer = countTimer();
                    countDownTimer.start();
                } else {
                    showToast(DcpErrorcodeDef.buildErrorMsg(baseResult.getReson()));
                    mSendCodeBtn.setEnabled(true);
                    mSendCodeBtn.setText(R.string.login_message_again_send_code);
                }
            }
        });
    }

    public void checkVerifyCode() {
        int randomPassword = new Random().nextInt(999999);
        mUserInfoManager.checkVerifyCode(mUserId, mPhone, mCode, AppConstant.RegCodeType.MSG_LOGIN, String.format("%06d", randomPassword), new LoginStateChangeListener() {
            @Override
            public void onLoginStateChanged(int code, int msg) {
                if (code == OnResultListener.RESULT_SUCCESS) {
                    AppApplication.getInstance().getManager(AudioAdapterManager.class).clearModeConfig();
                    AppApplication.getInstance().getManager(AudioAdapterManager.class).refreshData(true);
                    AppApplication.getInstance().getManager(MaterialTaskManager.class).uploadLocalTasks();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showToast(msg);
                    mLogin.showButtonText();
                }
            }
        });
    }

    /**
     * 判断输入合法性
     */
    private boolean checkInput() {
        //校验手机号码
        mPhone = mPhoneEt.getText().toString().trim();
        if (!checkPhoneNum(mPhone)) {
            return false;
        }

        //校验验证
        mCode = mCodeEt.getText().toString();
        if (TextUtils.isEmpty(mCode)) {
            showToast(R.string.login_message_code_empty_tip);
            return false;
        }
        if (!StringUtils.isDigitsOnly(mCode) || mCode.length() != 4) {
            showToast(R.string.login_message_code_error_tip);
            return false;
        }
        return true;
    }
}
