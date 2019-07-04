package com.sykent.utils;

import android.text.TextUtils;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/07/03
 */
public class NumberUtils {
    /**
     * 判断字符串是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String reg = "^[0-9]+(.[0-9]+)?$";
        return str.matches(reg);
    }

    public static int string2Int(String numStr) {
        if (TextUtils.isEmpty(numStr)) {
            return 0;
        }
        numStr = numStr.replace(" ", "");
        if (!isNumeric(numStr)) {
            return 0;
        }
        return Integer.parseInt(numStr);
    }

    public static long string2Long(String numStr) {
        if (TextUtils.isEmpty(numStr)) {
            return 0;
        }
        numStr = numStr.replace(" ", "");
        if (!isNumeric(numStr)) {
            return 0;
        }
        return Long.parseLong(numStr);
    }

    public static float string2Float(String numStr) {
        if (TextUtils.isEmpty(numStr)) {
            return 0;
        }
        numStr = numStr.replace(" ", "");
        if (!isNumeric(numStr)) {
            return 0;
        }
        return Float.parseFloat(numStr);
    }

    /**
     * @param colorStr
     * @return
     */
    public static int strColor2Int(String colorStr) {
        int colorBg = 0;
        if (colorStr != null && !colorStr.isEmpty()) {
            if (colorStr.length() == 8) {
                colorBg = Integer.parseInt(colorStr, 16);
            } else {
                colorBg = 0xff000000 | Integer.parseInt(colorStr, 16);
            }
        }
        return colorBg;
    }
}
