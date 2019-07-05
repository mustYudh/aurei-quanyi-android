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
    public final static String MSG = "message";
    public final static String DATA = "result";

    private int code;
    private String message;
    private T result;

    public int getCode() {
        return code;
    }

    public ApiResult setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ApiResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public ApiResult setResult(T result) {
        this.result = result;
        return this;
    }

    /**
     * 获取请求响应的数据，自定义api的时候需要重写【很关键】
     *
     * @return
     */
    public T getResult() {
        return result;
    }

    /**
     * 是否请求成功,自定义api的时候需要重写【很关键】
     *
     * @return
     */
    public boolean isSuccess() {
        return getCode() == 200;
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
