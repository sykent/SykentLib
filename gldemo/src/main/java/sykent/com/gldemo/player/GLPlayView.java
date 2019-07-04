package sykent.com.gldemo.player;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.sykent.media.player.EMediaPlayer;
import com.sykent.media.player.IPlayer;

import java.io.IOException;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/07/03
 */
public class GLPlayView extends GLSurfaceView {
    private Context mContext;
    private IPlayer mPlayer;
    private PlayVideoRenderer mRenderer;

    public GLPlayView(Context context) {
        super(context);
        mContext = context;
    }

    public GLPlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void init(String path) {
        setEGLContextClientVersion(2);
        mRenderer = new PlayVideoRenderer(mContext);

        initMediaPlayer(path);
        mRenderer.setPlayer(mPlayer);

        mRenderer.setFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                requestRender();
            }
        });
        setRenderer(mRenderer);
        setRenderMode(RENDERMODE_CONTINUOUSLY);
    }

    private void initMediaPlayer(String path) {
        mPlayer = new EMediaPlayer();
        mPlayer.setLooping(true);
        try {
            mPlayer.setDataSource(path);
            mPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mPlayer.setOnPreparedListener(new IPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IPlayer mp) {
                mPlayer.start();
            }
        });
    }

    public void pause() {
        onPause();
        mPlayer.pause();
    }

    public void start() {
        onResume();
        mPlayer.start();
    }

    public void destroy() {
        mPlayer.stop();
        mPlayer.release();
        mRenderer.destroy();
    }
}
