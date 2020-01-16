package cn.longmaster.hospital.doctor.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.Display;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.ui.AppDisplay;
import cn.longmaster.utils.StringUtils;
import io.reactivex.disposables.CompositeDisposable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 基本的Fragment，程序所有Fragment都必须继承本类。
 * Created by biao on 2019-05-29.
 */
public abstract class NewBaseFragment extends Fragment {
    private long mBtnClickedTime;
    private final long BTN_CLICK_MIN_INTERVAL = 1000;
    public final String TAG = "TAG_" + this.getClass().getSimpleName();
    private Display display;
    protected final int PAGE_SIZE = 20;
    protected final int MIN_PAGE_INDEX_1 = 1;
    protected final int MIN_PAGE_INDEX_0 = 0;
    protected int PAGE_INDEX = 1;
    protected CompositeDisposable compositeDisposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.logI(Logger.COMMON, TAG + "#onCreate");
        compositeDisposable = new CompositeDisposable();
        display = new AppDisplay(this);
        AppApplication.getInstance().injectManager(this);
        initDatas();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getContentViewId(), container, false);
        ViewInjecter.inject(this, rootView);
        initAdapter();
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    public void onBackClick(View view) {
        getBaseActivity().finish();
    }

    /**
     * @return void
     * @description: 初始化数据
     * @author Abiao
     * @date 2018/11/28 9:48
     */
    protected void initDatas() {

    }

    /**
     * @return int
     * @description: 获取contentView
     * @author Abiao
     * @date 2018/11/28 9:48
     */
    @LayoutRes
    protected abstract int getContentViewId();

    /**
     * @param
     * @return void
     * @description: 初始化控件
     * @author Abiao
     * @date 2019/1/30 15:13
     */
    public abstract void initViews(View rootView);

    protected void initAdapter() {

    }

    /**
     * @param
     * @return android.view.View.OnClickListener
     * @description:
     * @author Abiao
     * @date 2019/1/24 14:55
     */
    protected View.OnClickListener setNetErrorListener() {
        return null;
    }

    /**
     * @param
     * @return android.view.View.OnClickListener
     * @description:
     * @author Abiao
     * @date 2019/1/24 14:55
     */
    protected View.OnClickListener setNoDataListener() {
        return null;
    }

    public FragmentActivity getBaseActivity() {
        return checkNotNull(getActivity(), TAG + "#getActivity is null");
    }

    /**
     * 显示输入法
     *
     * @param editText
     */
    public void showSoftInput(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getBaseActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 创建等待对话框
     *
     * @param msg        要显示的消息
     * @param cancelable 是否可以取消
     * @return 返回对话框
     */
    public ProgressDialog createProgressDialog(String msg, boolean cancelable) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(false);
        progressDialog.setMessage(msg);
        progressDialog.show();
        progressDialog.setCancelable(cancelable);
        return progressDialog;
    }

    /**
     * 创建等待对话框
     *
     * @param msg 要显示的消息
     * @return 返回对话框
     */
    public ProgressDialog createProgressDialog(String msg) {
        return createProgressDialog(msg, false);
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

    protected int getCompatColor(@ColorRes int color) {
        return ContextCompat.getColor(getActivity(), color);
    }

    protected String getString(TextView view) {
        return view.getText().toString().trim();
    }

    protected Display getDisplay() {
        return checkNotNull(display, "Display can not be null");
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
