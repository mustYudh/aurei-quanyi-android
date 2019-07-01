package com.yu.common.loading;

public interface LoadingProgress {

  void showLoading(String message);

  void dismissLoading();

  void setCancelable(boolean cancelable);
}
