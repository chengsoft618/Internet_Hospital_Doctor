package cn.longmaster.hospital.doctor.ui.user.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.entity.user.ProjectOptionalTimeInfo;
import cn.longmaster.hospital.doctor.view.timetable.SpecialCalendar;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.TimeUtils;

/**
 * Created by W·H·K on 2019/3/8.
 * Mod by biao on 2019/9/2
 */
public class CalendarAdapter extends BaseAdapter {
    private static String TAG = "CalendarAdapter";
    private boolean isLeapyear = false; //是否为闰年
    private int daysOfMonth = 0;//某月的天数
    private int dayOfWeek = 0;//具体某一天是星期几
    private int lastDaysOfMonth = 0; //上一个月的总天
    private Context context;
    private String[] dayNumber = new String[42]; //一个gridview中的日期存入此数组中
    private SpecialCalendar sc = null;
    private int currentYear = 0;
    private int currentMonth = 0;
    private int currentPosition = -1;
    private String currentDayStr = "";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
    private Set<Integer> schDateTagFlag = new HashSet<>(); //存储当月所有的日程日期(标签)
    private String showYear = "";
    //用于在头部显示的年份
    private String showMonth = ""; //用于在头部显示的月份
    private String animalsYear = "";
    private String leapMonth = "";
    //闰哪一个月
    private Set<String> mSet = null;
    private OnItemClickListener mOnItemClickListener;
    private List<ProjectOptionalTimeInfo> mOptionalTimes = new ArrayList<>();
    private List<String> mAgreedTimes = new ArrayList<>();
    private long mInitialTime = 0;

    public CalendarAdapter(Context context, long time) {
        this.context = context;
        mInitialTime = time;
        sc = new SpecialCalendar();
        int year = Integer.valueOf(SpecialCalendar.LongToString(time, "yyyy")).intValue();
        currentYear = year;
        int month = Integer.valueOf(SpecialCalendar.LongToString(time, "MM")).intValue();
        currentMonth = month; //得到跳转到的月份
        this.currentDayStr = SpecialCalendar.LongToString(time, "yyyy-MM-dd");
        getCalendar(currentYear, currentMonth);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public int getCount() {
        return dayNumber.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder myViewHolder = null;
        if (convertView == null || convertView.getTag() == null) {
            myViewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_select_time, null);
            myViewHolder.mIdTvItemSelectTimeDay = (TextView) convertView.findViewById(R.id.id_tv_item_select_time_day);
            myViewHolder.itemView = convertView.findViewById(R.id.item_view);
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (ViewHolder) convertView.getTag();
        }
        displayView(myViewHolder, position);
        return convertView;
    }

