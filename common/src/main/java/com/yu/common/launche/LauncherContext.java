package com.yu.common.launche;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

public class LauncherContext {

  private Object context;

  public LauncherContext(Context context) {
    this.context = context;
  }

  public LauncherContext(Fragment context) {
    this.context = context;
  }

  public void startActivity(Intent intent) {
    Context ctx = getContext();
    if (ctx instanceof Activity) {
      ctx.startActivity(intent);
    } else if (ctx != null) {
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      ctx.startActivity(intent);
    } else {
      Log.e("LauncherContext", "context is null,can not startActivity");
    }
  }

  public void startActivityForResult(Intent intent, int forResult) {
    if (context instanceof Activity) {
      ((Activity) context).startActivityForResult(intent, forResult);
    } else if (context instanceof Fragment) {
      ((Fragment) context).startActivityForResult(intent, forResult);
    } else {
      Log.e("LauncherContext", "context is null,can not startActivityForResult");
    }
  }

  public Context getContext() {
    Context ctx = null;
    if (context instanceof Fragment) {
      ctx = ((Fragment) context).getActivity();
    } else if (context instanceof Context) {
      ctx = (Context) context;
    }
    return ctx;
  }
}