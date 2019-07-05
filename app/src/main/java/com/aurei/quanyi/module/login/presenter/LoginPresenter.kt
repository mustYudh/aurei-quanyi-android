package com.aurei.quanyi.module.login.presenter

import android.text.TextUtils
import com.aurei.quanyi.showToast
import com.yu.common.framework.BaseViewPresenter

/**
 * @author yudneghao
 * @date 2019-07-05
 */
class LoginPresenter(viewer: LoginViewer): BaseViewPresenter<LoginViewer>(viewer) {


    fun login(account: String,password: String,call: () -> Unit) {
        if (TextUtils.isEmpty(account)) {
            showToast("输入账号不能为空")
            return
        }
        if (TextUtils.isEmpty(password)) {
            showToast("输入密码不能为空")
            return
        }
        if (call != null) {
            showToast("登录成功后操作")
        }
    }
}