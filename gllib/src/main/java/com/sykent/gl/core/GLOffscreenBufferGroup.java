package com.sykent.gl.core;

import android.opengl.GLES20;

import java.security.InvalidParameterException;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/04/29
 */
public class GLOffscreenBufferGroup {
    private GLOffscreenBuffer[] mElements;
    private int mWidth, mHeight;

    private int USE_FLAG = 0b00000000000000000000000000000000;

    public GLOffscreenBufferGroup(int width, int height, int num) {
        this(GLES20.GL_TEXTURE_2D, GLES20.GL_RGBA, width, height, num);
    }

    public GLOffscreenBufferGroup(int internalFormat,
                                  int width, int height, int num) {
        this(GLES20.GL_TEXTURE_2D, internalFormat, width, height, num);
    }

    public GLOffscreenBufferGroup(
            int target, int internalFormat,
            int width, int height, int num) {
        checkArgument(width, height, num);

        mWidth = width;
        mHeight = height;

        mElements = new GLOffscreenBuffer[num];
        for (int i = 0; i < num; i++) {
            mElements[i] = new GLOffscreenBuffer(target, internalFormat, mWidth, mHeight);
        }
    }

    public GLOffscreenBuffer next() {
        for (int i = 0; i < mElements.length; i++) {
            if (!isAble(i)) {
                disable(i);
                return mElements[i];
            }
        }

        throw new IllegalStateException("no accessible buffer !!!!");
    }

    public GLOffscreenBuffer getBuffer(int position) {
        if (position < 0 || position > mElements.length - 1) {
            throw new InvalidParameterException("the position is illegal!!!");
        }

        return mElements[position];
    }

    public void setCanAble(GLOffscreenBuffer buffer) {
        int position = -1;
        for (int i = 0; i < mElements.length; i++) {
            if (buffer == mElements[i]) {
                position = i;
            }
        }

        if (position == -1) {
            throw new InvalidParameterException("the buffer is not in group!!!!");
        }

        able(position);
    }

    public void setCanAble(int position) {
        if (position < 0 || position > mElements.length - 1) {
            throw new InvalidParameterException("the position is illegal!!!");
        }

        able(position);
    }

    public void setCanAbleAll() {
        USE_FLAG = 0b00000000000000000000000000000000;
    }

    public void destroy() {
        for (int i = 0; i < mElements.length; i++) {
            mElements[i].destroy();
        }
        mElements = null;
    }

    private void disable(int position) {
        int usePosition = 0b00000000000000000000000000000001;
        usePosition = usePosition << position;
        USE_FLAG = USE_FLAG | usePosition;
    }

    private void able(int position) {
        int usePosition = 0b00000000000000000000000000000001;
        usePosition = usePosition << position;
        USE_FLAG = USE_FLAG & (~usePosition);
    }

    private boolean isAble(int position) {
        int usePosition = 0b00000000000000000000000000000001;
        usePosition = usePosition << position;
        return (USE_FLAG & usePosition) != 0;
    }

    private void checkArgument(int width, int height, int num) {
        if (width <= 0 || height <= 0 || num <= 0) {
            throw new IllegalArgumentException("width or height or num must > 0");
        }

        if (num > 32) {
            throw new IllegalArgumentException("num must <= 32");
        }
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }
}
