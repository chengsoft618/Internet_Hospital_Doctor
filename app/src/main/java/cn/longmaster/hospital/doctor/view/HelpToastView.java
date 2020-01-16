package cn.longmaster.hospital.doctor.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.utils.DisplayUtil;

/**
 * 一键协助Toast
 */
public class HelpToastView extends FrameLayout {
    private static final int AUTO_HIDE_TIME = 3 * 1000;
    private TextView mMessageTv;
    private boolean isShowing;
    private String message;
    private boolean isCanUpdate;
    private int marinTop;
    private Context mContext;
    private CountDownTimer mDownTimer;

    public HelpToastView(Context context) {
        this(context, null);
    }

    public HelpToastView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HelpToastView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View rootView = LayoutInflater.from(context).inflate(R.layout.view_help_toast, this, false);
        mContext = context;
        initView(rootView);
        addView(rootView);
        initData();
    }

    private void initView(View view) {
        mMessageTv = (TextView) view.findViewById(R.id.view_help_toast_message_tv);
        this.setY(-DisplayUtil.dip2px(45));
    }

    private void initData() {
        isCanUpdate = true;
        marinTop = DisplayUtil.dip2px(44);
        mDownTimer = new CountDownTimer(AUTO_HIDE_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                mDownTimer.cancel();
                hideToast();
            }
        };
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
    public void showToastView(String message, boolean isEvaluate) {
        if (isEvaluate) {
            mMessageTv.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_008000));
        } else {
            mMessageTv.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_ff0000));
        }
        this.message = message;
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
        isCanUpdate = true;
        mDownTimer.start();
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
}
