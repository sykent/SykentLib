package com.sykent.simplelistener;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/07/09
 */
public class SimpleTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // no - op by default
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // no - op by default
    }

    @Override
    public void afterTextChanged(Editable s) {
        // no - op by default
    }
}
