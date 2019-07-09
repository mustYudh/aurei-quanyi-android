package com.aurei.quanyi.http.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * @author yudneghao
 * @date 2019-07-05
 */
public class ActivityRxLifecycleCallbacks implements Application.ActivityLifecycleCallbacks{
  @Override
  public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    RxLifecycle.injectRxLifecycle(activity);
  }

  @Override
  public void onActivityStarted(Activity activity) {

  }

  @Override
  public void onActivityResumed(Activity activity) {

  }

  @Override
  public void onActivityPaused(Activity activity) {

  }

  @Override
  public void onActivityStopped(Activity activity) {

  }

  @Override
  public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

  }

  @Override
  public void onActivityDestroyed(Activity activity) {

  }
}

