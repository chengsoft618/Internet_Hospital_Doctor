package cn.longmaster.hospital.doctor.core.manager.im;

import com.lmmedia.PPAmrPlayer;
import com.lmmedia.PPAmrPlayer.OnStateListener;

import java.io.File;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;

/**
 * Created by YY on 17/9/4.
 */

public class AudioPlayManager extends BaseManager {
    private final String TAG = AudioPlayManager.class.getSimpleName();
    private PPAmrPlayer mAudioPlayer;
    private OnStateListener mOnStateListener;
    private String mCurrentFilePath = "";
    private String mFileUrl = "";

    @Override
    public void onManagerCreate(AppApplication application) {
        mAudioPlayer = new PPAmrPlayer();
    }

    public String getCurrentPlayAudio() {
        return mCurrentFilePath;
    }

    public boolean isPlaying() {
        return mAudioPlayer.isPlaying();
    }

    public void stopPlay() {
        mAudioPlayer.stop();
        mOnStateListener.onStop(mCurrentFilePath);
    }

    public void startAudioPlay(String filePath, String url, OnStateListener listener) {
        Logger.logD(Logger.IM, TAG + "->downloadAudioFile()->onDownloadFinish()->filePath:" + filePath + ", url:" + url);
        mCurrentFilePath = filePath;
        mFileUrl = url;
        mOnStateListener = listener;
        File file = new File(filePath);
        if (file.exists()) {
            play();
        } else {
            downloadAudioFile();
        }
    }

    private void play() {
        mAudioPlayer.setDataSource(mCurrentFilePath);
        mAudioPlayer.setOnStateListener(mOnStateListener);
        mAudioPlayer.start();
    }

    private void downloadAudioFile() {
        new AudioDownloader().download(mCurrentFilePath, mFileUrl, new AudioDownloadListener() {
            @Override
            public void onProgressChange(String filePath, int totalSize, int currentSize) {
                Logger.logD(Logger.IM, TAG + "->downloadAudioFile()-->onProgressChange()->filePath:" + filePath + ", totalSize:" + totalSize + ", currentSize:" + currentSize);
            }

            @Override
            public void onDownloadFinish(String path, DownloadResult downloadResult) {
                Logger.logD(Logger.IM, TAG + "->downloadAudioFile()->onDownloadFinish()->path:" + path + ", downloadResult:" + downloadResult);
                if (!path.equals(mCurrentFilePath)) {
                    return;
                }
                if (downloadResult == DownloadResult.SUCCESSFUL) {
                    play();
                } else {
                    mOnStateListener.onError(path);
                }
            }
        });
    }
}
