package com.aurei.quanyi.http.lifecycle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author yudneghao
 * @date 2019-07-05
 */
public class LifecycleV4Fragment extends Fragment implements LifecycleManager {
  private final BehaviorSubject<ActivityLifecycle> mLifecycleSubject;

  public LifecycleV4Fragment() {
    mLifecycleSubject = BehaviorSubject.create();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    mLifecycleSubject.onNext(ActivityLifecycle.onCreate);
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onStart() {
    mLifecycleSubject.onNext(ActivityLifecycle.onStart);
    super.onStart();
  }

  @Override
  public void onResume() {
    mLifecycleSubject.onNext(ActivityLifecycle.onResume);
    super.onResume();
  }

  @Override
  public void onPause() {
    mLifecycleSubject.onNext(ActivityLifecycle.onPause);
    super.onPause();
  }

  @Override
  public void onStop() {
    mLifecycleSubject.onNext(ActivityLifecycle.onStop);
    super.onStop();
  }

  @Override
  public void onDestroy() {
    mLifecycleSubject.onNext(ActivityLifecycle.onDestroy);
    super.onDestroy();
  }

  @Override
  public Observable<ActivityLifecycle> getActivityLifecycle() {
    return mLifecycleSubject;
  }

  @Override
  public <T> LifecycleTransformer<T> bindToActivityLifecycle(final ActivityLifecycle activityLifecycle) {
    return new LifecycleTransformer<>(mLifecycleSubject, activityLifecycle);
  }

  @Override
  public <T> LifecycleTransformer<T> bindToLifecycle() {
    return new LifecycleTransformer<>(mLifecycleSubject);
  }

  @Override
  public <T> LifecycleTransformer<T> bindOnDestroy() {
    return bindToActivityLifecycle(ActivityLifecycle.onDestroy);
  }
}
