package cn.longmaster.hospital.doctor.ui.consult;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import java.io.IOException;

import cn.longmaster.doctorlibrary.util.common.DateUtil;
import cn.longmaster.doctorlibrary.util.common.FileUtil;
import cn.longmaster.doctorlibrary.util.fileloader.downloader.FileLoadListener;
import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.util.screen.ScreenUtil;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.download.MediaDownloadManager;
import cn.longmaster.hospital.doctor.core.receiver.PhoneStateReceiver;
import cn.longmaster.hospital.doctor.core.receiver.ScreenReceiver;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.view.AppActionBar;
import cn.longmaster.utils.DisplayUtil;

/**
 * 视频播放器
 * Created by JinKe on 2016-11-16.
 */
public class VideoPlayerActivity extends BaseActivity implements MediaPlayer.OnPreparedListener, SurfaceHolder.Callback, OnSeekBarChangeListener, FileLoadListener {
    private final String TAG = VideoPlayerActivity.class.getSimpleName();
    private final String KEY_UPLOAD_PROGRESS = "key_upload_progress";
    private final String KEY_CURRENT_POSITION = "key_current_position";
    private final String MESSAGE_UPLOAD_PROGRESS = "message_upload_progress";
    private final String TIME_FORMAT = "mm:ss";

    @FindViewById(R.id.activity_video_play_ab)
    private AppActionBar mActionBar;
    @FindViewById(R.id.activity_video_play_surface_sv)
    private SurfaceView mVideoSurfaceSv;
    @FindViewById(R.id.activity_video_play_voice_iv)
    private ImageView mVoiceIv;
    @FindViewById(R.id.activity_video_play_surface_view_rl)
    private RelativeLayout mVideoSurfaceRl;
    @FindViewById(R.id.activity_video_play_pause_play_ib)
    private ImageButton mPlayPauseIb;
    @FindViewById(R.id.activity_video_play_current_time_tv)
    private TextView mCurrentTimeTv;
    @FindViewById(R.id.activity_video_play_progress_bar_sb)
    private SeekBar mProgressSb;
    @FindViewById(R.id.activity_video_play_all_time_tv)
    private TextView mAllTimeTv;
    @FindViewById(R.id.activity_video_play_progress_rl)
    private RelativeLayout mProgressRl;
    @AppApplication.Manager
    private MediaDownloadManager mPlayManager;

    private String mPath;
    private String mTempPath;
    private String mUrl;
    private int mMediaType;
    private int mMaxDuration;
    private int mCurrentPosition;
    private boolean mHandlePost = false;

    private SurfaceHolder mSurfaceHolder;
    private MediaPlayer mMediaPlayer;

