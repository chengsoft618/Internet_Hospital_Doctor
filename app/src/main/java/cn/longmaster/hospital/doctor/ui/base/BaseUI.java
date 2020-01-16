package cn.longmaster.hospital.doctor.ui.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import cn.longmaster.hospital.doctor.Display;

/**
 * @author ABiao_Abiao
 * @date 2019/11/13 11:23
 * @description:
 */
public interface BaseUI {
    void handleIntent(Intent intent, Display display);

    @LayoutRes
    int getContentViewId();

    void initDatas();

    void initViews();

    Display getDisplay();

    FragmentActivity getCurrentActivity();

    boolean isFastClick();

    int getCompatColor(int color);

    String getString(TextView tv);

    ProgressDialog createProgressDialog(String msg, boolean cancelable);

    View createEmptyView(String msg);

    View createErrorView(String msg);

    boolean isActivityIsForeground();

}
