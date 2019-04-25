package com.sykent.gl.core;

import javax.microedition.khronos.opengles.GL10;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/04/21
 */
public interface GLLayer {

    void setProjectOrtho(GL10 gl, int width, int height);

    void initGlCoordinateBuffer(final float[] vertexCoord, final float[] textureCoord);

    void initShader(final String vertexShader, final String fragmentShader);

    void getHandle();

    void onDraw(final int textureId, float[] mvpMatrix, float[] texMatrix);

    void enableHandle(float[] mvpMatrix, float[] texMatrix);

    void disableHandle();

    void destroy();
}
