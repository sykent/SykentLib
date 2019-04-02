package com.sykent.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/03/26
 */
public class FileUtils {
    public static boolean isAssetsExists(
            Context context, String fileDir, String fileName) {
        AssetManager am = context.getAssets();
        try {
            String[] names = am.list(fileDir);
            for (int i = 0; i < names.length; i++) {
                if (names[i].equals(fileName.trim())) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
