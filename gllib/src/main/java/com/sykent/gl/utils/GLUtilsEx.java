package com.sykent.gl.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.opengl.GLES20;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import com.sykent.utils.FileUtils;

import java.nio.ByteBuffer;

import sykent.com.gllib.BuildConfig;


/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/04/21
 */
public class GLUtilsEx {
    private static final String TAG = GLUtilsEx.class.getSimpleName();
    public static final int NO_TEXTURE = -1;
    public static final int INVALID_HANDLE = -1;

    public static int createProgram(Context context, String assetsVertexPath, String assetsFragmentPath) {
        String vertexSource = FileUtils.assets2String(context, assetsVertexPath);
        String fragmentSource = FileUtils.assets2String(context, assetsFragmentPath);
        return createProgram(vertexSource, fragmentSource);
    }

    public static int createProgram(Context context, @RawRes int vertexRawId, @RawRes int fragmentRawId) {
        String vertexSource = FileUtils.raw2String(context, vertexRawId);
        String fragmentSource = FileUtils.raw2String(context, vertexRawId);
        return createProgram(vertexSource, fragmentSource);
    }

    /**
     * 创建着色器程序
     *
     * @param vertexSource   顶点着色器
     * @param fragmentSource 片元着色器
     * @return
     */
    public static int createProgram(String vertexSource, String fragmentSource) {
        int vertexShaderHandle = compileShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        int fragmentShaderHandle = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (vertexShaderHandle == 0 || fragmentShaderHandle == 0) {
            return 0;
        }

        return createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle, null);
    }

    /**
     * Helper function to compile and link a program.
     *
     * @param vertexShaderHandle   An OpenGL handle to an already-compiled vertex shader.
     * @param fragmentShaderHandle An OpenGL handle to an already-compiled fragment shader.
     * @param attributes           Attributes that need to be bound to the program.
     * @return An OpenGL handle to the program.
     */
    public static int createAndLinkProgram(
            final int vertexShaderHandle,
            final int fragmentShaderHandle,
            final String[] attributes) {

        int programHandle = GLES20.glCreateProgram();

        if (programHandle != 0) {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programHandle, vertexShaderHandle);

            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);

            // Bind attributes
            if (attributes != null) {
                final int size = attributes.length;
                for (int i = 0; i < size; i++) {
                    GLES20.glBindAttribLocation(programHandle, i, attributes[i]);
                }
            }

            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programHandle);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0) {
                Log.e(TAG, "Error compiling program: " + GLES20.glGetProgramInfoLog(programHandle));
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }

        if (programHandle == 0) {
            throw new RuntimeException("Error creating program.");
        }

        return programHandle;
    }

    /**
     * Helper function to compile a shader.
     *
     * @param shaderType   The shader type.
     * @param shaderSource The shader source code.
     * @return An OpenGL handle to the shader.
     */
    public static int compileShader(final int shaderType, final String shaderSource) {
        int shaderHandle = GLES20.glCreateShader(shaderType);

        if (shaderHandle != 0) {
            // Pass in the shader source.
            GLES20.glShaderSource(shaderHandle, shaderSource);

            // Compile the shader.
            GLES20.glCompileShader(shaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0) {
                Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shaderHandle));
                GLES20.glDeleteShader(shaderHandle);
                shaderHandle = 0;
            }
        }

        if (shaderHandle == 0) {
            throw new RuntimeException("Error creating shader.");
        }

        return shaderHandle;
    }

    /**
     * Checks to see if a GLES error has been raised.
     */
    public static void checkGlError(String op) {
        int error = GLES20.glGetError();
        if (BuildConfig.DEBUG && error != GLES20.GL_NO_ERROR) {
            String msg = op + ": glError 0x" + Integer.toHexString(error);
            Log.e(TAG, msg);
            throw new RuntimeException(msg);
        }
    }

    public static void checkGlError() {
        checkGlError("default");
    }


    /**
     * Creates a texture from raw data.
     *
     * @param data   Image data, in a "direct" ByteBuffer.
     * @param width  Texture width, in pixels (not bytes).
     * @param height Texture height, in pixels.
     * @param format Image data format (use constant appropriate for glTexImage2D(), e.g. GL_RGBA).
     * @return Handle to texture.
     */
    public static int createTexture(ByteBuffer data, int width, int height, int format) {
        int[] textureHandles = new int[1];
        int textureHandle;

        GLES20.glGenTextures(1, textureHandles, 0);
        textureHandle = textureHandles[0];
        checkGlError("glGenTextures");

        // Bind the texture handle to the 2D texture target.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle);

        // Configure min/mag filtering, i.e. what scaling method do we use if what we're rendering
        // is smaller or larger than the source image.
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        checkGlError("loadImageTexture");

        // Load the data from the buffer into the texture handle.
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, format,
                width, height, 0, format, GLES20.GL_UNSIGNED_BYTE, data);

        checkGlError("loadImageTexture");

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        return textureHandle;
    }

    public static int createTexture(int textureTarget) {
        return createTexture(textureTarget, null, GLES20.GL_LINEAR, GLES20.GL_LINEAR,
                GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_CLAMP_TO_EDGE);
    }

    public static int createTexture(Bitmap bitmap, boolean flipX, boolean flipY) {
        Matrix matrix = new Matrix();
        matrix.postScale(flipX ? -1 : 1, flipY ? -1 : 1);
        if (flipX || flipY) {
            bitmap = Bitmap.createBitmap(
                    bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
        }
        return createTexture(bitmap);
    }

    public static int createTexture(Bitmap bitmap) {
        return createTexture(GLES20.GL_TEXTURE_2D, bitmap, GLES20.GL_LINEAR, GLES20.GL_LINEAR,
                GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_CLAMP_TO_EDGE);
    }

    public static int createTexture(int textureTarget, @Nullable Bitmap bitmap, int minFilter,
                                    int magFilter, int wrapS, int wrapT) {
        int[] textureHandle = new int[1];

        GLES20.glGenTextures(1, textureHandle, 0);
        checkGlError("glGenTextures");

        GLES20.glBindTexture(textureTarget, textureHandle[0]);
        GLES20.glTexParameteri(textureTarget, GLES20.GL_TEXTURE_MIN_FILTER, minFilter);
        GLES20.glTexParameteri(textureTarget, GLES20.GL_TEXTURE_MAG_FILTER, magFilter); //线性插值
        GLES20.glTexParameteri(textureTarget, GLES20.GL_TEXTURE_WRAP_S, wrapS);
        GLES20.glTexParameteri(textureTarget, GLES20.GL_TEXTURE_WRAP_T, wrapT);

        if (textureTarget == GLES20.GL_TEXTURE_2D && bitmap != null) {
            android.opengl.GLUtils.texImage2D(textureTarget, 0, bitmap, 0);
        }

        GLES20.glBindTexture(textureTarget, 0);

        return textureHandle[0];
    }

    public static int setBitmapOnTexture(int textureId, Bitmap bitmap,
                                         boolean flipX, boolean flipY) {
        Matrix matrix = new Matrix();
        matrix.postScale(flipX ? -1 : 1, flipY ? -1 : 1);
        if (flipX || flipY) {
            bitmap = Bitmap.createBitmap(
                    bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
        }

        return setBitmapOnTexture(textureId, bitmap);
    }

    public static int setBitmapOnTexture(int textureId, Bitmap bitmap) {
        if (textureId == GLUtilsEx.NO_TEXTURE) {
            return createTexture(bitmap);
        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        android.opengl.GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        return textureId;
    }
}
