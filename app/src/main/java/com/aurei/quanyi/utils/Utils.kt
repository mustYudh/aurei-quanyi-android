package com.aurei.quanyi.utils

import android.content.Context
import android.widget.EditText
import com.aurei.quanyi.APP
import com.aurei.quanyi.module.web.WebViewActivity
import com.qianchang.optimizetax.data.UserProfile
import com.yu.common.launche.LauncherHelper
import com.yu.common.navigation.StatusBarUtils
import com.yu.common.toast.ToastUtils
import com.yu.common.utils.DensityUtil

/**
 * @author yudneghao
 * @date 2019-07-05
 */
fun showToast(context: Context?, text: String) {
    ToastUtils.show(context, text)
}


fun showToast(text: String) {
    ToastUtils.show(getAppContext(), text)
}


fun getAppContext(): Context? {
    return APP.appContext
}


fun EditText.getEditText(): String {
    return text.toString().trim()
}

fun getBaseUrl(): String {
    return "http://172.90.14.232:8080/#"
}

fun filtrationUrl(url: String, context: Context):String {
    var result: String = ""
    if (url.contains("?")) {
        var params =  ActionParams.parse(url).params
        if (params != null && params.size > 0) {
            params["fromApp"] = "1"
            params["statusBarHeight"] = DensityUtil.px2dip(StatusBarUtils.getStatusBarHeight(context).toFloat()).toString()
            params["access_token"] = UserProfile.token.toString()
        } else {
            params = HashMap<String,String>()
            params["fromApp"] = "1"
            params["statusBarHeight"] = DensityUtil.px2dip(StatusBarUtils.getStatusBarHeight(context).toFloat()).toString()
            params["access_token"] = UserProfile.token.toString()
        }
        var filtrationUrl = ActionParams.parse(url).action  + "?"
        var position = 0
        params.map {
            position++
            val data = it.key + "=" + it.value + if (position == params.size) "" else "&"
            filtrationUrl += data
        }
        result = filtrationUrl
    } else {
        result = url + "?" +  getParams(context)
    }
    return result

}

fun getParams(context: Context): String {
    return "fromApp=1&statusBarHeight=${DensityUtil.px2dip(StatusBarUtils.getStatusBarHeight(context).toFloat())}&access_token=${UserProfile.token}"
}

fun goHome(context: Context) {
    LauncherHelper.from(context).startActivity(WebViewActivity.callIntent(context,"${getBaseUrl()}/index?${getParams(context)}",true))
}

fun getPassword(context: Context) {
    LauncherHelper.from(context).startActivity(WebViewActivity.callIntent(context,"${getBaseUrl()}/#/findPwd?fromApp=1",false))
}

fun registerUrl(context: Context) {
    LauncherHelper.from(context).startActivity(WebViewActivity.callIntent(context,"${getBaseUrl()}/registerPhone?fromApp=1",false))
}