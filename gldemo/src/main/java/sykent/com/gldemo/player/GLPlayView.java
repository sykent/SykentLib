package sykent.com.gldemo.player;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.sykent.media.player.EMediaPlayer;
import com.sykent.media.player.IPlayer;

import java.io.IOException;

/**
 * 播放器的逻辑
 *
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/07/03
 */
public class GLPlayView extends GLSurfaceView {
    private Context mContext;
    private IPlayer mPlayer;
    private IVideoRenderer mRenderer;

    private IPlayer.OnPlayProgressListener mProgressListener;

    private boolean isRangeLoop;
    private int mRangeStart;
    private int mRangeEnd;

    public GLPlayView(Context context) {
        super(context);
        mContext = context;
    }

    public GLPlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void init(String videoPath, IVideoRenderer rendererImpl) {
        setEGLContextClientVersion(2);
        mRenderer = rendererImpl;

        initMediaPlayer(videoPath);
        mRenderer.setPlayer(mPlayer);

        mRenderer.setFrameAvailableListener(surfaceTexture -> {
            // 进度回调
            if (mProgressListener != null) {
                int duration = mPlayer.getDuration();
                if (duration > 0) {
                    mProgressListener.onProgress(
                            1.0f * mPlayer.getCurrentPosition() / duration);
                }
            }

            // 范围播放循环处理
            if (isRangeLoop() && isOutLoopRange()) {
                seekRangeStart();
            }

            requestRender();
        });
        setRenderer(mRenderer);
        setRenderMode(RENDERMODE_CONTINUOUSLY);
    }

    private void initMediaPlayer(String videoPath) {
        mPlayer = new EMediaPlayer();
        mPlayer.setLooping(true);
        try {
            mPlayer.setDataSource(videoPath);
            mPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mPlayer.setOnPreparedListener(mp -> mPlayer.start());
    }

    private boolean isOutLoopRange() {
        boolean isOutRange = false;
        int currentPosition = mPlayer.getCurrentPosition();
        if (mRangeEnd > mPlayer.getDuration()) {
            mRangeEnd = mPlayer.getDuration();
        }

        if (currentPosition >= mRangeEnd) {
            isOutRange = true;
        } else {
            int error = mRangeEnd - currentPosition;
//            Log.d("bbbbbbbbbbb: ", "error: " + error);
            if (error < 150) {
                isOutRange = true;
            }
        }
        return isOutRange;
    }

    public void setLoopRange(int start, int end) {
        if (start < 0) {
            throw new IllegalArgumentException("start must >0!!!");
        }
        if (start >= end) {
            throw new IllegalArgumentException("start >= end error!!!");
        }

        mRangeStart = start;
        mRangeEnd = end;

        isRangeLoop = true;
    }

    private void seekRangeStart() {
        mPlayer.seekTo(mRangeStart);
        mPlayer.start();
    }

    public boolean isRangeLoop() {
        return isRangeLoop;
    }

    public void setRangeLoop(boolean rangeLoop) {
        isRangeLoop = rangeLoop;
    }

    public void setProgressListener(IPlayer.OnPlayProgressListener progressListener) {
        mProgressListener = progressListener;
    }

    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    public void seekTo(int msec) {
        mPlayer.seekTo(msec);
    }

    public int getDuration() {
        return mPlayer.getDuration();
    }

    public void pause() {
        mPlayer.pause();
    }

    public void start() {
        mPlayer.start();
    }

    public IVideoRenderer getRenderer() {
        return mRenderer;
    }

    public void aa(){

    }

    public void destroy() {
        mPlayer.stop();
        mPlayer.release();
        mRenderer.destroy();
    }
}
