package cn.longmaster.utils;

import android.content.Context;
import android.view.View;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Abiao
 * @version 0.01
 * @date 2018/10/22 13:42
 * @description:
 */
public class TimePickUtils {
    private static final String TAG = "TAG_" + TimePickUtils.class.getSimpleName();

    public interface OnTimeSelectListener {
        void onTimeSelect(Date date, View v);
    }

    public static void showBottomPickTime(final Context context, Date startTime, final OnTimeSelectListener onTimeSelectListener) {
        showBottomPickTime(context, null, startTime, onTimeSelectListener);
    }

    public static void showBottomPickTime(final Context context, final String label, Date startTime, final OnTimeSelectListener onTimeSelectListener) {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        Calendar selectDate = Calendar.getInstance();
        selectDate.setTime(new Date());
        if (null == startTime) {
            startDate.set(2013, 1, 1);
        } else {
            startDate.setTime(startTime);
        }

        endDate.set(2100, 1, 1);
        TimePickerView pvTime = new TimePickerBuilder(context, (date, v) -> onTimeSelectListener.onTimeSelect(date, v))
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setTitleColor(0xFF25B8FF)//标题文字颜色
                .setSubmitColor(0xFF049EFF)//确定按钮文字颜色
                .setCancelColor(0xFF666666)//取消按钮文字颜色
                .setTitleBgColor(0xFFFFFFFF)//标题背景颜色 Night mode
                .setBgColor(0xFFFFFFFF)//滚轮背景颜色 Night mode
                .setDate(selectDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLineSpacingMultiplier(1.8F)
                .build();
        pvTime.show();
    }

    public static void showBottomPickTime(final Context context, final OnTimeSelectListener onTimeSelectListener) {
        showBottomPickTime(context, null, onTimeSelectListener);
    }

    public static void showDialogPickTime(final Context context, final OnTimeSelectListener onTimeSelectListener) {
        boolean[] type = {true, true, true, false, false, false};

        TimePickerView pvTime = new TimePickerBuilder(context, (date, v) -> onTimeSelectListener.onTimeSelect(date, v))
                .setTitleColor(0xFF25B8FF)//标题文字颜色
                .setSubmitColor(0xFF049EFF)//确定按钮文字颜色
                .setCancelColor(0xFF666666)//取消按钮文字颜色
                .setTitleBgColor(0xFFFFFFFF)//标题背景颜色 Night mode
                .setBgColor(0xFFFFFFFF)//滚轮背景颜色 Night mode
                .isDialog(true)
                .setType(type)
                .setLineSpacingMultiplier(1.8F)
                .build();
        pvTime.show();
    }
}
