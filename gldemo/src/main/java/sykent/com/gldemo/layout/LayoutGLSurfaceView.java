package sykent.com.gldemo.layout;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/07/04
 */
public class LayoutGLSurfaceView extends GLSurfaceView {
    private Context mContext;
    private LayoutRenderer mRenderer;

    public LayoutGLSurfaceView(Context context) {
        super(context);
        mContext = context;
    }

    public LayoutGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void init() {
        mRenderer = new LayoutRenderer(mContext);

        setEGLContextClientVersion(2);
        setRenderer(mRenderer);
        setRenderMode(RENDERMODE_CONTINUOUSLY);
    }

    public void destroy() {
        mRenderer.destroy();
    }
}
