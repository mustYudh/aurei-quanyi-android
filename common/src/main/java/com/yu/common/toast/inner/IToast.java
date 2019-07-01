package com.yu.common.toast.inner;

import android.support.annotation.IdRes;
import android.view.View;
import com.yu.common.toast.DToast;


public interface IToast {
    void show();

    void showLong();

    void cancel();

    IToast setView(View mView);

    View getView();

    IToast setDuration(@DToast.Duration int duration);

    IToast setGravity(int gravity);

    IToast setGravity(int gravity, int xOffset, int yOffset);

    IToast setAnimation(int animation);

    IToast setPriority(int mPriority);

    IToast setText(@IdRes int id, String text);
}
