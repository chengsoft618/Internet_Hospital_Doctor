package cn.longmaster.hospital.doctor.util;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.view.picktime.WheelView;
import cn.longmaster.utils.StringUtils;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author ABiao_Abiao
 * @date 2019/6/21 14:59
 * @description:
 */
public class WheelViewHelper {
    public static class OneWheelViewBuilder {
        private Fragment fragment;
        private Activity activity;
        private View parent;
        private int height;
        private WheelView.OnItemSelectedListener onItemSelectedListener;
        private WheelView.OnItemSelectedListener onCommitListener;
        private View.OnClickListener onCancelListener;
        private List<String> items;
        private String title;

        public OneWheelViewBuilder setFragment(Fragment fragment) {
            activity = fragment.getActivity();
            this.fragment = fragment;
            return this;
        }

        public OneWheelViewBuilder setActivity(Activity activity) {
            this.activity = activity;
            return this;
        }

        public OneWheelViewBuilder setParent(View parent) {
            this.parent = parent;
            return this;
        }

        public OneWheelViewBuilder setHeight(int height) {
            this.height = height;
            return this;
        }

        public OneWheelViewBuilder setItems(List<String> items) {
            this.items = items;
            return this;
        }

        public OneWheelViewBuilder setOnCommitListener(WheelView.OnItemSelectedListener onCommitListener) {
            this.onCommitListener = onCommitListener;
            return this;
        }

        public OneWheelViewBuilder setOnCancelListener(View.OnClickListener onCancelListener) {
            this.onCancelListener = onCancelListener;
            return this;
        }

        public OneWheelViewBuilder setOnItemSelectedListener(WheelView.OnItemSelectedListener onItemSelectedListener) {
            this.onItemSelectedListener = onItemSelectedListener;
            return this;
        }

        public OneWheelViewBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public PopupWindow build() {
            View contentView = LayoutInflater.from(activity).inflate(R.layout.choice_date_pop_wind_one, null);
            final PopupWindow mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            if (null == parent) {
                mPopupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            } else {
                mPopupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
            final TextView tvCancel = contentView.findViewById(R.id.tv_cancel);
            final TextView tvCommit = contentView.findViewById(R.id.tv_commit);
            final TextView tvTitle = contentView.findViewById(R.id.tv_choice_title);
            final WheelView mWvItem = contentView.findViewById(R.id.wv_item);
            tvTitle.setText(title);
            mWvItem.setItems(items, 0);
            tvCancel.setOnClickListener(v -> {
                if (null != onCancelListener) {
                    onCancelListener.onClick(v);
                }
                mPopupWindow.dismiss();
            });
            tvCommit.setOnClickListener(v -> {
                if (null != onCommitListener) {
                    onCommitListener.onItemSelected(mWvItem.getSelectedPosition(), mWvItem.getSelectedItem());
                }
                mPopupWindow.dismiss();
            });
            mWvItem.setOnItemSelectedListener((selectedIndex, item) -> {
                if (null != onItemSelectedListener) {
                    onItemSelectedListener.onItemSelected(selectedIndex, item);
                }
                tvTitle.setText(item);
            });
            return mPopupWindow;
        }
    }

    public static class TwoWheelViewBuilder {
        private Fragment fragment;
        private Activity activity;
        private View parent;
        private OnDateSelectListener onDateSelectListener;
        private List<String> dayItems;
        private List<String> timeItems;
        private int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        private int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        private Calendar startDate;
        private Calendar endDate;
        private String yearLabel = "";
        private String monthLabel = "";

        public TwoWheelViewBuilder setFragment(Fragment fragment) {
            this.fragment = fragment;
            return this;
        }

        public TwoWheelViewBuilder setStartDate(Calendar startDate) {
            this.startDate = startDate;
            return this;
        }

        public TwoWheelViewBuilder setEndDate(Calendar endDate) {
            this.endDate = endDate;
            return this;
        }

        public TwoWheelViewBuilder setActivity(Activity activity) {
            this.activity = activity;
            return this;
        }

        public TwoWheelViewBuilder setParent(View parent) {
            this.parent = parent;
            return this;
        }

        public TwoWheelViewBuilder setOnDateSelectListener(OnDateSelectListener onDateSelectListener) {
            this.onDateSelectListener = onDateSelectListener;
            return this;
        }

        public TwoWheelViewBuilder setDayItems(List<String> dayItems) {
            this.dayItems = dayItems;
            return this;
        }

        public TwoWheelViewBuilder setTimeItems(List<String> timeItems) {
            this.timeItems = timeItems;
            return this;
        }

        public TwoWheelViewBuilder setCurrentTime(int currentYear, int currentMonth) {
            this.currentMonth = currentMonth;
            this.currentYear = currentYear;
            return this;
        }

        private List<String> createYearRange(Calendar startDate, Calendar endDate) {
            int startYear = startDate.get(Calendar.YEAR);
            int endYear = endDate.get(Calendar.YEAR);
            List<String> yearList = new ArrayList<>();
            for (int i = startYear; i < endYear; i++) {
                yearList.add(i + yearLabel);
            }
            return yearList;
        }

        private List<String> createMonthRange() {
            List<String> month = new ArrayList<>(12);
            for (int i = 1; i <= 12; i++) {
                if (i < 10) {
                    month.add("0" + i + monthLabel);
                } else {
                    month.add(i + "" + monthLabel);
                }
            }
            return month;
        }

