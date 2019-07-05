package com.aurei.quanyi.module.web

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES
import android.text.TextUtils
import android.util.Log
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.xuexiang.xhttp2.XHttp
import com.yu.common.launche.LauncherHelper

/*
 * @author chenwei
 * @date 2018/1/25
 */
open class AppBaseWebViewClient : WebViewClient() {

  override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
    if (TextUtils.isEmpty(url)) {
      return true
    }
    if (url.startsWith("http://") || url.startsWith("https://")) {
      synCookie(view, url)
      view.loadUrl(url)
      return false
    } else {
      LauncherHelper.from(view.context)
          .startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
      return true
    }
  }


  private fun synCookie(webView: WebView, url: String) {
    if (Build.VERSION.SDK_INT < VERSION_CODES.LOLLIPOP) {
      CookieSyncManager.createInstance(webView.context)
    } else {
      CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
    }
    val cookieManager = CookieManager.getInstance()
    CookieManager.getInstance().setAcceptCookie(true)
    for (cookie in XHttp.getCookieJar().cookieStore.cookies) {
      Log.e(url, cookie.toString())
      cookieManager.setCookie(XHttp.getBaseUrl(), cookie.toString())
      if (Build.VERSION.SDK_INT > VERSION_CODES.LOLLIPOP) {
        cookieManager.flush()
      }
    }
  }


}