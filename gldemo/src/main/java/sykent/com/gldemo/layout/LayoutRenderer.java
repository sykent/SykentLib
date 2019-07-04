package sykent.com.gldemo.layout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.sykent.gl.GLSimpleLayer;
import com.sykent.gl.core.GLOffscreenBuffer;
import com.sykent.gl.core.MatrixState;
import com.sykent.gl.data.GLConversion;
import com.sykent.gl.utils.GLConversionUtils;
import com.sykent.gl.utils.GLMatrixUtils;
import com.sykent.gl.utils.GLUtilsEx;
import com.sykent.imagedecode.EBitmapFactory;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/04/25
 */
public class LayoutRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = LayoutRenderer.class.getSimpleName();

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

    private int[] mMaskTextureIds;

    private float[][] mLaoutData = new float[][]{
            {0, 0, 683, 512},
            {683, 0, 683, 512},
            {1366, 0, 682, 512},
            {0, 512, 683, 512},
            {0, 1024, 683, 512},
            {683, 512, 1365, 1024},
            {0, 1536, 683, 512},
            {683, 1536, 683, 512},
            {1366, 1536, 682, 512}
    };

    private float[][] mLaoutData1 = new float[][]{
            {0, 0, 1024, 1024},
            {1024, 0, 1024, 1024},
            {0, 1024, 1024, 1024},
            {1024, 1024, 1024, 1024},
    };


    private RectF[] mRectFS;

    public LayoutRenderer(Context context) {
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

        // 矩形位置
        mRectFS = getLocationRectFs(2048, width);
        mMaskTextureIds = new int[mRectFS.length];
        for (int i = 0; i < mRectFS.length; i++) {
            Bitmap mask = getMaskBmp((int) mRectFS[i].width(), (int) mRectFS[i].height());
            mMaskTextureIds[i] = GLUtilsEx.createTexture(mask);
        }

        GLES20.glViewport(0, 0, width, height);
        mPicLayer.setProjectOrtho(width, height);
        mMaskLayer.setProjectOrtho(width, height);
        if (mOffscreenBuffer != null) {
            mOffscreenBuffer.destroy();
            mOffscreenBuffer = null;
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


        mOffscreenBuffer.onBind();
        moveToViewport(textureId, simpleLayerMatrixState);
        mOffscreenBuffer.onUnBind();

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFuncSeparate(GLES20.GL_SRC_ALPHA,
                GLES20.GL_ONE_MINUS_SRC_ALPHA, GLES20.GL_ONE, GLES20.GL_ONE);
        GLES20.glBlendEquation(GLES20.GL_FUNC_ADD);
        mPicLayer.onDraw(mOffscreenBuffer.getTextureId(), simpleLayerMatrixState.getMVPMatrix(), GLMatrixUtils.getIdentityMatrix());
        GLES20.glDisable(GLES20.GL_BLEND);
    }

    public RectF[] getLocationRectFs(int inStandardSize, int outStandardSize) {
        float[][] datas = mLaoutData;
        RectF[] rectFs = new RectF[datas.length];
        for (int i = 0; i < datas.length; i++) {
            float[] tmp = datas[i];
            rectFs[i] = new RectF(
                    (tmp[0] / inStandardSize) * outStandardSize,
                    (tmp[1] / inStandardSize) * outStandardSize,
                    ((tmp[0] + tmp[2]) / inStandardSize) * outStandardSize + 1,
                    ((tmp[1] + tmp[3]) / inStandardSize) * outStandardSize + 1);
        }

        return rectFs;
    }

    private Bitmap getMaskBmp(int w, int h) {
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAlpha(255);
        Canvas canvas = new Canvas(bitmap);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0,
                Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawRoundRect(new RectF(50, 50, w - 50, h - 50), 60, 60, paint);
        return bitmap;
    }

    public void moveToViewport(int textureId, MatrixState simpleLayerMatrixState) {
        for (int i = 0; i < mRectFS.length; i++) {
            RectF androidBmpRectF = mRectFS[i];
            RectF androidShowRectF = mRectFS[i];

            GLConversion glConversion = GLConversionUtils.getGLRectConversion(androidBmpRectF, androidShowRectF);
            GLES20.glViewport((int) androidShowRectF.left, (int) (mHeight - androidShowRectF.height() - androidShowRectF.top),
                    (int) androidShowRectF.width(), (int) androidShowRectF.height());
            mPicLayer.setProjectOrtho((int) androidShowRectF.width(), (int) androidShowRectF.height());
            simpleLayerMatrixState.pushMatrix();
            simpleLayerMatrixState.translate(glConversion.getTranslateX(), glConversion.getTranslateY(), 0);
            simpleLayerMatrixState.scale(glConversion.getScaleX(), glConversion.getScaleY(), 0);
            mPicLayer.onDraw(textureId, simpleLayerMatrixState.getMVPMatrix(), GLMatrixUtils.getIdentityMatrix());
            simpleLayerMatrixState.popMatrix();

            // 画遮罩
            MatrixState maskMatrixState = mMaskLayer.getMatrixState();
            int maskTextureId = mMaskTextureIds[i];
            mMaskLayer.setProjectOrtho((int) androidShowRectF.width(), (int) androidShowRectF.height());
            maskMatrixState.pushMatrix();
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFuncSeparate(GLES20.GL_ONE_MINUS_SRC_ALPHA,
                    GLES20.GL_SRC_ALPHA, GLES20.GL_SRC_ALPHA, GLES20.GL_SRC_ALPHA);
            GLES20.glBlendEquation(GLES20.GL_FUNC_ADD);
            mMaskLayer.onDraw(maskTextureId, mMaskLayer.getMVPMatrix(), GLMatrixUtils.getIdentityMatrix());
            maskMatrixState.popMatrix();
            GLES20.glDisable(GLES20.GL_BLEND);

            GLES20.glViewport(0, 0, mWidth, mHeight);
            mPicLayer.setProjectOrtho(mWidth, mHeight);
        }
    }


    public void destroy() {
        mPicLayer.destroy();
    }
}
