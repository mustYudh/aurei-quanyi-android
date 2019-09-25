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
import com.aurei.quanyi.base.BaseActivity
import com.aurei.quanyi.module.web.bea.UploadInfo
import com.aurei.quanyi.module.web.bea.UploadStatus.uploadSuccess
import com.aurei.quanyi.module.web.js.WebJs
import com.aurei.quanyi.module.web.presenter.WebViewPresenter
import com.aurei.quanyi.module.web.presenter.WebViewViewer
import com.aurei.quanyi.utils.BaiDuStartWebBean
import com.aurei.quanyi.utils.PressHandle
import com.aurei.quanyi.utils.getBaseUrl
import com.aurei.quanyi.utils.getParams
import com.baidu.mobstat.SendStrategyEnum
import com.baidu.mobstat.StatService
import com.google.gson.Gson
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.tbruyelle.rxpermissions2.RxPermissions
import com.xuexiang.xhttp2.XHttp
import com.yu.common.mvp.PresenterLifeCycle
import com.yu.common.navigation.StatusBarUtils
import com.yu.common.web.ProgressWebChromeClient
import com.yu.common.web.ProgressWebViewLayout
import kotlinx.android.synthetic.main.main_web_view_activity.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File


/**
 * @author yudneghao
 */
class MainWebViewActivity : BaseActivity(), WebViewViewer {


    private var webView: WebView? = null
    private var webJs: WebJs? = null
    @PresenterLifeCycle
    private val mPresenter = WebViewPresenter(this)


    override fun setView(savedInstanceState: Bundle?) {
        setContentView(R.layout.main_web_view_activity)
        val webViewLayout = bindView<ProgressWebViewLayout>(R.id.webViewLayout)
        webView = webViewLayout.webView
        webView!!.setDownloadListener(WebViewDownLoadListener(activity))
        webView!!.webChromeClient = object : ProgressWebChromeClient(webViewLayout.progressBar) {
            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                if (!TextUtils.isEmpty(view.title)) {
                    setTitle(view.title)
                }
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
        webView!!.webChromeClient = object : ProgressWebChromeClient(webViewLayout.progressBar) {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress >= 100) {
                    splash_bg.visibility = View.GONE
                    val res = resources
                    val drawable = res.getDrawable(R.drawable.bkcolor)
                    window.setBackgroundDrawable(drawable)
                }
            }
        }
        val webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                try {
                    if (url.startsWith("weixin://wap/pay?") // 微信
                        || url.startsWith("alipays://") // 支付宝
                        || url.startsWith("mailto://") // 邮件
                        || url.startsWith("tel:")// 电话
                        || url.startsWith("dianping://")// 大众点评
// 其他自定义的scheme
                    ) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                        return true
                    } else if (url.startsWith("https://wx.tenpay.com")) {
                        val extraHeaders = HashMap<String, String>()
                        extraHeaders["Referer"] = "http://m.aurei.com.cn"
                        view.loadUrl(url, extraHeaders)
                        return true
                    } else if (url.startsWith("bmtj:")) {
                        val data = url.split("bmtj:")
                        if (data.size >= 2) {
                            val result: BaiDuStartWebBean =
                                Gson().fromJson(data[1], BaiDuStartWebBean::class.java)
                            if (!TextUtils.isEmpty(result.action) && result.action!! == "onEventWithAttributes") {
                                StatService.onEvent(
                                    activity,
                                    result.obj?.event_id,
                                    if (result.obj?.label == null) null else result.obj?.label.toString()
                                )
                            }
                        }
                        return super.shouldOverrideUrlLoading(view, url)
                    }
                } catch (e: Exception) {
                    return true
                }
                view.loadUrl(url)
                return true
            }


            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view?.loadUrl(request?.getUrl().toString())
                } else {
                    view?.loadUrl(request.toString())
                }
                return true
            }

        }
        webView!!.webViewClient = webViewClient
        StatService.bindJSInterface(this, webView, webViewClient)
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
        initBaiDu()
        EventBus.getDefault().register(this)
        val showBg = intent.getBooleanExtra(SHOW_BG, true)
        splash_bg.visibility = if (showBg) View.VISIBLE else View.GONE
        val intentUrl = intent.getStringExtra(WEB_URL)
        val url =
            if (TextUtils.isEmpty(intentUrl)) "${getBaseUrl()}/index?${getParams(activity)}" else intentUrl
        Log.e("======>", "H5加载的url$url")
        synCookie(url)
//        webView?.postDelayed({
        webView!!.loadUrl(url)
//        }, 1000)
        val permiss = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (Build.VERSION.SDK_INT >= 23) {
            val rxPermissions = RxPermissions(this)
            rxPermissions.request(*permiss).subscribe {}
        }
        webJs?.setOnGetTitleListener(object : WebJs.GetTitleListener {
            override fun setTitle(title: String) {
//                setTitle(title)
            }
        })
    }

    private fun initBaiDu() {
        StatService.setDebugOn(true)
        StatService.setSendLogStrategy(this, SendStrategyEnum.APP_START, 1, false)
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
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    companion object {
        val WEB_URL = "webUrl"
        val SHOW_BG = "show_bg"
        /**
         * @param url url
         */
        fun callIntent(context: Context, url: String, show: Boolean): Intent {
            val intent = Intent(context, MainWebViewActivity::class.java)
            intent.putExtra(WEB_URL, url)
            intent.putExtra(SHOW_BG, show)
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
        } else if (requestCode == 1000 && resultCode == 1) {
            if (!TextUtils.isEmpty(data?.getStringExtra("result")) && data?.getStringExtra("result").equals(
                    "支付成功"
                )
            ) {
                activity?.runOnUiThread {
                    webView?.loadUrl("javascript:onPaySuccess(3)")
                }
            } else {
                activity?.runOnUiThread {
                    webView?.loadUrl("javascript:onPayFailed(3)")
                }
            }
        }
    }

    override fun uploadImageSuccess(url: UploadInfo?) {
//        showToast("上传成功")
        uploadSuccess = true
        webView?.loadUrl("javascript:getPhotoSuccess(${Gson().toJson(url)})")
    }

    private val pressHandle = PressHandle(this)


    override fun onBackPressed() {
        if (webView!!.canGoBack()) {
            super.onBackPressed()
        } else {
            if (!pressHandle.handlePress(KeyEvent.KEYCODE_BACK)) {
                super.onBackPressed()
            }
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: String) {
        if (!TextUtils.isEmpty(event)) {
            webView?.loadUrl(event)
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
