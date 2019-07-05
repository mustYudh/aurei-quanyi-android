package com.aurei.quanyi.module.splash

import android.os.Bundle
import com.aurei.quanyi.R
import com.aurei.quanyi.base.BaseActivity
import com.aurei.quanyi.module.web.WebViewActivity

/**
 * @author yudneghao
 * @date 2019-07-05
 */
class SplashActivity : BaseActivity() {


    override fun setView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_spalsh_layout)
    }


    override fun loadData() {
        launchHelper.startActivity(WebViewActivity.callIntent(activity,""))
    }

}