package com.sykent.imagedecode;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import android.view.View;

import com.blankj.utilcode.util.ScreenUtils;
import com.commit451.nativestackblur.NativeStackBlur;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/03/26
 */
public class BitmapBlur {
    public final static int DEFAULT_BLUR_COLOR = 0x04d000000;

    /**
     * 获取高斯模糊背景图Drawable
     *
     * @param context
     * @return
     */
    public static Drawable getBlurBackgraoundDrawable(Context context) {
        return new BitmapDrawable(context.getResources(), getBlurBackground(context));
    }

    /**
     * 获取高斯模糊背景图
     *
     * @param context
     * @return
     */
    public static Bitmap getBlurBackground(Context context) {
        long lastTime = System.currentTimeMillis();
        if (!(context instanceof Activity)) {
            return getDefaultBgBmp(context);
        }

        Activity activity = (Activity) context;
        Bitmap screenShot = screenShot(activity);
        Bitmap blurBmp = blurCoverColor(screenShot, DEFAULT_BLUR_COLOR);

        if (BitmapHelper.isInvalid(blurBmp)) {
            blurBmp = getDefaultBgBmp(context);
        }

        return blurBmp;
    }

    /**
     * 高斯模糊并覆盖一层颜色
     *
     * @param bitmap
     * @param color
     * @return
     */
    public static Bitmap blurCoverColor(Bitmap bitmap, int color) {
        return blur(bitmap, color, false);
    }

    public static Bitmap blurCoverColor(Bitmap bitmap, int color, boolean isRecycleSrc) {
        return blur(bitmap, color, isRecycleSrc);
    }

    public static Bitmap blur(Bitmap overlay) {
        return blur(overlay, DEFAULT_BLUR_COLOR, false);
    }

    public static Bitmap blur(Bitmap overlay, boolean isRecycleSrc) {
        return blur(overlay, DEFAULT_BLUR_COLOR, isRecycleSrc);
    }

    public static Bitmap blur(Bitmap overlay, int color) {
        return blur(overlay, color, false);
    }

    public static Bitmap blur(Bitmap overlay, int color, boolean isRecycleSrc) {
        if (BitmapHelper.isInvalid(overlay)) {
            return null;
        }
        int w = overlay.getWidth();
        int h = overlay.getHeight();
        int radius = 14;
        int min = Math.min(w, h);
        int scale = 1;

        // 模糊半径，是图片的radius/x*radius
        while (min / scale > 4 * radius) {
            scale++;
        }
        scale--;

        if (scale == 0) {
            scale = 1;
        }

        Bitmap bmp = Bitmap.createScaledBitmap(overlay,
                w / scale, h / scale, true);

//        if (isRecycleSrc) {
//            BitmapHelper.recycle(overlay);
//        }

        // 先覆盖背景颜色
        if (BitmapHelper.isValid(bmp) && color != 0x00000000) {
            Canvas canvas = new Canvas(bmp);
            Paint paint = new Paint();
            paint.setColor(color);
            canvas.drawRect(0, 0, bmp.getWidth(), bmp.getHeight(), paint);
        }

        Bitmap result = Bitmap.createScaledBitmap(NativeStackBlur.process(bmp, radius),
                (int) (w / 1f), (int) (h / 1f), true);

        BitmapHelper.recycle(bmp);

        return result;
    }

    /**
     * 获取背景高斯模糊的一部分
     *
     * @param context
     * @param srcRect 原rect
     * @param cutRect 相对于 srcRect裁剪的部位
     * @return bitmap
     */
    public static Bitmap getBlurBackgroundPartBitmap(
            Context context, Rect srcRect, Rect cutRect) {
        if (srcRect.width() <= 0 || srcRect.height() <= 0
                || cutRect.width() <= 0 || cutRect.height() <= 0) {
            throw new IllegalArgumentException(
                    "getBlurBackgroundPart: width and height must more than 0");
        }
        // 边界规整
        if (cutRect.left < srcRect.left) {
            cutRect.left = srcRect.left;
        }
        if (cutRect.top < srcRect.top) {
            cutRect.top = srcRect.top;
        }
        if (cutRect.right > srcRect.right) {
            cutRect.right = srcRect.right;
        }
        if (cutRect.bottom > srcRect.bottom) {
            cutRect.bottom = srcRect.bottom;
        }

        Bitmap resutBmp = null;
        Bitmap blurBmp = getBlurBackground(context);
        if (BitmapHelper.isInvalid(blurBmp)) {
            blurBmp = getDefaultBgBmp(context);
        }
        int bmpW = blurBmp.getWidth();
        int bmpH = blurBmp.getHeight();
        int x = (int) ((1.0f * cutRect.left / srcRect.width()) * bmpW);
        int y = (int) ((1.0f * cutRect.top / srcRect.height()) * bmpH);

        int width = (int) ((1.0f * cutRect.width() / srcRect.width()) * bmpW);
        int height = (int) ((1.0f * cutRect.height() / srcRect.height()) * bmpH);
        resutBmp = Bitmap.createBitmap(blurBmp, x, y, width, height);

        BitmapHelper.recycle(blurBmp);

        return resutBmp;
    }

    /**
     * 获取背景高斯模糊的一部分
     *
     * @param context
     * @param srcRect 原rect
     * @param cutRect 相对于 srcRect裁剪的部位
     * @return Drawable
     */
    public static Drawable getBlurBackgroundPartDrawable(
            Context context, Rect srcRect, Rect cutRect) {
        return new BitmapDrawable(context.getResources(),
                getBlurBackgroundPartBitmap(context, srcRect, cutRect));
    }

    /**
     * 获取默认的背景drawable
     *
     * @param context
     * @return
     */
    public static Drawable getDefaultBgDrawable(Context context) {
        return new BitmapDrawable(context.getResources(), getDefaultBgBmp(context));
    }

    /**
     * 获取默认的背景
     */
    public static Bitmap getDefaultBgBmp(Context context) {
        Bitmap bitmap = Bitmap.createBitmap(ScreenUtils.getScreenWidth() / 2,
                ScreenUtils.getScreenHeight() / 2, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(0xffffffff);
        return bitmap;
    }


    /**
     * 截屏
     *
     * @param activity
     * @return
     */
    public static Bitmap screenShot(@NonNull final Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        Bitmap bmp = decorView.getDrawingCache();
        Bitmap ret = null;
        if (BitmapHelper.isValid(bmp)) {
            Matrix matrix = new Matrix();
            matrix.postScale(0.5f, 0.5f);
            ret = Bitmap.createBitmap(bmp, 0, 0,
                    bmp.getWidth(), bmp.getHeight(), matrix, true);
        }
        decorView.destroyDrawingCache();
        return ret;
    }
}
