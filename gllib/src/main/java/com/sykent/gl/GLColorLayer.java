package com.sykent.gl;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;

import com.sykent.gl.core.GLBaseLayer;
import com.sykent.gl.core.GLCoordBuffer;
import com.sykent.gl.utils.GLMatrixUtils;
import com.sykent.gl.utils.GLUtilsEx;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/05/14
 */
public class GLColorLayer extends GLBaseLayer {

    private int uColorLoc;
    private float[] mColor;

    public GLColorLayer(Context context) {
        super(GLCoordBuffer.DEFAULT_VERTEX_COORDINATE,
                GLCoordBuffer.DEFAULT_TEXTURE_COORDINATE,
                VERTEX_SHADER, FRAGMENT_SHADER);
        mColor = new float[4];
    }

    @Override
    public void getHandle() {
        super.getHandle();
        uColorLoc = GLES20.glGetUniformLocation(mProgramHandle, "uColor");
    }

    @Override
    public void enableHandle(float[] mvpMatrix, float[] texMatrix) {
        super.enableHandle(mvpMatrix, texMatrix);
        GLES20.glUniform4fv(uColorLoc, 1, mColor, 0);
    }

    public void onDraw(int color, float[] mvpMatrix, GLCoordBuffer glCoordBuffer, int drawMode) {
        setColor(color);
        super.onDraw(GLUtilsEx.NO_TEXTURE, mvpMatrix, GLMatrixUtils.getIdentityMatrix(), glCoordBuffer, drawMode);
    }

    public void onDraw(int color, float[] mvpMatrix) {
        setColor(color);
        super.onDraw(GLUtilsEx.NO_TEXTURE, mvpMatrix, GLMatrixUtils.getIdentityMatrix());
    }

    private void setColor(int color) {
        mColor[0] = Color.red(color) / 255f;
        mColor[1] = Color.green(color) / 255f;
        mColor[2] = Color.blue(color) / 255f;
        mColor[3] = Color.alpha(color) / 255f;
    }

    private static final String VERTEX_SHADER = "uniform mat4 uMVPMatrix;\n" +
            "attribute vec4 aPosition;\n" +
            "\n" +
            "void main () {\n" +
            "    gl_Position = uMVPMatrix * aPosition;\n" +
            "}";
    private static final String FRAGMENT_SHADER = "precision highp float;\n" +
            "uniform vec4 uColor;\n" +
            "\n" +
            "void main () {\n" +
            "    gl_FragColor = uColor;\n" +
            "}";
}
