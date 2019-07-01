/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xuexiang.xhttp2.model;

/**
 * 提供的默认的标注返回api
 *
 * @author xuexiang
 * @since 2018/5/22 下午4:22
 */
public class ApiResult<T> {
    public final static String CODE = "code";
    public final static String MSG = "msg";
    public final static String DATA = "data";

    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public ApiResult setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ApiResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public ApiResult setData(T data) {
        this.data = data;
        return this;
    }

    /**
     * 获取请求响应的数据，自定义api的时候需要重写【很关键】
     *
     * @return
     */
    public T getData() {
        return data;
    }

    /**
     * 是否请求成功,自定义api的时候需要重写【很关键】
     *
     * @return
     */
    public boolean isSuccess() {
        return getCode() == 0;
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