    private void displayView(final ViewHolder myViewHolder, final int position) {
        myViewHolder.mIdTvItemSelectTimeDay.setText(dayNumber[position]);
        myViewHolder.mIdTvItemSelectTimeDay.setBackgroundColor(Color.WHITE);//不是当前月为灰
        myViewHolder.mIdTvItemSelectTimeDay.setTextColor(ContextCompat.getColor(context, R.color.color_cccccc));//不是当前月为灰
        if (position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {
            // 当前月信息显示
            myViewHolder.mIdTvItemSelectTimeDay.setTextColor(ContextCompat.getColor(context, R.color.color_1a1a1a));// 当月字体设黑
            myViewHolder.mIdTvItemSelectTimeDay.setBackgroundColor(Color.WHITE);
            myViewHolder.mIdTvItemSelectTimeDay.setTag(AppConstant.CurrentDateState.NO_RESERVATION);
            if (position == currentPosition) {
                myViewHolder.mIdTvItemSelectTimeDay.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_course_current_time));
            }
            String itemTime = getItemTime(position);
            int week = TimeUtils.getWeekNumByDate(TimeUtils.string2Millis(itemTime, dateFormat));
            for (int i = 0; i < LibCollections.size(mOptionalTimes); i++) {
                ProjectOptionalTimeInfo timeInfo = mOptionalTimes.get(i);
                if (null == timeInfo) {
                    continue;
                }
                if (week == timeInfo.getWeek()) {
                    myViewHolder.mIdTvItemSelectTimeDay.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_course_not_can_appoin));
                    //今天之前的时间不显示可约状态背景色
                    long selectTime = TimeUtils.string2Millis(itemTime, dateFormat);
                    selectTime += timeInfo.getHour() * 60 * 60 * 1000;
                    if (selectTime < mInitialTime) {
                        if (position == currentPosition) {
                            myViewHolder.mIdTvItemSelectTimeDay.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_course_current_time));
                        } else {
                            myViewHolder.mIdTvItemSelectTimeDay.setTextColor(ContextCompat.getColor(context, R.color.color_1a1a1a));// 当月字体设黑
                            myViewHolder.mIdTvItemSelectTimeDay.setBackgroundColor(Color.WHITE);
                        }
                    } else {
                        if (position == currentPosition) {
                            myViewHolder.mIdTvItemSelectTimeDay.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_course_current_time_checked));
                        }
                        myViewHolder.mIdTvItemSelectTimeDay.setTag(AppConstant.CurrentDateState.CAN_RESERVATION);
                    }
                }
            }

            if (mAgreedTimes.contains(getItemTime(position))) {
                myViewHolder.mIdTvItemSelectTimeDay.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_course_can_appoin));
                myViewHolder.mIdTvItemSelectTimeDay.setTextColor(Color.WHITE);
                myViewHolder.mIdTvItemSelectTimeDay.setTag(AppConstant.CurrentDateState.ALREADY_RESERVATION);
            }
        } else {
            myViewHolder.mIdTvItemSelectTimeDay.setTag(AppConstant.CurrentDateState.NO_RESERVATION);
        }
        myViewHolder.itemView.setOnClickListener(view -> {
            if (position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, position);
                }
            }
        });

    }

    /**
     * 可预约的时间
     */
    public void setOptionalAgreedTime(List<ProjectOptionalTimeInfo> optionalList, List<String> agreedList) {
        mOptionalTimes.clear();
        if (null == optionalList) {
            optionalList = new ArrayList<>(0);
        }
        if (null == agreedList) {
            agreedList = new ArrayList<>(0);
        }
        mOptionalTimes.addAll(optionalList);
        mAgreedTimes.clear();
        mAgreedTimes.addAll(agreedList);
        notifyDataSetChanged();
    }

    /**
     * 设置当前选择的item
     *
     * @param position
     */
    public void setCurrentPosition(int position) {
        currentPosition = position;
        notifyDataSetChanged();
    }

    /**
     * * 更新日历数据
     *    
     */
    public void upData(int stepYear, int stepMonth) {
        getCalendar(stepYear, stepMonth);
    }

    /**
     *    * 得到某年的某月的天数且这月的第一天是星期几
     *    *
     *    * @param year
     *    * @param month
     *    
     */
    private void getCalendar(int year, int month) {
        isLeapyear = SpecialCalendar.isLeapYear(year);//是否为闰年
        daysOfMonth = SpecialCalendar.getDaysOfMonth(isLeapyear, month); //某月的总天数
        dayOfWeek = SpecialCalendar.getWeekdayOfMonth(year, month);//某月第一天为星期几
        lastDaysOfMonth = SpecialCalendar.getDaysOfMonth(isLeapyear, month - 1); //上一个月的总天数
        getWeek(year, month);
    }

    /**
     *    * 将一个月中的每一天的值添加入数组dayNuber中
     *    *
     *    * @param year
     *    * @param month
     *    
     */
    private void getWeek(int year, int month) {
        schDateTagFlag.clear();
        currentPosition = -1;
        int j = 1;
        //得到当前月的所有日程日期(这些日期需要标记)
        for (int i = 0; i < dayNumber.length; i++) {
            if (i < dayOfWeek) { //前一个月
                int temp = lastDaysOfMonth - dayOfWeek + 1;
                dayNumber[i] = (temp + i) + "";
            } else if (i < daysOfMonth + dayOfWeek) {//本月
                int day = i - dayOfWeek + 1; //得到的日期
                dayNumber[i] = i - dayOfWeek + 1 + "";
                //对于当前月才去标记当前日期
                String yearStr = String.valueOf(year);
                String monthStr = getStr(String.valueOf(month), 2);
                String dayStr = getStr(String.valueOf(day), 2);
                String timeAll = yearStr + "-" + monthStr + "-" + dayStr;
                if (timeAll.equals(currentDayStr)) {//判断选中的位置
                    currentPosition = i;
                }
                if (mSet != null && mSet.size() > 0) {
                    for (String s : mSet) {//迭代器遍历判断是否需要带标签
                        if (timeAll.equals(s)) {
                            schDateTagFlag.add(i);
                        }
                    }
                }
                setShowYear(yearStr);
                setShowMonth(String.valueOf(month));
            } else { //下一个月
                dayNumber[i] = j + "";
                j++;
            }
        }
    }

    /**
     *    * 获取当前时间 样式:20170830
     *    * @param position
     *    * @return
     *    
     */
    public String getItemTime(int position) {
        String month = getStr(getShowMonth(), 2);
        String day = getStr(getDateByClickItem(position), 2);
        return getShowYear() + "年" + month + "月" + day + "日";
    }

    /**
     *    * 保留N位整数,不足前面补0
     *    *
     *    * @param file String
     *    * @param bit 位数
     *    * @return
     *    
     */
    public static String getStr(String file, int bit) {
        while (file.length() < bit) {
            file = "0" + file;
        }
        return file;
    }

    /**
     *    * 点击每一个item时返回item中的日期
     *    *
     *    * @param position
     *    * @return
     *    
     */
    public String getDateByClickItem(int position) {
        return dayNumber[position];
    }

    /**
     *    * 在点击gridView时，得到这个月中第一天的位置
     *    *
     *    * @return
     *    
     */
    public int getStartPositon() {
        return dayOfWeek + 7;
    }

    /**
     *    * 在点击gridView时，得到这个月中最后一天的位置
     *    *
     *    * @return
     *    
     */

    public int getEndPosition() {
        return (dayOfWeek + daysOfMonth + 7) - 1;
    }

    public String getShowYear() {
        return showYear;
    }

    public void setShowYear(String showYear) {
        this.showYear = showYear;
    }

    public String getShowMonth() {
        return showMonth;
    }

    public void setShowMonth(String showMonth) {
        this.showMonth = showMonth;
    }

    public String getAnimalsYear() {
        return animalsYear;
    }

    public void setAnimalsYear(String animalsYear) {
        this.animalsYear = animalsYear;
    }

    public String getLeapMonth() {
        return leapMonth;
    }

    public void setLeapMonth(String leapMonth) {
        this.leapMonth = leapMonth;
    }

    public Set<String> getSet() {
        return mSet;
    }

    public void setSet(Set<String> set) {
        mSet = set;
    }

    class ViewHolder {
        TextView mIdTvItemSelectTimeDay;
        View itemView;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
