package com.sykent.gl.data;

/**
 * gl坐标系的变换
 *
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/04/25
 */
public class GLConversion {

    private float mTranslateX;
    private float mTranslateY;
    private float mTranslateZ;

    private float mScaleX = 1.0f;
    private float mScaleY = 1.0f;
    private float mScaleZ = 1.0f;

    private float mRotateAngle;


    public GLConversion(float translateX, float translateY,
                        float scaleX, float scaleY) {
        this(translateX, translateY, scaleX, scaleY, 0);
    }

    public GLConversion(float translateX, float translateY,
                        float scaleX, float scaleY, float rotateAngle) {
        this(translateX, translateY, 0, scaleX, scaleY, 1.0f, rotateAngle);
    }

    public GLConversion(float translateX, float translateY, float translateZ,
                        float scaleX, float scaleY, float scaleZ, float rotateAngle) {
        mTranslateX = translateX;
        mTranslateY = translateY;
        mTranslateZ = translateZ;

        mScaleX = scaleX;
        mScaleY = scaleY;
        mScaleZ = scaleZ;

        mRotateAngle = rotateAngle;
    }

    public float getTranslateX() {
        return mTranslateX;
    }

    public void setTranslateX(float translateX) {
        mTranslateX = translateX;
    }

    public float getTranslateY() {
        return mTranslateY;
    }

    public void setTranslateY(float translateY) {
        mTranslateY = translateY;
    }

    public float getScaleX() {
        return mScaleX;
    }

    public void setScaleX(float scaleX) {
        mScaleX = scaleX;
    }

    public float getScaleY() {
        return mScaleY;
    }

    public void setScaleY(float scaleY) {
        mScaleY = scaleY;
    }

    public float getRotateAngle() {
        return mRotateAngle;
    }

    public void setRotateAngle(float rotateAngle) {
        mRotateAngle = rotateAngle;
    }

    @Override
    public String toString() {
        return "GLConversion{" +
                "mTranslateX=" + mTranslateX +
                ", mTranslateY=" + mTranslateY +
                ", mScaleX=" + mScaleX +
                ", mScaleY=" + mScaleY +
                ", mRotateAngle=" + mRotateAngle +
                '}';
    }
}
