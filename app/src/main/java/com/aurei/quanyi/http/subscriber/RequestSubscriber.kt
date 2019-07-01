package com.qianchang.optimizetax.http.subscriber

import android.app.Activity
import android.util.Log
import com.xuexiang.xhttp2.exception.ApiException
import com.xuexiang.xhttp2.subsciber.BaseSubscriber

open abstract class RequestSubscriber<T> : BaseSubscriber<T> {
  private var activity: Activity? = null
  private var cancelDialog: Boolean? = true


  constructor(activity: Activity, cancelDialog: Boolean? = true) {
    this.activity = activity
    this.cancelDialog = cancelDialog
  }

  override fun onStart() {
    if (activity != null) {
      NetLoadingDialog.showLoading(activity, cancelDialog!!)
    }
    Log.e("====>","开始")
    super.onStart()
  }

  override fun onComplete() {
    Log.e("====>","终止")
    NetLoadingDialog.dismissLoading()
    super.onComplete()
  }

  override fun onError(apiException: ApiException) {
    NetLoadingDialog.dismissLoading()
    Log.e("====>","异常")
    showToast(apiException.displayMessage)
    super.onError(apiException)
  }

  override fun onSuccess(t: T) {
    Log.e("====>","成功")
    NetLoadingDialog.dismissLoading()
    onSuccessData(t)
  }

  abstract fun onSuccessData(t: T?)
}
