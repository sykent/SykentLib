package com.sykent.gl.core;

import android.opengl.GLES20;
import android.util.Log;

import com.sykent.gl.utils.GLUtilsEx;

import sykent.com.gllib.BuildConfig;

import static com.sykent.gl.utils.GLUtilsEx.INVALID_HANDLE;


/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/04/21
 */
public class GLBaseLayer implements GLLayer {
    private static final String TAG = GLBaseLayer.class.getSimpleName();

    protected int mProgramHandle = INVALID_HANDLE;

    protected int aPositionLoc = INVALID_HANDLE;
    protected int aTextureCoordLoc = INVALID_HANDLE;
    protected int uMVPMatrixLoc = INVALID_HANDLE;
    protected int uTexMatrixLoc = INVALID_HANDLE;
    protected int mTextureLoc = INVALID_HANDLE;

    // 顶点坐标和纹理坐标
    protected GLCoordBuffer mGLCoordBuffer;
    // 矩阵控制
    protected MatrixState mMatrixState;

    protected int mDrawMode = GLES20.GL_TRIANGLE_STRIP; // 绘制的模式

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
    public void setProjectOrtho(int width, int height) {
        float ratio = 1.0f * height / width;
        mMatrixState.setProjectOrtho(-1.0f, 1.0f, -ratio, ratio, -1.0f, 1.0f);
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
    public void preDraw() {
        // no - op by default
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
    public void enableHandle(float[] mvpMatrix, float[] texMatrix) {
        // 矩阵传入shader程序
        if (GLUtilsEx.INVALID_HANDLE != uMVPMatrixLoc) {
            GLES20.glUniformMatrix4fv(uMVPMatrixLoc, 1, false, mvpMatrix, 0);
        }
        if (GLUtilsEx.INVALID_HANDLE != uTexMatrixLoc) {
            GLES20.glUniformMatrix4fv(uTexMatrixLoc, 1, false, texMatrix, 0);
        }

        // 为画笔指定顶点位置数据
        if (GLUtilsEx.INVALID_HANDLE != aPositionLoc) {
            GLES20.glVertexAttribPointer(aPositionLoc, mGLCoordBuffer.getCoordsPerVertex(),
                    GLES20.GL_FLOAT, false, mGLCoordBuffer.getVertexStride(),
                    mGLCoordBuffer.getVertexBuffer());
        }

        // 为画笔指定纹理位置数据
        if (GLUtilsEx.INVALID_HANDLE != aTextureCoordLoc) {
            GLES20.glVertexAttribPointer(aTextureCoordLoc, mGLCoordBuffer.getCoordsPerVertex(),
                    GLES20.GL_FLOAT, false, mGLCoordBuffer.getTexCoordStride(),
                    mGLCoordBuffer.getTexCoordBuffer());
        }

        // 允许位置数据数组
        if (GLUtilsEx.INVALID_HANDLE != aPositionLoc) {
            GLES20.glEnableVertexAttribArray(aPositionLoc);
        }
        if (GLUtilsEx.INVALID_HANDLE != aTextureCoordLoc) {
            GLES20.glEnableVertexAttribArray(aTextureCoordLoc);
        }
    }

    @Override
    public void onDraw(int textureId, float[] mvpMatrix, float[] texMatrix) {
        onDraw(GLES20.GL_TEXTURE_2D, textureId, mvpMatrix, texMatrix);
    }

    public void onDraw(int textureId, float[] texMatrix, float rotateAngle) {
        mMatrixState.pushMatrix();
        mMatrixState.rotate(rotateAngle, 0, 0, 1);
        onDraw(textureId, getMVPMatrix(), texMatrix);
        mMatrixState.popMatrix();
    }

    @Override
    public void onDraw(int textureId, float[] mvpMatrix,
                       float[] texMatrix, GLCoordBuffer glCoordBuffer) {
        if (glCoordBuffer != null) {
            mGLCoordBuffer = glCoordBuffer;
        }

        onDraw(textureId, mvpMatrix, texMatrix);
    }

    @Override
    public void onDraw(int textureId, float[] mvpMatrix,
                       float[] texMatrix, GLCoordBuffer glCoordBuffer, int drawMode) {
        mDrawMode = drawMode;

        onDraw(textureId, mvpMatrix, texMatrix, glCoordBuffer);
    }

    @Override
    public void onDraw(int target, int textureId, float[] mvpMatrix, float[] texMatrix) {
        onUseProgram();
        onEnableTexture(target, textureId);
        enableHandle(mvpMatrix, texMatrix);
        GLES20.glDrawArrays(mDrawMode, 0, mGLCoordBuffer.getVertexCount());
        disableHandle();
        onUnbindTexture(target);
        onUnUseProgram();
    }

    @Override
    public void disableHandle() {
        if (GLUtilsEx.INVALID_HANDLE != aPositionLoc) {
            GLES20.glDisableVertexAttribArray(aPositionLoc);
        }
        if (GLUtilsEx.INVALID_HANDLE != aTextureCoordLoc) {
            GLES20.glDisableVertexAttribArray(aTextureCoordLoc);
        }
    }

    @Override
    public void onEnableTexture(int target,
                                int textureId) {
        if (textureId != GLUtilsEx.NO_TEXTURE) {
            // 激活纹理单元0
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            // 绑定2D纹理
            GLES20.glBindTexture(target, textureId);
            // 将纹理设置给Shader
            GLES20.glUniform1i(mTextureLoc, 0);
        }
    }

    @Override
    public void onUnbindTexture(int target) {
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

    public float[] getFullMVPMatrix(float w, float h) {
        return mMatrixState.getFullMVPMatrix(w, h);
    }

    @Override
    public void destroy() {
        if (mProgramHandle != 0) {
            GLES20.glDeleteProgram(mProgramHandle);
            mProgramHandle = 0;
        }
    }

    private void checkProgram(int programHandle) {
        if (programHandle == 0) {
            if (BuildConfig.DEBUG) {
                throw new IllegalStateException("着色器程序创建失败！！！！");
            } else {
                Log.e(TAG, "着色器程序创建失败！！！！");
            }
        }
    }
}
