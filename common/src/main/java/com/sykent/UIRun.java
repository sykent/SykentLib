package com.sykent;

import android.os.Handler;
import android.os.Looper;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/07/04
 */
public class UIRun {

    public static void post(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public static void postDelayed(Runnable runnable, long delayMillis) {
        new Handler(Looper.getMainLooper()).postDelayed(runnable, delayMillis);
    }
}
