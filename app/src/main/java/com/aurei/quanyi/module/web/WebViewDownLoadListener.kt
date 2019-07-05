package com.aurei.quanyi.module.web

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.DownloadListener
import com.yu.common.launche.LauncherHelper

/**
 * 默认调用系统的下载
 */
class WebViewDownLoadListener(private val context: Context) : DownloadListener {

  override fun onDownloadStart(url: String, userAgent: String, contentDisposition: String,
      type: String,
      contentLength: Long) {
    LauncherHelper.from(context).startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
  }
}