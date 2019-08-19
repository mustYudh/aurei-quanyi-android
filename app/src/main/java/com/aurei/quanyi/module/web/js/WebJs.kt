package com.aurei.quanyi.module.web.js

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.aurei.quanyi.base.BaseActivity
import com.aurei.quanyi.module.login.LoginActivity
import com.aurei.quanyi.module.web.CommonWebViewActivity
import com.aurei.quanyi.module.web.MainWebViewActivity
import com.aurei.quanyi.utils.filtrationUrl
import com.aurei.quanyi.utils.getBaseUrl
import com.aurei.quanyi.utils.getParams
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.qianchang.optimizetax.data.UserProfile
import com.tbruyelle.rxpermissions2.RxPermissions
import com.yu.common.launche.LauncherHelper

/**
 * @author chenwei
 * @date 2017/8/30
 */
class WebJs(activity: BaseActivity, webView: WebView) : BaseWebJs(activity, webView) {



    //退出登录
    @JavascriptInterface
    fun logout() {
        Log.e("======>bridge-logout", "触发")
        UserProfile.clean()
        LauncherHelper.from(activity).startActivity(LoginActivity.getIntent(activity!!, {
            LauncherHelper.from(activity).startActivity(
                MainWebViewActivity.callIntent(
                    activity!!,
                    filtrationUrl("${getBaseUrl()}/person?${getParams(activity!!)}", activity!!),
                    false
                )
            )
        }, {
            LauncherHelper.from(activity).startActivity(
                MainWebViewActivity.callIntent(
                    activity!!,
                    filtrationUrl("${getBaseUrl()}/index?${getParams(activity!!)}", activity!!),
                    false
                )
            )
            activity?.finish()

        }))
    }


    @JavascriptInterface
    fun login(url: String, isCenter: Boolean) {
        LauncherHelper.from(activity).startActivity(LoginActivity.getIntent(activity!!, {
            if (url == "/msgList") {
                LauncherHelper.from(activity).startActivity(
                    CommonWebViewActivity.callIntent(
                        activity!!, filtrationUrl("${getBaseUrl()}$url", activity!!),
                        ""
                    )
                )
            } else {
                if (activity is CommonWebViewActivity) {
                    LauncherHelper.from(activity).startActivity(
                        CommonWebViewActivity.callIntent(
                            activity!!, filtrationUrl("${getBaseUrl()}$url", activity!!),
                            ""
                        )
                    )
                } else if (activity is MainWebViewActivity) {
                    LauncherHelper.from(activity).startActivity(
                        MainWebViewActivity.callIntent(
                            activity!!,
                            filtrationUrl("${getBaseUrl()}$url", activity!!),
                            false
                        )
                    )
                }
                activity?.finish()

            }
        }, {
            webView.clearCache(true)
            if (isCenter) {
                webView.loadUrl("${getBaseUrl()}/index")
            } else {
                if (url == "/msgList") {
                    webView.loadUrl("${getBaseUrl()}/index")
                } else {
                    webView.loadUrl(filtrationUrl("${getBaseUrl()}$url${getParams(activity!!)}", activity!!))
                }

            }
        }))
    }

    // //返回 delta 返回的页面数，如果 delta 大于现有页面数，则返回到首页
    @JavascriptInterface
    fun goBack(delta: Int) {
        activity?.finish()
    }

    //拍照
    @SuppressLint("CheckResult")
    @JavascriptInterface
    fun getPhoto(callBack: String) {
        runOnUiThread(Runnable {
            getPermission {
                PictureSelector.create(activity)
                    .openCamera(PictureMimeType.ofImage())
                    .enableCrop(true)
                    .compress(true)
                    .forResult(PictureConfig.CHOOSE_REQUEST)
            }
        })
    }

    //相册选择照片
    @JavascriptInterface
    fun selectPhoto(callBack: String) {
        runOnUiThread(Runnable {
            getPermission {
                PictureSelector.create(activity)
                    .openGallery(PictureMimeType.ofImage())
//            .theme(R.style.PictureSelector)
                    .maxSelectNum(1)
                    .imageSpanCount(4)
                    .previewImage(true)
                    .isCamera(false)
                    .imageFormat(PictureMimeType.PNG)
                    .isZoomAnim(true)
                    .sizeMultiplier(0.5f)
                    .enableCrop(false)
                    .compress(true)
                    .hideBottomControls(false)
                    .isGif(false)
                    .openClickSound(false)
                    .previewEggs(true)
                    .minimumCompressSize(100)
                    .forResult(PictureConfig.CHOOSE_REQUEST)
            }
        })
    }


    @SuppressLint("CheckResult")
    private fun getPermission(result: (success: Boolean) -> Unit) {
        if (Build.VERSION.SDK_INT >= 23) {
            val rxPermissions = RxPermissions(activity!!)
            rxPermissions.request(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).subscribe { granted ->
                result(granted)
            }
        } else {
            result(true)
        }
    }

    @JavascriptInterface
    fun fixStatusBarHeight(show: Boolean) {
        Log.e("======>show", show.toString())
        runOnUiThread(Runnable {
            if (fixStatusBarListener != null) {
                fixStatusBarListener?.fix(show)
            }
        })
    }


    private var fixStatusBarListener: FixStatusBarListener? = null

    public interface FixStatusBarListener {
        fun fix(need: Boolean)
    }


    fun setOnFixStatusBarListener(listener: FixStatusBarListener) {
        this.fixStatusBarListener = listener
    }


    @JavascriptInterface
    fun openNewWebView(url: String, title: String) {
        var resulr = if (url.startsWith("http") || url.startsWith("https")) {
            LauncherHelper.from(activity).startActivity(
                CommonWebViewActivity.callIntent(
                    activity!!, url,
                    title
                )
            )
        } else {
            Log.e("======>二级页面", filtrationUrl("${getBaseUrl()}$url", activity!!, true))
            LauncherHelper.from(activity).startActivity(
                CommonWebViewActivity.callIntent(
                    activity!!, filtrationUrl("${getBaseUrl()}$url", activity!!, true),
                    title
                )
            )
        }

    }


    @JavascriptInterface
    fun setTitle(title: String) {
        Log.e("=======>title", title)
        if (!TextUtils.isEmpty(title) && listener != null) {
            listener?.setTitle(title)
        }
    }


    private var listener: GetTitleListener? = null

    @JavascriptInterface
    fun finishWebView() {
        if (activityNotFinish(activity)) {
            activity?.finish()
        }

    }


    fun activityNotFinish(activity: Activity?): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            activity != null && !activity.isFinishing && !activity.isDestroyed
        } else {
            activity != null && !activity.isFinishing
        }
    }

    interface GetTitleListener {
        fun setTitle(title: String)
    }


    fun setOnGetTitleListener(listener: GetTitleListener) {
        this.listener = listener
    }

}
