package com.sykent.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.RawRes;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

    /**
     * assets 文件转为字符串
     *
     * @param context
     * @param file    文件路径
     * @return
     */
    public static String assets2String(Context context, String file) {
        if (context == null || TextUtils.isEmpty(file)) {
            return null;
        }

        String result = null;
        InputStream is = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
        byte[] buff = new byte[1024];
        try {
            is = context.getAssets().open(file);
            int readSize;
            while ((readSize = is.read(buff)) > -1) {
                baos.write(buff, 0, readSize);
            }
            result = new String(baos.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * raw 资源转为字符串
     *
     * @param context
     * @param rawId   资源id
     * @return
     */
    public static String raw2String(Context context, @RawRes int rawId) {
        if (context == null || rawId == -1) {
            return null;
        }

        String result = null;
        InputStream is = context.getResources().openRawResource(rawId);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
        byte[] buff = new byte[1024];
        int readSize;

        try {
            while ((readSize = is.read(buff)) > -1) {
                baos.write(buff, 0, readSize);
            }

            result = new String(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
