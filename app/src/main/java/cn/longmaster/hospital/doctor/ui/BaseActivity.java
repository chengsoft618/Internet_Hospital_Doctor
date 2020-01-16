package cn.longmaster.hospital.doctor.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.ColorRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import cn.longmaster.doctorlibrary.util.common.FileUtil;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.manager.common.ShareManager;
import cn.longmaster.hospital.doctor.view.dialog.KickOffDialog;
import cn.longmaster.utils.SPUtils;
import io.reactivex.disposables.CompositeDisposable;

/**
 * 基本的Activity，程序所有Activity都必须继承本类。
 * Created by yangyong on 2015-07-07.
 *
 * @see cn.longmaster.hospital.doctor.ui.base.NewBaseActivity
 */
@Deprecated
public abstract class BaseActivity extends FragmentActivity {
    protected final String TAG = this.getClass().getSimpleName();
    /**
     * tintManager.setStatusBarTintEnabled(true);
     * tintManager.setNavigationBarTintEnabled(true);
     * tintManager.setTintColor(Color.parseColor("#99000FF"));
     */
    protected SystemBarTintManager tintManager;
    private boolean mForeGround;
    private boolean mEnableShowKickoff = true;
    /**
     * 当前界面中的EditText集合，当触摸屏幕的非EditTextView区域时，隐藏软键盘
     */
    private Set<EditText> mEditTextViewSet;
    private long mBtnClickedTime;
    private final long BTN_CLICK_MIN_INTERVAL = 1000;
    protected CompositeDisposable compositeDisposable;
    /**
     * 分享管理器
     */
    private ShareManager mShareManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();
        AppApplication.getInstance().injectManager(this);
        setShareManager(new ShareManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mEditTextViewSet = new HashSet<EditText>();
        addEditTextView(getWindow().getDecorView());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mForeGround = true;
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
        super.onStop();
        mForeGround = false;
    }


    public int getCompatColor(int color) {
        return ContextCompat.getColor(this, color);
    }

    public Drawable getCompatDrawable(int drawableId) {
        return ContextCompat.getDrawable(this, drawableId);
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected int getResColor(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    public SystemBarTintManager getTintManager() {
        return tintManager;
    }

    protected void initSystemBar() {
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setTintColor(ContextCompat.getColor(this, R.color.color_app_main_color));
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

    public ShareManager getShareManager() {
        return mShareManager;
    }

    private void setShareManager(ShareManager shareManager) {
        mShareManager = shareManager;
    }

    /**
     * 获得管理器
     *
     * @param manager 管理器类型
     * @param <M>     管理器Class
     * @return 管理器
     */
    public <M extends BaseManager> M getManager(Class<M> manager) {
        return AppApplication.getInstance().getManager(manager);
    }

    public void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    public BaseActivity getActivity() {
        return this;
    }

    public void onBackClick(View view) {
        finish();
    }

    /**
     * 显示输入法
     *
     * @param editText
     */
    public void showSoftInput(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }

    public void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected void hideSoftInput() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 创建等待对话框
     *
     * @param msg        要显示的消息
     * @param cancelable 是否可以取消
     * @return 返回对话框
     */
    public ProgressDialog createProgressDialog(String msg, boolean cancelable) {
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
    public ProgressDialog createProgressDialog(String msg) {
        return createProgressDialog(msg, false);
    }

    public boolean isForeGround() {
        return mForeGround;
    }

    public void setEnableShowKickoff(boolean isEnable) {
        mEnableShowKickoff = isEnable;
    }

    /**
     * 将界面中所有的EditText放到集合中
     *
     * @param v view
     */
    private void addEditTextView(View v) {
        if (v instanceof EditText) {
            mEditTextViewSet.add((EditText) v);
        } else if (v instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) v;
            for (int i = 0; i < group.getChildCount(); i++) {
                addEditTextView(group.getChildAt(i));
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {//触摸屏幕位置不在EditText View内，隐藏软键盘
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            //long start = System.currentTimeMillis();
            boolean isContains = false;
            for (EditText v : mEditTextViewSet) {
                final Rect rect = new Rect();
                final int[] location = new int[2];

                v.getLocationOnScreen(location);
                rect.set(location[0], location[1], location[0] + v.getWidth(), location[1] + v.getHeight());

                final int x = (int) ev.getX();
                final int y = (int) ev.getY();

                if (rect.contains(x, y)) {
                    isContains = true;
                    break;
                }
            }
            if (!isContains) {
                hideSoftInput();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判断是否连续快速点击
     *
     * @return
     */
    public boolean isFastClick() {
        if (System.currentTimeMillis() - mBtnClickedTime < BTN_CLICK_MIN_INTERVAL) {
            mBtnClickedTime = System.currentTimeMillis();
            return true;
        }
        mBtnClickedTime = System.currentTimeMillis();
        return false;
    }

    /**
     * 保存到本地相册
     *
     * @param bitmap 图片bitmap
     * @return 图片路径
     */
    public String savePictureToLocal(Bitmap bitmap) {
        String bitPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "", "");
        if (TextUtils.isEmpty(bitPath)) {
            return "";
        }
        Uri uuUri = Uri.parse(bitPath);
        String path = FileUtil.getRealPathFromURI(this, uuUri);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(new File(path)));
        sendBroadcast(intent);
        return path;
    }
}
