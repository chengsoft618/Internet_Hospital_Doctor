package cn.longmaster.hospital.doctor.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.widget.Button;

import cn.longmaster.hospital.doctor.R;

/**
 * Created by W·H·K on 2018/10/17.
 */

public class TimeCountUtil extends CountDownTimer {
    private Activity mActivity;
    private Button btn;//按钮

    // 在这个构造方法里需要传入三个参数，一个是Activity，一个是总的时间millisInFuture，一个是countDownInterval，然后就是你在哪个按钮上做这个是，就把这个按钮传过来就可以了
    public TimeCountUtil(Activity mActivity, long millisInFuture, long countDownInterval, Button btn) {
        super(millisInFuture, countDownInterval);
        this.mActivity = mActivity;
        this.btn = btn;
    }

    @SuppressLint("NewApi")
    @Override
    public void onTick(long millisUntilFinished) {
        btn.setClickable(false);//设置不能点击
        btn.setText("重新发送 (" + millisUntilFinished / 1000 + "s)");//设置倒计时时间
        //设置按钮为灰色，这时是不能点击的
        btn.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
        btn.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.bg_solid_999898_radius_5));
    }

    @SuppressLint("NewApi")
    @Override
    public void onFinish() {
        btn.setClickable(true);//重新获得点击
        btn.setText("重新发送");
        btn.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
        btn.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.bg_live_video_input_enter_room));
    }
}
