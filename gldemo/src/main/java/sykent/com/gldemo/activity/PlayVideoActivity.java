package sykent.com.gldemo.activity;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.sykent.UIRun;
import com.sykent.framework.activity.BaseActivity;
import com.sykent.media.info.VideoInfo;
import com.sykent.media.player.IPlayer;
import com.sykent.utils.MediaUtils;
import com.sykent.utils.ToastUtils;
import com.sykent.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;
import sykent.com.gldemo.R;
import sykent.com.gldemo.player.GLPlayView;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/07/01
 */
public class PlayVideoActivity extends BaseActivity {

    @BindView(R.id.play_video_sv)
    GLPlayView mPlayVideo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.normal_back_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.normal_back_icon:
                finish();
                break;
        }
    }

    @Override
    public void initView() {
        super.initView();

        // 设置标题
        ((TextView) findViewById(R.id.normal_title_caption)).setText("GL 播放器");


        ViewGroup.LayoutParams layoutParams = mPlayVideo.getLayoutParams();
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

        layoutParams.height = (int) (Utils.getScreenWidth() * aspectRatio);
        mPlayVideo.setLayoutParams(layoutParams);
        mPlayVideo.init(videoPath, coverPath);
        mPlayVideo.setLoopRange(0, 10 * 1000);

        mPlayVideo.setProgressListener(new IPlayer.OnPlayProgressListener() {
            @Override
            public void onProgress(float progress) {
//                Log.d("TTTTTTTTT", "progress:  " + progress);
            }
        });

        if (!FileUtils.isFileExists(videoPath)) {
            UIRun.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showToast(PlayVideoActivity.this, "视频路径不存在，请确认！！！！");
                }
            }, 500);
        }
    }

    @Override
    public int provideTitleViewLayoutResID() {
        return R.layout.normal_title;
    }

    @Override
    public int provideContentViewLayoutResID() {
        return R.layout.activity_play_video;
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
