package com.aurei.quanyi.module.login.presenter

import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Log
import com.aurei.quanyi.http.api.ApiServices
import com.aurei.quanyi.module.login.bean.UserInfo
import com.aurei.quanyi.utils.goHome
import com.aurei.quanyi.utils.showToast
import com.qianchang.optimizetax.data.UserProfile
import com.qianchang.optimizetax.http.subscriber.TipRequestSubscriber
import com.xuexiang.xhttp2.XHttpProxy
import com.yu.common.framework.BaseViewPresenter

/**
 * @author yudneghao
 * @date 2019-07-05
 */
@SuppressLint("CheckResult")
class LoginPresenter(viewer: LoginViewer) : BaseViewPresenter<LoginViewer>(viewer) {


    fun login(account: String, password: String, call: (() -> Unit)?) {
        if (TextUtils.isEmpty(account)) {
            showToast("输入账号不能为空")
            return
        }
        if (TextUtils.isEmpty(password)) {
            showToast("输入密码不能为空")
            return
        }
        XHttpProxy.proxy(ApiServices::class.java)
            .login(account, password)
            .subscribeWith(object : TipRequestSubscriber<UserInfo>() {
                override fun onSuccess(t: UserInfo?) {
                    t?.account = account
                    Log.e("=====>token", t?.token)
                    UserProfile.login(t)
                    if (call != null) {

                    } else {
                        goHome(activity)
                    }
                    activity?.finish()
                }
            })
    }
}