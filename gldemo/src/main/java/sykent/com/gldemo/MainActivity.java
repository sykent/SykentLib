package sykent.com.gldemo;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;

import static com.sykent.widget.GLTextureView.RENDERMODE_CONTINUOUSLY;

public class MainActivity extends AppCompatActivity {

    private Button mButton;
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
