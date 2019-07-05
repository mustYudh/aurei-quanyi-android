package com.qianchang.optimizetax.data

import android.content.Context
import android.text.TextUtils
import com.aurei.quanyi.getAppContext
import com.aurei.quanyi.module.login.bean.UserInfo
import com.yu.common.base.SharedPreferencesHelper
import java.io.Serializable


object UserProfile : Serializable {

    private var spHelper: SharedPreferencesHelper? = null
    private const val SHARE_PREFERENCES_NAME = ".user_profile"
    private const val TOKEN = "token"
    private const val ACCOUNT = "account"


    init {
        spHelper = SharedPreferencesHelper.create(
            getAppContext()?.getSharedPreferences(SHARE_PREFERENCES_NAME, Context.MODE_PRIVATE)
        )
    }


    fun login(userInfo: UserInfo?) {
        token = userInfo?.token
        account = userInfo?.account
    }


    var token: String? = null
        get() {
            field = spHelper?.getString(TOKEN, "")!!
            return field
        }
        set(value) {
            spHelper?.putString(TOKEN, value).toString()
        }


    var account: String? = null
        get() {
            field = spHelper?.getString(ACCOUNT, "")
            return field
        }
        set(value) {
            spHelper?.putString(ACCOUNT, value).toString()
        }


    var isLogin: Boolean = !TextUtils.isEmpty(token) && !TextUtils.isEmpty(account)


    fun clean() {
        spHelper?.clear()
    }

}
