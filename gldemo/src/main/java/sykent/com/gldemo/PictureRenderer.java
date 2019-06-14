package sykent.com.gldemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.util.Log;

import com.sykent.gl.GLSimpleLayer;
import com.sykent.gl.core.GLOffscreenBuffer;
import com.sykent.gl.core.MatrixState;
import com.sykent.gl.utils.GLMatrixUtils;
import com.sykent.gl.utils.GLUtilsEx;
import com.sykent.imagedecode.EBitmapFactory;
import com.sykent.widget.GLTextureView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/04/21
 */
public class PictureRenderer implements GLTextureView.Renderer {
    private Context mContext;
    private Bitmap mPicBmp;
    private int mPicTextureId;

    private GLSimpleLayer mPicLayer;
    private GLSimpleLayer mMaskLayer;
    private int mWidth, mHeight;

    private int mBmpW, mBmpH;
    private GLOffscreenBuffer mOffscreenBuffer;

    private Bitmap mMaskBmp;
    private int mMaskTextureId;

    public PictureRenderer(Context context) {
        mContext = context;
        mPicBmp = EBitmapFactory.decode(mContext, "pic/myhead.jpg");
        mBmpW = mPicBmp.getWidth();
        mBmpH = mPicBmp.getHeight();

        // 遮罩
        mMaskBmp = Bitmap.createBitmap(mBmpW, mBmpH, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAlpha(255);
        Canvas canvas = new Canvas(mMaskBmp);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0,
                Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawRoundRect(new RectF(50, 50, mBmpW - 50, mBmpH - 50), 60, 60, paint);

        Bitmap src = Bitmap.createBitmap(mBmpW, mBmpH, Bitmap.Config.ARGB_8888);
        src.eraseColor(Color.WHITE);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mPicLayer = new GLSimpleLayer(mContext);
        mMaskLayer = new GLSimpleLayer(mContext);
        mPicTextureId = GLUtilsEx.createTexture(mPicBmp, false, true);
        mMaskTextureId = GLUtilsEx.createTexture(mMaskBmp);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mWidth = width;
        mHeight = height;
        GLES20.glViewport(0, 0, width, height);
        mPicLayer.setProjectOrtho(width, height);
        mMaskLayer.setProjectOrtho(width, height);
        if (mOffscreenBuffer != null) {
            mOffscreenBuffer.destroy();
        }

        mOffscreenBuffer = new GLOffscreenBuffer(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

//        mOffscreenBuffer.onBind();
//        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
//        mPicLayer.onDraw(mPicTextureId, mPicLayer.getMVPMatrix(), GLMatrixUtils.getIdentityMatrix());
//        GLES20.glEnable(GLES20.GL_BLEND);
//        GLES20.glBlendFuncSeparate(GLES20.GL_ONE_MINUS_SRC_ALPHA, GLES20.GL_SRC_ALPHA, GLES20.GL_SRC_ALPHA, GLES20.GL_SRC_ALPHA);
//        GLES20.glBlendEquation(GLES20.GL_FUNC_ADD);
//        mMaskLayer.onDraw(mMaskTextureId, mMaskLayer.getMVPMatrix(), GLMatrixUtils.getIdentityMatrix());
//        GLES20.glDisable(GLES20.GL_BLEND);
//        mOffscreenBuffer.onUnBind();
//
//        int textureId = mOffscreenBuffer.getTextureId();
        int textureId = mPicTextureId;

        float objectAspectRatio = 1.0f * mBmpH / mBmpW;
        float viewPortAspectRatio = 1.0f * mHeight / mWidth;

        float scale = 0.5f;
        float objectW = scale * 1.0f;
        float objectH = objectAspectRatio * objectW;

        MatrixState simpleLayerMatrixState = mPicLayer.getMatrixState();


//        mPicLayer.onDraw(textureId, simpleLayerMatrixState.getMVPMatrix(), GLMatrixUtils.getIdentityMatrix());

//        simpleLayerMatrixState.pushMatrix();
//        simpleLayerMatrixState.translate(objectW - 1.0f, viewPortAspectRatio - objectH, 0.0f);
//        simpleLayerMatrixState.scale(scale, scale, 1.0f);
//        GLES20.glEnable(GLES20.GL_BLEND);
//        GLES20.glBlendFuncSeparate(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA, GLES20.GL_ONE, GLES20.GL_ONE);
//        GLES20.glBlendEquation(GLES20.GL_FUNC_ADD);
//        mPicLayer.onDraw(textureId, simpleLayerMatrixState.getMVPMatrix(), GLMatrixUtils.getIdentityMatrix());
//        GLES20.glDisable(GLES20.GL_BLEND);
//        simpleLayerMatrixState.popMatrix();

//        simpleLayerMatrixState.pushMatrix();
//        simpleLayerMatrixState.translate(1.0f - objectW, viewPortAspectRatio - objectH, 0.0f);
//        simpleLayerMatrixState.scale(scale, scale, 1.0f);
//        simpleLayerMatrixState.rotate(180, 0, 0, 1);
//        mPicLayer.onDraw(textureId, simpleLayerMatrixState.getMVPMatrix(), GLMatrixUtils.getIdentityMatrix());
//        simpleLayerMatrixState.popMatrix();
        moveToViewport(textureId, simpleLayerMatrixState);
    }

    public void moveToViewport(int textureId, MatrixState simpleLayerMatrixState) {

        RectF androidBmpRectF = new RectF(-50f, -50f, 230f, 280);
        RectF androidShowRectF = new RectF(0.0f, 0.0f, 180.0f, 280.0f);
        RectF androidPanelRectF = new RectF(0.0f, 0.0f, 400, 500);

        float bmpAspectRatio = androidBmpRectF.height() / androidBmpRectF.width();
        float vpAspectRatio = androidShowRectF.height() / androidShowRectF.width();
        float panelAspectRatio = androidPanelRectF.height() / androidPanelRectF.width();

        // 因为是高宽比，所以宽的一半为基准1，以宽铺满为标准
        float androidShowPartOfW = androidShowRectF.width() / 2;
        float androidShowPartOfH = androidShowRectF.height() / 2;

        // 以宽为标准，缩放比
        float scale = androidBmpRectF.width() / androidShowRectF.width();

        // 先计算齐宽bmp在显示框的居中的Rect
        float alignWHeightMorePart = (bmpAspectRatio * androidShowRectF.width()
                - androidShowRectF.height()) / 2;
        RectF androidBmpAlignWCenterRectF = new RectF(
                androidShowRectF.left,
                androidShowRectF.top - alignWHeightMorePart,
                androidShowRectF.right,
                androidShowRectF.bottom + alignWHeightMorePart);

        // 再计算放大时，居中时bmp的位置
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
//        matrix.postTranslate(-(androidBmpRectF.width() - androidBmpAlignWRectF.width()) / 2,
//                -(androidBmpRectF.height() - androidBmpAlignWRectF.height()) / 2);
        RectF androidBmpScaleCenterRectF = new RectF();
        matrix.mapRect(androidBmpScaleCenterRectF, androidBmpAlignWCenterRectF);

        // 再计算最终bmp位置到放大居中RectF 偏移的距离
        float tx = (androidBmpRectF.left - androidBmpScaleCenterRectF.left) / androidShowPartOfW;
        float ty = ((androidBmpScaleCenterRectF.top - androidBmpRectF.top) / androidShowPartOfH) * vpAspectRatio;

        Log.d("ttttt", " tx: " + tx + "  ty: " + ty);
        GLES20.glViewport((int) androidShowRectF.left, (int) (mHeight - androidShowRectF.height() - androidShowRectF.top),
                (int) androidShowRectF.width(), (int) androidShowRectF.height());
        mPicLayer.setProjectOrtho((int) androidShowRectF.width(), (int) androidShowRectF.height());
        simpleLayerMatrixState.pushMatrix();
        simpleLayerMatrixState.translate(tx, ty, 0);
        simpleLayerMatrixState.scale(scale, scale, 0);
        mPicLayer.onDraw(textureId, simpleLayerMatrixState.getMVPMatrix(), GLMatrixUtils.getIdentityMatrix());
        simpleLayerMatrixState.popMatrix();

        GLES20.glViewport(0, 0, mWidth, mHeight);
    }

    public void destroy() {
        mPicLayer.destroy();
    }
}
