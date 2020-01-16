package cn.longmaster.hospital.doctor.util;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.utils.StringUtils;

/**
 * 查房相关工具类
 * Created by W·H·K on 2018/6/6.
 */

public class CommonlyUtil {

    /**
     * 获取星期
     *
     * @param date
     * @return
     */
    public static String getWeek(Date date, Context context) {
        String[] weeks = context.getResources().getStringArray(R.array.week);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return weeks[week_index];
    }

    /**
     * 获取上午时间集合
     */
    public static List<String> getMorningTimeList() {
        List<String> list = new ArrayList<>();
        String str = "";
        for (int i = 8; i < 12; i++) {
            if (i < 10) {
                str = "0" + i;
            } else {
                str = "" + i;
            }
            list.add(str);
        }
        return list;
    }

    /**
     * 获取中午时间集合
     */
    public static List<String> getNoonTimeList() {
        List<String> list = new ArrayList<>();
        list.add("12");
        list.add("13");
        return list;
    }

    /**
     * 获取下午时间集合
     */
    public static List<String> getAfternoonTimeList() {
        List<String> list = new ArrayList<>();
        for (int i = 14; i < 18; i++) {
            list.add("" + i);
        }
        return list;
    }

    /**
     * 获取其他时间集合
     */
    public static List<String> getOtherTimeList() {
        List<String> list = new ArrayList<>();
        for (int i = 18; i < 24; i++) {
            list.add("" + i);
        }
        for (int i = 0; i < 8; i++) {
            list.add("0" + i);
        }
        return list;
    }

    /**
     * 时间yyyy-mm-dd HH:mm:ss转为yyyy.mm.dd HH:mm
     *
     * @param strTime
     * @return
     * @throws ParseException
     */
    public static String conversionTime(String strTime) {
        String str = "";
        if (StringUtils.isEmpty(strTime)) {
            return str;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(strTime);
            SimpleDateFormat f = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            str = f.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 时间yyyy-mm-dd HH:mm:ss转为yyyy.mm.dd HH:mm:ss
     *
     * @param strTime
     * @return
     * @throws ParseException
     */
    public static String conversionTime2(String strTime) {
        String str = "";
        if (StringUtils.isEmpty(strTime)) {
            return str;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(strTime);
            SimpleDateFormat f = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            str = f.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 时间yyyy-mm-dd HH:mm:ss转为yyyy.mm.dd
     *
     * @param strTime
     * @return
     * @throws ParseException
     */
    public static String conversionTime3(String strTime) {
        String str = "";
        if (StringUtils.isEmpty(strTime)) {
            return str;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(strTime);
            SimpleDateFormat f = new SimpleDateFormat("yyyy.MM.dd");
            str = f.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
