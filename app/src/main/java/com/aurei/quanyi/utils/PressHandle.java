package com.aurei.quanyi.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import com.yu.common.toast.ToastUtils;

import java.lang.ref.SoftReference;


/**
 * @author yudenghao
 */
public class PressHandle {

  private SoftReference<Activity> activity;

  private boolean isPress = false;

  public PressHandle(Activity activity) {
    this.activity = new SoftReference<>(activity);
  }

  public boolean handlePress(int keyCode) {
    if (keyCode == KeyEvent.KEYCODE_BACK && activity.get() != null) {
      if (!isPress) {
        isPress = true;
        ToastUtils.show(activity.get(),"再按一次退出");
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
          @Override public void run() {
            isPress = false;
          }
        }, 1500);
        return true;
      } else {
        isPress = false;
        return false;
      }
    }
    return false;
  }
}
