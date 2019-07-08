package com.aurei.quanyi.module.splash

import android.Manifest
import android.os.Build
import android.os.Bundle
import com.aurei.quanyi.R
import com.aurei.quanyi.base.BaseActivity
import com.aurei.quanyi.module.web.WebViewActivity
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * @author yudneghao
 * @date 2019-07-05
 */
class SplashActivity : BaseActivity() {


    override fun setView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_spalsh_layout)
    }


    override fun loadData() {
        val permiss = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (Build.VERSION.SDK_INT >= 23) {
            val rxPermissions = RxPermissions(this)
            rxPermissions.request(*permiss).subscribe { granted -> }
        } else {

        }
//        launchHelper.startActivity(WebViewActivity.callIntent(activity,""))
        launchHelper.startActivity(WebViewActivity.callIntent(activity,"http://129.28.196.121:8080/#/bridge"))
    }

}