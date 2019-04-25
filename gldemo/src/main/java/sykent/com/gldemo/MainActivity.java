package sykent.com.gldemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;

import com.sykent.widget.GLTextureView;

import static com.sykent.widget.GLTextureView.RENDERMODE_CONTINUOUSLY;

public class MainActivity extends AppCompatActivity {

    private Button mButton;
    private LinearLayout mRoot;

    private GLTextureView.Renderer mRenderer;
    private GLTextureView mGLTextureView;

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

        mGLTextureView = new GLTextureView(this);
        mGLTextureView.setEGLContextClientVersion(2);
        mGLTextureView.setRenderer(mRenderer);
        mGLTextureView.setRenderMode(RENDERMODE_CONTINUOUSLY);

        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(1080, 1080);
        mRoot.addView(mGLTextureView, lParams);
    }

    private void initListener() {

    }

    private void findView() {
        mButton = findViewById(R.id.bt_scroll);
        mRoot = findViewById(R.id.ll_root);
    }
}
