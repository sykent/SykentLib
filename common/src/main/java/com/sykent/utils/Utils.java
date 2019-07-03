package com.sykent.utils;

import com.blankj.utilcode.util.ScreenUtils;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/07/02
 */
public class Utils {
    private static int sScreenW;
    private static int sScreenH;

    public static void init() {
        sScreenW = ScreenUtils.getScreenWidth();
        sScreenH = ScreenUtils.getScreenHeight();
    }

    public static int getRealPixel(int pxSrc) {
        int result = (int) (pxSrc * (sScreenW / 720.f));
        if (pxSrc != 0 && result == 0) {
            result = 1;
        }

        return result;
    }


    public static int getScreenWidth() {
        return sScreenW;
    }

    public static int getScreenHeight() {
        return sScreenH;
    }
}
