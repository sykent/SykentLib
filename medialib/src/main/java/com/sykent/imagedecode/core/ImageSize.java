package com.sykent.imagedecode.core;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/03/26
 */
public class ImageSize {
    private int mWidth;
    private int mHeight;

    public ImageSize(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    public ImageSize(int width, int height, int rotation) {
        if (rotation % 180 == 0) {
            mWidth = width;
            mHeight = height;
        } else {
            mWidth = height;
            mHeight = width;
        }
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }
}
