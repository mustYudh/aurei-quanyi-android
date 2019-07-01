package com.qianchang.optimizetax.http.subscriber

import com.xuexiang.xhttp2.exception.ApiException
import com.xuexiang.xhttp2.subsciber.BaseSubscriber

abstract class TipRequestSubscriber<T> : BaseSubscriber<T>() {


  public override fun onError(e: ApiException) {
    showToast(e.displayMessage)
  }
}
