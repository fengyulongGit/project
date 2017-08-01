package com.android.common.utils;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 字符串常用工具类
 * Created by mac on 2016/11/21.
 */

public class StringUtils {
    public static Map<String, String[]> urlParams2Map(String urlString) {

        Map<String, String[]> paramsMap = new HashMap<String, String[]>();
        int questIndex = urlString.indexOf('?');
        if (questIndex == -1) {
            return paramsMap;
        }

        String url = urlString.substring(0, questIndex);
        String queryString = urlString.substring(questIndex + 1, urlString.length());

        if (queryString != null && queryString.length() > 0) {
            int ampersandIndex, lastAmpersandIndex = 0;
            String subStr, param, value;
            String[] paramPair, values, newValues;
            do {
                ampersandIndex = queryString.indexOf('&', lastAmpersandIndex) + 1;
                if (ampersandIndex > 0) {
                    subStr = queryString.substring(lastAmpersandIndex, ampersandIndex - 1);
                    lastAmpersandIndex = ampersandIndex;
                } else {
                    subStr = queryString.substring(lastAmpersandIndex);
                }
                paramPair = subStr.split("=");
                param = paramPair[0];
                value = paramPair.length == 1 ? "" : paramPair[1];
                try {
                    value = URLDecoder.decode(value, "UTF-8");
                } catch (Exception ignored) {
                }
                if (paramsMap.containsKey(param)) {
                    values = paramsMap.get(param);
                    int len = values.length;
                    newValues = new String[len + 1];
                    System.arraycopy(values, 0, newValues, 0, len);
                    newValues[len] = value;
                } else {
                    newValues = new String[]{value};
                }
                paramsMap.put(param, newValues);
            } while (ampersandIndex > 0);
        }
        return paramsMap;
    }

    public static String replace(String oldStr, char oldChar, char newChar) {
        char[] charArray = oldStr.toCharArray();
        for (int i = 0; i < oldStr.length(); ++i) {
            if (oldStr.charAt(i) == oldChar) {
                charArray[i] = newChar;
            }
        }
        return String.valueOf(charArray);
    }

}
