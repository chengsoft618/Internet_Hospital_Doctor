package cn.longmaster.hospital.doctor.ui.college;

import android.app.Service;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.common.DateUtil;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.executor.AppExecutors;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.college.CourseListInfo;
import cn.longmaster.hospital.doctor.core.entity.college.InteractionInfo;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.college.GetInteractionListRequester;
import cn.longmaster.hospital.doctor.core.requests.college.SubmitContentRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.college.adapter.VideoPlayerInteractionAdapter;
import cn.longmaster.hospital.doctor.ui.college.listener.SoftKeyBoardListener;
import cn.longmaster.utils.ToastUtils;

/**
 * 互动Fragment
 * Created by Yang² on 2018/3/27.
 */

public class InteractionFragment extends NewBaseFragment {
    @FindViewById(R.id.fragment_interaction_recycler_view)
    private RecyclerView mRecyclerView;
    @FindViewById(R.id.fragment_interaction_no_comment)
    private TextView mNoCommentView;
    @FindViewById(R.id.fragment_interaction_comment_num)
    private TextView mCommentNumView;
    @FindViewById(R.id.fragment_interaction_interaction_say)
    private TextView mInteractionSayTv;
    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    private CourseListInfo mCourseInfo;
    private PopupWindow mPopupWindow;
    private View mPopupView;
    private VideoPlayerInteractionAdapter mVideoPlayerInteractionAdapter;
    private GetInteractionListRequester mGetInteractionListRequester;
    private boolean mLoadMoreEnd;//是否已经全部拉取完成
    private int mCommentNum;//评论数
    private InteractionCallBack mInteractionCallBack;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_interaction;
    }

    @Override
    public void initViews(View rootView) {
        initData();
        initListener();
        displayInteractionView();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    @OnClick({R.id.fragment_interaction_interaction_say})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_interaction_interaction_say:
                showPopupInputMethodWindow();
                break;
        }
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCourseInfo = (CourseListInfo) bundle.getSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_COURSE_INFO);
        }
    }

    private void initListener() {
        SoftKeyBoardListener.setListener(getActivity(), new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
            }

            @Override
            public void keyBoardHide(int height) {
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
            }
        });
    }

    private void displayInteractionView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setNestedScrollingEnabled(false);
        mVideoPlayerInteractionAdapter = new VideoPlayerInteractionAdapter(R.layout.item_video_player_interaction, new ArrayList<>(0));
        mRecyclerView.setAdapter(mVideoPlayerInteractionAdapter);
        List<InteractionInfo> interactionList = new ArrayList<>();
        mVideoPlayerInteractionAdapter.addData(interactionList);
        if (mGetInteractionListRequester == null) {
            getInteractionList(true, 0, "");
        }
        mVideoPlayerInteractionAdapter.setOnLoadMoreListener(() -> {
            InteractionInfo interactionInfo = mVideoPlayerInteractionAdapter.getData().get(mVideoPlayerInteractionAdapter.getData().size() - 1);
            getInteractionList(false, interactionInfo.getMsgId(), interactionInfo.getInsertDt());
        }, mRecyclerView);
    }

    private void getInteractionList(final boolean firstPage, int msgId, String insertDt) {
        mGetInteractionListRequester = new GetInteractionListRequester((baseResult, interactionInfos) -> {
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                if (firstPage) {
                    mVideoPlayerInteractionAdapter.replaceData(interactionInfos);
                } else {
                    mVideoPlayerInteractionAdapter.addData(interactionInfos);
                }
                mLoadMoreEnd = baseResult.getIsFinish() == 0;
                if (mLoadMoreEnd) {
                    mVideoPlayerInteractionAdapter.loadMoreEnd();
                } else {
                    mVideoPlayerInteractionAdapter.loadMoreComplete();
                }
                //是否显示无数据
                if (mVideoPlayerInteractionAdapter.getItemCount() == 0) {
                    mNoCommentView.setVisibility(View.VISIBLE);
                } else {
                    mNoCommentView.setVisibility(View.GONE);
                }
                //设置评论数
                if (firstPage) {
                    mCommentNum = baseResult.getCount();
                    mCommentNumView.setText(getString(R.string.medical_college_comment_num, mCommentNum));
                }
            } else {
                ToastUtils.showShort(R.string.no_network_connection);
            }

        });
        mGetInteractionListRequester.courseId = mCourseInfo.getCourseId();
        mGetInteractionListRequester.appointmentId = mCourseInfo.getAppointmentId();
        mGetInteractionListRequester.msgId = msgId;
        mGetInteractionListRequester.insertDt = insertDt;
        mGetInteractionListRequester.pageSize = 10;
        mGetInteractionListRequester.doPost();
    }

    private void showPopupInputMethodWindow() {
        if (mPopupWindow == null) {
            mPopupView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_comment_popupwindow, null);
            final EditText inputComment = mPopupView.findViewById(R.id.activity_college_video_player_input_edt);
            TextView btnSubmit = mPopupView.findViewById(R.id.activity_college_video_player_input_send);
            final TextView inputNum = mPopupView.findViewById(R.id.activity_college_video_player_input_num);
            ImageView deleteContent = mPopupView.findViewById(R.id.activity_college_video_player_input_delete);
            inputNum.setText(getString(R.string.medical_college_text_num, 0 + ""));
            mPopupWindow = new PopupWindow(mPopupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            mPopupWindow.setTouchable(true);
            mPopupWindow.setTouchInterceptor((v, event) -> false);
            mPopupWindow.setFocusable(true);
            // 设置点击窗口外边窗口消失
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popuwindow_white_bg));

            // 设置弹出窗体需要软键盘
            mPopupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
            // 再设置模式，和Activity的一样，覆盖，调整大小。
            mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
            params.alpha = 0.4f;
            getActivity().getWindow().setAttributes(params);
            // 设置popWindow的显示和消失动画
            mPopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
            mPopupWindow.update();
            setListener(btnSubmit, inputComment, deleteContent, inputNum);
        }
        popupInputMethodWindow();
        mPopupWindow.showAtLocation(mPopupView, Gravity.BOTTOM, 0, 0);

    }

    private void setListener(TextView btnSubmit, final EditText inputComment, ImageView deleteContent, final TextView inputNum) {
        // 在dismiss中恢复透明度
        mPopupWindow.setOnDismissListener(() -> {
            WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
            params.alpha = 1f;
            getActivity().getWindow().setAttributes(params);
        });
        btnSubmit.setOnClickListener(v -> {
            String comment = inputComment.getText().toString().trim();
            if (comment.length() <= 0) {
                ToastUtils.showShort(R.string.medical_college_please_input_context);
                return;
            }
            mPopupWindow.dismiss();
            submitContent(comment);

        });
        inputComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputNum.setText(getString(R.string.medical_college_text_num, s.length() + ""));
                if (s.length() > 200) {
                    inputNum.setTextColor(Color.RED);
                } else {
                    inputNum.setTextColor(getCompatColor(R.color.color_999999));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        deleteContent.setOnClickListener(v -> inputComment.setText(""));
    }

    private void submitContent(String content) {
        final InteractionInfo interactionInfo = new InteractionInfo();
        interactionInfo.setUserId(mUserInfoManager.getCurrentUserInfo().getUserId());
        interactionInfo.setMsgContent(content);
        interactionInfo.setCourseId(mCourseInfo.getCourseId());
        interactionInfo.setAppointmentId(mCourseInfo.getAppointmentId());
        interactionInfo.setMsgType(301);
        interactionInfo.setMsgId(0);
        interactionInfo.setInsertDt(DateUtil.millisecondToStandardDate(System.currentTimeMillis()));
        SubmitContentRequester requester = new SubmitContentRequester((baseResult, aVoid) -> {
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                addInteractionInfo(interactionInfo);
            }
            ToastUtils.showShort(baseResult.getCode() == OnResultListener.RESULT_SUCCESS ?
                    R.string.medical_college_comment_suc : R.string.medical_college_comment_fail);
        });
        requester.courseId = mCourseInfo.getCourseId();
        requester.appointmentId = mCourseInfo.getAppointmentId();
        requester.msgContent = content;
        requester.doPost();
    }

    private void addInteractionInfo(InteractionInfo interactionInfo) {
        mVideoPlayerInteractionAdapter.addData(0, interactionInfo);
        if (mLoadMoreEnd) {
            mVideoPlayerInteractionAdapter.loadMoreEnd();
        } else {
            mVideoPlayerInteractionAdapter.loadMoreComplete();
        }
        mNoCommentView.setVisibility(View.GONE);
        mRecyclerView.smoothScrollToPosition(0);
        mCommentNum++;
        mCommentNumView.setText(getString(R.string.medical_college_comment_num, mCommentNum));
        EditText inputComment = mPopupView.findViewById(R.id.activity_college_video_player_input_edt);
        inputComment.setText("");
        if (mInteractionCallBack != null) {
            mInteractionCallBack.onCallBack(mCommentNum);
        }
    }

    private void popupInputMethodWindow() {
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    public void setInteractionCallBack(InteractionCallBack interactionCallBack) {
        this.mInteractionCallBack = interactionCallBack;
    }

    interface InteractionCallBack {
        void onCallBack(int num);
    }
}
