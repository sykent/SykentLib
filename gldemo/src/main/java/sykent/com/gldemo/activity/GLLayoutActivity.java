package sykent.com.gldemo.activity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.sykent.framework.activity.BaseActivity;

import sykent.com.gldemo.LayoutRenderer;
import sykent.com.gldemo.R;

import static com.sykent.widget.GLTextureView.RENDERMODE_CONTINUOUSLY;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/07/01
 */
public class GLLayoutActivity extends BaseActivity {
    private LinearLayout mRoot;

    private GLSurfaceView.Renderer mRenderer;
    private GLSurfaceView mGLTextureView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
        init();
        initListener();
    }

    private void init() {
        mRenderer = new LayoutRenderer(this);

        mGLTextureView = new GLSurfaceView(this);
        mGLTextureView.setEGLContextClientVersion(2);
        mGLTextureView.setRenderer(mRenderer);
        mGLTextureView.setRenderMode(RENDERMODE_CONTINUOUSLY);

        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(1080, 1080);
        mRoot.addView(mGLTextureView, lParams);
    }

    private void initListener() {

    }

    private void findView() {
        mRoot = findViewById(R.id.ll_root);
    }
}
