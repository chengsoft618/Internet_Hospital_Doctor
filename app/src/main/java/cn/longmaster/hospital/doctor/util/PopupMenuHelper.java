package cn.longmaster.hospital.doctor.util;

import android.app.Activity;
import android.support.annotation.MenuRes;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * @author lm_Abiao
 * @date 2019/5/29 18:21
 * @description:
 */
public class PopupMenuHelper {
    private WeakReference<Activity> activity;
    private View view;
    private int menuRes;
    private PopupMenu.OnMenuItemClickListener onMenuItemClickListener;

    private PopupMenuHelper(View view) {
        this.view = view;
    }

    public static PopupMenuHelper init(View view) {
        return new PopupMenuHelper(view);
    }

    public PopupMenuHelper addActivity(Activity activity) {
        this.activity = new WeakReference<>(activity);
        return this;
    }

    public PopupMenuHelper addMenuRes(@MenuRes int menuRes) {
        this.menuRes = menuRes;
        return this;
    }

    public PopupMenuHelper addMenuItemClickListener(PopupMenu.OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
        return this;
    }

    public void show() {
        //创建弹出式菜单对象（最低版本11）
        //第二个参数是绑定的那个view
        PopupMenu popup = new PopupMenu(activity.get(), view);
        //获取菜单填充器
        MenuInflater inflater = popup.getMenuInflater();
        //填充菜单
        inflater.inflate(menuRes, popup.getMenu());
        //绑定菜单项的点击事件
        popup.setOnMenuItemClickListener(onMenuItemClickListener);
        //这一行代码不要忘记了
        popup.show();
    }
}
