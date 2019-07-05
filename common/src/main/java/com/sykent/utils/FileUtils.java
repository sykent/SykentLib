package com.sykent.utils;

import android.content.Context;
import android.content.res.AssetManager;
import androidx.annotation.RawRes;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.content.ContentValues.TAG;

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


    /**
     * 把流数据写到SD卡
     *
     * @param close 关闭stream
     */
    public static boolean write2SD(InputStream is, String path, boolean append, boolean deleteOld,
                                   boolean close) {
        if (is == null || TextUtils.isEmpty(path)) {
            Log.i(TAG, "InputStream is null or path is (null or empty)");
            return false;
        }
        File file = new File(path);
        if (file.isDirectory()) {
            Log.i(TAG, "path is directory");
            return false;
        }
        if (file.exists()) {
            if (deleteOld) {
                file.delete();
            } else {
                return true;
            }
        } else {
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    Log.i(TAG, "path's parent not exists");
                    return false;
                }
            }
        }
        try {
            if (!file.createNewFile()) {
                Log.i(TAG, "file(path) create new file error");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "IOException");
            return false;
        }

        OutputStream os = null;
        try {
            os = new FileOutputStream(path, append);
            byte[] buf = new byte[4096];
            int len = 0;
            while ((len = is.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
            os.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, "FileNotFoundException");
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "IOException");
            return false;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                os = null;
            }
            if (close && is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                is = null;
            }
        }
        return true;
    }

    /**
     * @param is        数据流
     * @param path      文件路径 包含文件名
     * @param deleteOld 是否删除旧文件
     * @param close     是否关闭数据流
     */
    public static boolean write2SD(InputStream is, String path, boolean deleteOld, boolean close) {
        return write2SD(is, path, false, deleteOld, close);
    }

    /**
     * @param is   数据流
     * @param path 文件路径 包含文件名
     */
    public static boolean write2SD(InputStream is, String path) {
        return write2SD(is, path, true, true);
    }

    /**
     * 将文本写入SD卡文件
     *
     * @param content   内容
     * @param path      文件路径 包含文件名
     * @param deleteOld 是否删除旧文件
     */
    public static boolean write2SD(String content, String path, boolean deleteOld) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        return write2SD(content.getBytes(), path, deleteOld);
    }

    /**
     * 将文本写入SD卡文件，会删除旧文件
     *
     * @param content 内容
     * @param path    文件路径 包含文件名
     */
    public static boolean write2SD(String content, String path) {
        return write2SD(content, path, true);
    }

    /**
     * 将byte数组数据保存的sd卡文件中
     */
    public static boolean write2SD(byte[] data, int offset, int count, String path,
                                   boolean deleteOld, boolean append) {
        if (data == null) {
            return false;
        }
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        if (file.isDirectory()) {
            Log.i(TAG, "path is directory");
            return false;
        } else {
            if (file.exists()) {
                if (deleteOld) {
                    if (!file.delete()) {
                        return false;
                    }
                }
            } else {
                if (!file.getParentFile().exists()) {
                    if (!file.getParentFile().mkdirs()) {
                        return false;
                    }
                }
            }
        }
        if (count == -1 || count > data.length) {
            count = data.length;
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(file, append);
            os.write(data, offset, count);
            os.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, "FileNotFoundException");
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "IOException");
            return false;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                os = null;
            }
        }
        return true;
    }

    /**
     * 将byte数组数据保存的sd卡文件中
     */
    public static boolean write2SD(byte[] data, String path, boolean deleteOld) {
        return write2SD(data, 0, -1, path, deleteOld, false);
    }

    /**
     * 将图片保存到sd卡目录
     */
    public static boolean write2SD(Bitmap bitmap, String path, boolean deleteOld,
                                   boolean needRecycle) {
        if (bitmap == null || bitmap.isRecycled()) {
            return false;
        }
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        Bitmap.CompressFormat format = Bitmap.CompressFormat.PNG;
        if (path.toLowerCase().endsWith(".jpg")) {
            format = Bitmap.CompressFormat.JPEG;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Bitmap temp = null;
        bitmap.compress(format, 100, baos);
        byte[] data = baos.toByteArray();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        baos = null;
        if (needRecycle && bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
        if (temp != null) {
            temp.recycle();
            temp = null;
        }
        return write2SD(data, path, deleteOld);
    }

    /**
     * 将图片保存到sd卡目录
     *
     * @param deleteOld 是否删除旧文件
     */
    public static boolean write2SD(Bitmap bitmap, String path, boolean deleteOld) {
        return write2SD(bitmap, path, deleteOld, true);
    }
}
