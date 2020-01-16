package cn.longmaster.hospital.doctor.core.manager.room;


import java.io.File;
import java.io.FileInputStream;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfo;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.phoneplus.audioadapter.model.AudioAdapter;
import cn.longmaster.utils.SPUtils;

/**
 * 语音适配管理类
 * Created by yangyong on 15/9/1.
 */
public class AudioAdapterManager extends BaseManager {
    private static final String TAG = AudioAdapterManager.class.getSimpleName();
    private static final String mAMXFileName = "amx.xml";
    private AudioAdapter mAudioAdapter;
    private boolean isDownLoading = false;
    private Runnable reloadRunnable;

    private UserInfoManager mUserInfoManager;

    @Override
    public void onManagerCreate(AppApplication application) {
        mUserInfoManager = getManager(UserInfoManager.class);
        mAudioAdapter = new AudioAdapter(application, AppConfig.IS_DEBUG_MODE ? 0 : -1);
        if (mUserInfoManager.getCurrentUserInfo().getUserId() != 0) {
            loadConfig(getFilePath());
            refreshData(false);
        }
    }

    public AudioAdapter getAudioAdapter() {
        return mAudioAdapter;
    }

    private static File getFilePath() {
        return new File(AppApplication.getInstance().getApplicationContext().getFilesDir(), "audio_adapter/mode.xml");
    }

    /**
     * 设置音频模式
     * 开始进入聊天室之前调用：AudioModule的NAME_PROCESSINCALL
     * 初始化成功后，调用：AudioModule的NAME_AFTERVOICE
     * 接着调用：AudioModule的NAME_SPEAKERON
     * 视频完成后，调用AudioModule的NAME_RESET
     */
    public void setMode(String mode) {
        mAudioAdapter.setRightMode(mode);
    }

    public void clearModeConfig() {
        getFilePath().delete();
        SPUtils.getInstance().put(AppPreference.KEY_AMX_TOKEN, "-1");
        mAudioAdapter.initData();
    }

    /**
     * 刷新XML
     *
     * @param isForced 是否强制刷新，强制刷新：删除本地文件，重新下载
     */
    public void refreshData(boolean isForced) {
        if (isDownLoading) {
            reloadRunnable = new ReloadRunnable(isForced);
        } else {
            Logger.logD(Logger.COMMON, "开始下载 audio adapter XML");
            final String currentToken = SPUtils.getInstance().getString(AppPreference.KEY_AMX_TOKEN, "-1");
            AudioAdapterDownloader audioAdapterDownloader = new AudioAdapterDownloader(isForced, currentToken, getFilePath(), new AudioAdapterDownloader.OnAudioAdapterDownloadListener() {
                @Override
                public void onAudioAdapterDownloadSuccess(String token, File file) {
                    Logger.logD(Logger.COMMON, "下载 audio adapter XML 成功");

                    isDownLoading = false;
                    SPUtils.getInstance().put(AppPreference.KEY_AMX_TOKEN, token);
                    if (reloadRunnable != null) {
                        reloadRunnable.run();
                        reloadRunnable = null;
                    } else {
                        loadConfig(file);
                    }
                }

                @Override
                public void onAudioAdapterDownloadFailed() {
                    Logger.logD(Logger.COMMON, "下载 audio adapter XML 失败");
                    isDownLoading = false;
                    if (reloadRunnable != null) {
                        reloadRunnable.run();
                    }
                }
            });
            isDownLoading = true;
            audioAdapterDownloader.start();
        }
    }

    private void loadConfig(File file) {
        if (isDownLoading) {
            return;
        }

        UserInfo userInfo = mUserInfoManager.getCurrentUserInfo();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            if (!mAudioAdapter.initData(fileInputStream, userInfo.getLoginAuthKey())) {
                Logger.logD(Logger.COMMON, "initData失败,调用默认的initData方法！");
                mAudioAdapter.initData();
            }
            Logger.logD(Logger.COMMON, "Audio加载成功！");
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            mAudioAdapter.initData();
        }
    }

    private class ReloadRunnable implements Runnable {
        private boolean isForced;

        public ReloadRunnable(boolean isForced) {
            this.isForced = isForced;
        }

        @Override
        public void run() {
            refreshData(isForced);
        }
    }
}
