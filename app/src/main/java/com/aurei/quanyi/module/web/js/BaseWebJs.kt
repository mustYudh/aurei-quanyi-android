package com.aurei.quanyi.module.web.js

import android.app.Activity
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.webkit.WebView
import java.io.Serializable

/**
 * @author yudenghao
 */
open class BaseWebJs(activity: Activity, val webView: WebView) : Serializable {
  var activity: Activity? = null
    private set

  private val mHandler = Handler(Looper.getMainLooper())

  val isDestroy: Boolean
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      activity == null || activity!!.isFinishing || activity!!.isDestroyed
    } else {
      activity == null || activity!!.isFinishing
    }

  init {
    this.activity = activity
  }

  @JvmOverloads
  fun runOnUiThread(runnable: Runnable?, delay: Int = 0) {
    if (runnable != null && !isDestroy) {
      mHandler.postDelayed({
        if (!isDestroy) {
          runnable.run()
        }
      }, delay.toLong())
    }
  }

  fun finish() {
    if (activity != null) {
      activity!!.finish()
    }
  }

  fun destroy() {
    mHandler.removeCallbacksAndMessages(null)
    activity = null
  }
}
