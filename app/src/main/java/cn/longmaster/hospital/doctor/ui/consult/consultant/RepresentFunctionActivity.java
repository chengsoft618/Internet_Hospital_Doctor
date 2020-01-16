package cn.longmaster.hospital.doctor.ui.consult.consultant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consultant.RepresentFunctionInfo;
import cn.longmaster.hospital.doctor.core.manager.user.AuthenticationManager;
import cn.longmaster.hospital.doctor.core.manager.user.RepresentFunctionManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.ui.consult.adapter.RepresentFunctionGridAdapter;
import cn.longmaster.hospital.doctor.ui.user.BrowserActivity;
import cn.longmaster.utils.SPUtils;

/**
 * 销售代表功能Activity
 * Created by Yang² on 2017/11/7.
 */

public class RepresentFunctionActivity extends BaseActivity {
    @FindViewById(R.id.activity_represent_function_list_gv)
    private GridView mFunctionListGv;
    @AppApplication.Manager
    private AuthenticationManager mAuthenticationManager;
    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    private RepresentFunctionManager mRepresentFunctionManager;

    private RepresentFunctionGridAdapter mRepresentFunctionGridAdapter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_represent_function);
        ViewInjecter.inject(this);
        initView();
        getFunctionList();
    }

    private void initView() {
        mRepresentFunctionGridAdapter = new RepresentFunctionGridAdapter(this);
        mFunctionListGv.setAdapter(mRepresentFunctionGridAdapter);
        mRepresentFunctionGridAdapter.setOnAdapterItemClickListener((position, info) -> {
            if (position == 0) {
                Intent mIntent = new Intent(getActivity(), SchedulingActivity.class);
                startActivity(mIntent);
            } else {
                getAuthentication(info.getUrl());
            }
        });
    }

    private void getFunctionList() {
        mRepresentFunctionManager.getRepresentFunction((baseResult, representFunctionInfos) -> {
            Logger.logI(Logger.USER, "getFunctionList-->baseResult:" + baseResult.getCode() + ",representFunctionInfos:" + representFunctionInfos);
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                List<RepresentFunctionInfo> list = new ArrayList<>();
                list.add(new RepresentFunctionInfo());
                list.addAll(representFunctionInfos);
                mRepresentFunctionGridAdapter.setData(list);
            } else {
                showToast(R.string.no_network_connection);
            }
        });
    }

    /**
     * 拉取鉴权
     *
     * @param configUrl
     */
    private void getAuthentication(final String configUrl) {
        mProgressDialog = createProgressDialog(getString(R.string.loading), true);
        mAuthenticationManager.getAuthenticationInfo(new AuthenticationManager.GetAuthenticationListener() {
            @Override
            public void onGetAuthentication() {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }
                startBrowserActivity(configUrl);
            }

            @Override
            public void onTimeOut() {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }
                showToast(R.string.no_network_connection);
            }
        });
    }

    /**
     * 启动内置浏览器
     *
     * @param configUrl
     */
    private void startBrowserActivity(String configUrl) {
        Intent intent = new Intent(this, BrowserActivity.class);
        String urlStringBuilder = configUrl + (configUrl.contains("?") ? "&user_id=" : "?user_id=") +
                mUserInfoManager.getCurrentUserInfo().getUserId() +
                "&c_auth=" +
                SPUtils.getInstance().getString(AppPreference.KEY_AUTHENTICATION_AUTH, "");
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, urlStringBuilder);
        startActivity(intent);
    }
}
