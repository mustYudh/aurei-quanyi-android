package com.aurei.quanyi

import android.content.Context
import cn.com.chinatelecom.account.sdk.CtAuth
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
        private const val NET_TYPE = BuildConfig.API_MODE
        const val DEBUG: Boolean = NET_TYPE == 0 || NET_TYPE == 2
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        initHttp()
    }


    //请在天翼账号开放平台申请应用的APPID
    val APPID = "8148613147"
    //请在天翼账号开放平台申请应用的APPSECRET
    val APPSECRET = "iumiPldXIOW8eRM0jJHT6QH7gyZGpDQV"

    private fun initHttp() {
        XHttpSDK.init(this)
        XHttpSDK.debug()

        //自定义日志拦截器
        XHttpSDK.debug(CustomLoggingInterceptor())
        XHttpSDK.setBaseUrl(getBaseUrl())
        CtAuth.getInstance().init(getAppContext(), APPID, APPSECRET, DEBUG)


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
        return when (BuildConfig.API_MODE) {
            0 -> {
                "http://tax-saving-app-member.int.qianli-inc.com"
            }
            1 -> {
                "https://tax-saving-member-jar.51youshui.com"
            }
            else ->
                "https://tax-saving-member-jar.51youshui.com"
        }
    }
}