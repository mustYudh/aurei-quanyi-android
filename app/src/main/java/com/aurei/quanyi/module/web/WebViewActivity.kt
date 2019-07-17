package com.aurei.quanyi.module.web

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import com.aurei.quanyi.R
import com.aurei.quanyi.base.BaseActivity
import com.aurei.quanyi.module.web.bea.UploadInfo
import com.aurei.quanyi.module.web.js.WebJs
import com.aurei.quanyi.module.web.presenter.WebViewPresenter
import com.aurei.quanyi.module.web.presenter.WebViewViewer
import com.aurei.quanyi.utils.*
import com.google.gson.Gson
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.xuexiang.xhttp2.XHttp
import com.yu.common.mvp.PresenterLifeCycle
import com.yu.common.navigation.StatusBarUtils
import com.yu.common.web.ProgressWebChromeClient
import com.yu.common.web.ProgressWebViewLayout
import kotlinx.android.synthetic.main.activity_common_webview.*
import java.io.File


/**
 * @author yudneghao
 */
class WebViewActivity : BaseActivity(), WebViewViewer {


    private var webView: WebView? = null
    private var webJs: WebJs? = null
    @PresenterLifeCycle
    private val mPresenter = WebViewPresenter(this)


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
        webView!!.webViewClient = object : WebViewClient() {

            override fun onLoadResource(view: WebView, url: String) {
                super.onLoadResource(view, filtrationUrl(url,activity!!))
            }
        }
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
        webView!!.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress >= 100) {
                    splash_bg.visibility = View.GONE
                    val res = resources
                    val drawable = res.getDrawable(R.drawable.bkcolor)
                    window.setBackgroundDrawable(drawable)
                }
                super.onProgressChanged(view, newProgress)
            }
        }

        webView!!.setWebViewClient(object : WebViewClient() {
            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
            }
        })
    }


    fun getData() {

    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView != null && webView!!.canGoBack()) {
//                webView!!.goBack()
                return true
        }  else {
            return super.onKeyDown(keyCode, event)
        }


    }

    override fun loadData() {
//        val url = intent.getStringExtra(WEB_URL)
        val url = "${getBaseUrl()}/index?${getParams(activity)}"
        synCookie(url)
        webView!!.loadUrl(url)
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
        /**
         * @param url url
         */
        fun callIntent(context: Context, url: String): Intent {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(WEB_URL, url)
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
        showToast("上传成功")
        webView?.loadUrl("javascript:getPhotoSuccess(${Gson().toJson(url)})")
    }
    private val pressHandle = PressHandle(this)



    override fun onBackPressed() {
        if(webView!!.canGoBack()) {
            super.onBackPressed()
        } else {
            if (!pressHandle.handlePress(KeyEvent.KEYCODE_BACK)) {
                super.onBackPressed()
            }
        }

    }



}
