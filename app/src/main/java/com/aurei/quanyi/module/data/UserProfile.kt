package com.qianchang.optimizetax.data

import android.content.Context
import android.text.TextUtils
import com.aurei.quanyi.getAppContext
import com.aurei.quanyi.module.login.bean.UserInfo
import com.xuexiang.xhttp2.cookie.CookieManager
import com.yu.common.base.SharedPreferencesHelper
import java.io.Serializable


object UserProfile : Serializable {

    private var spHelper: SharedPreferencesHelper? = null
    private const val SHARE_PREFERENCES_NAME = ".user_profile"
    private const val USER_ID = "user_id"
    private const val COMPANY_ID = "company_id"
    private const val MOBILE = "mobile"


    init {
        spHelper = SharedPreferencesHelper.create(
            getAppContext()?.getSharedPreferences(SHARE_PREFERENCES_NAME, Context.MODE_PRIVATE)
        )
    }


    fun login(userInfo: UserInfo?) {

    }


    var userID: String? = null
        get() {
            field = spHelper?.getString(USER_ID, "")!!
            return field
        }
        set(value) {
            spHelper?.putString(USER_ID, value).toString()
        }


    var companyId: String? = null
        get() {
            field = spHelper?.getString(COMPANY_ID, "")
            return field
        }
        set(value) {
            spHelper?.putString(COMPANY_ID, value).toString()
        }

    var mobile: String? = null
        get() {
            field = spHelper?.getString(MOBILE, "")
            return field
        }
        set(value) {
            spHelper?.putString(MOBILE, value).toString()
        }


    var isLogin: Boolean = !TextUtils.isEmpty(userID) && !TextUtils.isEmpty(companyId)


    fun clean() {
        userID = ""
        companyId = ""
        spHelper?.clear()
        CookieManager.getInstance(getAppContext()).removeAll()
    }

}
