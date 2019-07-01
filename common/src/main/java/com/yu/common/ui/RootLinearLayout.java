package com.yu.common.ui;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;


public class RootLinearLayout extends LinearLayout {
  private int[] mInsets = new int[4];

  public RootLinearLayout(Context context) {
    super(context);
  }

  public RootLinearLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public RootLinearLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public final int[] getInsets() {
    return mInsets;
  }

  @Override protected final boolean fitSystemWindows(Rect insets) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

      mInsets[0] = insets.left;
      mInsets[1] = insets.top;
      mInsets[2] = insets.right;

      insets.left = 0;
      insets.top = 0;
      insets.right = 0;
    }

    return super.fitSystemWindows(insets);
  }
}