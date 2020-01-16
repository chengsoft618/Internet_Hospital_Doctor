package cn.longmaster.hospital.doctor.ui.college;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.model.VideoOptionModel;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.college.CourseListInfo;
import cn.longmaster.hospital.doctor.core.entity.college.InteractionInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.college.GetCourseInfoRequester;
import cn.longmaster.hospital.doctor.core.requests.college.GetInteractionListRequester;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;


/**
 * 视频播放activity
 * <p>
 * Created by W·H·K on 2018/3/23.
 */
public class CollegeVideoPlayerActivity extends BaseActivity {
    @FindViewById(R.id.activity_college_video_player_player_view)
    private StandardGSYVideoPlayer mVideoPlayer;
    @FindViewById(R.id.activity_college_video_player_radio_group)
    private RadioGroup mRadioGroup;
    @FindViewById(R.id.activity_college_video_player_data)
    private RadioButton mDataTabView;
    @FindViewById(R.id.activity_college_video_player_interaction)
    private RadioButton mInteractionTabView;
    @FindViewById(R.id.activity_college_video_player_vertical_line)
    private View mVerticalLine;
    @FindViewById(R.id.activity_college_video_player_data_fragment)
    private FrameLayout mFragmentLayout;
    @FindViewById(R.id.activity_college_video_player_radio_layout)
    private RelativeLayout mRadioLayout;

    private int mCourseId;
    private CourseListInfo mCourseListInfo;

    private boolean mIsPlay;
    private boolean mIsPause;
    private OrientationUtils mOrientationUtils;

    public static void startCollegeVideoPlayerActivity(Context context, int courseId) {
        Intent intent = new Intent(context, CollegeVideoPlayerActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_COURSE_ID, courseId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_video_player);
        ViewInjecter.inject(this);
        initData();
        getCourseInfo();
        initListener();
    }

    @Override
    public void onBackPressed() {
        if (mOrientationUtils != null) {
            mOrientationUtils.backToProtVideo();
        }
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        getCurPlay().onVideoPause();
        super.onPause();
        mIsPause = true;
    }


