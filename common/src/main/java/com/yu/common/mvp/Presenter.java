package com.yu.common.mvp;

import android.content.Intent;
import android.view.View;

public interface Presenter {

  int priority();

  void createdView(View view);

  void start();

  void resume();

  void pause();

  void stop();

  void willDestroy();

  void destroy();

  void newIntent(Intent intent);

  void activityResult(int requestCode, int resultCode, Intent data);
}
