package sykent.com.gldemo.activity;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.FileUtils;
import com.sykent.UIRun;
import com.sykent.framework.activity.BaseActivity;
import com.sykent.media.info.VideoInfo;
import com.sykent.simplelistener.SimpleOnSeekBarChangeListener;
import com.sykent.utils.ColorUtils;
import com.sykent.utils.MediaUtils;
import com.sykent.utils.ToastUtils;
import com.sykent.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;
import sykent.com.gldemo.R;
import sykent.com.gldemo.gleffect.EffectRenderer;
import sykent.com.gldemo.player.GLPlayView;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/07/08
 */
public class EffectVideoActivity extends BaseActivity {
    @BindView(R.id.play_video_sv)
    GLPlayView mPlayVideo;
    @BindView(R.id.play_video_container)
    RelativeLayout mPlayVideoContainer;
    @BindView(R.id.play_video_pause)
    ImageView mPause;

    @BindView(R.id.et_blur_color)
    EditText mEtColor;
    @BindView(R.id.bt_sure_blur_color)
    Button mBtSureColor;

    @BindView(R.id.tv_play_progress)
    TextView mTvProgress;
    @BindView(R.id.play_video_seek_bar)
    SeekBar mProgressSeekBar;

    @BindView(R.id.tv_blur_radius)
    TextView mTvRadius;
    @BindView(R.id.sb_blur_radius)
    SeekBar mRadiusSeekBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.normal_back_icon, R.id.play_video_pause, R.id.play_video_sv, R.id.bt_sure_blur_color})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.normal_back_icon:
                finish();
                break;
            case R.id.play_video_pause:
            case R.id.play_video_sv:
                if (mPlayVideo.isPlaying()) {
                    mPlayVideo.pause();
                    mPause.setVisibility(View.VISIBLE);
                } else {
                    mPlayVideo.start();
                    mPause.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.bt_sure_blur_color:
                String s1 = mEtColor.getText().toString();
                if (TextUtils.isEmpty(s1)) {
                    return;
                }
                int color = ColorUtils.string2Int(s1);

                EffectRenderer effectRenderer = (EffectRenderer) mPlayVideo.getRenderer();
                mPlayVideo.queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        effectRenderer.setOverlayColor(color);
                    }
                });
                break;
        }
    }

    @Override
    public void initView() {
        super.initView();

        // 设置标题
        ((TextView) findViewById(R.id.normal_title_caption)).setText("GL Effect Demo");


        String videoPath = "/storage/emulated/0/DCIM/Camera/8.mp4";
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath,
                MediaStore.Images.Thumbnails.MICRO_KIND);
        String coverPath = "/storage/emulated/0/DCIM/Camera/8.png";
        com.sykent.utils.FileUtils.write2SD(bitmap, coverPath, true);

        float aspectRatio = 1.0f;
        VideoInfo videoInfo = MediaUtils.getVideoInfo(videoPath);
        if (videoInfo != null) {
            aspectRatio = 1.0f * videoInfo.getHeight() / videoInfo.getWidth();
        }

        ViewGroup.LayoutParams layoutParams = null;
        int height = (int) (Utils.getScreenWidth() * aspectRatio);
        layoutParams = mPlayVideoContainer.getLayoutParams();
        layoutParams.height = height;
        mPlayVideoContainer.setLayoutParams(layoutParams);

        layoutParams = mPlayVideo.getLayoutParams();
        layoutParams.height = height;
        mPlayVideo.setLayoutParams(layoutParams);
        mPlayVideo.init(videoPath, new EffectRenderer(this, coverPath));
        // 设置播放范围
//        mPlayVideo.setLoopRange(0, 10 * 1000);

        // 初始化监听器
        initListener();

        if (!FileUtils.isFileExists(videoPath)) {
            UIRun.postDelayed(() -> ToastUtils.showToast(EffectVideoActivity.this, "视频路径不存在，请确认！！！！"), 500);
        }
    }

    private void initListener() {
        mPlayVideo.setProgressListener(progress -> mProgressSeekBar.setProgress((int) (progress * mProgressSeekBar.getMax())));

        mProgressSeekBar.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    float seekTimeProgress = 1.0f * progress / seekBar.getMax();
                    mPlayVideo.seekTo((int) (seekTimeProgress * mPlayVideo.getDuration()));
                }
                mTvProgress.setText("播放进度(" + progress + "):");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mPlayVideo.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPlayVideo.start();
            }

        });

        mRadiusSeekBar.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    EffectRenderer effectRenderer = (EffectRenderer) mPlayVideo.getRenderer();
                    mPlayVideo.queueEvent(new Runnable() {
                        @Override
                        public void run() {
                            effectRenderer.setBlurRadius(progress);
                        }
                    });
                    mTvRadius.setText("模糊半径(" + progress + "):");
                }
            }
        });
    }

    @Override
    public int provideTitleViewLayoutResID() {
        return R.layout.normal_title;
    }

    @Override
    public int provideContentViewLayoutResID() {
        return R.layout.activity_video_effect;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayVideo.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayVideo.start();
    }

    @Override
    protected void onDestroy() {
        mPlayVideo.destroy();
        super.onDestroy();
    }
}
