package sykent.com.gldemo.player;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.Surface;

import com.sykent.gl.GLYuvLayer;
import com.sykent.gl.utils.GLMatrixUtils;
import com.sykent.gl.utils.GLUtilsEx;
import com.sykent.media.player.IPlayer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/07/03
 */
public class PlayVideoRenderer implements GLSurfaceView.Renderer, OnFrameAvailableListener {

    private Context mContext;
    private int mWidth;
    private int mHeight;

    private IPlayer mPlayer;
    private SurfaceTexture mSurfaceTexture;
    private Surface mSurface;
    private boolean isFrameAvailable;
    private OnFrameAvailableListener mFrameAvailableListener;

    private float[] mSTMatrix = GLMatrixUtils.getIdentityMatrix();
    private int mYuvTexture;
    private int mTexture;

    private GLYuvLayer mYuvLayer;

    public PlayVideoRenderer(Context context) {
        mContext = context;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        synchronized (this) {
            if (isFrameAvailable) {
                mSurfaceTexture.updateTexImage();
                mSurfaceTexture.getTransformMatrix(mSTMatrix);
                isFrameAvailable = false;
            }
        }

        mYuvLayer.onDraw(mYuvTexture, mYuvLayer.getFullMVPMatrix(mWidth, mHeight), mSTMatrix);

    }

    private void createTextures() {
        mYuvTexture = GLUtilsEx.createTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES);
        mTexture = GLUtilsEx.createTexture(GLES20.GL_TEXTURE_2D);
    }

    private void createFilter() {
        mYuvLayer = new GLYuvLayer(mContext);
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
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        isFrameAvailable = true;
        if (mFrameAvailableListener != null) {
            mFrameAvailableListener.onFrameAvailable(surfaceTexture);
        }
    }

    public void setFrameAvailableListener(OnFrameAvailableListener frameAvailableListener) {
        mFrameAvailableListener = frameAvailableListener;
    }

    /**
     * 必须在{@link GLSurfaceView#setRenderer}之前调用
     *
     * @param player
     */
    public void setPlayer(IPlayer player) {
        mPlayer = player;
    }

    public void destroy() {
        GLES20.glDeleteTextures(1, new int[]{mYuvTexture}, 0);
        mYuvLayer.destroy();
        mSurfaceTexture.release();
        mSurface.release();
    }
}
