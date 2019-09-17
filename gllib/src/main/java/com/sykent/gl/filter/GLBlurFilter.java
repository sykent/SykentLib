package com.sykent.gl.filter;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;

import com.sykent.gl.GLColorLayer;
import com.sykent.gl.GLSimpleLayer;
import com.sykent.gl.core.GLBaseLayer;
import com.sykent.gl.core.GLCoordBuffer;
import com.sykent.gl.core.GLOffscreenBuffer;
import com.sykent.gl.core.GLOffscreenBufferGroup;
import com.sykent.gl.utils.GLMatrixUtils;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @see <a href https://github.com/HokoFly/HokoBlur></a>
 * @since 2019/07/08
 */
public class GLBlurFilter extends GLBaseLayer {
    // 最大的模糊半径
    private static final int MAX_RADIUS = 25;
    private Context mContext;

    private int mRadius;
    private boolean isHorizontal;
    private int mWidth;
    private int mHeight;

    private int uRadiusLoc;
    private int uWidthOffsetLoc;
    private int uHeightOffsetLoc;

    private GLOffscreenBufferGroup mOffscreenBufferGroup;

    private int mColor = Color.TRANSPARENT;
    private GLColorLayer mColorLayer;
    private GLSimpleLayer mSimpleLayer;

    public GLBlurFilter(Context context) {
        super(GLCoordBuffer.DEFAULT_VERTEX_COORDINATE,
                GLCoordBuffer.DEFAULT_TEXTURE_COORDINATE,
                VERTEX_SHADER, FRAGMENT_SHADER);
        mContext = context;

        mColorLayer = new GLColorLayer(mContext);
        mSimpleLayer = new GLSimpleLayer(mContext);
    }

    @Override
    public void setProjectOrtho(int width, int height) {
        super.setProjectOrtho(width, height);
        mWidth = width;
        mHeight = height;

        if (mOffscreenBufferGroup != null) {
            mOffscreenBufferGroup.destroy();
        }

        // 缩小的buff
        float scale = getScale(width, height);
        int w = (int) (mWidth * scale);
        int h = (int) (mHeight * scale);
        mOffscreenBufferGroup = new GLOffscreenBufferGroup(w, h, 3);
        mColorLayer.setProjectOrtho(w, h);
        mSimpleLayer.setProjectOrtho(w, h);
    }

    @Override
    public void getHandle() {
        super.getHandle();
        uRadiusLoc = GLES20.glGetUniformLocation(mProgramHandle, "uRadius");
        uWidthOffsetLoc = GLES20.glGetUniformLocation(mProgramHandle, "uWidthOffset");
        uHeightOffsetLoc = GLES20.glGetUniformLocation(mProgramHandle, "uHeightOffset");
    }

    @Override
    public void enableHandle(float[] mvpMatrix, float[] texMatrix) {
        super.enableHandle(mvpMatrix, texMatrix);
        GLES20.glUniform1i(uRadiusLoc, mRadius);
        GLES20.glUniform1f(uWidthOffsetLoc, isHorizontal ? 0 : 1f / mOffscreenBufferGroup.getWidth());
        GLES20.glUniform1f(uHeightOffsetLoc, isHorizontal ? 1f / mOffscreenBufferGroup.getHeight() : 0);
    }

