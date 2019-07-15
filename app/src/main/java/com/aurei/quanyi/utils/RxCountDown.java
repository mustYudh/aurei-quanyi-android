package com.aurei.quanyi.utils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import java.util.concurrent.TimeUnit;

/**
 * @author yudneghao
 * @date 2019-07-15
 */
public class RxCountDown {

    public static Observable<Integer> countdown(int time) {
        if (time < 0) {
            time = 0;
        }
        final int countTime = time;
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(aLong -> countTime - aLong.intValue())
                .take(countTime + 1);

    }
}
