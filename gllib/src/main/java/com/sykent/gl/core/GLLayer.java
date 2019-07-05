package com.sykent.gl.core;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/04/21
 */
public interface GLLayer {

    void setProjectOrtho(int width, int height);

    void initGlCoordinateBuffer(final float[] vertexCoord, final float[] textureCoord);

    void initShader(final String vertexShader, final String fragmentShader);

    void preDraw();

    void onEnableTexture(int target, int textureId);

    void getHandle();

    void enableHandle(float[] mvpMatrix, float[] texMatrix);

    void onDraw(final int textureId, float[] mvpMatrix, float[] texMatrix);

    void onDraw(int target, final int textureId, float[] mvpMatrix, float[] texMatrix);

    void onDraw(final int textureId, float[] mvpMatrix, float[] texMatrix, GLCoordBuffer glCoordBuffer);

    void onDraw(final int textureId, float[] mvpMatrix, float[] texMatrix, GLCoordBuffer glCoordBuffer, int drawMode);

    void disableHandle();

    void onUnbindTexture(int target);

    void destroy();
}
