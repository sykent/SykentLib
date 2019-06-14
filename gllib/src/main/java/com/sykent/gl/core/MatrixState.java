package com.sykent.gl.core;

import android.opengl.Matrix;

import com.sykent.gl.data.GLConversion;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/04/21
 */
public class MatrixState {

    private float[] mProjectMatrix = new float[16]; // 4x4 矩阵，投影使用
    private static float[] mVMatrix = new float[16];//摄像机位置朝向9参数矩阵
    private float[] mMMatrix = new float[16];    //具体物体的3D变换矩阵，包括旋转、平移、缩放
    private float[] mMVPMatrix = new float[16]; // 4x4 最终的变换矩阵，总的变换矩阵

    // 栈
    private final int MAX_COUNT = 10;
    private float[][] mStack = new float[MAX_COUNT][16];
    private int mPointer = -1;

    public MatrixState() {
        init();
    }

    private void init() {
        Matrix.setIdentityM(mVMatrix, 0);
        Matrix.setIdentityM(mMMatrix, 0);
    }

    //设置摄像机
    public static void setCamera(
            float cx,    //摄像机位置x
            float cy,   //摄像机位置y
            float cz,   //摄像机位置z
            float tx,   //摄像机目标点x
            float ty,   //摄像机目标点y
            float tz,   //摄像机目标点z
            float upx,  //摄像机UP向量X分量
            float upy,  //摄像机UP向量Y分量
            float upz   //摄像机UP向量Z分量
    ) {
        Matrix.setLookAtM(
                mVMatrix,
                0,
                cx, cy, cz,
                tx, ty, tz,
                upx, upy, upz
        );
    }


    /**
     * 设置正交投影参数
     *
     * @param left
     * @param right
     * @param bottom
     * @param top
     * @param near
     * @param far
     */
    public void setProjectOrtho(
            float left, float right, // near 面的 left、right
            float bottom, float top, // near 面的bottom、top
            float near, float far    // near 面，far面与视点的距离
    ) {
        Matrix.orthoM(mProjectMatrix, 0, left, right, bottom, top, near, far);
    }

    /**
     * 默认的gl变换是 rotate -> scale -> translate
     *
     * @param glConversion
     */
    public void executeDefaultGLConversion(GLConversion glConversion) {
        // 是前乘，执行的顺序是rotate -> scale -> translate ，先缩放到和最终一样大小，再平移
        translate(glConversion.getTranslateX(), glConversion.getTranslateY(), 0);
        scale(glConversion.getScaleX(), glConversion.getScaleY(), 0);
        rotate(glConversion.getRotateAngle(), 0, 0, 1);
    }


    public void translate(float x, float y, float z) {
        Matrix.translateM(mMMatrix, 0, x, y, z);
    }

    public void rotate(float angle, float x, float y, float z) {
        Matrix.rotateM(mMMatrix, 0, angle, x, y, z);
    }

    public void scale(float x, float y, float z) {
        Matrix.scaleM(mMMatrix, 0, x, y, z);
    }

    public float[] getMVPMatrix() {
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mMMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }

    public float[] getFullMVPMatrix(float w, float h) {
        pushMatrix();
        scale(1.0f, (1.0f * h) / w, 1.0f);
        float[] mvpMatrix = getMVPMatrix();
        popMatrix();
        return mvpMatrix;
    }

    public void pushMatrix() {
        mPointer++;
        checkPointerBounds();

        for (int i = 0; i < 16; i++) {
            mStack[mPointer][i] = mMMatrix[i];
        }
    }

    public void popMatrix() {
        for (int i = 0; i < 16; i++) {
            mMMatrix[i] = mStack[mPointer][i];
        }

        mPointer--;
        checkPointerBounds();
    }


    private void checkPointerBounds() {
        if (mPointer < -1) {
            throw new IndexOutOfBoundsException("指针低于最低栈底");
        }

        if (mPointer > MAX_COUNT - 1) {
            throw new IndexOutOfBoundsException("指针超过最大栈");
        }
    }
}
