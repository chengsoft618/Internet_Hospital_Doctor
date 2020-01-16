package cn.longmaster.hospital.doctor.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.manager.common.DcpManager;
import cn.longmaster.hospital.doctor.core.manager.user.AccountManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfo;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.upload.newupload.MaterialTaskManager;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.ui.LoginActivity;
import cn.longmaster.utils.SPUtils;

/**
 * 选择服务器地址
 * Created by JinKe on 2016-09-23.
 */
public class ChooseAddressActivity extends BaseActivity {
    @FindViewById(R.id.choose_address_current_address_tv)
    private TextView mCurrentServiceTv;
    @FindViewById(R.id.choose_address_rg)
    private RadioGroup mRadioGroup;
    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    private DcpManager mDcpManager;
    @AppApplication.Manager
    private AccountManager mAccountManager;
    @AppApplication.Manager
    private MaterialTaskManager mMaterialTaskManager;
    @AppConfig.ServerName
    private int mServerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_address);
        ViewInjecter.inject(this);
        initData();
        regListener();
    }

    private void initData() {
        String serviceName = "";
        switch (AppConfig.getServerName()) {
            case AppConfig.SERVER_BEIJING:// 北京服务器
                serviceName = getString(R.string.choose_address_beijing);
                break;

            case AppConfig.SERVER_ISSUE:// 北京发布服务器
                serviceName = getString(R.string.choose_address_iosask);
                break;

            case AppConfig.SERVER_TEST:// 北京测试服务器
                serviceName = getString(R.string.choose_address_beijing_test);
                break;

            case AppConfig.SERVER_SANDBOX:// 109测试服务器
                serviceName = getString(R.string.choose_address_sandbox);
                break;

            case AppConfig.SERVER_YG:// 云谷备用机房
                serviceName = getString(R.string.choose_address_yg);
                break;
        }
        mCurrentServiceTv.setText(serviceName);
    }

    private void regListener() {
        mRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.choose_address_beijing_rb:
                    mServerName = AppConfig.SERVER_BEIJING;
                    break;

                case R.id.choose_address_iosask_rb:
                    mServerName = AppConfig.SERVER_ISSUE;
                    break;

                case R.id.choose_address_beijing_test_rb:
                    mServerName = AppConfig.SERVER_TEST;
                    break;

                case R.id.choose_address_sandbox_rb:
                    mServerName = AppConfig.SERVER_SANDBOX;
                    break;

                case R.id.choose_address_yg_rb:
                    mServerName = AppConfig.SERVER_YG;
                    break;

                default:
                    mServerName = AppConfig.SERVER_ISSUE;
                    break;
            }
        });
    }

    @OnClick({R.id.choose_address_config_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_address_config_tv:
                if (mServerName != AppConfig.getServerName()) {
                    SPUtils.getInstance().put(AppPreference.KEY_SERVICE_ADDRESS, mServerName);
                    AppConfig.setUrl();
                    UserInfo userInfo = mUserInfoManager.getCurrentUserInfo();
                    mDcpManager.logout(userInfo.getUserId());
                    mDcpManager.disconnect();
                    mUserInfoManager.removeUserInfo(null);
                    mMaterialTaskManager.reset();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                getActivity().finish();
                break;

        }
    }

}
