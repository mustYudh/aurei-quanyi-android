package com.yu.common.navigation;

import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


public class NavigationUtils {

  public static void setNavigationBaColor(Window window, int color) {
    if (window == null) {
      return;
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      window.getDecorView()
          .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | window.getDecorView()
              .getSystemUiVisibility());

      WindowManager.LayoutParams winParams = window.getAttributes();
      winParams.flags |= WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;
      //winParams.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
      winParams.flags &= ~WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;

      window.setAttributes(winParams);

      window.setNavigationBarColor(color);
    } else {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      }
    }
  }
}
