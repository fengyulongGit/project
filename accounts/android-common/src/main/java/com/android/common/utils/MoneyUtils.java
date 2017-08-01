package com.android.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 金额工具类
 * Created by fengyulong on 2017/4/15.
 */

public class MoneyUtils {

    /**
     * 金额格式化
     *
     * @param s   金额
     * @param len 小数位数
     * @return 格式后的金额
     */
    public static String insertComma(String s, int len) {
        if (s == null || s.length() < 1) {
            return "";
        }
        NumberFormat formater = null;
        double num = Double.parseDouble(s);
        if (len == 0) {
            formater = new DecimalFormat("###,###");

        } else {
            StringBuffer buff = new StringBuffer();
            buff.append("###,###.");
            for (int i = 0; i < len; i++) {
                buff.append("#");
            }
            formater = new DecimalFormat(buff.toString());
        }
        return formater.format(num);
    }

    /**
     * 金额去掉“,”
     *
     * @param s 金额
     * @return 去掉“,”后的金额
     */
    public static String delComma(String s) {
        String formatString = "";
        if (s != null && s.length() >= 1) {
            formatString = s.replaceAll(",", "");
        }

        return formatString;
    }


    /**
     * 两个Double数相加
     */
    public static double add(double v1, double v2) {
        return add(v1 + "", v2 + "");
    }

    /**
     * 两个Double数相加
     */
    public static double add(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return make45number(b1.add(b2).doubleValue(), 2);
    }

    /**
     * 两个Double数相减
     */
    public static double sub(double v1, double v2) {
        return sub(v1 + "", v2 + "");
    }

    /**
     * 两个Double数相减
     */
    public static double sub(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return make45number(b1.subtract(b2).doubleValue(), 2);
    }

    /**
     * 两个Double数相乘
     */
    public static double mul(double v1, double v2) {
        return mul(v1 + "", v2 + "");
    }

    /**
     * 两个Double数相乘
     */
    public static double mul(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return make45number(b1.multiply(b2).doubleValue(), 2);
    }

    /**
     * 两个Double数相除
     */
    public static double div(double v1, double v2) {
        return div(v1 + "", v2 + "");
    }

    /**
     * 两个Double数相除
     */
    public static double div(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return make45number(b1.divide(b2).doubleValue(), 2);
    }

    public static double make45number(double num, int bit) {
        BigDecimal b = new BigDecimal(num);
        double result = b.setScale(bit, BigDecimal.ROUND_HALF_UP).doubleValue();
        return result;
    }

    /**
     * 若有，先去除，进行计算之后再添加
     */
    public static String formatSymbol(String str) {
        if (ValidateUtil.isEmpty(str)) {
            return str;
        }
        str = insertComma(str, 2);

        int position = str.indexOf('.');
        if (position < 0) {
            str = str + ".00";
        } else {
            if (str.length() - 1 - position == 0) {
                str = str + "00";
            } else if (str.length() - 1 - position == 1) {
                str = str + "0";
            } else {
                str = str.substring(0, position + 3);
            }
        }

        return str;
    }

}
