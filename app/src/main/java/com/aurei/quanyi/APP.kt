package com.aurei.quanyi

import android.content.Context
import com.qianchang.optimizetax.http.interceptor.CustomDynamicInterceptor
import com.qianchang.optimizetax.http.interceptor.CustomLoggingInterceptor
import com.xuexiang.xhttp2.XHttp
import com.xuexiang.xhttp2.XHttpSDK
import com.xuexiang.xhttp2.model.HttpHeaders
import com.yu.common.base.BaseApp

/**
 * @author yudneghao
 * @date 2019-07-01
 */
class APP : BaseApp() {
    companion object {
        var appContext: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        initHttp()
    }

    private fun initHttp() {
        XHttpSDK.init(this)
        XHttpSDK.debug()

        //自定义日志拦截器
        XHttpSDK.debug(CustomLoggingInterceptor())
        XHttpSDK.setBaseUrl(getBaseUrl())



//    XHttpSDK.setSubUrl(getSubUrl())

        //自定义动态添加请求参数的拦截器
        XHttpSDK.addInterceptor(CustomDynamicInterceptor())
        //全局超时时间
        XHttp.getInstance().setTimeout(60000)
        //设置全局超时重试次数
        XHttp.getInstance().setRetryCount(3)
        //添加全局公共请求参数
        XHttp.getInstance().addCommonHeaders(getHttpHeaders())
    }


    private fun getHttpHeaders(): HttpHeaders {
        val headers = HttpHeaders()
        return headers
    }

    private fun getBaseUrl(): String {
        //http://172.90.14.241:8081
        //"http://tax-saving-member.int.qianli.com.cn"
        return "http://m.aurei.cc:8081"

    }
}