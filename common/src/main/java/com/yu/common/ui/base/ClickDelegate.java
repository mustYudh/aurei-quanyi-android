package com.yu.common.ui.base;

import android.support.annotation.Nullable;
import android.view.View;


public class ClickDelegate implements View.OnClickListener {


  @Nullable public static View.OnClickListener delay(View.OnClickListener listener, long minDelay) {
    if (listener == null) {
      return null;
    }
    return new ClickDelegate(listener, minDelay);
  }

  private View.OnClickListener listener;
  private long lastTime;

  private long minDelay = 1200;

  private ClickDelegate(View.OnClickListener listener, long minDelay) {
    this.listener = listener;
    this.minDelay = minDelay;
  }

  @Override public void onClick(View v) {
    if (System.currentTimeMillis() - lastTime > minDelay) {
      lastTime = System.currentTimeMillis();
      if (listener != null) {
        listener.onClick(v);
      }
    }
  }
}