    public void onDraw(int textureId, float[] mvpMatrix,
                       float[] texMatrix, int radius, int overlayColor) {
        if (radius > MAX_RADIUS) {
            throw new IllegalArgumentException("radius must less than 25");
        }

        mRadius = radius;
        mColor = overlayColor;

        // 先模糊行，再模糊列
        GLOffscreenBuffer buffer = mOffscreenBufferGroup.getBuffer(0);
        int width = buffer.getWidth();
        int height = buffer.getHeight();

        GLES20.glViewport(0, 0, width, height);
        super.setProjectOrtho(width, height);


        buffer.onBind();
        isHorizontal = true;
        onDraw(textureId, getFullMVPMatrix(width, height), GLMatrixUtils.getIdentityMatrix());
        buffer.onUnBind();

        int tmpTexture = buffer.getTextureId();
        buffer = mOffscreenBufferGroup.getBuffer(1);
        buffer.onBind();
        isHorizontal = false;
        onDraw(tmpTexture, getFullMVPMatrix(width, height), GLMatrixUtils.getIdentityMatrix());
        buffer.onUnBind();

        // 画颜色
        if (mColor != Color.TRANSPARENT) {
            GLOffscreenBuffer colorBuffer = mOffscreenBufferGroup.getBuffer(0);
            colorBuffer.onBind();
            mColorLayer.onDraw(mColor, mColorLayer.getFullMVPMatrix(width, height));
            colorBuffer.onUnBind();

            GLES20.glEnable(GLES20.GL_BLEND);
            buffer = mOffscreenBufferGroup.getBuffer(1);
            buffer.onBind(false);
            GLES20.glBlendFuncSeparate(GLES20.GL_SRC_ALPHA,
                    GLES20.GL_ONE_MINUS_SRC_ALPHA, GLES20.GL_ONE, GLES20.GL_ONE);
            GLES20.glBlendEquation(GLES20.GL_FUNC_ADD);
            mSimpleLayer.onDraw(colorBuffer.getTextureId(), mSimpleLayer.getFullMVPMatrix(width, height), GLMatrixUtils.getIdentityMatrix());
            GLES20.glDisable(GLES20.GL_BLEND);
            buffer.onUnBind();
        }

        GLES20.glViewport(0, 0, mWidth, mHeight);
        super.setProjectOrtho(mWidth, mHeight);

        // 画到外面
        onDraw(buffer.getTextureId(), mvpMatrix, texMatrix);
    }

    @Override
    public void destroy() {
        super.destroy();
        mOffscreenBufferGroup.destroy();
    }

    public void setRadius(int radius) {
        mRadius = radius;
    }

    public void setHorizontal(boolean horizontal) {
        isHorizontal = horizontal;
    }

    private float getScale(int w, int h) {
        int min = Math.min(w, h);
        int scale = 1;

        // 模糊半径，是的radius/x*radius
        while (min / scale > 4 * MAX_RADIUS) {
            scale++;
        }
        scale--;

        if (scale == 0) {
            scale = 1;
        }

        return 1.0f / scale;
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
    private static final String FRAGMENT_SHADER;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(" \n").append("precision lowp float;   \n")
                .append("varying vec2 vTextureCoord;   \n")
                .append("uniform sampler2D sourceImage;   \n")
                .append("uniform int uRadius;   \n")
                .append("uniform float uWidthOffset;  \n")
                .append("uniform float uHeightOffset;  \n")
//                .append("mediump float getGaussWeight(mediump float currentPos, mediump float sigma) \n")
//                .append("{ \n")
//                .append("   return 1.0 / sigma * exp(-(currentPos * currentPos) / (2.0 * sigma * sigma)); \n")
//                .append("} \n")
                .append("void main() {   \n")
                .append("int diameter = 2 * uRadius + 1;  \n")
                .append("   vec4 sampleTex;\n")
                .append("   vec3 col;  \n")
                .append("   float weightSum = 0.0; \n")
                .append("   for(int i = 0; i < diameter; i++) {\n")
                .append("       vec2 offset = vec2(float(i - uRadius) * uWidthOffset, float(i - uRadius) * uHeightOffset);  \n")
                .append("       sampleTex = vec4(texture2D(sourceImage, vTextureCoord.st+offset));\n")
                .append("       float index = float(i); \n")
                .append("       float boxWeight = float(uRadius) + 1.0 - abs(index - float(uRadius)); \n")
                .append("       col += sampleTex.rgb * boxWeight; \n")
                .append("       weightSum += boxWeight;\n")
                .append("   }   \n")
                .append("   gl_FragColor = vec4(col / weightSum, sampleTex.a);   \n")
                .append("}   \n");
        FRAGMENT_SHADER = sb.toString();
    }

}
