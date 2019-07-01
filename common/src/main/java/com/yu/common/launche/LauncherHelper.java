package com.yu.common.launche;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;


public class LauncherHelper {

  private LauncherContext context;

  private List<Integer> flags = new ArrayList<>();

  private LauncherHelper(LauncherContext launcherContext) {
    this.context = launcherContext;
  }

  public static LauncherHelper from(Context context) {
    return new LauncherHelper(new LauncherContext(context));
  }

  public static LauncherHelper from(Fragment fragment) {
    return new LauncherHelper(new LauncherContext(fragment));
  }


  public LauncherHelper addFlag(int flag) {
    if (!flags.contains(flag)) {
      flags.add(flag);
    }
    return this;
  }

  public void startActivity(Class<? extends Activity> clazz) {
    startActivity(clazz, (Bundle) null);
  }

  public void startActivity(Class<? extends Activity> clazz, Bundle bundle) {
    Intent intent = new Intent(context.getContext(), clazz);
    if (bundle != null) {
      intent.putExtras(bundle);
    }
    startActivity(intent);
  }

  public void startActivity(Intent intent) {
    if (flags != null && flags.size() > 0) {
      for (Integer flag : flags) {
        intent.addFlags(flag);
      }
    }
    if (checkIntentAvailable(context.getContext(), intent)) {
      try {
        context.startActivity(intent);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void startActivityForResult(Class<? extends Activity> clazz, int forResult) {
    startActivityForResult(clazz, null, forResult);
  }

  public void startActivityForResult(Class<? extends Activity> clazz, Bundle bundle,
      int forResult) {
    Intent intent = new Intent(context.getContext(), clazz);
    if (bundle != null) {
      intent.putExtras(bundle);
    }
    startActivityForResult(intent, forResult);
  }

  public void startActivityForResult(Intent intent, int forResult) {
    if (flags != null && flags.size() > 0) {
      for (Integer flag : flags) {
        intent.addFlags(flag);
      }
    }
    if (checkIntentAvailable(context.getContext(), intent)) {
      try {
        context.startActivityForResult(intent, forResult);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static boolean checkIntentAvailable(Context context, Intent intent) {
    return intent != null && intent.resolveActivity(context.getPackageManager()) != null;
  }
}