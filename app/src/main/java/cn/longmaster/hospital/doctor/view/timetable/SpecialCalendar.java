package cn.longmaster.hospital.doctor.view.timetable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *  * 日历工具类
 *  
 */
public class SpecialCalendar {
    private static int daysOfMonth = 0;//某月的天数
    private static int dayOfWeek = 0;//具体某一天是星期几

    /**
     *    * 判断是否为闰年
     *    * @param year
     *    * @return
     *    
     */
    public static boolean isLeapYear(int year) {
        if (year % 100 == 0 && year % 400 == 0) {
            return true;
        } else {
            return year % 100 != 0 && year % 4 == 0;
        }
    }

    /**
     *    * 得到某月有多少天数
     *    * @param isLeapyear
     *    * @param month
     *    * @return
     *    
     */

    public static int getDaysOfMonth(boolean isLeapyear, int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                daysOfMonth = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                daysOfMonth = 30;
                break;
            case 2:
                if (isLeapyear) {
                    daysOfMonth = 29;
                } else {
                    daysOfMonth = 28;
                }

        }
        return daysOfMonth;
    }

    /**
     *    * 指定某年中的某月的第一天是星期几
     *    * @param year
     *    * @param month
     *    * @return
     *    
     */
    public static int getWeekdayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return dayOfWeek;
    }

    /**
     * 时间string转long
     *
     * @param time
     * @param formatType
     * @return
     */
    public static long StringToLong(String time, String formatType) {
        if (time != null) {
            Date date = StringToDate(time, formatType);
            return date.getTime();
        }
        return 0;
    }

    /**
     * 时long转string
     *
     * @param num
     * @param formatType
     * @return
     */
    public static String LongToString(long num, String formatType) {
        Date date = LongToDate(num);
        return DateToString(date, formatType);

    }

    /**
     * 时间String转Date
     *
     * @param time
     * @param formatType
     * @return
     */
    public static Date StringToDate(String time, String formatType) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            if (time != null) {
                date = formatter.parse(time);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 时间LongToDate
     *
     * @param num
     * @return
     */
    public static Date LongToDate(long num) {
        Date date = new Date(num);
        return date;
    }

    /**
     * 时间DateToString
     *
     * @param date
     * @param formatType
     * @return
     */
    public static String DateToString(Date date, String formatType) {
        if (date != null) {
            return new SimpleDateFormat(formatType).format(date);
        }
        return null;
    }

    /**
     * 时间DateToLong
     *
     * @param date
     * @return
     */
    public static long DateToLong(Date date) {
        if (date != null) {
            return date.getTime();
        }
        return 0;

    }

    /**
     * 下个或上个月
     *
     * @param date
     * @return
     */
    public static String getMonth(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + i);
        Date time = calendar.getTime();
        return DateToString(time, "yyyy年MM月dd");
    }

    /**
     * 获取星期
     */
    public static String getWeek(Date date) {
        String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return weeks[week_index];
    }

    /**
     *    * 保留N位整数,不足前面补0
     *    *
     *    * @param file String
     *    * @param bit 位数
     *    * @return
     *    
     */
    public static String getBitStr(String file, int bit) {
        while (file.length() < bit) {
            file = "0" + file;
        }
        return file;
    }

    /**
     * 获取某天所在周的所有日期
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static List<String> getWeekDayList(String date, String formatSrt) {
        // 存放每一天时间的集合
        List<String> weekMillisList = new ArrayList<>();
        long dateMill = 0;
        try {
            dateMill = StringToLong(date, formatSrt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateMill);
        // 本周的第几天
        int weekNumber = calendar.get(Calendar.DAY_OF_WEEK);
        // 获取本周一的毫秒值
        long mondayMill = dateMill - 86400000 * (weekNumber - 2);
        for (int i = 0; i < 7; i++) {
            weekMillisList.add(LongToString(mondayMill + 86400000 * i, formatSrt));
        }
        return weekMillisList;
    }
}