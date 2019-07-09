package com.aurei.quanyi.http.lifecycle;

import io.reactivex.Observable;

/**
 * @author yudneghao
 * @date 2019-07-05
 */
public interface LifecycleManager {

  /**
   * 获取Activity绑定的生命周期
   * @return
   */
  Observable<ActivityLifecycle> getActivityLifecycle();

  /**
   * 绑定到特定的Activity生命周期进行订阅注销
   * @param activityLifecycle
   * @param <T>
   * @return
   */
  <T> LifecycleTransformer<T> bindToActivityLifecycle(ActivityLifecycle activityLifecycle);

  /**
   * 自动绑定Activity生命周期进行订阅注销
   * @param <T>
   * @return
   */
  <T> LifecycleTransformer<T> bindToLifecycle();

  /**
   * 绑定到Activity的OnDestroy进行订阅注销
   * @param <T>
   * @return
   */
  <T> LifecycleTransformer<T> bindOnDestroy();

}
