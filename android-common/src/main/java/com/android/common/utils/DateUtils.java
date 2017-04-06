package com.android.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fengyulong on 2016/8/5.
 */
public class DateUtils {
    /**
     * 将时间字符串转为2014-11-18 12:20:16
     *
     * @param s
     * @return
     */
    public static String formatStringTime(String s) {
        String dateStr = "";
        if (s != null && !(s.equals(""))) {
            String st = s.trim();
            if (st.length() == 14) {
                String year = s.substring(0, 4);
                String month = s.substring(4, 6);
                String day = s.substring(6, 8);
                String hour = s.substring(8, 10);
                String minute = s.substring(10, 12);
                String seconds = s.substring(12, 14);
                dateStr = year + "-" + month + "-" + day + " " + hour + ":"
                        + minute + ":" + seconds;
            }
        }
        return dateStr;
    }

    /**
     * 将时间字符串转为2014/11/18 12:20:16
     *
     * @param s
     * @return
     */
    public static String formatStrTime(String s) {
        String dateStr = "";
        if (s != null && !(s.equals(""))) {
            String st = s.trim();
            if (st.length() == 14) {
                String year = s.substring(0, 4);
                String month = s.substring(4, 6);
                String day = s.substring(6, 8);
                String hour = s.substring(8, 10);
                String minute = s.substring(10, 12);
                String seconds = s.substring(12, 14);
                dateStr = year + "/" + month + "/" + day + " " + hour + ":"
                        + minute + ":" + seconds;
            }
        }
        return dateStr;
    }

    public static String formatString(String s) {
        String dateStr = "";
        if (s != null && !(s.equals(""))) {
            String st = s.trim();
            if (st.length() == 14) {
                String year = s.substring(0, 4);
                String month = s.substring(4, 6);
                String day = s.substring(6, 8);
                dateStr = year + "/" + month + "/" + day;
            }
        }
        return dateStr;
    }

    public static String dataFormat(String str) {
        String strData = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date(Long.valueOf(str));
            strData = format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strData;
    }

    /**
     * 获取系统时间（获取不到服务器时间时使用）
     *
     * @return
     */
    public static String getSysTime() {
        String result = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        result = format.format(curDate);
        return result;
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
