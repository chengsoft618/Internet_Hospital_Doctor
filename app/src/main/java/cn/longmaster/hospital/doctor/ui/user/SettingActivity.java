package cn.longmaster.hospital.doctor.ui.user;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.allen.library.SuperTextView;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.user.VersionInfo;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.hospital.doctor.core.manager.user.VersionManager;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.user.VersionRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.utils.SPUtils;
import cn.longmaster.utils.Utils;

public class SettingActivity extends NewBaseActivity {
    @FindViewById(R.id.activity_set_version_number_tv)
    private TextView mVersionNumberTv;
    @FindViewById(R.id.activity_set_clear_cache_stv)
    private SuperTextView mCacheTv;//清除缓存
    @FindViewById(R.id.activity_set_version_update_stv)
    private SuperTextView activitySetVersionUpdateStv;

    @Override
    protected void initDatas() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initViews() {
        initView();
        checkVersion();
    }

    private void initView() {
        String appVersionName = Utils.getAppVersion();
        mVersionNumberTv.setText(getString(R.string.version_number, appVersionName));
        mCacheTv.setRightString(getString(R.string.cache_size, SdManager.getInstance().getAllFileSize() + ""));
    }

    @OnClick({R.id.activity_set_change_passed_stv,
            R.id.activity_set_contact_us_stv,
            R.id.activity_set_common_problems_stv,
            R.id.activity_set_service_terms_stv,
            R.id.activity_set_newbie_guide_stv,
            R.id.activity_set_clear_cache_stv,
            R.id.activity_set_version_update_stv})
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.activity_set_change_passed_stv:
                getDisplay().startPasswordChangeActivity(0);
                break;
            case R.id.activity_set_contact_us_stv://联系我们
                intent.setClass(SettingActivity.this, ContactUsActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_set_common_problems_stv://常见问题
                intent.setClass(SettingActivity.this, BrowserActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TITLE, getResources().getString(R.string.user_faq));
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, AppConfig.getCommonProblemUrl());
                startActivity(intent);
                break;
            case R.id.activity_set_service_terms_stv://服务条款
                intent.setClass(getThisActivity(), BrowserActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TITLE, getResources().getString(R.string.user_service_contract));
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, AppConfig.getAgreenUrl());
                startActivity(intent);
                break;
            case R.id.activity_set_newbie_guide_stv://新手引导
                intent.setClass(getThisActivity(), BrowserActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, AppConfig.getNewbieGuideUrl());
                startActivity(intent);
                break;
            case R.id.activity_set_clear_cache_stv://清除缓存
                showClearCacheDialog();
                break;

            case R.id.activity_set_version_update_stv://版本更新
                if (VersionManager.getInstance().getCurentClientVersion() < SPUtils.getInstance().getInt(AppPreference.KEY_SERVER_LASTEST_VERSION, 0)) {
                    VersionManager.getInstance().showUpdateDialog(getThisActivity());
                } else {
                    showNoUpdateDialog();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 检查更新
     */
    private void checkVersion() {
        new VersionRequester((baseResult, versionInfo) -> {
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                setVersion(versionInfo);
            }
        }).doPost();
    }

    private void setVersion(VersionInfo versionInfo) {
        SPUtils.getInstance().put(AppPreference.KEY_SERVER_LASTEST_VERSION, versionInfo.getClientVersionLatest());
        if (VersionManager.getInstance().getCurentClientVersion() < SPUtils.getInstance().getInt(AppPreference.KEY_SERVER_LASTEST_VERSION, 0)) {
            activitySetVersionUpdateStv.setRightTvDrawableRight(ContextCompat.getDrawable(getThisActivity(), R.drawable.ic_version_new));
            activitySetVersionUpdateStv.setRightString(getString(R.string.version_can_update));
        } else {
            activitySetVersionUpdateStv.setRightString(getString(R.string.version_is_new));
        }
    }

    /**
     * 清除缓存
     */
    private void showClearCacheDialog() {
        new CommonDialog.Builder(this)
                .setMessage(R.string.sure_clear_cache)
                .setPositiveButton(R.string.cancel, () -> {

                })
                .setNegativeButton(R.string.confirm, () -> SdManager.getInstance().cleanAllFilePath(() -> mCacheTv.setRightString(getString(R.string.cache_size, "0"))))
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .show();
    }


    /**
     * 已是最新版本
     */
    private void showNoUpdateDialog() {
        new CommonDialog.Builder(this)
                .setMessage(R.string.version_dialog_is_new)
                .setNegativeButton(R.string.confirm, () -> {

                })
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .show();
    }


}
