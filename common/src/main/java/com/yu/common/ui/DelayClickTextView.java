package com.yu.common.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.yu.common.ui.base.BaseTextView;
import com.yu.common.ui.base.ClickDelegate;


public class DelayClickTextView extends BaseTextView {
    public DelayClickTextView(Context context) {
        super(context);
    }

    public DelayClickTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DelayClickTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnClickListener(@Nullable View.OnClickListener l) {
        super.setOnClickListener(l instanceof ClickDelegate ? l : ClickDelegate.delay(l, 1200));
    }

    public void setOnClickListener(@Nullable View.OnClickListener l, int delay) {
        super.setOnClickListener(ClickDelegate.delay(l, delay));
    }
}
