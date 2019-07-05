package com.aurei.quanyi.module.web

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebView
import com.aurei.quanyi.R
import com.aurei.quanyi.base.BaseActivity
import com.aurei.quanyi.module.web.js.WebJs
import com.yu.common.web.ProgressWebChromeClient
import com.yu.common.web.ProgressWebViewLayout

/**
 * @author yudneghao
 */
class WebViewActivity : BaseActivity() {
  private var webView: WebView? = null
  private var webJs: WebJs? = null





  override fun setView(savedInstanceState: Bundle?) {
    setContentView(R.layout.activity_common_webview)
    val webViewLayout = bindView<ProgressWebViewLayout>(R.id.webViewLayout)
    webView = webViewLayout.webView
    webView!!.setDownloadListener(WebViewDownLoadListener(activity))
    webView!!.webChromeClient = object : ProgressWebChromeClient(webViewLayout.progressBar) {
      override fun onReceivedTitle(view: WebView, title: String) {
        super.onReceivedTitle(view, title)
      }
    }
    webView!!.webViewClient = object : AppBaseWebViewClient() {
      override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)
      }
    }
    initJs()
  }

  @SuppressLint("JavascriptInterface", "AddJavascriptInterface")
  private fun initJs() {
    webJs = WebJs(this, webView!!)
    webView!!.addJavascriptInterface(webJs, "android")
  }



  override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      if (webView != null && webView!!.canGoBack()) {
        webView!!.goBack()
        return true
      }
    }
    return super.onKeyDown(keyCode, event)
  }

  override fun loadData() {
    val url = intent.getStringExtra(WEB_URL)
    webView!!.loadUrl(url)

  }




  override fun onDestroy() {
    if (webJs != null) {
      webJs!!.destroy()
    }
    if (webView != null) {
      webView!!.removeJavascriptInterface("android")
      webView!!.webViewClient = null
      webView!!.webChromeClient = null
      webView!!.removeAllViews()
      webView!!.destroy()
    }
    super.onDestroy()
  }

  companion object {
    val WEB_URL = "webUrl"
    /**
     * @param url url
     */
    fun callIntent(context: Context, url: String): Intent {
      val intent = Intent(context, WebViewActivity::class.java)
      intent.putExtra(WEB_URL, url)
      return intent
    }
  }




}
