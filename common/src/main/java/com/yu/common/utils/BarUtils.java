package com.yu.common.utils;

import android.os.Build;
import android.view.View;

import com.yu.common.navigation.StatusBarUtils;


public class BarUtils {

  public static void setActionBarLayout(View actionBar) {
    setActionBarLayout(actionBar, 48);
  }

  public static void setActionBarLayout(View actionBar, int sHeight) {
    if (actionBar != null && actionBar.getContext() != null) {
      int height =
          Build.VERSION.SDK_INT >= 19 ? StatusBarUtils.getStatusBarHeight(actionBar.getContext())
              : 0;
      if (actionBar.getLayoutParams() != null) {
        actionBar.getLayoutParams().height =
            height + DensityUtil.dip2px(actionBar.getContext(), sHeight);
        actionBar.setPadding(0, height, 0, 0);
      }
    }
  }

  public static void setStatusBarTopPadding(View actionBar) {
    if (actionBar != null && actionBar.getContext() != null) {
      int height =
          Build.VERSION.SDK_INT >= 19 ? StatusBarUtils.getStatusBarHeight(actionBar.getContext())
              : 0;
      actionBar.setPadding(0, height, 0, 0);
    }
  }
}
