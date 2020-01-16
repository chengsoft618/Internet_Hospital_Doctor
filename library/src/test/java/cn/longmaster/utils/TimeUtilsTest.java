package cn.longmaster.utils;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author ABiao_Abiao
 * @date 2019/12/2 15:16
 * @description:
 */
public class TimeUtilsTest {

    @Test
    public void getFriendlyTimeSpanByNow() {
        //System.out.println(TimeUtils.getFriendlyTimeSpanByNow("2010-01-15 14:30:00"));
       System.out.println(TimeUtils.getFriendlyTimeSpanByNow("2020-12-02 14:13:02"));
    }
    @Test
    public void string2String(){
        log(TimeUtils.string2String("2020-12-02 14:13:02", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())));
        log(TimeUtils.string2String("2020-12-02 14:13:02","yyyy-MM-dd"));
        log(TimeUtils.string2String("2020-12-02 14:13:02","yyyy-MM-dd"));
        log(TimeUtils.string2String("2020-12-02 14:13:02","yyyy-MM-dd"));
        log(TimeUtils.string2String("2020-12-02 14:13:02","yyyy-MM-dd"));
        log(TimeUtils.string2String("2020-12-02 14:13:02","yyyy-MM-dd"));
        log(TimeUtils.string2String("2020-12-02 14:13:02","yyyy-MM-dd"));
    }

    private void log(String msg){
        System.out.println(msg);
    }
}