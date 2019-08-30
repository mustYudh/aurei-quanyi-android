package com.aurei.quanyi.module.web

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import java.util.*

/**
 * @author yudneghao
 * @date 2019-08-23
 */
class Test : Activity() {
    var firstVisitWXH5PayUrl = false

    init {
        object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

                if (url.startsWith("weixin://")) {
                    try {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                        return true
                    } catch (e: Exception) {
                        // 防止手机没有安装处理某个 scheme 开头的 url 的 APP 导致 crash
                        //                        showToast("该手机没有安装微信");
                        return true
                    }

                } else if (url.startsWith("alipays://") || url.startsWith("alipay")) {
                    try {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                        return true
                    } catch (e: Exception) {
                        return true
                    }

                }


                if (!(url.startsWith("http") || url.startsWith("https"))) {
                    return true
                }

                if (url.contains("wx.tenpay.com")) {


                    val referer = "https://wx.tenpay.com"

                    // 兼容 Android 4.4.3 和 4.4.4 两个系统版本设置 referer 无效的问题
                    if ("4.4.3" == android.os.Build.VERSION.RELEASE || "4.4.4" == android.os.Build.VERSION.RELEASE) {
                        if (firstVisitWXH5PayUrl) {
                            view.loadDataWithBaseURL(
                                referer, "<script>window.location.href=\"$url\";</script>",
                                "text/html", "utf-8", null
                            )
                            firstVisitWXH5PayUrl = false
                        }
                        return false
                    } else {
                        val map = HashMap<String, String>(1)
                        map["Referer"] = referer
                        view.loadUrl(url, map)
                        return true
                    }
                }
                return false
            }
        }
    }
}
