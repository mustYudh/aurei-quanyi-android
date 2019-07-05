package com.aurei.quanyi

import android.os.Bundle
import com.aurei.quanyi.base.BaseActivity
import com.yu.common.navigation.StatusBarFontColorUtil

class LoginActivity : BaseActivity() {

    override fun setView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_login_layout)
        StatusBarFontColorUtil.statusBarDarkMode(activity)
    }


    override fun loadData() {

    }


}
