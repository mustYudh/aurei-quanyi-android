package com.aurei.quanyi.http.loading;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import com.aurei.quanyi.R;
import com.yu.common.loading.LoadingProgress;

/**
 * @author yudneghao
 * @date 2018/8/9
 */

public class NetLoadingProgressImpl extends Dialog implements LoadingProgress {
  private Activity activity;

  NetLoadingProgressImpl(@NonNull Context context) {
    super(context, R.style.common_ui_loading_progress);
    setContentView(R.layout.dialog_net_loading_layout);
    initContext(context);
  }

  private void initContext(Context context) {
    activity = (Activity) context;
  }

  private boolean isActivityAttached() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      return activity != null
          && !activity.isFinishing()
          && !activity.isDestroyed()
          && getWindow() != null;
    } else {
      return activity != null && !activity.isFinishing() && getWindow() != null;
    }
  }

  @Override public void showLoading(String message) {
    if (isActivityAttached()) {
      if (!isShowing()) {
        super.show();
      }
    }
  }

  @Override public void dismissLoading() {
    if (isActivityAttached()) {
      if (isShowing()) {
        super.dismiss();
      }
    }
  }
}
