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
import com.aurei.quanyi.module.web.js.WebJs
import com.aurei.quanyi.module.web.presenter.WebViewPresenter
import com.aurei.quanyi.module.web.presenter.WebViewViewer
import com.aurei.quanyi.utils.getBaseUrl
import com.aurei.quanyi.utils.getParams
import com.aurei.quanyi.utils.showToast
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


    override fun setView(savedInstanceState: Bundle?) {
        setContentView(R.layout.common_web_view_activity)
        val webViewLayout = bindView<ProgressWebViewLayout>(R.id.webViewLayout)
        webView = webViewLayout.webView
        webViewLayout.progressBar.visibility = View.GONE
        webView!!.setDownloadListener(WebViewDownLoadListener(activity))
        webView!!.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView, text: String) {
                title = if (!TextUtils.isEmpty(view.title)) {
                    view.title
                } else {
                    intent.getStringExtra(TITLE)
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

        webView!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                val uri = Uri.parse(url)
                val scheme = uri.getScheme()
                if (TextUtils.isEmpty(scheme)) {
                    return true
                }
                if (scheme == "http" || scheme == "https") {
                    //处理http协议
                    return super.shouldOverrideUrlLoading(view, url)
                } else {
                    val intent = Intent(Intent.ACTION_VIEW)
                    try {
                        intent.data = Uri.parse(url)
                        startActivity(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    return true
                }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                loading.visibility = View.GONE
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                loading.visibility = View.GONE
                super.onReceivedError(view, request, error)
            }
        }
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
        val url = if (TextUtils.isEmpty(intentUrl)) "${getBaseUrl()}/index?${getParams(activity)}" else intentUrl
        Log.e("======>", "H5加载的url$url")
        synCookie(url)
        webView?.postDelayed({
            webView!!.loadUrl(url)
        }, 1000)
        val permiss = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (Build.VERSION.SDK_INT >= 23) {
            val rxPermissions = RxPermissions(this)
            rxPermissions.request(*permiss).subscribe {}
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
        showToast("上传成功")
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

}