    private Handler mUploadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (MESSAGE_UPLOAD_PROGRESS.equals(msg.getData().getString(KEY_UPLOAD_PROGRESS)) && mHandlePost) {
                if (mMediaPlayer.isPlaying()) {
                    mCurrentTimeTv.setText(DateUtil.millisecondToFormatDate(TIME_FORMAT, mMediaPlayer.getCurrentPosition()));
                    mProgressSb.setProgress(msg.getData().getInt(KEY_CURRENT_POSITION));
                }
                mUploadHandler.post(mUpProgressRunnable);
            }
        }
    };

    private Runnable mUpProgressRunnable = new Runnable() {
        @Override
        public void run() {
            if (mMediaPlayer == null) {
                return;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int currentPosition = mMediaPlayer.getCurrentPosition();
            int maxPosition = mMediaPlayer.getDuration();
            Message msg = mUploadHandler.obtainMessage();
            Bundle bundle = msg.getData();
            bundle.putString(KEY_UPLOAD_PROGRESS, MESSAGE_UPLOAD_PROGRESS);
            bundle.putInt(KEY_CURRENT_POSITION, currentPosition);
            msg.setData(bundle);
            Logger.logD(Logger.APPOINTMENT, "mUpProgressRunnable->currentPosition:" + currentPosition + "->maxPosition:" + maxPosition);
            if ((currentPosition + 100 > maxPosition) && (currentPosition != 0 && maxPosition != 0)) {
                videoPlayOverDeal();
            } else {
                mUploadHandler.sendMessage(msg);
            }
        }
    };

    private ScreenReceiver mScreenReceiver = new ScreenReceiver(new ScreenReceiver.ScreenStateListener() {
        @Override
        public void onScreenOn() {
        }

        @Override
        public void onScreenOff() {
            pauseVideoPlay();
        }

        @Override
        public void onUserPresent() {
        }
    });

    private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                pauseVideoPlay();
            }
        }
    };

    private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
        String SYSTEM_REASON = "reason";
        String SYSTEM_HOME_KEY = "homekey";
        String SYSTEM_HOME_KEY_LONG = "recentapps";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (TextUtils.equals(reason, SYSTEM_HOME_KEY) || TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)) {
                    Logger.logD(Logger.APPOINTMENT, TAG + "->mHomeKeyEventReceiver");
                    mMediaPlayer.stop();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_video_player);
        ViewInjecter.inject(this);
        initData();
        initView();
        regListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mMediaPlayer != null && FileUtil.isFileExist(mPath)) {
            mCurrentPosition = mMediaPlayer.getCurrentPosition();
            mMediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        Logger.logD(Logger.APPOINTMENT, TAG + "onDestroy");
        mPlayManager.removeLoadListener(mTempPath);
        exitVideoPlay();
        super.onDestroy();
        unregisterReceiver(mScreenReceiver);
        PhoneStateReceiver.removePhoneListener(mPhoneStateListener);
        unregisterReceiver(mHomeKeyEventReceiver);
    }

    @OnClick({R.id.activity_video_play_pause_play_ib, R.id.activity_video_play_surface_play_iv})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.activity_video_play_pause_play_ib:
            case R.id.activity_video_play_surface_play_iv:
                Logger.logI(Logger.APPOINTMENT, TAG + "OnClick-->mMediaPlayer.isPlaying()-->" + mMediaPlayer.isPlaying());
                Logger.logI(Logger.APPOINTMENT, TAG + "OnClick-->FileUtil.isFileExist(mPath)-->" + FileUtil.isFileExist(mPath) + "-->mPath-->" + mPath);
                if (FileUtil.isFileExist(mPath)) {
                    if (mMediaPlayer.isPlaying()) {
                        pauseVideoPlay();
                    } else {
                        continueVideoPlay();
                    }
                } else {
                    downLoadDate();
                }

                break;
        }
    }

    /**
     * actionbar返回点击事件
     *
     * @param view 点击的对象
     */
    public void exitClick(View view) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->exitClick");
        exitVideoPlay();
    }

    private void initData() {
        mMaxDuration = 0;
        mCurrentPosition = 0;
        mPath = getIntent().getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_PATH);
        mUrl = getIntent().getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_URL);
        setTempPath();
        mActionBar.setTitle(getIntent().getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_TITLE));
        mMediaType = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDIA_TYPE, AppConstant.MediaType.MEDIA_TYPE_VOICE);
        Logger.logD(Logger.APPOINTMENT, TAG + "->initData->mTempPath:" + mTempPath + "->mUrl:" + mUrl);
        mMediaPlayer = new MediaPlayer();
        mSurfaceHolder = mVideoSurfaceSv.getHolder();
        mSurfaceHolder.setKeepScreenOn(true);
    }

    private void initView() {
        Logger.logD(Logger.APPOINTMENT, TAG + "->initView->是否正在下载:" + mPlayManager.isLoading(mTempPath));
        if (mPlayManager.isLoading(mTempPath)) {
            mProgressRl.setVisibility(View.VISIBLE);
            mVoiceIv.setVisibility(View.VISIBLE);
            mVideoSurfaceRl.setVisibility(View.GONE);
            Logger.logD(Logger.APPOINTMENT, TAG + "->initView->");
            mPlayPauseIb.setEnabled(false);
            mProgressSb.setEnabled(false);
        } else {
            if (FileUtil.isFileExist(mPath)) {
                mVideoSurfaceRl.setVisibility(View.VISIBLE);
                if (AppConstant.MediaType.MEDIA_TYPE_VIDEO == mMediaType) {
                    mVoiceIv.setVisibility(View.GONE);
                } else {
                    mVoiceIv.setVisibility(View.VISIBLE);
                }
                mProgressRl.setVisibility(View.GONE);
                mPlayPauseIb.setEnabled(true);
                mProgressSb.setEnabled(true);
            } else {
                mProgressRl.setVisibility(View.GONE);
                mVideoSurfaceRl.setVisibility(View.VISIBLE);
                mVoiceIv.setVisibility(View.VISIBLE);
                mPlayPauseIb.setEnabled(true);
                mProgressSb.setEnabled(false);
            }
        }
    }

    private void regListener() {
        mProgressSb.setOnSeekBarChangeListener(this);
        mSurfaceHolder.addCallback(this);
        if (mPlayManager.isLoading(mTempPath)) {
            mPlayManager.addLoadListener(mTempPath, this);
        }
        regScreenListener();
        PhoneStateReceiver.addPhoneListener(mPhoneStateListener);
        registerReceiver(mHomeKeyEventReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    private void downLoadDate() {
        if (!FileUtil.isFileExist(mPath) && !mPlayManager.isLoading(mTempPath)) {
            Logger.logD(Logger.APPOINTMENT, TAG + "->downLoadDate->下载:");
            mProgressRl.setVisibility(View.VISIBLE);
            mVoiceIv.setVisibility(View.VISIBLE);
            mVideoSurfaceRl.setVisibility(View.GONE);
            mPlayPauseIb.setEnabled(false);
            mProgressSb.setEnabled(false);
            mPlayManager.fileDownload(mTempPath, mUrl, this);
        }
    }

    /**
     * 设置surfaceView大小
     */
    private void setSurfaceSize() {
        if (mMediaPlayer != null && FileUtil.isFileExist(mPath)) {
            int screenWidth = ScreenUtil.getScreenWidth();
            int screenHeight = ScreenUtil.getScreenHeight() - ScreenUtil.getStatusHeight(this) - DisplayUtil.dip2px(104);
            int videoWidth = mMediaPlayer.getVideoWidth();
            int videoHeight = mMediaPlayer.getVideoHeight();
            if (videoWidth <= 0 || videoHeight <= 0) {
                return;
            }
            LayoutParams lp = mVideoSurfaceSv.getLayoutParams();
            if (screenWidth * videoHeight <= screenHeight * videoWidth) {
                lp.width = screenWidth;
                lp.height = videoHeight * screenWidth / videoWidth;
            } else {
                lp.width = videoWidth * screenHeight / videoHeight;
                lp.height = screenHeight;
            }
            mVideoSurfaceSv.setLayoutParams(lp);
            mVideoSurfaceRl.setLayoutParams(lp);
        }

    }

    /**
     * 获取临时路径
     */
    private void setTempPath() {
        if (!TextUtils.isEmpty(mPath)) {
            mTempPath = mPath.substring(0, mPath.lastIndexOf("/")) + "/temp" + mPath.substring(mPath.lastIndexOf("/"));
        }
        Logger.logD(Logger.APPOINTMENT, TAG + "->setTempPath->mTempPath:" + mTempPath);
    }

    /**
     * 继续播放
     */
    private void continueVideoPlay() {
        mVideoSurfaceRl.setVisibility(ViewGroup.INVISIBLE);
        mPlayPauseIb.setBackgroundResource(R.drawable.ic_video_player_play);
        if (!mHandlePost) {
            mUploadHandler.post(mUpProgressRunnable);
            mHandlePost = true;
        }
        mMediaPlayer.start();
    }

    /**
     * 暂停播放
     */
    private void pauseVideoPlay() {
        mMediaPlayer.pause();
        mVideoSurfaceRl.setVisibility(ViewGroup.VISIBLE);
        mPlayPauseIb.setBackgroundResource(R.drawable.ic_video_player_pause);
        if (mHandlePost) {
            mUploadHandler.removeCallbacks(mUpProgressRunnable);
            mHandlePost = false;
        }
        mCurrentPosition = mMediaPlayer.getCurrentPosition();
    }

    /**
     * 播放结束后续处理
     */
    private void videoPlayOverDeal() {
        Logger.logD(Logger.APPOINTMENT, TAG + "->videoPlayOverDeal");
        mUploadHandler.removeCallbacks(mUpProgressRunnable);
        mHandlePost = false;
        mMediaPlayer.seekTo(0);
        mMediaPlayer.pause();
        mProgressSb.setProgress(0);
        mVideoSurfaceRl.setVisibility(ViewGroup.VISIBLE);
        mPlayPauseIb.setBackgroundResource(R.drawable.ic_video_player_pause);
    }

    /**
     * 退出播放
     */
    private void exitVideoPlay() {
        if (mHandlePost) {
            mUploadHandler.removeCallbacks(mUpProgressRunnable);
            mHandlePost = false;
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        finish();
    }

    private void initMediaPlay() {
        if (!FileUtil.isFileExist(mPath)) {
            return;
        }
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(mPath);
            mMediaPlayer.setDisplay(mSurfaceHolder);
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initAfterSurface() {
        if (!FileUtil.isFileExist(mPath)) {
            return;
        }
        setSurfaceSize();
        mMaxDuration = mMediaPlayer.getDuration();
        mProgressSb.setMax(mMediaPlayer.getDuration());
        mProgressSb.setProgress(mCurrentPosition);
        Logger.logD(Logger.APPOINTMENT, "initAfterSurface->mCurrentPosition:" + mCurrentPosition);
        mMediaPlayer.seekTo(mCurrentPosition);
        if (FileUtil.isFileExist(mPath)) {
            mAllTimeTv.setText(DateUtil.millisecondToFormatDate(TIME_FORMAT, mMaxDuration));
        }
        //显示第一帧画面
        mMediaPlayer.start();
        pauseVideoPlay();
//        while (true) {
//            if (mMediaPlayer.getCurrentPosition() - mCurrentPosition >= 1) {
//                break;
//            }
//        }
    }

    /**
     * 启动screen状态广播接收器
     */
    private void regScreenListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(mScreenReceiver, filter);
    }

    /***********************************************锁屏监听 end*******************************************************/

    /**
     * OnPreparedListener监听
     *
     * @param mp 播放实体
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->surfaceCreated->onPrepared:");
        mMediaPlayer.start();
    }

    /*********************************************
     * SurfaceHolder.Callback start
     ***************************************************/
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->surfaceCreated->holder:" + holder);
        mSurfaceHolder = holder;
        mSurfaceHolder.setKeepScreenOn(true);
        initMediaPlay();
        initAfterSurface();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->surfaceDestroyed->holder:" + holder);
        if (mMediaPlayer != null && FileUtil.isFileExist(mPath)) {
            mCurrentPosition = mMediaPlayer.getCurrentPosition();
            mMediaPlayer.stop();
        }
    }

    /*******************************************
     * SurfaceHolder.Callback end
     ***************************************************/

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mCurrentTimeTv.setText(DateUtil.millisecondToFormatDate(TIME_FORMAT, seekBar.getProgress()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        AppHandlerProxy.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (mMediaPlayer.isPlaying()) {
                    mUploadHandler.removeCallbacks(mUpProgressRunnable);
                    mHandlePost = false;
                }
            }
        });
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mCurrentTimeTv.setText(DateUtil.millisecondToFormatDate(TIME_FORMAT, seekBar.getProgress()));
        mMediaPlayer.seekTo(seekBar.getProgress());
        if (seekBar.getProgress() - 100 > mMaxDuration && seekBar.getProgress() != 0 && mMaxDuration != 0) {
            videoPlayOverDeal();
        } else {
            if (mMediaPlayer.isPlaying()) {
                mUploadHandler.post(mUpProgressRunnable);
                mHandlePost = true;
            } else {
                continueVideoPlay();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitVideoPlay();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onStartDownload(String filePath) {
        if (mTempPath.equals(filePath)) {
            mProgressRl.setVisibility(View.VISIBLE);
            mVideoSurfaceRl.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoadProgressChange(String filePath, long totalSize, long currentSize) {

    }

    @Override
    public void onLoadFailed(String filePath, String reason) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->onLoadFailed");
        if (mTempPath.equals(filePath)) {
            initView();
        }
    }

    @Override
    public void onLoadSuccessful(String filePath) {
        Logger.logD(Logger.APPOINTMENT, TAG + "->onLoadSuccessful->filePath:" + filePath);
        Logger.logD(Logger.APPOINTMENT, TAG + "->onLoadSuccessful->mTempPath:" + mTempPath);
        mCurrentPosition = 0;
        if (mTempPath.equals(filePath)) {
            initView();
            initMediaPlay();
            initAfterSurface();
            continueVideoPlay();
        }
    }

    @Override
    public void onLoadStopped(String filePath) {

    }
}
