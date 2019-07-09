package com.aurei.quanyi.http.loading;

import android.app.Activity;
import android.support.annotation.NonNull;
import com.yu.common.loading.LoadingProgress;
import com.yu.common.loading.LoadingProgressFactory;

/**
 * @author yudneghao
 * @date 2018/8/9
 */

public class NetLoadingProgressFactory {
  private static LoadingProgressFactory.Producer producer;

  public static LoadingProgress createLoadingProgress(Activity activity) {
    LoadingProgress progress = producer == null ? null : producer.create(activity);
    if (progress == null) {
      return new NetLoadingProgressImpl(activity);
    } else {
      return progress;
    }
  }

  public static void registerProducer(LoadingProgressFactory.Producer producer) {
    NetLoadingProgressFactory.producer = producer;
  }

  public interface Producer {

    @NonNull
    LoadingProgress create(Activity activity);
  }
}
