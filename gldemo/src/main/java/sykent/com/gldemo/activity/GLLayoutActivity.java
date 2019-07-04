package sykent.com.gldemo.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sykent.framework.activity.BaseActivity;
import com.sykent.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;
import sykent.com.gldemo.R;
import sykent.com.gldemo.layout.LayoutGLSurfaceView;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/07/01
 */
public class GLLayoutActivity extends BaseActivity {

    @BindView(R.id.layout_gl_surface_view)
    LayoutGLSurfaceView mGLTextureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initListener();
    }

    @Override
    public void initView() {
        super.initView();

        // 设置标题
        ((TextView) findViewById(R.id.normal_title_caption)).setText("GL 布局");

        ViewGroup.LayoutParams layoutParams = mGLTextureView.getLayoutParams();
        layoutParams.height = Utils.getScreenWidth();
        mGLTextureView.setLayoutParams(layoutParams);
        mGLTextureView.init();
    }

    @OnClick({R.id.normal_back_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.normal_back_icon:
                finish();
                break;
        }
    }

    private void initListener() {

    }

    @Override
    public int provideTitleViewLayoutResID() {
        return R.layout.normal_title;
    }

    @Override
    public int provideContentViewLayoutResID() {
        return R.layout.activity_layout;
    }

    @Override
    protected void onDestroy() {
        mGLTextureView.destroy();
        super.onDestroy();
    }
}
