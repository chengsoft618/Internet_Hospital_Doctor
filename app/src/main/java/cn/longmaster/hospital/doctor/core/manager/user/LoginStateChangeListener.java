package cn.longmaster.hospital.doctor.core.manager.user;

/**
 * 登录状态变化回调
 * Created by yangyong on 16/7/28.
 */
public interface LoginStateChangeListener {
    void onLoginStateChanged(int code, int msg);
}
