package com.aurei.quanyi.module.web

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import com.aurei.quanyi.R
import com.aurei.quanyi.base.BaseBarActivity
import com.aurei.quanyi.module.web.bea.UploadInfo
import com.aurei.quanyi.module.web.bea.UploadStatus.uploadSuccess
import com.aurei.quanyi.module.web.js.WebJs
import com.aurei.quanyi.module.web.presenter.WebViewPresenter
import com.aurei.quanyi.module.web.presenter.WebViewViewer
import com.google.gson.Gson
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.tbruyelle.rxpermissions2.RxPermissions
import com.xuexiang.xhttp2.XHttp
import com.yu.common.mvp.PresenterLifeCycle
import com.yu.common.navigation.StatusBarUtils
import com.yu.common.ui.BarIconContainer
import com.yu.common.web.ProgressWebViewLayout
import kotlinx.android.synthetic.main.common_web_view_activity.*
import kotlinx.android.synthetic.main.main_web_view_activity.webViewLayout
import java.io.File


/**
 * @author yudneghao
 */
class CommonWebViewActivity : BaseBarActivity(), WebViewViewer {


    private var webView: WebView? = null
    private var webJs: WebJs? = null
    @PresenterLifeCycle
    private val mPresenter = WebViewPresenter(this)
    private var isPay = false
    val extraHeaders = HashMap<String, String>()
    private var firstVisitWXH5PayUrl = true

    override fun setView(savedInstanceState: Bundle?) {
        setContentView(R.layout.common_web_view_activity)
        val webViewLayout = bindView<ProgressWebViewLayout>(R.id.webViewLayout)
        webView = webViewLayout.webView
        webViewLayout.progressBar.visibility = View.GONE
        webView!!.setDownloadListener(WebViewDownLoadListener(activity))
        webView!!.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView, text: String) {
                title = if (!TextUtils.isEmpty(intent.getStringExtra(TITLE))) {
                    intent.getStringExtra(TITLE)
                } else {
                    if (view.title != null && view.title == "weixin") {
                        "支付"
                    } else {
                        view.title
                    }

                }
                super.onReceivedTitle(view, text)
            }
        }
        initLeftClose()
        initJs()
        webJs?.setOnFixStatusBarListener(object : WebJs.FixStatusBarListener {
            override fun fix(need: Boolean) {
                fixStatusBar(need)
            }

        })
    }


    fun fixStatusBar(fix: Boolean) {
        val params = webViewLayout.emptyView().layoutParams
        params.height = StatusBarUtils.getStatusBarHeight(activity)
        webViewLayout.emptyView().layoutParams = params
        webViewLayout.emptyView().visibility = if (fix) View.VISIBLE else View.GONE
    }

    @SuppressLint("JavascriptInterface", "AddJavascriptInterface")
    private fun initJs() {
        webJs = WebJs(this, webView!!)
        webView!!.addJavascriptInterface(webJs, "android")
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView != null && webView!!.canGoBack()) {
            webView!!.goBack()
            return true
        } else {
            return super.onKeyDown(keyCode, event)
        }


    }


    @SuppressLint("CheckResult")
    override fun loadData() {
        val intentUrl = intent.getStringExtra(WEB_URL)
        Log.e("======>", "H5加载的url$intentUrl")
        synCookie(intentUrl)
        webView!!.loadUrl(intentUrl)
        val permiss = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (Build.VERSION.SDK_INT >= 23) {
            val rxPermissions = RxPermissions(this)
            rxPermissions.request(*permiss).subscribe {}
        }
        webView!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                try {
                    if (url.startsWith("weixin://wap/pay?") // 微信
                        || url.startsWith("alipays://") // 支付宝
                        || url.startsWith("mailto://")
                        || url.startsWith("tel:")
                        || url.startsWith("dianping://")
                    ) {
                        title = "支付"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                        isPay = true
                        Log.e("======>重定向1",url)
                        return true
                    } else if (url.startsWith("https://wx.tenpay.com")) {
                        title = "支付"
                        if (firstVisitWXH5PayUrl) {
                            extraHeaders.clear()
                            extraHeaders["Referer"] = "http://m.aurei.com.cn"
                            view.loadUrl(url, extraHeaders)
                            firstVisitWXH5PayUrl = false
                        }
                        return super.shouldOverrideUrlLoading(view,url)
                    }
                } catch (e: Exception) {
                    return true
                }
                view.loadUrl(url)
                return true
            }


            //            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    view?.loadUrl(request?.getUrl().toString())
//                } else {
//                    view?.loadUrl(request.toString())
//                }
//                return true
//            }
//
            override fun onPageFinished(view: WebView?, url: String?) {
                loading.visibility = View.GONE
                bindView<BarIconContainer>(R.id.close, webView != null && webView?.canGoBack()!!)
                super.onPageFinished(view, url)
            }

            //
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                loading.visibility = View.GONE
                super.onReceivedError(view, request, error)
            }
        }
    }

    private fun synCookie(url: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(this)
        } else {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
        }
        CookieManager.getInstance().setAcceptCookie(true)
        val cookieManager = CookieManager.getInstance()
        for (cookie in XHttp.getCookieJar().cookieStore.cookies) {
            Log.e(url, cookie.toString())
            cookieManager.setCookie(XHttp.getBaseUrl(), cookie.toString())
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                cookieManager.flush()
            }
        }
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
        val TITLE = "title"
        /**
         * @param url url
         */
        fun callIntent(context: Context, url: String, title: String): Intent {
            val intent = Intent(context, CommonWebViewActivity::class.java)
            Log.e("======>url-intent", url)
            intent.putExtra(WEB_URL, url)
            intent.putExtra(TITLE, title)
            return intent
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                val selectList = PictureSelector.obtainMultipleResult(data)
                if (selectList.size > 0 && !TextUtils.isEmpty(selectList[0].compressPath)) {
                    mPresenter.uploadImage(File(selectList[0].compressPath))
                }
            }
        }
    }

    override fun uploadImageSuccess(url: UploadInfo?) {
        uploadSuccess = true
        webView?.loadUrl("javascript:getPhotoSuccess(${Gson().toJson(url)})")
    }


    private fun initLeftClose() {
        bindView<BarIconContainer>(R.id.close).setOnClickListener {
            onBackPressed()
        }
        bindView<BarIconContainer>(R.id.action_back).setOnClickListener {
            if (webView != null && webView!!.canGoBack()) {
                webView!!.goBack()
                bindView<BarIconContainer>(R.id.close, webView != null && webView!!.canGoBack())
            } else {
                onBackPressed()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (uploadSuccess) {
            webView?.reload()
            uploadSuccess = false
        }

    }

}
