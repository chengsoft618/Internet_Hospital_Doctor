package cn.longmaster.doctorlibrary.util.common;

import android.content.Context;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.longmaster.doctorlibrary.R;
import cn.longmaster.doctorlibrary.util.UtilStatus;
import cn.longmaster.doctorlibrary.util.log.Logger;

/**
 * 日期处理工具类
 * Created by Tengshuxiang on 2015-08-04.
 */
public class DateUtil {
    public final static String TAG = DateUtil.class.getSimpleName();

    /**
     * 将毫秒转换成如“2014-12-08 14:59:11”格式
     *
     * @param millisecond 毫秒格式的时间
     */
    public static String millisecondToStandardDate(long millisecond) {
        Date date = new Date(millisecond);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return format.format(date);
    }

    public static String millisecondToYMD(long millisecond) {
        Date date = new Date(millisecond);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.format(date);
    }

    public static String dateToYMD(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return format.format(date);
    }

    /**
     * 日期转化毫秒，日期格式为“2014-12-12 12:12:12”
     *
     * @return 毫秒数
     */
    public static long dateToMillisecond(String data) {
        if (data == null || data.isEmpty()) {
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            sdf.setLenient(false);
            sdf.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        Calendar cal = sdf.getCalendar();
        return cal.getTimeInMillis();
    }

    /**
     * 判断日期是否符合格式
     *
     * @param str    日期字符串
     * @param format 日期格式
     * @return true 符合 ；false 不符合
     */
    public static boolean isValidDate(String str, String format) {
        boolean convertSuccess = true;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            sdf.setLenient(false);
            sdf.parse(str);
        } catch (ParseException e) {
            convertSuccess = false;
        }
        return convertSuccess;
    }

    /**
     * 获取岁数
     *
     * @param context  上下文
     * @param birthday 生日日期
     * @return 岁数
     */
    @SuppressWarnings("deprecation")
    public static int getAge(Context context, String birthday) {
        if (birthday == null || birthday.length() != 8) {
            return 0;
        }

        if (UtilStatus.isDebugMode()) {
            Logger.logD(Logger.COMMON, TAG + "->getAge()->birthday:" + birthday);
        }

        int year = Integer.valueOf(birthday.substring(0, 4));
        int monthOfYear = Integer.valueOf(birthday.substring(4, 6));
        int dayOfMonth = Integer.valueOf(birthday.substring(6));

        Date date = new Date(year, monthOfYear, dayOfMonth);
        Calendar calendar = Calendar.getInstance();

        if (calendar.before(date)) {
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
        }

        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        int yearOfBirthday = date.getYear();
        int monthOfBirthday = date.getMonth();
        int dayOfBirthday = date.getDate();

        int age = currentYear - yearOfBirthday;

        if (currentMonth <= monthOfBirthday) {
            if (currentMonth == monthOfBirthday) {
                if (currentDay < dayOfBirthday) {
                    age--;
                }
            } else {
                age--;
            }
        }
        return age;
    }

    /**
     * 判断两个时间是否在一天内
     *
     * @param firstDate  基本日期
     * @param secondDate 比较日期
     * @return 如果secondDate与firstDate在一天内返回true，否则返回false
     */
    public static boolean isSameDay(long firstDate, long secondDate) {
        boolean result = false;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String firstDateFormat = format.format(new Date(firstDate));
        String secondDateFormat = format.format(new Date(secondDate));
        if (firstDateFormat.equals(secondDateFormat)) {
            result = true;
        }
        return result;
    }

    /**
     * "2014-12-08 14:59:12"转换成“2014年12月08日 14:59”格式
     *
     * @param data 时间
     */
    public static String standardDateToChinaDate(Context context, String data) {
        if (data == null || data.isEmpty()) {
            return "";
        }
        Date date = new Date(DateUtil.dateToMillisecond(data));
        SimpleDateFormat format = new SimpleDateFormat(context.getString(R.string.date_format_ymdhm), Locale.getDefault());
        return format.format(date);
    }

    /**
     * "2014-12-08 14:59:12"转换成“2014/12/08/ 14:59”格式
     *
     * @param data 时间
     */
    public static String standardDateToDate(Context context, String data) {
        if (data == null || data.isEmpty()) {
            return "";
        }
        Date date = new Date(DateUtil.dateToMillisecond(data));
        SimpleDateFormat format = new SimpleDateFormat(context.getString(R.string.date_format_ymdhm2), Locale.getDefault());
        return format.format(date);
    }

    /**
     * 毫秒转换成“2014年12月08日 14点59分”格式
     *
     * @param ms 时间
     */
    public static String millisecondToChinaDate(Context context, long ms) {
        Date date = new Date(ms);
        SimpleDateFormat format = new SimpleDateFormat(context.getString(R.string.date_format_ymdhm_china), Locale.getDefault());
        return format.format(date);
    }

    /**
     * 判断是否是同一年
     *
     * @param firstDate
     * @param secondDate
     * @return
     */
    public static boolean isSameYear(long firstDate, long secondDate) {
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        Date currentTime = new Date(firstDate);
        Date revTime = new Date(secondDate);
        if (formatYear.format(currentTime).equals(formatYear.format(revTime))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 转换为制定格式的时间
     *
     * @param format
     * @param date
     * @return
     */
    public static String millisecondToFormatDate(String format, long date) {
        SimpleDateFormat formatTime = new SimpleDateFormat(format);
        return formatTime.format(date);
    }

    /**
     * 根据日期获取星期
     *
     * @param date
     * @return
     */
    public static String getWeekByDate(Context context, String date) {
        String Week = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        int daOfWeek = c.get(Calendar.DAY_OF_WEEK);
        if (daOfWeek == 1) {
            Week += context.getString(R.string.day_of_week_0);
        }
        if (daOfWeek == 2) {
            Week += context.getString(R.string.day_of_week_1);
        }
        if (daOfWeek == 3) {
            Week += context.getString(R.string.day_of_week_2);
        }
        if (daOfWeek == 4) {
            Week += context.getString(R.string.day_of_week_3);
        }
        if (daOfWeek == 5) {
            Week += context.getString(R.string.day_of_week_4);
        }
        if (daOfWeek == 6) {
            Week += context.getString(R.string.day_of_week_5);
        }
        if (daOfWeek == 7) {
            Week += context.getString(R.string.day_of_week_6);
        }

        return Week;
    }

    /**
     * 获取指定格式的日期
     *
     * @param date
     * @param formatStr
     * @return
     */
    public static String getTime(Date date, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(date);
    }

    /**
     * 获取指定格式的日期
     *
     * @param date
     * @param formatStr
     * @return
     */
    public static String getTime(String date, String formatStr) {
        return getTime(new Date(DateUtil.dateToMillisecond(date)), formatStr);
    }

    /**
     * 获取几个月前的日期
     *
     * @param date     传入的日期
     * @param monthNum 月数
     * @return
     */
    public static String getMonthAgo(Date date, int monthNum) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -monthNum);
        calendar.set(Calendar.DATE, 1);
        return simpleDateFormat.format(calendar.getTime());
    }

}
