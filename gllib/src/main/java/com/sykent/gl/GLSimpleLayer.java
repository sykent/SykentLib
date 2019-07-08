package com.sykent.gl;

import android.content.Context;

import com.sykent.gl.core.GLBaseLayer;
import com.sykent.gl.core.GLCoordBuffer;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/04/21
 */
public class GLSimpleLayer extends GLBaseLayer {

    public GLSimpleLayer(Context context) {
        this(context, false);
    }

    public GLSimpleLayer(Context context, boolean flipY) {
        super(GLCoordBuffer.DEFAULT_VERTEX_COORDINATE,
                flipY ? GLCoordBuffer.DEFAULT_FLIP_Y_TEXTURE_COORDINATE
                        : GLCoordBuffer.DEFAULT_TEXTURE_COORDINATE,
                VERTEX_SHADER, FRAGMENT_SHADER);
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
    private static final String FRAGMENT_SHADER = "precision highp float;\n" +
            "uniform sampler2D sourceImage;\n" +
            "varying vec2 vTextureCoord;\n" +
            "\n" +
            "void main () {\n" +
            "    vec4 color = texture2D(sourceImage, vTextureCoord);\n" +
            "    gl_FragColor = color;\n" +
            "}";
}
