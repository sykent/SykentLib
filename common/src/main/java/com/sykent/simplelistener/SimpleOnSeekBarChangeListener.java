package com.sykent.simplelistener;

import android.widget.SeekBar;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/07/07
 */
public class SimpleOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // no - op by default
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // no - op by default
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // no - op by default
    }
}
