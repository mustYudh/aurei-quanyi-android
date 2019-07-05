package com.aurei.quanyi

import android.content.Context
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