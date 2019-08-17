package com.qianchang.optimizetax.http.interceptor

import android.text.TextUtils
import com.qianchang.optimizetax.data.UserProfile
import com.xuexiang.xhttp2.interceptor.BaseDynamicInterceptor
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

/**
 * @author yudneghao
 * @date 2019/4/19
 * 自定义动态添加请求参数的拦截器
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class CustomDynamicInterceptor : BaseDynamicInterceptor<CustomDynamicInterceptor>() {


  override fun updateDynamicParams(dynamicMap: TreeMap<String, Any>): TreeMap<String, Any> {
    return dynamicMap
  }

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    return if (TextUtils.isEmpty(UserProfile.getToken())) {
      chain.proceed(request
        .newBuilder()
        .post(request.body())
        .addHeader("X-Access-Token",UserProfile.getToken()).build())
    } else{
      super.intercept(chain)
    }

  }
}

