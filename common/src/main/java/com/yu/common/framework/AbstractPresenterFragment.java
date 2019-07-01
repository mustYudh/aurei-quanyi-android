package com.yu.common.framework;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.yu.common.mvp.Presenter;
import com.yu.common.mvp.PresenterInjector;
import com.yu.common.mvp.PresenterSetter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class AbstractPresenterFragment extends AbstractSupportFragment implements PresenterSetter {

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

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    PresenterInjector.injectPresenter(this);
    for (int i = 0; i < presenters.size(); i++) {
      presenters.get(i).createdView(getView());
    }
  }

  @Override public void onStart() {
    super.onStart();
    for (int i = 0; i < presenters.size(); i++) {
      presenters.get(i).start();
    }
  }

  @Override public void onResume() {
    super.onResume();
    for (int i = 0; i < presenters.size(); i++) {
      presenters.get(i).resume();
    }
  }

  @Override public void onPause() {
    super.onPause();
    for (int i = 0; i < presenters.size(); i++) {
      presenters.get(i).pause();
    }
  }

  @Override public void onStop() {
    super.onStop();
    for (int i = 0; i < presenters.size(); i++) {
      presenters.get(i).stop();
    }
  }

  @Override public void onDestroy() {
    for (int i = 0; i < presenters.size(); i++) {
      presenters.get(i).willDestroy();
    }
    super.onDestroy();
    for (int i = 0; i < presenters.size(); i++) {
      presenters.get(i).destroy();
    }
    presenters.clear();
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    for (int i = 0; i < presenters.size(); i++) {
      presenters.get(i).activityResult(requestCode, resultCode, data);
    }
  }
}
