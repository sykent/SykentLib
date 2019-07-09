package sykent.com.gldemo.gleffect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.Surface;

import com.blankj.utilcode.util.FileUtils;
import com.sykent.gl.GLSimpleLayer;
import com.sykent.gl.GLYuvLayer;
import com.sykent.gl.core.GLOffscreenBuffer;
import com.sykent.gl.core.GLOffscreenBufferGroup;
import com.sykent.gl.filter.GLBlurFilter;
import com.sykent.gl.utils.GLMatrixUtils;
import com.sykent.gl.utils.GLUtilsEx;
import com.sykent.imagedecode.EBitmapFactory;
import com.sykent.imagedecode.core.ImageSize;
import com.sykent.media.player.IPlayer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import sykent.com.gldemo.player.IVideoRenderer;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/07/08
 */
public class EffectRenderer implements OnFrameAvailableListener, IVideoRenderer {

    private Context mContext;
    private int mWidth;
    private int mHeight;

    private IPlayer mPlayer;
    private SurfaceTexture mSurfaceTexture;
    private Surface mSurface;
    private boolean isFrameAvailable;
    private SurfaceTexture.OnFrameAvailableListener mFrameAvailableListener;

    private boolean isDrawCover = true;
    private String mCoverPath;

    private float[] mSTMatrix = GLMatrixUtils.getIdentityMatrix();
    private int mYuvTexture;

    private GLYuvLayer mYuvLayer;
    private GLSimpleLayer mSimpleLayer;
    private GLBlurFilter mBlurFilter;
    private GLOffscreenBuffer mOffscreenBuffer;

    private GLOffscreenBufferGroup mOffscreenBufferGroup;
    private int mRadius;
    private int mOverlayColor = 0x33000000;

    public EffectRenderer(Context context, String coverPath) {
        mContext = context;
        mCoverPath = coverPath;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        synchronized (this) {
            if (isFrameAvailable) {
                mSurfaceTexture.updateTexImage();
                mSurfaceTexture.getTransformMatrix(mSTMatrix);
                isFrameAvailable = false;
                isDrawCover = false;
            }
        }

        if (isDrawCover()) {
            return;
        }

        // 离屏画，yuv --> texture2D(后续可以做视频处理)
        mOffscreenBuffer.onBind();
        mYuvLayer.onDraw(mYuvTexture, mYuvLayer.getFullMVPMatrix(mWidth, mHeight), mSTMatrix);
        mOffscreenBuffer.onUnBind();

//        mSimpleLayer.onDraw(mOffscreenBuffer.getTextureId(),
//                mSimpleLayer.getFullMVPMatrix(mWidth, mHeight), GLMatrixUtils.getIdentityMatrix());

        GLUtilsEx.checkGlError();
        long lastTime = System.currentTimeMillis();
        mBlurFilter.onDraw(mOffscreenBuffer.getTextureId(),
                mBlurFilter.getFullMVPMatrix(mWidth, mHeight),
                GLMatrixUtils.getIdentityMatrix(), mRadius, mOverlayColor);
        Log.d("ttttttttttttttt", "time:" + (System.currentTimeMillis() - lastTime));
    }

    private boolean isDrawCover() {
        if (isDrawCover && FileUtils.isFileExists(mCoverPath)) {
            Bitmap bitmap = EBitmapFactory.decode(mContext, mCoverPath, new ImageSize(mWidth, mHeight));
            int texture = GLUtilsEx.createTexture(bitmap, false, true);
            mSimpleLayer.onDraw(texture, mSimpleLayer.getFullMVPMatrix(mWidth, mHeight),
                    GLMatrixUtils.getIdentityMatrix());
            return true;
        } else {
            return false;
        }
    }

    private void createTextures() {
        mYuvTexture = GLUtilsEx.createTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES);
    }

    private void createFilter() {
        mYuvLayer = new GLYuvLayer(mContext);
        mSimpleLayer = new GLSimpleLayer(mContext);
        mBlurFilter = new GLBlurFilter(mContext);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        createTextures();
        createFilter();

        mSurfaceTexture = new SurfaceTexture(mYuvTexture);
        mSurfaceTexture.setOnFrameAvailableListener(this);
        mSurface = new Surface(mSurfaceTexture);
        mPlayer.setSurface(mSurface);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mWidth = width;
        mHeight = height;
        mYuvLayer.setProjectOrtho(width, height);
        mSimpleLayer.setProjectOrtho(width, height);
        mBlurFilter.setProjectOrtho(width, height);

        if (mOffscreenBuffer != null) {
            mOffscreenBuffer.destroy();
        }
        mOffscreenBuffer = new GLOffscreenBuffer(width, height);

        if (mOffscreenBufferGroup != null) {
            mOffscreenBufferGroup.destroy();
        }
        mOffscreenBufferGroup = new GLOffscreenBufferGroup(width, height, 2);
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        isFrameAvailable = true;

        if (mFrameAvailableListener != null) {
            mFrameAvailableListener.onFrameAvailable(surfaceTexture);
        }
    }

    @Override
    public void setFrameAvailableListener(SurfaceTexture.OnFrameAvailableListener frameAvailableListener) {
        mFrameAvailableListener = frameAvailableListener;
    }

    /**
     * 必须在{@link GLSurfaceView#setRenderer}之前调用
     *
     * @param player
     */
    @Override
    public void setPlayer(IPlayer player) {
        mPlayer = player;
    }

    public void setBlurRadius(int radius) {
        mRadius = radius;
    }

    public void setOverlayColor(int overlayColor) {
        mOverlayColor = overlayColor;
    }

    @Override
    public void destroy() {
        GLES20.glDeleteTextures(1, new int[]{mYuvTexture}, 0);
        mOffscreenBuffer.destroy();
        mOffscreenBufferGroup.destroy();
        mYuvLayer.destroy();
        mSimpleLayer.destroy();

        mSurfaceTexture.release();
        mSurface.release();
    }
}
