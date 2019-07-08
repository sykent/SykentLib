package sykent.com.gldemo.player;

import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.opengl.GLSurfaceView;

import com.sykent.media.player.IPlayer;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/07/08
 */
public interface IVideoRenderer extends GLSurfaceView.Renderer {

    void setFrameAvailableListener(OnFrameAvailableListener frameAvailableListener);

    /**
     * 必须在{@link GLSurfaceView#setRenderer}之前调用
     *
     * @param player
     */
    void setPlayer(IPlayer player);

    void destroy();
}
