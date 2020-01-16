package cn.longmaster.hospital.doctor.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.receiver.NetStateReceiver;
import cn.longmaster.hospital.doctor.ui.user.MeetingRoomActivity;
import cn.longmaster.utils.DisplayUtil;

/**
 * Created by W·H·K on 2018/12/20.
 */

public class MeetingToastView extends FrameLayout {
    public static final int TOAST_TYPE_MESSAGE_BASE = 0;//只有消息
    public static final int TOAST_TYPE_MESSAGE_HAVE_BTN = 1;//带按键

    private static final int AUTO_HIDE_TIME = 3 * 1000;

    private TextView mMessageTv;
    private TextView mPositiveTv;
    private int toastType;
    private boolean isShowing;


    private String message;
    private String btnText;
    private boolean isCanUpdate;
    private int marinTop;
    private Context mContext;

    private CountDownTimer mDownTimer;
    private OnToastPositiveClickListener mPositiveClickListener;

    public MeetingToastView(Context context) {
        this(context, null);
    }

    public MeetingToastView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeetingToastView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View rootView = LayoutInflater.from(context).inflate(R.layout.view_toast, this, false);
        mContext = context;
        initView(rootView);
        addView(rootView);
        initData();
        addListener();
    }

    private void initView(View view) {
        mMessageTv = (TextView) view.findViewById(R.id.view_toast_message_tv);
        mPositiveTv = (TextView) view.findViewById(R.id.view_toast_tv);
        this.setY(-DisplayUtil.dip2px(45));
    }

    private void initData() {
        isCanUpdate = true;
        marinTop = DisplayUtil.dip2px(20);
        mDownTimer = new CountDownTimer(AUTO_HIDE_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (toastType != TOAST_TYPE_MESSAGE_HAVE_BTN) {
                    mDownTimer.cancel();
                    hideToast();
                }
            }
        };
    }

    private void addListener() {
        mPositiveTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPositiveClickListener != null) {
                    mPositiveClickListener.onPositiveClicked(String.valueOf(((TextView) v).getText()));
                }
                if (NetStateReceiver.NETWORK_TYPE_WIFI == NetStateReceiver.getCurrentNetType(mContext) ||
                        NetStateReceiver.NETWOKR_TYPE_MOBILE == NetStateReceiver.getCurrentNetType(mContext)) {
                    hideToast();
                }
            }
        });
    }

    public void setPositiveClickListener(MeetingRoomActivity positiveClickListener) {
        mPositiveClickListener = positiveClickListener;
    }

    /**
     * 隐藏toast
     */
    public void hideToast() {
        isCanUpdate = true;
        isShowToast(false);
    }

    /**
     * 获取toast显示内容
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * 外部调用显示Toast
     *
     * @param message toast显示内容
     */
    public void showToastView(String message) {
        showToastView(message, null);
    }

    /**
     * 外部调用显示Toast
     *
     * @param message toast显示内容
     * @param btnText 按钮文案
     */
    public void showToastView(String message, String btnText) {
        this.message = message;
        if (!TextUtils.isEmpty(btnText)) {
            isCanUpdate = true;
            this.btnText = btnText;
            this.toastType = TOAST_TYPE_MESSAGE_HAVE_BTN;
        } else {
            this.toastType = TOAST_TYPE_MESSAGE_BASE;
        }
        if (!isCanUpdate) {
            return;
        }
        show();
    }

    /**
     * 设置数据并显示toast
     */
    private void show() {
        mDownTimer.cancel();
        AppHandlerProxy.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                mMessageTv.setText(message);
            }
        });
        if (toastType == TOAST_TYPE_MESSAGE_HAVE_BTN) {
            mPositiveTv.setVisibility(View.VISIBLE);
            mPositiveTv.setText(btnText);
            isCanUpdate = false;
        } else {
            mPositiveTv.setVisibility(View.GONE);
            isCanUpdate = true;
            mDownTimer.start();
        }
        isShowToast(true);
    }

    /**
     * 显示和隐藏Toast动画
     *
     * @param isShow 是否显示toast
     */
    public void isShowToast(final boolean isShow) {
        final View view = this;
        final int selfHeight = DisplayUtil.dip2px(45);//当前控件高度
        int yDeltaTemp = marinTop + selfHeight;
        if (!isShow) {
            if (view.getY() != marinTop) {
                return;
            }
            yDeltaTemp = -yDeltaTemp;
        } else {
            if (view.getY() != -selfHeight) {
                return;
            }
        }
        final int yDelta = yDeltaTemp;
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, yDelta);
        translateAnimation.setDuration(200);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                if (isShow) {
                    view.setY(marinTop);
                    isShowing = true;
                } else {
                    view.setY(-selfHeight);
                    isShowing = false;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(translateAnimation);
    }

    public boolean isShowing() {
        return isShowing;
    }

    public String getBtnText() {
        return btnText;
    }


    public interface OnToastPositiveClickListener {
        void onPositiveClicked(String clickText);
    }
}
