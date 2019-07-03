package com.sykent.framework.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.BarUtils;
import com.sykent.framework.IBasePage;
import com.sykent.utils.Utils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/07/01
 */
public class BaseActivity extends AppCompatActivity implements IBasePage {
    private static final int INVALID_LAYOUT_ID = -1;

    private FrameLayout mRoot;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.init();
        initView();
        initData(savedInstanceState, getIntent());
    }

    @Override
    public void initView() {
        FrameLayout.LayoutParams fParams = null;
        fParams = new FrameLayout.LayoutParams(-1, -1);
        mRoot = new FrameLayout(this);
        setContentView(mRoot, fParams);

        // 设置状态栏
        setStatusBar();

        // 标题栏
        int titleLayoutResID = provideTitleViewLayoutResID();
        int titleHeight = 0;
        View titleView = provideTitleView();
        checkLegalInitView(titleView, titleLayoutResID);
        if (titleLayoutResID != INVALID_LAYOUT_ID) {
            titleView = LayoutInflater.from(this)
                    .inflate(titleLayoutResID, mRoot, false);
        }
        if (titleView != null) {
            titleHeight = Utils.getRealPixel(90);
            fParams = new FrameLayout.LayoutParams(-1, titleHeight);
            fParams.gravity = Gravity.TOP;
            mRoot.addView(titleView, fParams);
            titleView.setBackgroundColor(0xfff2f2f2);
        }

        // 内容区
        int contentLayoutResID = provideContentViewLayoutResID();
        if (contentLayoutResID != INVALID_LAYOUT_ID) {
            View view = LayoutInflater.from(this)
                    .inflate(contentLayoutResID, mRoot, false);
            fParams = new FrameLayout.LayoutParams(-1, -1);
            fParams.topMargin = titleHeight;
            mRoot.addView(view, fParams);
        }

        // try use butter knife
        if (titleLayoutResID != INVALID_LAYOUT_ID || contentLayoutResID != INVALID_LAYOUT_ID) {
            mUnbinder = ButterKnife.bind(this);
        }

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState, Intent intent) {
        // no - op by default
    }

    @Override
    public int provideTitleViewLayoutResID() {
        return INVALID_LAYOUT_ID;
    }

    @Override
    public View provideTitleView() {
        // no - op by default
        return null;
    }

    @Override
    public View provideContentView() {
        // no - op by default
        return null;
    }

    @Override
    public int provideContentViewLayoutResID() {
        return INVALID_LAYOUT_ID;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
    }

    private void setStatusBar() {
        // 设置状态栏颜色
        BarUtils.setStatusBarColor(this, 0xfff2f2f2, 0);
        BarUtils.addMarginTopEqualStatusBarHeight(mRoot);
        // 设置文字效果颜色为暗色
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void checkLegalInitView(View view, int layoutResID) {
        if (view != null && layoutResID != INVALID_LAYOUT_ID) {
            throw new IllegalArgumentException("can't both provide view and layoutID!!");
        }
    }
}
