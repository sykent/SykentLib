package com.sykent.media.player;

import android.media.MediaPlayer;
import android.view.Surface;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/03/11
 */
public class EMediaPlayer implements IPlayer {
    private MediaPlayer mPlayer;

    public EMediaPlayer() {
        mPlayer = new MediaPlayer();
    }

    @Override
    public void setSurface(Surface surface) {
        mPlayer.setSurface(surface);
    }

    @Override
    public void setDataSource(String path) throws
            IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        mPlayer.setDataSource(path);
    }

    @Override
    public void setDataSource(FileDescriptor fd, long offset, long length) throws IOException, IllegalArgumentException, IllegalStateException {
        mPlayer.setDataSource(fd, offset, length);
    }

    @Override
    public void prepareAsync() throws IllegalStateException {
        mPlayer.prepareAsync();
    }

    @Override
    public void start() throws IllegalStateException {
        mPlayer.start();
    }

    @Override
    public void pause() throws IllegalStateException {
        mPlayer.pause();
    }

    @Override
    public void seekTo(int msec) throws IllegalStateException {
        mPlayer.seekTo(msec);
    }

    @Override
    public void stop() throws IllegalStateException {
        mPlayer.stop();
    }

    @Override
    public void release() {
        mPlayer.setOnSeekCompleteListener(null);
        mPlayer.setOnCompletionListener(null);
        mPlayer.setOnPreparedListener(null);
        mPlayer.release();
    }

    @Override
    public int getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    @Override
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    @Override
    public void reset() {
        mPlayer.reset();
    }

    @Override
    public int getDuration() {
        return mPlayer.getDuration();
    }

    @Override
    public void setLooping(boolean looping) {
        mPlayer.setLooping(looping);
    }

    @Override
    public void setOnSeekCompleteListener(final IPlayer.OnSeekCompleteListener listener) {
        if (listener != null) {
            mPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    listener.onSeekComplete(EMediaPlayer.this);
                }
            });
        } else {
            mPlayer.setOnSeekCompleteListener(null);
        }
    }

    @Override
    public void setOnCompletionListener(final IPlayer.OnCompletionListener listener) {
        if (listener != null) {
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    listener.onCompletion(EMediaPlayer.this);
                }
            });
        } else {
            mPlayer.setOnCompletionListener(null);
        }

    }

    @Override
    public void setOnPreparedListener(final IPlayer.OnPreparedListener listener) {
        if (listener != null) {
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    listener.onPrepared(EMediaPlayer.this);
                }
            });
        } else {
            mPlayer.setOnPreparedListener(null);
        }
    }

    public MediaPlayer getPlayer() {
        return mPlayer;
    }
}
