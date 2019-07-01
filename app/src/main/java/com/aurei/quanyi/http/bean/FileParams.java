package com.aurei.quanyi.http.bean;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.io.File;
import java.io.Serializable;

public class FileParams implements Serializable {

    private final MultipartBody.Part body;

    public FileParams(File file) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
    }

    public MultipartBody.Part getBody() {
        return body;
    }
}
