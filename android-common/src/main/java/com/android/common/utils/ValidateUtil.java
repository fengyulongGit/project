package com.android.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fengyulong on 2016/7/22.
 */
public class ValidateUtil {

    /**
     * 判断对象是否为null或空字符 注：不空返回true
     *
     * @param obj
     * @return
     */
    public static boolean isNotNull(Object obj) {
        if (obj != null && !(obj.equals(""))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断对象是否为null或空字符 注：不空返回true
     *
     * @param obj
     * @return
     */
    public static boolean isNull(Object obj) {
        if (obj == null || obj.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmpty(String content) {
        if (content == null || content.trim().length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isMobile(String mobile) {
        if (isEmpty(mobile))
            return false;

        if (mobile.length() != 11) {
            return false;
        }

        if (!mobile.startsWith("13") && !mobile.startsWith("15") &&
                !mobile.startsWith("18") && !mobile.startsWith("17") && !mobile.startsWith("14"))
            return false;

        return true;
    }

    public static boolean isUsername(String name) {
        if (isEmpty(name))
            return false;

        //TODO正则判断 A-Za-z0-9

        return true;
    }

    public static boolean isLengthInScope(String content, int minLength, int maxLength) {
        if (isEmpty(content))
            return false;

        if (content.length() >= minLength && content.length() <= maxLength)
            return true;

        return false;
    }

    public static boolean isContainSpace(String content) {
        if (isEmpty(content))
            return false;

        if (content.contains(" ")) {
            return true;
        }

        return false;
    }

    public static boolean isSampleString(String newPassword, String repassword) {
        if (newPassword == repassword)
            return true;

        if (newPassword != null && newPassword.equals(repassword))
            return true;

        return false;
    }

    public static String replaceMobile(String mobile) {
        if (mobile == null) {
            return "";
        }

        StringBuffer sb = new StringBuffer(mobile);
        if (mobile.length() < 11 && mobile.length() > 3) {
            sb.replace(1, mobile.length() - 3, "***");
            return sb.toString();
        } else if (mobile.length() >= 11) {
            sb.replace(3, 7, "****");
            return sb.toString();
        } else {
            return mobile;
        }
    }

    public static String replaceIdentityCard(String identityCard) {
        if (identityCard == null) {
            return "";
        }

        int length = identityCard.length();
        StringBuffer sb = new StringBuffer(identityCard);

        if (length == 15) {
            sb.replace(2, length - 2, "***********");
            return sb.toString();
        } else if (length == 18) {
            sb.replace(2, length - 2, "**************");
            return sb.toString();
        } else {
            return identityCard;
        }

    }

    public static String replaceNum(String num) {
        if (num == null) {
            return null;
        }
        StringBuffer buffer = new StringBuffer("");
        if (num.length() <= 4)
            return num;
        for (int i = 0; i < num.length(); i++) {
            char c = num.charAt(i);
            if (i >= 2 && i <= num.length() - 3) {
                buffer.append("*");
            } else {
                buffer.append(c);
            }
        }
        return buffer.toString();
    }

    public static String isIdentityCard(String identityCard) throws NumberFormatException, ParseException {
        String errorInfo = "";// 记录错误信息
        String[] valCodeArr = {"1", "0", "X", "9", "8", "7", "6", "5", "4",
                "3", "2"};
        String[] wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                "9", "10", "5", "8", "4", "2"};
        String ai = "";
        // ================ 号码的长度 15位或18位 ================
        if (identityCard.length() != 15 && identityCard.length() != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。";
            return errorInfo;
        }
        // =======================(end)========================
        // ================ 数字 除最后以为都为数字 ================
        if (identityCard.length() == 18) {
            ai = identityCard.substring(0, 17);
        } else if (identityCard.length() == 15) {
            ai = identityCard.substring(0, 6) + "19" + identityCard.substring(6, 15);
        }
        if (isNumeric(ai) == false) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return errorInfo;
        }
        // =======================(end)========================
        // ================ 出生年月是否有效 ================
        String strYear = ai.substring(6, 10);// 年份
        String strMonth = ai.substring(10, 12);// 月份
        String strDay = ai.substring(12, 14);// 月份
        if (isDataFormat(strYear + "-" + strMonth + "-" + strDay) == false) {
            errorInfo = "身份证生日无效。";
            return errorInfo;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150 || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
            errorInfo = "身份证生日不在有效范围。";
            return errorInfo;
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "身份证月份无效";
            return errorInfo;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "身份证日期无效";
            return errorInfo;
        }
        // ==============================================
        // ================ 判断最后一位的值 ================
        int totalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            totalmulAiWi = totalmulAiWi + Integer.parseInt(String.valueOf(ai.charAt(i))) * Integer.parseInt(wi[i]);
        }
        int modValue = totalmulAiWi % 11;
        String strVerifyCode = valCodeArr[modValue];
        ai = ai + strVerifyCode;
        if (identityCard.length() == 18) {
            if (ai.equals(identityCard) == false) {
                errorInfo = "身份证无效，不是合法的身份证号码";
                return errorInfo;
            }
        } else {
            return "";
        }
        // =====================(end)=====================
        return "";
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isDataFormat(String str) {
        boolean flag = false;
        //String regxStr="[1-9][0-9]{3}-[0-1][0-2]-((0[1-9])|([12][0-9])|(3[01]))";
        String regxStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        Pattern pattern1 = Pattern.compile(regxStr);
        Matcher isNo = pattern1.matcher(str);
        if (isNo.matches()) {
            flag = true;
        }
        return flag;
    }

    /**
     * 是否是汉字
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 是否是纯数字
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        if (isEmpty(str)) {
            return false;
        }
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("[0-9]*");
        java.util.regex.Matcher match = pattern.matcher(str);
        if (match.matches() == false) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isNumberNotZero(String str) {
        if (isNotNull(str)) {
            try {
                return Double.valueOf(str) != 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
