package cn.longmaster.hospital.doctor.ui.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.Display;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.manager.common.ShareManager;
import cn.longmaster.hospital.doctor.ui.AppDisplay;
import cn.longmaster.hospital.doctor.ui.LoginActivity;
import cn.longmaster.hospital.doctor.view.dialog.KickOffDialog;
import cn.longmaster.utils.SPUtils;
import cn.longmaster.utils.StringUtils;
import io.reactivex.disposables.CompositeDisposable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 基本的Activity，程序所有Activity都必须继承本类。
 * Created by yangyong on 2015-07-07.
 */
public abstract class NewBaseActivity extends FragmentActivity {
    private Display display;
    public final String TAG = "TAG_" + this.getClass().getSimpleName();
    /**
     * tintManager.setStatusBarTintEnabled(true);
     * tintManager.setNavigationBarTintEnabled(true);
     * tintManager.setTintColor(Color.parseColor("#99000FF"));
     */
    protected SystemBarTintManager tintManager;
    private long mBtnClickedTime;
    private final long BTN_CLICK_MIN_INTERVAL = 1000;
    private boolean activityIsForeground;
    /**
     * 分页大小
     */
    protected final int PAGE_SIZE = 20;
    /**
     * 最小分页索引
     */
    protected final int MIN_PAGE_INDEX_0 = 0;
    /**
     * 最小分页索引
     */
    protected final int MIN_PAGE_INDEX_1 = 1;
    protected int PAGE_INDEX = 1;
    /**
     * 分享管理器
     */
    protected ShareManager mShareManager;
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();
    private boolean mEnableShowKickoff = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        display = new AppDisplay(this);
        handleIntent(getIntent(), display);
        AppApplication.getInstance().injectManager(this);
        mShareManager = new ShareManager(this);
        initDatas();
        setContentView(getContentViewId());
        ViewInjecter.inject(this);
        initViews();
    }

    protected void handleIntent(Intent intent, Display display) {

    }

    public void onBackClick(View view) {
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent, getDisplay());
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityIsForeground = true;
        if (SPUtils.getInstance().getBoolean(AppPreference.FLAG_BACKGROUND_KICKOFF, false) && mEnableShowKickoff) {
            boolean popToLogin = true;
            if (getClass().getSimpleName().equals(LoginActivity.class.getSimpleName())) {
                popToLogin = false;
            }
            SPUtils.getInstance().put(AppPreference.FLAG_BACKGROUND_KICKOFF, false);
            Intent intent = new Intent(this, KickOffDialog.class);
            intent.putExtra("pop_to_login", popToLogin);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        activityIsForeground = false;
        super.onStop();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initSystemBar();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        initSystemBar();
    }

    protected void initSystemBar() {
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setTintColor(ContextCompat.getColor(this, R.color.color_app_main_color));
    }

    /**
     * 创建等待对话框
     *
     * @param msg        要显示的消息
     * @param cancelable 是否可以取消
     * @return 返回对话框
     */
    protected ProgressDialog createProgressDialog(String msg, boolean cancelable) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setMessage(msg);
        progressDialog.show();
        progressDialog.setCancelable(cancelable);
        return progressDialog;
    }

    /**
     * 创建等待对话框，不可取消
     *
     * @param msg 要显示的消息
     * @return 返回对话框
     */
    protected ProgressDialog createProgressDialog(String msg) {
        return createProgressDialog(msg, false);
    }

    /**
     * 判断是否连续快速点击
     *
     * @return
     */
    protected boolean isFastClick() {
        if (System.currentTimeMillis() - mBtnClickedTime < BTN_CLICK_MIN_INTERVAL) {
            mBtnClickedTime = System.currentTimeMillis();
            return true;
        }
        mBtnClickedTime = System.currentTimeMillis();
        return false;
    }

    protected void setEnableShowKickoff(boolean isEnable) {
        mEnableShowKickoff = isEnable;
    }

    protected abstract void initDatas();

    @LayoutRes
    protected abstract int getContentViewId();

    protected abstract void initViews();

    protected String getString(TextView tv) {
        return tv.getText().toString().trim();
    }

    public int getCompatColor(int color) {
        return ContextCompat.getColor(this, color);
    }

    protected FragmentActivity getThisActivity() {
        return this;
    }

    public Display getDisplay() {
        return checkNotNull(display, "Display not init");
    }

    protected ShareManager getShareManager() {
        return checkNotNull(mShareManager, "ShareManager not init");
    }

    protected boolean isActivityIsForeground() {
        return activityIsForeground;
    }

    protected View createEmptyListView(String errorMsg, @DrawableRes int resId) {
        View view = getLayoutInflater().inflate(R.layout.include_new_no_data_list, null);
        ImageView includeNewNoDataIv = view.findViewById(R.id.include_new_no_data_iv);
        TextView includeNewNoDataTv = view.findViewById(R.id.include_new_no_data_tv);
        if (!StringUtils.isEmpty(errorMsg)) {
            includeNewNoDataTv.setText(errorMsg);
        }
        if (0 != resId) {
            includeNewNoDataIv.setImageResource(resId);
        }
        return view;
    }

    protected View createEmptyListView() {
        return createEmptyListView(null, 0);
    }

    protected View createEmptyListView(String msg) {
        return createEmptyListView(msg, 0);
    }

    protected View createEmptySearchListView(String errorMsg, @DrawableRes int resId) {
        View view = getLayoutInflater().inflate(R.layout.include_new_no_search_data_list, null);
        ImageView includeNewNoDataIv = view.findViewById(R.id.include_new_no_data_iv);
        TextView includeNewNoDataTv = view.findViewById(R.id.include_new_no_data_tv);
        if (!StringUtils.isEmpty(errorMsg)) {
            includeNewNoDataTv.setText(errorMsg);
        }
        if (0 != resId) {
            includeNewNoDataIv.setImageResource(resId);
        }
        return view;
    }

    protected View createEmptySearchListView() {
        return createEmptySearchListView(null, 0);
    }

    protected View createErrorListView(String errorMsg, @DrawableRes int resId, View.OnClickListener listener) {
        View view = getLayoutInflater().inflate(R.layout.include_new_no_data_list, null);
        ImageView includeNewNoDataIv = view.findViewById(R.id.include_new_no_data_iv);
        TextView includeNewNoDataTv = view.findViewById(R.id.include_new_no_data_tv);
        if (StringUtils.isEmpty(errorMsg)) {
            includeNewNoDataTv.setText("点击重试");
        } else {
            includeNewNoDataTv.setText(errorMsg);
        }
        includeNewNoDataTv.setTextColor(getCompatColor(R.color.color_049eff));
        view.setOnClickListener(listener);
        if (resId != 0) {
            includeNewNoDataIv.setImageResource(resId);
        }
        return view;
    }

    protected View createErrorListView(View.OnClickListener listener) {
        return createErrorListView(null, 0, listener);
    }
}
