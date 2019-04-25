package com.sykent.gl.core;

import android.opengl.GLES20;
import android.util.Log;

import com.sykent.gl.utils.GLUtilsEx;

import javax.microedition.khronos.opengles.GL10;

import sykent.com.gllib.BuildConfig;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/04/21
 */
public class GLBaseLayer implements GLLayer {
    private static final String TAG = GLBaseLayer.class.getSimpleName();

    protected int mProgramHandle;

    protected int aPositionLoc;
    protected int aTextureCoordLoc;
    protected int uMVPMatrixLoc;
    protected int uTexMatrixLoc;
    protected int mTextureLoc;

    // 顶点坐标和纹理坐标
    protected GLCoordBuffer mGLCoordBuffer;
    // 矩阵控制
    protected MatrixState mMatrixState;

    private GLBaseLayer() {
        // no - op by default
    }

    public GLBaseLayer(float[] vertexCoord, float[] textureCoord,
                       String vertexShader, String fragmentShader) {
        this(vertexCoord, textureCoord, vertexShader, fragmentShader, new MatrixState());
    }

    public GLBaseLayer(float[] vertexCoord, float[] textureCoord,
                       String vertexShader, String fragmentShader, MatrixState matrixState) {
        mMatrixState = matrixState;
        initGlCoordinateBuffer(vertexCoord, textureCoord);
        initShader(vertexShader, fragmentShader);
    }

    @Override
    public void setProjectOrtho(GL10 gl, int width, int height) {
        float ratio = 1.0f * height / width;
        mMatrixState.setProjectOrtho(-1.0f, 1.0f, -ratio, ratio, 1.0f, -1.0f);
    }

    @Override
    public void initGlCoordinateBuffer(float[] vertexCoord, float[] textureCoord) {
        mGLCoordBuffer = new GLCoordBuffer(vertexCoord, textureCoord);
    }

    @Override
    public void initShader(String vertexShader, String fragmentShader) {
        mProgramHandle = GLUtilsEx.createProgram(vertexShader, fragmentShader);
        checkProgram(mProgramHandle);

        if (mProgramHandle > 0) {
            getHandle();
        }
    }

    @Override
    public void getHandle() {
        aPositionLoc = GLES20.glGetAttribLocation(mProgramHandle, "aPosition");
        aTextureCoordLoc = GLES20.glGetAttribLocation(mProgramHandle, "aTextureCoord");
        uMVPMatrixLoc = GLES20.glGetUniformLocation(mProgramHandle, "uMVPMatrix");
        uTexMatrixLoc = GLES20.glGetUniformLocation(mProgramHandle, "uTexMatrix");
        mTextureLoc = GLES20.glGetUniformLocation(mProgramHandle, "sourceImage");
    }

    @Override
    public void onDraw(int textureId, float[] mvpMatrix, float[] texMatrix) {
        onUseProgram();
        onEnableTexture(GLES20.GL_TEXTURE_2D, textureId);
        enableHandle(mvpMatrix, texMatrix);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, mGLCoordBuffer.getVertexCount());
        disableHandle();
        onUnbindTexture(GLES20.GL_TEXTURE_2D);
        onUnUseProgram();
    }

    public void onDraw(int textureId, float[] texMatrix, float rotateAngle) {
        mMatrixState.pushMatrix();
        mMatrixState.rotate(rotateAngle, 0, 0, 1);
        onDraw(textureId, getMVPMatrix(), texMatrix);
        mMatrixState.popMatrix();
    }

    @Override
    public void enableHandle(float[] mvpMatrix, float[] texMatrix) {
        // 矩阵传入shader程序
        GLES20.glUniformMatrix4fv(uMVPMatrixLoc, 1, false, mvpMatrix, 0);
        GLES20.glUniformMatrix4fv(uTexMatrixLoc, 1, false, texMatrix, 0);

        // 为画笔指定顶点位置数据
        GLES20.glVertexAttribPointer(aPositionLoc, mGLCoordBuffer.getCoordsPerVertex(),
                GLES20.GL_FLOAT, false, mGLCoordBuffer.getVertexStride(),
                mGLCoordBuffer.getVertexBuffer());

        // 为画笔指定纹理位置数据
        GLES20.glVertexAttribPointer(aTextureCoordLoc, mGLCoordBuffer.getCoordsPerVertex(),
                GLES20.GL_FLOAT, false, mGLCoordBuffer.getTexCoordStride(),
                mGLCoordBuffer.getTexCoordBuffer());

        // 允许位置数据数组
        GLES20.glEnableVertexAttribArray(aPositionLoc);
        GLES20.glEnableVertexAttribArray(aTextureCoordLoc);
    }

    @Override
    public void disableHandle() {
        GLES20.glDisableVertexAttribArray(aPositionLoc);
        GLES20.glDisableVertexAttribArray(aTextureCoordLoc);
    }

    private void onEnableTexture(int target,
                                 int textureId) {
        // 激活纹理单元0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        // 绑定2D纹理
        GLES20.glBindTexture(target, textureId);
        // 将纹理设置给Shader
        GLES20.glUniform1i(mTextureLoc, 1);
    }

    private void onUnbindTexture(int target) {
        GLES20.glBindTexture(target, 0);
    }

    public void onUseProgram() {
        GLES20.glUseProgram(mProgramHandle);
    }

    public void onUnUseProgram() {
        GLES20.glUseProgram(0);
    }

    public MatrixState getMatrixState() {
        return mMatrixState;
    }

    public float[] getMVPMatrix() {
        return mMatrixState.getMVPMatrix();
    }

    @Override
    public void destroy() {
        if (mProgramHandle != 0) {
            GLES20.glDeleteProgram(mProgramHandle);
            mProgramHandle = 0;
        }
    }

    private void checkProgram(int programHandle) {
        if (BuildConfig.DEBUG && programHandle == 0) {
            throw new IllegalStateException("着色器程序创建失败！！！！");
        } else {
            Log.e(TAG, "着色器程序创建失败！！！！");
        }
    }
}
