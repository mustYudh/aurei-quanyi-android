package com.aurei.quanyi.module.splash

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import com.aurei.quanyi.R
import com.aurei.quanyi.base.BaseActivity
import com.aurei.quanyi.utils.RxCountDown
import com.aurei.quanyi.utils.goHome
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


/**
 * @author yudneghao
 * @date 2019-07-05
 */
class SplashActivity : BaseActivity() {


    override fun setView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_spalsh_layout)
    }


    @SuppressLint("CheckResult")
    override fun loadData() {
        val permiss = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (Build.VERSION.SDK_INT >= 23) {
            val rxPermissions = RxPermissions(this)
            rxPermissions.request(*permiss).subscribe { granted ->
                next()
            }
        } else {
            next()
        }


    }

    private fun next() {
        RxCountDown.countdown(1)
            .subscribe(object : Observer<Int> {
                override fun onNext(t: Int) {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    goHome(activity)
                    finish()

                }

                override fun onComplete() {
                    goHome(activity)
                    finish()

                }
            })
    }

}