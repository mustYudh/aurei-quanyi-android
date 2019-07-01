
package com.aurei.quanyi.http.subscriber;

import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xhttp2.subsciber.BaseSubscriber;

public abstract class NoTipRequestSubscriber<T> extends BaseSubscriber<T> {



    @Override
    public void onError(ApiException e) {

    }
}
