package com.android.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 * Created by fengyulong on 2016/8/5.
 */
public class DateUtils {


    /**
     * 格式化时间格式
     */
    public static String format(String object, String pattern) {
        String strData = "";
        try {
            long ts = Long.valueOf((String) object);
            strData = format(ts, pattern);
        } catch (Exception e) {
            e.printStackTrace();
            return format(parse(object, "yyyy-MM-dd HH:mm:ss"), pattern);
        }
        return strData;
    }

    /**
     * 格式化时间格式
     */
    public static String format(long object, String pattern) {
        String strData = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            strData = format.format(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strData;
    }

    /**
     * 格式化时间格式
     */
    public static String format(Date object, String pattern) {
        String strData = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            strData = format.format(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strData;
    }

    /**
     * 时间字符串转换为date
     */
    public static Date parse(String str, String pattern) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            date = format.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 两个时间差的long
     */
    public static long getBetweenLong(String dateA, String dateB) {
        long result = 0;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            java.util.Date da = df.parse(dateA);
            java.util.Date db = df.parse(dateB);
            result = db.getTime() - da.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 传入差值long判断显示内容
     */
    public static String getShowTime(long cha_l) {
        String s_show = "";
        // 大于两天
        long SEC = 1000;
        long MIN = 60 * SEC;
        long HOUR = 60 * MIN;
        long DAY = 24 * HOUR;

        long d = cha_l / DAY;
        long h = (cha_l % DAY) / HOUR;
        long mm = ((cha_l % DAY) % HOUR) / MIN;
        long s = (((cha_l % DAY) % HOUR) % MIN) / SEC;
        if (d > 1) {
            String day_s = String.format("%02d", d);
            String second_s = String.format("%02d", s);
            String minute_s = String.format("%02d", mm);
            String hour_s = String.format("%02d", h);

            s_show = (day_s + "天" + hour_s + "小时" + minute_s + "分" + second_s + "秒");
        } else {
            // 小于两天,不显示天数
            // long SEC = 1000;
            // long MIN = 60 * SEC;
            // long HOUR = 60 * MIN;
            // long DAY = 24 * HOUR;

            // long d = cha_l / DAY;
            long ho = cha_l / HOUR;
            long min = (cha_l % HOUR) / MIN;
            long sec = ((cha_l % HOUR) % MIN) / SEC;
            // String day_s = String.format("%02d", d);
            String hour_s = String.format("%02d", ho);
            String minute_s = String.format("%02d", min);
            String second_s = String.format("%02d", sec);

            s_show = (hour_s + "小时" + minute_s + "分" + second_s + "秒");
        }
        return s_show;
    }
}