    @Override
    protected void onResume() {
        getCurPlay().onVideoResume(false);
        super.onResume();
        mIsPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIsPlay) {
            getCurPlay().release();
        }
        //GSYPreViewManager.instance().releaseMediaPlayer();
        if (mOrientationUtils != null) {
            mOrientationUtils.releaseListener();
        }
    }

    private void initData() {
        mCourseId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_COURSE_ID, 0);
    }

    private void initListener() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                for (int i = 0; i < group.getChildCount(); i++) {
                    if (group.getChildAt(i).getId() == checkedId) {
                        Fragment fragment = getSupportFragmentManager().findFragmentByTag(checkedId + "");
                        if (fragment == null) {
                            fragment = createFragment(checkedId);
                            transaction.add(mFragmentLayout.getId(), fragment, checkedId + "");
                            Bundle bundle = new Bundle();
                            switch (checkedId) {
                                case R.id.activity_college_video_player_introduce:
                                    bundle.putSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_COURSE_INFO, mCourseListInfo);
                                    break;
                                case R.id.activity_college_video_player_data:
                                    bundle.putInt(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, mCourseListInfo.getAppointmentId());
                                    break;
                                case R.id.activity_college_video_player_interaction:
                                    InteractionFragment interactionFragment = (InteractionFragment) fragment;
                                    interactionFragment.setInteractionCallBack(num -> setInteractionNum(num));
                                    if (mCourseListInfo == null) {
                                        mCourseListInfo = new CourseListInfo();
                                        mCourseListInfo.setCourseId(mCourseId);
                                        mCourseListInfo.setAppointmentId(0);
                                    }
                                    bundle.putSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_COURSE_INFO, mCourseListInfo);
                                    break;
                            }
                            fragment.setArguments(bundle);
                        } else {
                            transaction.show(fragment);
                        }
                    } else {
                        Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentByTag(group.getChildAt(i).getId() + "");
                        if (fragment != null) {
                            transaction.hide(fragment);
                        }
                    }
                }
                transaction.commitAllowingStateLoss();
            }
        });
    }

    private void getCourseInfo() {
        GetCourseInfoRequester getCourseInfoRequester = new GetCourseInfoRequester(new OnResultListener<CourseListInfo>() {
            @Override
            public void onResult(BaseResult baseResult, CourseListInfo courseListInfo) {
                if (baseResult.getCode() == RESULT_SUCCESS && courseListInfo != null) {
                    mCourseListInfo = courseListInfo;
                    setView();
                    getInteractionList();
                    initVideoPlayer();
                    initFistTab(R.id.activity_college_video_player_introduce);
                } else {
                    showToast(R.string.no_network_connection);
                }
            }
        });
        getCourseInfoRequester.courseId = mCourseId;
        getCourseInfoRequester.doPost();
    }

    private void getInteractionList() {
        GetInteractionListRequester mGetInteractionListRequester = new GetInteractionListRequester(new OnResultListener<List<InteractionInfo>>() {
            @Override
            public void onResult(BaseResult baseResult, List<InteractionInfo> interactionInfos) {
                if (baseResult.getCount() != 0) {
                    setInteractionNum(baseResult.getCount());
                }
            }
        });
        mGetInteractionListRequester.courseId = mCourseListInfo.getCourseId();
        mGetInteractionListRequester.appointmentId = mCourseListInfo.getAppointmentId();
        mGetInteractionListRequester.msgId = 0;
        mGetInteractionListRequester.insertDt = "";
        mGetInteractionListRequester.pageSize = 1;
        mGetInteractionListRequester.doPost();
    }

    private void setInteractionNum(int num) {
        mInteractionTabView.setText(getString(R.string.medical_college_interaction_with_num, num > 999 ? 999 : num));

    }

    private void setView() {
        mDataTabView.setVisibility(mCourseListInfo.getAppointmentId() == 0 ? View.GONE : View.VISIBLE);
        mVerticalLine.setVisibility(mCourseListInfo.getAppointmentId() == 0 ? View.GONE : View.VISIBLE);
//        View view = new View(getActivity());
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
//        layoutParams.width = ScreenUtil.getScreenWidth() / (mCourseListInfo.getAppointmentId() == 0 ? 2 : 3) / 2;
//        view.setLayoutParams(layoutParams);
//        mRadioLayout.addView(view);
//        BadgeView badgeView = new BadgeView(this, view);
//        badgeView.setText("123");
//        badgeView.setTextSize(8);
//        badgeView.setBackgroundResource(R.drawable.bg_video_player_comments);
//        badgeView.setBadgePosition(BadgeView.POSITION_TOP_LEFT);
//        badgeView.setTextColor(getResColor(R.color.color_000000));
//        badgeView.setBadgeMargin(30, 2);
//        badgeView.show();
    }

    private void initFistTab(int checkedId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentByTag(checkedId + "");
        if (fragment == null) {
            fragment = createFragment(checkedId);
            transaction.add(mFragmentLayout.getId(), fragment, checkedId + "");
            Bundle bundle = new Bundle();
            bundle.putSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_COURSE_INFO, mCourseListInfo);
            CourseIntroduceFragment courseIntroduceFragment = (CourseIntroduceFragment) fragment;
            courseIntroduceFragment.setArguments(bundle);
            transaction.commitAllowingStateLoss();
        }
    }

    private Fragment createFragment(int tab) {
        Fragment fragment = new Fragment();
        switch (tab) {
            case R.id.activity_college_video_player_introduce:
                fragment = new CourseIntroduceFragment();
                break;
            case R.id.activity_college_video_player_data:
                fragment = new CourseDataFragment();
                break;
            case R.id.activity_college_video_player_interaction:
                fragment = new InteractionFragment();
                break;
        }
        return fragment;
    }

    /********************************************
     * 以下是播放器相关
     ****************************************************/

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (mIsPlay && !mIsPause) {
            mVideoPlayer.onConfigurationChanged(this, newConfig, mOrientationUtils, true, true);
        }
    }

    private void initVideoPlayer() {
        //增加封面
//        ImageView imageView = new ImageView(this);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setImageResource(R.drawable.ic_media_play);
        String source = AppConfig.getMediaUrl() + mCourseListInfo.getFileName();
        //外部辅助的旋转，帮助全屏
        mOrientationUtils = new OrientationUtils(this, mVideoPlayer);
        //初始化不打开外部的旋转
        mOrientationUtils.setEnable(false);
        resolveNormalVideoUI();

        GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
        gsyVideoOption/*.setThumbImageView(imageView)*/
                .setIsTouchWiget(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setSeekRatio(1)
                .setUrl(source)
                .setCacheWithPlay(false)
                .setVideoTitle(mCourseListInfo.getCourseTitle())
                .setStartAfterPrepared(true)
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        Debuger.printfError("***** onPrepared **** " + objects[0]);
                        Debuger.printfError("***** onPrepared **** " + objects[1]);
                        super.onPrepared(url, objects);
                        //开始播放了才能旋转和全屏
                        mOrientationUtils.setEnable(true);
                        mIsPlay = true;
                    }

                    @Override
                    public void onEnterFullscreen(String url, Object... objects) {
                        super.onEnterFullscreen(url, objects);
                        Debuger.printfError("***** onEnterFullscreen **** " + objects[0]);//title
                        Debuger.printfError("***** onEnterFullscreen **** " + objects[1]);//当前全屏player
                    }

                    @Override
                    public void onAutoComplete(String url, Object... objects) {
                        super.onAutoComplete(url, objects);
                    }

                    @Override
                    public void onClickStartError(String url, Object... objects) {
                        super.onClickStartError(url, objects);
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        Debuger.printfError("***** onQuitFullscreen **** " + objects[0]);//title
                        Debuger.printfError("***** onQuitFullscreen **** " + objects[1]);//当前非全屏player
                        if (mOrientationUtils != null) {
                            mOrientationUtils.backToProtVideo();
                        }
                    }
                })
                .setLockClickListener(new LockClickListener() {
                    @Override
                    public void onClick(View view, boolean lock) {
                        if (mOrientationUtils != null) {
                            //配合下方的onConfigurationChanged
                            mOrientationUtils.setEnable(!lock);
                        }
                    }
                })
                .setGSYVideoProgressListener(new GSYVideoProgressListener() {
                    @Override
                    public void onProgress(int progress, int secProgress, int currentPosition, int duration) {
                        Debuger.printfLog(" progress " + progress + " secProgress " + secProgress + " currentPosition " + currentPosition + " duration " + duration);
                    }
                })
                .build(mVideoPlayer);

        mVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接横屏
                mOrientationUtils.resolveByClick();

                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                mVideoPlayer.startWindowFullscreen(getActivity(), true, true);
            }
        });
        mVideoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOrientationUtils != null) {
                    mOrientationUtils.backToProtVideo();
                }
                if (GSYVideoManager.backFromWindowFull(getActivity())) {
                    return;
                }
                finish();
            }
        });

        //优化播放时音视频不同步
        VideoOptionModel videoOptionModel =
                new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 50);
        List<VideoOptionModel> list = new ArrayList<>();
        list.add(videoOptionModel);
        GSYVideoManager.instance().setOptionModelList(list);
        //开始播放
        mVideoPlayer.startPlayLogic();
    }

    private void resolveNormalVideoUI() {
        //增加title
        mVideoPlayer.getTitleTextView().setVisibility(View.GONE);
//        mVideoPlayer.getBackButton().setVisibility(View.GONE);
    }

    private GSYVideoPlayer getCurPlay() {
        if (mVideoPlayer.getFullWindowPlayer() != null) {
            return mVideoPlayer.getFullWindowPlayer();
        }
        return mVideoPlayer;
    }

}
