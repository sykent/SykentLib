package com.sykent.media.info;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/07/03
 */
public class VideoInfo extends MediaInfo implements Cloneable {
    private int mWidth;
    private int mHeight;
    private int mDuration;

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

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    @Override
    public VideoInfo clone() {
        VideoInfo  object = null;
        try {
            object = (VideoInfo ) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return object;

    }
}