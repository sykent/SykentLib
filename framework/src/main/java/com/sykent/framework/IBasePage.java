package com.sykent.framework;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/07/02
 */
public interface IBasePage {

    void preInitView();

    void initView();

    void initData(@Nullable Bundle savedInstanceState, Intent intent);

    int provideTitleViewLayoutResID();

    View provideTitleView();

    View provideContentView();

    int provideContentViewLayoutResID();

}
