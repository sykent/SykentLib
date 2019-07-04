package com.sykent.utils;

import android.content.Context;
import android.widget.Toast;

import me.drakeet.support.toast.ToastCompat;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/07/04
 */
public class ToastUtils {
    public static void showToast(Context context, String msg) {
        ToastCompat.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
