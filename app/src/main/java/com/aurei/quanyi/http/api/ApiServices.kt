package com.aurei.quanyi.http.api

import com.aurei.quanyi.module.login.bean.UserInfo
import com.xuexiang.xhttp2.annotation.NetMethod
import io.reactivex.Observable

/**
 * @author yudneghao
 * @date 2019-07-05
 */
interface ApiServices {
    @NetMethod(ParameterNames = ["username","password"],Url = "/api/user/login")
    fun login(username: String,password: String): Observable<UserInfo>
}