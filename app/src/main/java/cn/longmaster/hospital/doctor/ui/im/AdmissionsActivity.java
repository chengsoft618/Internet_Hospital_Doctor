package cn.longmaster.hospital.doctor.ui.im;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.view.AppActionBar;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.hospital.doctor.view.picktime.WheelView;

/**
 * Created by YY on 17/8/22.
 */

public class AdmissionsActivity extends BaseActivity {
    private final int TYPE_SELECT_YEAR = 1;//选择日期
    private final int TYPE_SELECT_HOUR = 2;//选择时间
    @FindViewById(R.id.admission_choice_date)
    private LinearLayout mChoiceDateView;
    @FindViewById(R.id.admission_choice_date_value)
    private TextView mDateValueView;
    @FindViewById(R.id.admission_choice_time)
    private LinearLayout mChoiceTimeView;
    @FindViewById(R.id.admission_choice_time_value)
    private TextView mTimeValueView;
    @FindViewById(R.id.activity_action_bar)
    private AppActionBar mActionBar;

    private WheelView mYear;
    private WheelView mHour;
    private WheelView mMinute;
    private TextView mChoiceTime;
    private String mTime;
    private String mSelectDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admissions);
        ViewInjecter.inject(this);
        initView();
        initListener();
    }

    private void initListener() {
        mActionBar.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExitDialog();
            }
        });
    }

    private void showExitDialog() {
        new CommonDialog.Builder(getActivity())
                .setTitle(R.string.chat_determine_break)
                .setMessage(R.string.chat_break_content)
                .setPositiveButton(R.string.video_dialog_cancel, new CommonDialog.OnPositiveBtnClickListener() {
                    @Override
                    public void onPositiveBtnClicked() {
                    }
                })
                .setNegativeButton(R.string.video_dialog_ok, new CommonDialog.OnNegativeBtnClickListener() {
                    @Override
                    public void onNegativeBtnClicked() {
                        finish();
                    }
                })
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .show();

    }

    private void initView() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = Calendar.getInstance().getTime();
        String year = format.format(date);
        String week = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        if ("1".equals(week)) {
            week = "日";
        } else if ("2".equals(week)) {
            week = "一";
        } else if ("3".equals(week)) {
            week = "二";
        } else if ("4".equals(week)) {
            week = "三";
        } else if ("5".equals(week)) {
            week = "四";
        } else if ("6".equals(week)) {
            week = "五";
        } else if ("7".equals(week)) {
            week = "六";
        }
        SimpleDateFormat formats = new SimpleDateFormat("HH:mm");
        String time = formats.format(Calendar.getInstance().getTime());

        mDateValueView.setText(year + " 星期" + week);
        mTimeValueView.setText(time);
    }

    @OnClick({R.id.admission_choice_date_value,
            R.id.admission_choice_time_value})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.admission_choice_date_value:
                shwDatePopupWindow(TYPE_SELECT_YEAR);
                break;
            case R.id.admission_choice_time_value:
                shwDatePopupWindow(TYPE_SELECT_HOUR);
                break;

        }
    }

    private void shwDatePopupWindow(final int type) {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.choice_date_pop_wind_three, null);
        final PopupWindow mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        TextView cancel = (TextView) contentView.findViewById(R.id.cancel);
        TextView sure = (TextView) contentView.findViewById(R.id.sure);
        mYear = (WheelView) contentView.findViewById(R.id.year);
        mHour = (WheelView) contentView.findViewById(R.id.hour);
        mMinute = (WheelView) contentView.findViewById(R.id.minute);
        mChoiceTime = (TextView) contentView.findViewById(R.id.choice_time);
        setPickTimeView(type);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == TYPE_SELECT_YEAR) {
                    mSelectDate = mYear.getSelectedItem();
                    mDateValueView.setText(mSelectDate);
                }
                if (type == TYPE_SELECT_HOUR) {
                    String hourItemStr = mHour.getSelectedItem();
                    String minuteItemStr = mMinute.getSelectedItem();
                    mTime = hourItemStr + ":" + minuteItemStr;
                    mTimeValueView.setText(mTime);
                }
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
            }
        });
    }

    public void rightBtnClick(View view) {
        new CommonDialog.Builder(this)
                .setTitle(R.string.chat_definite_video_time)
                .setMessage(R.string.chat_definite_video_time_set)
                .setPositiveButton(R.string.video_dialog_cancel, new CommonDialog.OnPositiveBtnClickListener() {
                    @Override
                    public void onPositiveBtnClicked() {
                    }
                })
                .setNegativeButton(R.string.video_dialog_ok, new CommonDialog.OnNegativeBtnClickListener() {
                    @Override
                    public void onNegativeBtnClicked() {
                        try {
                            String timeStr = mDateValueView.getText().toString();
                            StringBuffer buffer = new StringBuffer(timeStr);
                            buffer.replace(11, timeStr.length(), mTimeValueView.getText().toString());
                            Logger.logD(Logger.IM, "->EXTRA_DATA_VIDEO_DATE()" + buffer);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            long videoTime = sdf.parse(String.valueOf(buffer)).getTime() / 1000;
                            Intent intent = new Intent();
                            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_VIDEO_DATE, videoTime);
                            setResult(RESULT_OK, intent);
                            finish();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .show();
    }

    public static String getFetureDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        String week = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(week)) {
            week = "日";
        } else if ("2".equals(week)) {
            week = "一";
        } else if ("3".equals(week)) {
            week = "二";
        } else if ("4".equals(week)) {
            week = "三";
        } else if ("5".equals(week)) {
            week = "四";
        } else if ("6".equals(week)) {
            week = "五";
        } else if ("7".equals(week)) {
            week = "六";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today) + "  星期" + week;
        Log.e(null, result);
        return result;
    }

    public void setPickTimeView(int type) {
        if (type == TYPE_SELECT_YEAR) {
            mYear.setVisibility(View.VISIBLE);
            mHour.setVisibility(View.GONE);
            mMinute.setVisibility(View.GONE);
            mChoiceTime.setText(getString(R.string.chat_please_choice_time));
            List<String> yearList = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                String date = getFetureDate(i);
                yearList.add(date);
            }
            Logger.logI(Logger.COMMON, "calendarStart-test" + yearList);
            mYear.setItems(yearList, 0);
        }
        if (type == TYPE_SELECT_HOUR) {
            mYear.setVisibility(View.GONE);
            mHour.setVisibility(View.VISIBLE);
            mMinute.setVisibility(View.VISIBLE);
            mChoiceTime.setText(getString(R.string.chat_please_choice_data));
            List<String> hourList = new ArrayList<>();
            String hourString = "";
            for (int i = 0; i < 24; i++) {
                if (i < 10) {
                    hourString = "0" + i;
                } else {
                    hourString = "" + i;
                }
                hourList.add(hourString);
            }
            List<String> minuteList = new ArrayList<>();
            String minuteString = "";
            for (int i = 0; i < 6; i++) {
                if (i == 0) {
                    minuteString = "00";
                } else {
                    minuteString = "" + i * 10;
                }
                minuteList.add(minuteString);
            }
            mHour.setItems(hourList, 0);
            mMinute.setItems(minuteList, 0);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showExitDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
