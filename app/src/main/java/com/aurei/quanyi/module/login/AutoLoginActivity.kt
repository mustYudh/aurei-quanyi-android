package com.aurei.quanyi.module.login

import android.os.Bundle
import com.aurei.quanyi.R
import com.aurei.quanyi.base.BaseBarActivity

/**
 * @author yudneghao
 * @date 2019-07-09
 */
class AutoLoginActivity : BaseBarActivity() {


    override fun setView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_auto_login_layout)
    }


    override fun loadData() {
        title = "SIM登录"
    }
}