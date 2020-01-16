package cn.longmaster.hospital.doctor.ui.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.manager.common.DcpManager;
import cn.longmaster.hospital.doctor.core.manager.user.AccountManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.ui.BaseActivity;

/**
 * 联系我们
 * Created by Yang² on 2016/6/3.
 */
public class ContactUsActivity extends BaseActivity {

    @FindViewById(R.id.activity_contact_us_tel_tv)
    private TextView mTelTv;

    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    private DcpManager mDcpManager;
    @AppApplication.Manager
    private AccountManager mAccountManager;

    private long[] mHits;
    private int mCount;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ViewInjecter.inject(this);
        initData();
    }

    private void initData() {
        mCount = 0;
        mHits = new long[5];
    }

    @OnClick({R.id.activity_contact_us_tel_tv, R.id.activity_contact_us_logo_iv})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_contact_us_tel_tv:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "400-860-3939"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

            case R.id.activity_contact_us_logo_iv:
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
                            if (mToast != null) {
                                mToast.cancel();
                            }
                            showChooseAddress();
                        }
                    }
                    Logger.logD(Logger.COMMON, "onClick->mCount:" + mCount);
                    if (mCount > 1 && mCount < 5) {
                        setToast(5 - mCount);
                    }
                }
                break;
            default:
                break;
        }

    }

    private void setToast(int count) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, getString(R.string.choose_address_click_tip, count), Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * 显示选择服务器界面
     */
    private void showChooseAddress() {
        Intent intent = new Intent(this, ChooseAddressActivity.class);
        startActivity(intent);
    }
}
