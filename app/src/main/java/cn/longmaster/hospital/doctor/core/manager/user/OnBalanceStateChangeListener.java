package cn.longmaster.hospital.doctor.core.manager.user;

/**
 * 余额变化状态监听
 * Created by yangyong on 16/8/9.
 */
public interface OnBalanceStateChangeListener {
    void onBalanceStateChanged(int result, double totalValue, double availaValue);
}
