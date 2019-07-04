package sykent.com.gldemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sykent.framework.activity.BaseActivity;
import com.sykent.gl.GLSimpleLayer;
import com.sykent.gl.utils.GLMatrixUtils;
import com.sykent.gl.utils.GLUtilsEx;
import com.sykent.imagedecode.EBitmapFactory;
import com.sykent.widget.GLTextureView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import butterknife.BindView;
import butterknife.OnClick;
import sykent.com.gldemo.R;
import sykent.com.gldemo.widget.WheelView;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/04/23
 */
public class BlendActivity extends BaseActivity implements GLTextureView.Renderer {

    @BindView(R.id.blend_gl_view)
    GLTextureView mGLView;
    @BindView(R.id.blend_equa_textview)
    TextView mEqua;


    private Bitmap srcBitmap;
    private Bitmap dstBitmap;
    private int mDstTextureId;
    private int mSrcTextureId;
    private GLSimpleLayer mDstFilter;
    private GLSimpleLayer mSrcFilter;
    private WheelView mSrcParamView;
    private WheelView mDstParamsView;

    private int width, height;

    private int nDstPar = GLES20.GL_ONE_MINUS_SRC_ALPHA;
    private int nSrcPar = GLES20.GL_SRC_ALPHA;
    private int nEquaIndex = 0;


    private String[] paramStr = new String[]{
            "GL_ZERO", "GL_ONE", "GL_SRC_COLOR", "GL_ONE_MINUS_SRC_COLOR",
            "GL_DST_COLOR", "GL_ONE_MINUS_DST_COLOR", "GL_SRC_ALPHA", "GL_ONE_MINUS_SRC_ALPHA",
            "GL_DST_ALPHA", "GL_ONE_MINUS_DST_ALPHA", "GL_CONSTANT_COLOR", "GL_ONE_MINUS_CONSTANT_COLOR",
            "GL_CONSTANT_ALPHA", "GL_ONE_MINUS_CONSTANT_ALPHA", "GL_SRC_ALPHA_SATURATE"
    };

    private int[] paramInt = new int[]{
            GLES20.GL_ZERO, GLES20.GL_ONE, GLES20.GL_SRC_COLOR, GLES20.GL_ONE_MINUS_SRC_COLOR,
            GLES20.GL_DST_COLOR, GLES20.GL_ONE_MINUS_DST_COLOR, GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA,
            GLES20.GL_DST_ALPHA, GLES20.GL_ONE_MINUS_DST_ALPHA, GLES20.GL_CONSTANT_COLOR, GLES20.GL_ONE_MINUS_CONSTANT_COLOR,
            GLES20.GL_CONSTANT_ALPHA, GLES20.GL_ONE_MINUS_CONSTANT_ALPHA, GLES20.GL_SRC_ALPHA_SATURATE
    };

    private int[] equaInt = new int[]{
            GLES20.GL_FUNC_ADD, GLES20.GL_FUNC_SUBTRACT, GLES20.GL_FUNC_REVERSE_SUBTRACT
    };

    private String[] equaStr = new String[]{
            "GL_FUNC_ADD", "GL_FUNC_SUBTRACT", "GL_FUNC_REVERSE_SUBTRACT"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.normal_back_icon})
    public void onClick(View view) {
        finish();
    }

    @Override
    public void initView() {
        super.initView();

        // 设置标题
        ((TextView) findViewById(R.id.normal_title_caption)).setText("GL 混合");


        mGLView.setEGLContextClientVersion(2);
        mGLView.setEGLConfigChooser(8, 8,
                8, 8, 16, 8);
        mGLView.setRenderer(this);
        mGLView.setRenderMode(GLTextureView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState, Intent intent) {
        super.initData(savedInstanceState, intent);
        mEqua.setText(equaStr[nEquaIndex]);
        mEqua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nEquaIndex++;
                if (nEquaIndex >= 3) nEquaIndex = 0;
                mEqua.setText(equaStr[nEquaIndex]);
            }
        });

        dstBitmap = EBitmapFactory.decode(this, "pic/bg.jpg");
        srcBitmap = EBitmapFactory.decode(this, "pic/src.png");
        mSrcParamView = (WheelView) findViewById(R.id.mSrcParam);
        mDstParamsView = (WheelView) findViewById(R.id.mDstParam);

        initParamData();
    }

    private void initParamData() {
        for (int i = 0; i < paramStr.length; i++) {
            mSrcParamView.addData(paramStr[i], paramInt[i]);
            mDstParamsView.addData(paramStr[i], paramInt[i]);
        }
        mSrcParamView.setCenterItem("GL_SRC_COLOR");
        mDstParamsView.setCenterItem("GL_ONE_MINUS_SRC_ALPHA");
        mSrcParamView.setTextSize(10);
        mDstParamsView.setTextSize(10);
        mSrcParamView.setOnCenterItemChangedListener(new WheelView.OnCenterItemChangedListener() {
            @Override
            public void onItemChange(Object now) {
                nSrcPar = (int) now;
            }
        });
        mDstParamsView.setOnCenterItemChangedListener(new WheelView.OnCenterItemChangedListener() {
            @Override
            public void onItemChange(Object now) {
                nDstPar = (int) now;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0, 0, 0, 0);
        mDstFilter = new GLSimpleLayer(this);
        mSrcFilter = new GLSimpleLayer(this);
        mSrcTextureId = GLUtilsEx.createTexture(srcBitmap, false, true);
        mDstTextureId = GLUtilsEx.createTexture(dstBitmap, false, true);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.width = width;
        this.height = height;

        mDstFilter.setProjectOrtho(width, height);
        mSrcFilter.setProjectOrtho(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
//        GLES20.glClearColor(1.0f,1.0f,1.0f,1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(nSrcPar, nDstPar);
        GLES20.glBlendEquation(equaInt[nEquaIndex]);
        GLES20.glViewport(0, 0, width, height);

        mSrcFilter.onDraw(mSrcTextureId, mSrcFilter.getMVPMatrix(), GLMatrixUtils.getIdentityMatrix());
        mDstFilter.onDraw(mDstTextureId, mDstFilter.getMVPMatrix(), GLMatrixUtils.getIdentityMatrix());
    }


    @Override
    public int provideContentViewLayoutResID() {
        return R.layout.activity_blend;
    }

    @Override
    public int provideTitleViewLayoutResID() {
        return R.layout.normal_title;
    }
}
