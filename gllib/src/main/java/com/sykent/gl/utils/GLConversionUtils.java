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
    private static final String TAG = GLConversionUtils.class.getSimpleName();

    /**
     * android 系统的坐标源矩形在目标矩形的相对位置
     * 转换
     * gl 坐标系源矩形在目标矩形的变换参数
     * 注意：以宽一半1为基准
     *
     * @param srcRectF 源 将要画的图形区域
     * @param dstRectF 目标 显示的区域
     * @return
     */
    public static GLConversion getGLRectConversion(RectF srcRectF, RectF dstRectF) {
        float srcAspectRatio = srcRectF.height() / srcRectF.width();
        float dstAspectRatio = dstRectF.height() / dstRectF.width();

        // 先计算宽铺满在显示框目标dst居中的Rect
        float srcAlignWidthPartOfH = (srcAspectRatio * dstRectF.width()) / 2; // 源铺满目的宽时，高的一半
        RectF srcAlignWidthInCenterRectF = new RectF(
                dstRectF.left,
                dstRectF.top + (dstRectF.height() / 2 - srcAlignWidthPartOfH),
                dstRectF.right,
                dstRectF.bottom - (dstRectF.height() / 2 - srcAlignWidthPartOfH));

        // 以宽为标准，缩放比
        float scale = srcRectF.width() / dstRectF.width();

        // 再计算铺满目dst宽矩形srcAlignWidthInCenterRectF缩放到src 大小并居中的矩形
        Matrix matrix = new Matrix();
        float cx = srcAlignWidthInCenterRectF.left + srcAlignWidthInCenterRectF.width() / 2;
        float cy = srcAlignWidthInCenterRectF.top + srcAlignWidthInCenterRectF.height() / 2;
        matrix.postScale(scale, scale, cx, cy);
        RectF srcScaleInCenterRectF = new RectF();
        matrix.mapRect(srcScaleInCenterRectF, srcAlignWidthInCenterRectF);

        // 因为是高宽比，所以宽的一半为基准1，以宽铺满为标准
        float dstRectFPartOfW = dstRectF.width() / 2;
        float dstRectFPartOfH = dstRectF.height() / 2;

        // 再计算最终src位置到放大居中srcScaleInCenterRectF偏移的距离，以宽的一半1为基准，高则是dstAspectRatio
        float tx = (srcRectF.left - srcScaleInCenterRectF.left) / dstRectFPartOfW;
        float ty = ((srcScaleInCenterRectF.top - srcRectF.top) / dstRectFPartOfH) * dstAspectRatio;

        // 由于顶点坐标是标准的[-1,-1],[1,-1],[-1,1],[1,1],所以为了src 不被压，缩放比Y方向要乘以高宽比
        return new GLConversion(tx, ty, scale, scale * srcAspectRatio);
    }

}
