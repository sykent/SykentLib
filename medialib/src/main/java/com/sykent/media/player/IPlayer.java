package com.sykent.media.player;

import android.view.Surface;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/03/26
 */
public interface IPlayer {

    void setSurface(Surface surface);

    void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    void setDataSource(FileDescriptor fd, long offset, long length)
            throws IOException, IllegalArgumentException, IllegalStateException;

    void prepareAsync() throws IllegalStateException;

    void start() throws IllegalStateException;

    void pause() throws IllegalStateException;

    void seekTo(int msec) throws IllegalStateException;

    void stop() throws IllegalStateException;

    void release();

    int getCurrentPosition();

    boolean isPlaying();

    void setLooping(boolean looping);

    void reset();

    int getDuration();

    /**
     * Register a callback to be invoked when a seek operation has been
     * completed.
     *
     * @param listener the callback that will be run
     */
    void setOnSeekCompleteListener(OnSeekCompleteListener listener);


    /**
     * Register a callback to be invoked when the end of a media source
     * has been reached during playback.
     *
     * @param listener the callback that will be run
     */
    void setOnCompletionListener(OnCompletionListener listener);

    /**
     * Register a callback to be invoked when the media source is ready
     * for playback.
     *
     * @param listener the callback that will be run
     */
    void setOnPreparedListener(OnPreparedListener listener);

    /**
     * Interface definition for a callback to be invoked when the media
     * source is ready for playback.
     */
    interface OnPreparedListener {
        /**
         * Called when the media file is ready for playback.
         *
         * @param mp the MediaPlayer that is ready for playback
         */
        void onPrepared(IPlayer mp);
    }


    /**
     * Interface definition for a callback to be invoked when playback of
     * a media source has completed.
     */
    interface OnCompletionListener {
        /**
         * Called when the end of a media source is reached during playback.
         *
         * @param mp the MediaPlayer that reached the end of the file
         */
        void onCompletion(IPlayer mp);
    }

    /**
     * Interface definition of a callback to be invoked indicating
     * the completion of a seek operation.
     */
    interface OnSeekCompleteListener {
        /**
         * Called to indicate the completion of a seek operation.
         *
         * @param mp the MediaPlayer that issued the seek operation
         */
        void onSeekComplete(IPlayer mp);
    }

    /**
     * play progress callback
     */
    interface OnPlayProgressListener {
        void onProgress(float progress);
    }

}
