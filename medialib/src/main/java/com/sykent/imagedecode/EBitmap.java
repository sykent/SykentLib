package com.sykent.imagedecode;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/03/26
 */
public class EBitmap {

    /**
     * 获取图片的一部分，cutRect是基于srcBitmap的一部分
     *
     * @param srcBitmap
     * @param cutRect
     * @return
     */
    public static Bitmap getPartBitmap(Bitmap srcBitmap, Rect cutRect) {
        return getPartBitmap(srcBitmap, cutRect, false);
    }

    public static Bitmap getPartBitmap(
            Bitmap srcBitmap, Rect cutRect, boolean isRecycleSrc) {
        if (BitmapHelper.isInvalid(srcBitmap)) {
            return null;
        }
        int bmpW = srcBitmap.getWidth();
        int bmpH = srcBitmap.getHeight();
        Rect srcRect = new Rect(0, 0, srcBitmap.getWidth(), srcBitmap.getHeight());
        if (srcRect.width() <= 0 || srcRect.height() <= 0
                || cutRect.width() <= 0 || cutRect.height() <= 0) {
            throw new IllegalArgumentException(
                    "width and height must more than 0");
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

        int x = (int) ((1.0f * cutRect.left / srcRect.width()) * bmpW);
        int y = (int) ((1.0f * cutRect.top / srcRect.height()) * bmpH);

        int width = (int) ((1.0f * cutRect.width() / srcRect.width()) * bmpW);
        int height = (int) ((1.0f * cutRect.height() / srcRect.height()) * bmpH);
        resutBmp = Bitmap.createBitmap(srcBitmap, x, y, width, height);

        if (isRecycleSrc) {
            BitmapHelper.recycle(srcBitmap);
        }

        return resutBmp;
    }

    /**
     * 绘制圆形bitmap
     *
     * @param bitmap
     * @return
     */
    public static Bitmap getRoundBitmap(Bitmap bitmap) {
        Bitmap result = bitmap;
        if (BitmapHelper.isValid(bitmap)) {
            Bitmap roundBitmap = Bitmap.createBitmap(
                    bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(roundBitmap);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            canvas.drawCircle(bitmap.getWidth() / 2,
                    bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, 0, 0, paint);
            result = roundBitmap;
        }
        return result;
    }

    public static Bitmap getSawtoothBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Canvas canvas = new Canvas(bitmap);
            DrawFilter drawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
            canvas.setDrawFilter(drawFilter);
            //画曲线，用于裁剪生成的草稿箱预览图
            Paint paint = new Paint();
            paint.reset();
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));//设置透明
            paint.setColor(0xffefe7e0);
            paint.setStyle(Paint.Style.FILL);
            Path p = new Path();
            p.moveTo(0, height);
            int w2 = (int) (width * 0.0355);
            int h2 = (int) (width * 0.03259);
            int j;
            for (int i = 1; i <= 30; i++) {
                if (i % 2 != 0) {
                    j = -1;
                } else {
                    j = 0;
                }
                p.lineTo(w2 * i, height + h2 * (j));
            }
            canvas.drawPath(p, paint);
            return bitmap;
        }
        return null;
    }
}
