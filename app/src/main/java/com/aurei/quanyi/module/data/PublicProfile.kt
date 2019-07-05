package com.qianli.housekeeper.data;

import android.content.Context
import com.aurei.quanyi.getAppContext
import com.yu.common.base.SharedPreferencesHelper
import java.io.Serializable


object PublicProfile : Serializable {

  private const val USER_ACCOUNT = "user_account"
  private var spHelper: SharedPreferencesHelper? = null
  private const val SHARE_PREFERENCES_NAME = ".public_profile"

  init {
    spHelper = SharedPreferencesHelper.create(
        getAppContext()?.getSharedPreferences(SHARE_PREFERENCES_NAME, Context.MODE_PRIVATE)
    )
  }


  var userAccount: String? = null
    get() {
      field = spHelper?.getString(USER_ACCOUNT, "")
      return field
    }
    set(value) {
      spHelper?.putString(USER_ACCOUNT, value).toString()
    }


  fun clean() {
    spHelper?.clear()
  }
}
