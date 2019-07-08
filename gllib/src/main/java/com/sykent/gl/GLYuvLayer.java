package com.sykent.gl;

import android.content.Context;
import android.opengl.GLES11Ext;

import com.sykent.gl.core.GLBaseLayer;
import com.sykent.gl.core.GLCoordBuffer;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/06/26
 */
public class GLYuvLayer extends GLBaseLayer {
    public GLYuvLayer(Context context) {
        super(GLCoordBuffer.DEFAULT_VERTEX_COORDINATE,
                GLCoordBuffer.DEFAULT_TEXTURE_COORDINATE, VERTEX_SHADER, FRAGMENT_SHADER);
    }

    @Override
    public void onDraw(int textureId, float[] mvpMatrix, float[] texMatrix) {
        super.onDraw(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId, mvpMatrix, texMatrix);
    }

    private static final String VERTEX_SHADER = "uniform mat4 uMVPMatrix;\n" +
            "uniform mat4 uTexMatrix;\n" +
            "attribute vec4 aPosition;\n" +
            "attribute vec4 aTextureCoord;\n" +
            "varying vec2 vTextureCoord;\n" +
            "\n" +
            "void main () {\n" +
            "    gl_Position = uMVPMatrix * aPosition;\n" +
            "    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n" +
            "}";
    private static final String FRAGMENT_SHADER =
            "#extension GL_OES_EGL_image_external : require\n" +
                    "precision mediump float;\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform samplerExternalOES sourceImage;\n" +
                    "void main() {\n" +
                    "   gl_FragColor = texture2D(sourceImage,vTextureCoord);\n" +
                    "}";
}
