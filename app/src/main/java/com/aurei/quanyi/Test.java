package com.aurei.quanyi;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author yudneghao
 * @date 2019-08-01
 */
public class Test {
    //模拟服务端返回
    private final static ArrayList<String> urls = new ArrayList<>();

    private int position = 0;

    public void setImage() {
        if (urls == null || urls.size() == 0) {
            return;
        }

        Disposable observable = Observable.interval(0, 10, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String url = urls.get(position);
                        // TODO: 2019-08-01 调用设置图片代码
                        position++;
                        if (position == urls.size() - 1) {
                            position = 0;
                        }

                    }
                });
    }
}
