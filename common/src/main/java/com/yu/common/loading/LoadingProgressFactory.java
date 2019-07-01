package com.yu.common.loading;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;


public class LoadingProgressFactory {

  private static Producer producer;

  public static LoadingProgress createLoadingProgress(Activity activity,View contentView) {
    LoadingProgress progress = producer == null ? null : producer.create(activity);
    if (progress == null) {
      return new LoadingProgressImpl(activity,contentView);
    } else {
      return progress;
    }
  }

  public static void registerProducer(Producer producer) {
    LoadingProgressFactory.producer = producer;
  }

  public interface Producer {

    @NonNull LoadingProgress create(Activity activity);
  }
}
