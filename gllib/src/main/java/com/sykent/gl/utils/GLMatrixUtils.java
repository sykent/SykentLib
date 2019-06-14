package com.sykent.gl.utils;

import android.graphics.RectF;
import android.opengl.Matrix;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/04/22
 */
public class GLMatrixUtils {

    public static RectF getObjectInCenterRect(RectF objectRect, RectF viewportRect) {
        RectF result = null;

        return result;
    }

    public static float[] rotate(float[] m, float angle) {
        Matrix.rotateM(m, 0, angle, 0, 0, 1);
        return m;
    }

    public static float[] flip(float[] m, boolean x, boolean y) {
        if (x || y) {
            Matrix.scaleM(m, 0, x ? -1 : 1, y ? -1 : 1, 1);
        }
        return m;
    }

    public static float[] scale(float[] m, float x, float y) {
        Matrix.scaleM(m, 0, x, y, 1);
        return m;
    }

    public static float[] getIdentityMatrix() {
        float[] identityMatrix = new float[16];
        Matrix.setIdentityM(identityMatrix, 0);
        return identityMatrix;
    }
}
