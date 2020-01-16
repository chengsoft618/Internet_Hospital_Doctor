package cn.longmaster.hospital.doctor.core.manager.user;

/**
 * 版本变化回调
 * Created by yangyong on 16/7/5.
 */
public interface VersionChangeListener {
    /**
     * 新版本
     */
    void onNewVersion();

    /**
     * 当前版本受限
     */
    void onVersionLimited();
}