        public PopupWindow build() {
            checkNotNull(startDate, "startDate can not be null");
            checkNotNull(endDate, "endDate can not be null");
            if (startDate.getTimeInMillis() >= endDate.getTimeInMillis()) {
                throw new IllegalArgumentException("startDate can not be large than endDate");
            }
            int startYear = startDate.get(Calendar.YEAR);
            View contentView = LayoutInflater.from(activity).inflate(R.layout.choice_date_two, null);
            final PopupWindow mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            mPopupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            final TextView cancel = contentView.findViewById(R.id.tv_cancel);
            final TextView commit = contentView.findViewById(R.id.tv_commit);
            final TextView title = contentView.findViewById(R.id.tv_choice_title);
            final WheelView mWvYear = contentView.findViewById(R.id.wv_year);
            final WheelView mWvMonth = contentView.findViewById(R.id.wv_month);
            mWvYear.setItems(createYearRange(startDate, endDate), currentYear - startYear);
            mWvMonth.setItems(createMonthRange(), currentMonth - 1);
            cancel.setOnClickListener(v -> {
                onDateSelectListener.onCancel();
                mPopupWindow.dismiss();
            });
            commit.setOnClickListener(v -> {
                Calendar calendar = Calendar.getInstance();
                int year = StringUtils.str2Integer(StringUtils.substringBefore(mWvYear.getSelectedItem(), yearLabel));
                int month = StringUtils.str2Integer(StringUtils.substringBefore(mWvMonth.getSelectedItem(), monthLabel));
                calendar.set(year, month - 1, 1, 0, 0);
                onDateSelectListener.onDateSelect(mWvYear.getSelectedItem(), mWvMonth.getSelectedItem(), calendar);
                mPopupWindow.dismiss();
            });
            String defaultTime = mWvYear.getSelectedItem() + mWvMonth.getSelectedItem();
            title.setText(defaultTime);
            mWvYear.setOnItemSelectedListener((selectedIndex, item) -> {
                String time = mWvYear.getSelectedItem() + mWvMonth.getSelectedItem();
                title.setText(time);
            });
            mWvMonth.setOnItemSelectedListener((selectedIndex, item) -> {
                String time = mWvYear.getSelectedItem() + mWvMonth.getSelectedItem();
                title.setText(time);
            });
            return mPopupWindow;
        }


        public TwoWheelViewBuilder setLabel(String yearLabel, String monthLabel) {
            this.yearLabel = yearLabel;
            this.monthLabel = monthLabel;
            return this;
        }
    }

    public static class RoundsChooseTimeBuilder {
        private Fragment fragment;
        private Activity activity;
        private View parent;
        private WheelView.OnItemSelectedListener onItemSelectedListener;
        private OnTwoDateSelectListener onCommitListener;
        private View.OnClickListener onCancelListener;
        private String title;
        private List<String> leftItems;
        private List<String> rightItems;

        public RoundsChooseTimeBuilder setFragment(Fragment fragment) {
            this.fragment = fragment;
            this.activity = fragment.getActivity();
            return this;
        }

        public RoundsChooseTimeBuilder setActivity(Activity activity) {
            this.activity = activity;
            return this;
        }

        public RoundsChooseTimeBuilder setParent(View parent) {
            this.parent = parent;
            return this;
        }

        public RoundsChooseTimeBuilder setOnItemSelectedListener(WheelView.OnItemSelectedListener onItemSelectedListener) {
            this.onItemSelectedListener = onItemSelectedListener;
            return this;
        }

        public RoundsChooseTimeBuilder setOnCommitListener(OnTwoDateSelectListener onCommitListener) {
            this.onCommitListener = onCommitListener;
            return this;
        }

        public RoundsChooseTimeBuilder setOnCancelListener(View.OnClickListener onCancelListener) {
            this.onCancelListener = onCancelListener;
            return this;
        }

        public RoundsChooseTimeBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public RoundsChooseTimeBuilder setLeftItems(List<String> leftItems) {
            this.leftItems = leftItems;
            return this;
        }

        public RoundsChooseTimeBuilder setRightItems(List<String> rightItems) {
            this.rightItems = rightItems;
            return this;
        }

        public void build() {
            View contentView = LayoutInflater.from(activity).inflate(R.layout.choice_date_pop_wind_three, null);
            PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            if (null == parent) {
                popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            } else {
                popupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
            TextView cancel = contentView.findViewById(R.id.cancel);
            TextView sure = contentView.findViewById(R.id.sure);
            TextView title = contentView.findViewById(R.id.choice_time);
            WheelView leftWv = contentView.findViewById(R.id.year);
            WheelView rightWv = contentView.findViewById(R.id.hour);
            leftWv.setItems(leftItems, 0);
            rightWv.setItems(rightItems, 0);
            title.setText(leftWv.getSelectedItem() + " " + rightWv.getSelectedItem());
            leftWv.setOnItemSelectedListener((selectedIndex, item) -> title.setText(leftWv.getSelectedItem() + " " + rightWv.getSelectedItem()));
            rightWv.setOnItemSelectedListener((selectedIndex, item) -> title.setText(leftWv.getSelectedItem() + " " + rightWv.getSelectedItem()));
            cancel.setOnClickListener(v -> {
                if (onCancelListener != null) {
                    onCancelListener.onClick(v);
                }
                popupWindow.dismiss();
            });
            sure.setOnClickListener(v -> {
                if (null != onCommitListener) {
                    onCommitListener.onDateSelect(leftWv.getSelectedPosition(), rightWv.getSelectedPosition(), leftWv.getSelectedItem() + " " + rightWv.getSelectedItem());
                }
                popupWindow.dismiss();
            });
        }
    }

    public interface OnTwoDateSelectListener {
        void onDateSelect(int leftIndex, int rightIndex, String result);
    }

    public interface OnDateSelectListener {
        void onDateSelect(String selectDate, String selectTime, Calendar calendar);

        void onCancel();
    }
}
