package com.sykent.gl.core;

import android.opengl.GLES20;

import com.sykent.gl.utils.GLUtilsEx;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/04/22
 */
public class GLOffscreenBuffer {
    private int mWidth;
    private int mHeight;

    private int mTextureId;
    private int mFrameBufferId;

    public GLOffscreenBuffer(int width, int height) {
        this(GLES20.GL_TEXTURE_2D, GLES20.GL_RGBA, width, height);
    }

    public GLOffscreenBuffer(
            int target, int internalFormat,
            int width, int height) {
        checkArgument(width, height);

        mWidth = width;
        mHeight = height;

        // 创建buffer 所需的纹理
        mTextureId = GLUtilsEx.createTexture(target);
        GLES20.glBindTexture(target, mTextureId);
        GLES20.glTexImage2D(target, 0, internalFormat,
                width, height, 0, internalFormat, GLES20.GL_UNSIGNED_BYTE, null);
        GLES20.glBindTexture(target, 0);

        // 创建fbo
        int[] frameBufferHandle = new int[1];
        GLES20.glGenFramebuffers(1, frameBufferHandle, 0);
        mFrameBufferId = frameBufferHandle[0];

        onBind(false);
        // 为FrameBuffer 挂载texture 来存储颜色
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER,
                GLES20.GL_COLOR_ATTACHMENT0, target, mTextureId, 0);
        onUnBind();
    }

    public void onBind() {
        onBind(true);
    }

    public void onBind(boolean clear) {
        // 绑定FrameBuffer
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBufferId);
        if (clear) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        }
    }

    public void onUnBind() {
        // 解绑FrameBuffer
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    public void destroy() {
        // 删除FrameBuffer
        GLES20.glDeleteBuffers(1, new int[]{mFrameBufferId}, 0);
        // 删除纹理
        GLES20.glDeleteTextures(0, new int[]{mTextureId}, 0);
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public int getTextureId() {
        return mTextureId;
    }

    public int getFrameBufferId() {
        return mFrameBufferId;
    }

    private void checkArgument(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("width or height must > 0");
        }
    }
}
