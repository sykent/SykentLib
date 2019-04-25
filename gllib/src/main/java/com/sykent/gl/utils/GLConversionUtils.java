package com.sykent.gl.utils;

import android.graphics.Matrix;
import android.graphics.RectF;

import com.sykent.gl.data.GLConversion;


/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/04/25
 */
public class GLConversionUtils {
    /**
     * android 系统的坐标源矩形在目标矩形的相对位置
     * 转换
     * gl 坐标系源矩形在目标矩形的变换参数
     * 注意：以宽一半1为基准
     *
     * @param androidBmpRectF  源
     * @param androidShowRectF 目标
     * @return
     */
    public static GLConversion getGLConversion(RectF androidBmpRectF, RectF androidShowRectF) {
        float bmpAspectRatio = androidBmpRectF.height() / androidBmpRectF.width();
        float vpAspectRatio = androidShowRectF.height() / androidShowRectF.width();

        // 因为是高宽比，所以宽的一半为基准1，以宽铺满为标准
        float androidShowPartOfW = androidShowRectF.width() / 2;
        float androidShowPartOfH = androidShowRectF.height() / 2;

        // 以宽为标准，缩放比
        float scale = androidBmpRectF.width() / androidShowRectF.width();

        // 先计算齐宽bmp在显示框的居中的Rect
        float alignWHeightMorePart = (bmpAspectRatio * androidShowRectF.width()
                - androidShowRectF.height()) / 2;
        RectF androidBmpAlignWCenterRectF = new RectF(
                androidShowRectF.left,
                androidShowRectF.top - alignWHeightMorePart,
                androidShowRectF.right,
                androidShowRectF.bottom + alignWHeightMorePart);

        // 再计算放大时，居中时bmp的位置
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
//        matrix.postTranslate(-(androidBmpRectF.width() - androidBmpAlignWRectF.width()) / 2,
//                -(androidBmpRectF.height() - androidBmpAlignWRectF.height()) / 2);
        RectF androidBmpScaleCenterRectF = new RectF();
        matrix.mapRect(androidBmpScaleCenterRectF, androidBmpAlignWCenterRectF);

        // 再计算最终bmp位置到放大居中RectF 偏移的距离
        float tx = (androidBmpRectF.left - androidBmpScaleCenterRectF.left) / androidShowPartOfW;
        float ty = ((androidBmpScaleCenterRectF.top - androidBmpRectF.top) / androidShowPartOfH) * vpAspectRatio;

        return new GLConversion(tx, ty, scale, scale);
    }

}
