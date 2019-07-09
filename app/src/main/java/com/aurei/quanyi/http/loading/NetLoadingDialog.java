package com.aurei.quanyi.http.loading;

import android.app.Activity;
import android.os.Build;
import com.yu.common.loading.LoadingProgress;
import com.yu.common.loading.RunnablePost;

/**
 * @author yudneghao
 * @date 2018/8/9
 */

public class NetLoadingDialog {
  private static LoadingProgress pDialog = null;

  private static int hashCode = 0;


  public static void showLoading(Activity activity, boolean isCancelable) {
    showLoading(activity, "", isCancelable);
  }

  private static boolean isActivityAttached(Activity activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      return activity != null && !activity.isFinishing() && !activity.isDestroyed();
    } else {
      return activity != null && !activity.isFinishing();
    }
  }

  private static void showLoading(final Activity activity, final String text,
      final boolean isCancelable) {
    RunnablePost.post(new Runnable() {
      @Override public void run() {
        if (!isActivityAttached(activity)) {
          return;
        }
        if (hashCode != activity.hashCode()) {
          destroyDialog();
        }
        if (null == pDialog) {
          pDialog = NetLoadingProgressFactory.createLoadingProgress(activity);
          hashCode = activity.hashCode();
        }
        pDialog.setCancelable(isCancelable);
        if (pDialog != null) {
          pDialog.showLoading(text);
        }
      }
    });
  }



  public static void dismissLoading() {
    destroyDialog();
  }

  private static void destroyDialog() {
    RunnablePost.post(new Runnable() {
      @Override public void run() {
        try {
          if (pDialog != null) {
            pDialog.dismissLoading();
            pDialog = null;
          }
          hashCode = 0;
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }
}
