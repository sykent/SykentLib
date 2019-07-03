package sykent.com.gldemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sykent.framework.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;
import sykent.com.gldemo.R;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/07/01
 */
public class PlayVideoActivity extends BaseActivity {

    @BindView(R.id.normal_title_caption)
    TextView mTitleContent;

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
        mTitleContent.setText("播放器");
    }

    @Override
    public int provideTitleViewLayoutResID() {
        return R.layout.normal_title;
    }

}
