package com.qianchang.optimizetax.http.interceptor

import com.xuexiang.xhttp2.interceptor.HttpLoggingInterceptor
import com.xuexiang.xhttp2.utils.HttpUtils
import okhttp3.*
import okhttp3.internal.http.HttpHeaders
import java.io.IOException

/**
 * @author yudneghao
 * @date 2019/4/19
 * 自定义日志拦截器【简单打印入参和出参】
 */
class CustomLoggingInterceptor : HttpLoggingInterceptor("HttpResult:") {
  init {
    level = HttpLoggingInterceptor.Level.PARAM
  }

  @Throws(IOException::class)
  override fun logForRequest(request: Request, connection: Connection?) {
    val requestBody = request.body()
    val hasRequestBody = requestBody != null
    val protocol = if (connection != null) connection.protocol() else Protocol.HTTP_1_1

    val logBuilder = StringBuilder()
    try {
      logBuilder.append("--> ")
          .append(request.method())
          .append(' ')
          .append(request.url())
          .append(' ')
          .append(protocol)
          .append("\r\n")
      if (hasRequestBody) {
        logBuilder.append("入参:")
        if (HttpUtils.isPlaintext(requestBody!!.contentType())) {
          logBuilder.append(bodyToString(request))
        } else {
          logBuilder.append("maybe [file part] , too large too print , ignored!")
        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }

    log(logBuilder.toString())
  }

  override fun logForResponse(response: Response, tookMs: Long): Response {
    val builder = response.newBuilder()
    val clone = builder.build()
    var responseBody = clone.body()

    log("<-- " + clone.code() + ' '.toString() + clone.message() + ' '.toString() + clone.request().url() + " (" + tookMs + "ms）")
    try {
      if (HttpHeaders.hasBody(clone)) {
        if (HttpUtils.isPlaintext(responseBody!!.contentType())) {
          val body = responseBody.string()
          log("\t出参:$body")
          responseBody = ResponseBody.create(responseBody.contentType(), body)
          return response.newBuilder().body(responseBody).build()
        } else {
          log("\t出参: maybe [file part] , too large too print , ignored!")
        }
      }
    } catch (e: IOException) {
      e.printStackTrace()
    }

    return response
  }

}
