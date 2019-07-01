package com.yu.common.framework;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.yu.common.mvp.Presenter;
import com.yu.common.mvp.PresenterInjector;
import com.yu.common.mvp.PresenterSetter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class AbstractPresenterActivity extends AbstractSupportActivity implements PresenterSetter {

  private List<Presenter> presenters = new ArrayList<>();

  private Comparator<Presenter> presenterComparator = new Comparator<Presenter>() {
    @Override public int compare(Presenter o1, Presenter o2) {
      return o2.priority() - o1.priority();
    }
  };


  @Override public void addPresenter(Presenter presenter) {
    if (presenter != null && !presenters.contains(presenter)) {
      presenters.add(presenter);
      Collections.sort(presenters, presenterComparator);
    }
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    for (int i = 0; i < presenters.size(); i++) {
      presenters.get(i).newIntent(intent);
    }
  }

  @Override protected void onViewCreated(View view) {
    PresenterInjector.injectPresenter(this);
    for (int i = 0; i < presenters.size(); i++) {
      presenters.get(i).createdView(view);
    }
  }

  @Override protected void onStart() {
    super.onStart();
    for (int i = 0; i < presenters.size(); i++) {
      presenters.get(i).start();
    }
  }

  @Override protected void onResume() {
    super.onResume();
    for (int i = 0; i < presenters.size(); i++) {
      presenters.get(i).resume();
    }
  }

  @Override protected void onPause() {
    super.onPause();
    for (int i = 0; i < presenters.size(); i++) {
      presenters.get(i).pause();
    }
  }

  @Override protected void onStop() {
    super.onStop();
    for (int i = 0; i < presenters.size(); i++) {
      presenters.get(i).stop();
    }
  }

  @Override protected void onDestroy() {
    for (int i = 0; i < presenters.size(); i++) {
      presenters.get(i).willDestroy();
    }
    super.onDestroy();
    for (int i = 0; i < presenters.size(); i++) {
      presenters.get(i).destroy();
    }
    presenters.clear();
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    for (int i = 0; i < presenters.size(); i++) {
      presenters.get(i).activityResult(requestCode, resultCode, data);
    }
  }
}
