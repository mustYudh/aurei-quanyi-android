package com.aurei.quanyi.utils

import android.content.Context
import android.widget.EditText
import com.aurei.quanyi.APP
import com.aurei.quanyi.module.web.WebViewActivity
import com.qianchang.optimizetax.data.UserProfile
import com.yu.common.launche.LauncherHelper
import com.yu.common.toast.ToastUtils

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

fun goHome(context: Context) {
    LauncherHelper.from(context).startActivity(WebViewActivity.callIntent(context,"http://129.28.196.121:8080/#/index?access_token=${UserProfile.token}"))
}