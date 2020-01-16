package cn.longmaster.hospital.doctor.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.util.common.CommonUtils;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.manager.room.AudioAdapterManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.receiver.NetStateReceiver;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.upload.newupload.MaterialTaskManager;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.main.MainActivity;
import cn.longmaster.hospital.doctor.ui.user.ChooseAddressActivity;
import cn.longmaster.hospital.doctor.ui.user.LoginMessageActivity;
import cn.longmaster.hospital.doctor.ui.user.PasswordChangeActivity;
import cn.longmaster.hospital.doctor.view.LoadingButton;
import cn.longmaster.utils.PhoneUtil;
import cn.longmaster.utils.SPUtils;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;
import cn.longmaster.utils.Utils;

/**
 * 登录页
 *
 * @author Tengshuxiang
 */
public class LoginActivity extends NewBaseActivity implements TextView.OnEditorActionListener {
    @FindViewById(R.id.activity_login_phone_et)
    private EditText mPhoneEt;
    @FindViewById(R.id.activity_login_password_et)
    private EditText mPasswordEt;
    @FindViewById(R.id.activity_login_login_btn)
    private LoadingButton mLoginBtn;
    @FindViewById(R.id.activity_login_version_tv)
    private TextView mVersionTv;

    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    private AudioAdapterManager mAudioAdapterManager;
    @AppApplication.Manager
    private MaterialTaskManager mMaterialTaskManager;
    private String mPhone;
    private String mPassword;
    private boolean mIsClicked = false;
    private long[] mHits = new long[5];
    private int mCount = 0;

    @Override
    protected void onResume() {
        super.onResume();
        mIsClicked = false;
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews() {
        mVersionTv.setText("V" + "\t" + Utils.getAppVersion());
        mPasswordEt.setOnEditorActionListener(this);
    }

    @OnClick({R.id.activity_login_login_btn,
            R.id.activity_login_version_iv,
            R.id.activity_login_message_login_tv,
            R.id.activity_login_register_tv,
            R.id.activity_login_found_password_tv})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.activity_login_login_btn:
                login();
                break;

            case R.id.activity_login_message_login_tv:
                if (mIsClicked) {
                    return;
                }
                mIsClicked = true;
                intent.setClass(this, LoginMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_login_version_iv:
                changeService();
                break;
            case R.id.activity_login_register_tv:
                if (mIsClicked) {
                    return;
                }
                mIsClicked = true;
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(AppConfig.getAdwsUrl() + "index/register.html");
                intent.setData(content_url);
                startActivity(intent);
//                intent.setClass(this, BrowserActivity.class);
//                intent.putExtra(BrowserActivity.EXTRA_DATA_KEY_LOADING_URL, AppConfig.getAdwsUrl() + "settled/index.html");
//                intent.putExtra(BrowserActivity.EXTRA_DATA_KEY_IS_SETTLED, true);
//                startActivity(intent);
                break;

            case R.id.activity_login_found_password_tv:
                if (mIsClicked) {
                    return;
                }
                mIsClicked = true;
                intent.setClass(this, PasswordChangeActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PAGE_TYPE, AppConstant.PAGE_TYPE_FOUND);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            login();
            return true;
        }
        return false;
    }

    /**
     * 登录 登录流程:激活账号(activeAccount)--> 设置PES的IP和端口(setPesInfo)-->登录(login)
     */
    private void login() {
        if (mLoginBtn.isLoadingShowing()) {
            return;
        }

        if (!checkInput()) {
            return;
        }

        if (!NetStateReceiver.hasNetConnected(this)) {//无网络
            ToastUtils.showShort(R.string.no_network_connection);
            return;
        }
        CommonUtils.hideSoftInput(getThisActivity());
        activeAccount();
        SPUtils.getInstance().put(AppPreference.KEY_INQUIRY_ACCOUNT_INFO, false);//登录后让进入我的账户时的验证码失效
    }

    /**
     * 判断输入合法性
     */
    private boolean checkInput() {
        mPhone = getString(mPhoneEt);
        if (StringUtils.isEmpty(mPhone)) {
            ToastUtils.showShort(R.string.login_phone_number_empty_tip);
            return false;
        }

        if (!PhoneUtil.isPhoneNumber(mPhone)) {
            ToastUtils.showShort(R.string.user_phone_number_error_tip);
            return false;
        }

        mPassword = getString(mPasswordEt);
        if (StringUtils.isEmpty(mPassword)) {
            ToastUtils.showShort(R.string.user_password_empty_tip);
            return false;
        }
        if (mPassword.length() < 6) {
            ToastUtils.showShort(R.string.user_password_short_tip);
            return false;
        }
        return true;
    }

    /**
     * 激活账号
     */
    private void activeAccount() {
        mLoginBtn.showLoading();
        mUserInfoManager.activeAccount(mPhone, AppConstant.UserAccountType.ACCOUNT_PHONE_NUMBER, mPassword, (code, msg) -> {
            if (code == OnResultListener.RESULT_SUCCESS) {
                mAudioAdapterManager.clearModeConfig();
                mAudioAdapterManager.refreshData(true);
                mMaterialTaskManager.uploadLocalTasks();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                ToastUtils.showShort(msg);
                mLoginBtn.showButtonText();
            }
        });
    }

    private void changeService() {
        if (AppConfig.IS_DEBUG_MODE) {
            System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
            if (mCount == 0) {
                mHits[mHits.length - 2] = SystemClock.uptimeMillis();
            }
            mHits[mHits.length - 1] = SystemClock.uptimeMillis();
            if (mHits[mHits.length - 1] - mHits[mHits.length - 2] > 1000) {
                mCount = 0;
            } else {
                mCount++;
                if (mCount >= 5) {
                    mCount = 0;
                    showChooseAddress();
                }
            }
            Logger.logD(Logger.COMMON, "onClick->mCount:" + mCount);
            if (mCount > 1 && mCount < 5) {
                ToastUtils.showShort(getString(R.string.choose_address_click_tip, 5 - mCount));
            }
        }
    }

    /**
     * 显示选择服务器界面
     */
    private void showChooseAddress() {
        Intent intent = new Intent(this, ChooseAddressActivity.class);
        startActivity(intent);
    }
}
