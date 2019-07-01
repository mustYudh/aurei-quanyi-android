package com.yu.common.toast.inner;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.support.annotation.NonNull;
import android.view.WindowManager;


public class ActivityToast extends DovaToast {

    //context非Activity时会抛出异常:Unable to add window -- token null is not valid; is your activity running?
    public ActivityToast(@NonNull Context mContext) {
        super(mContext);
    }

    @Override
    public WindowManager.LayoutParams getWMParams() {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        lp.format = PixelFormat.TRANSLUCENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.windowAnimations = android.R.style.Animation_Toast;
        // TODO: 2018/11/20 考虑是否需要引入windowToken
//        lp.token=((Activity)getContext()).getWindow().getDecorView().getWindowToken();
        lp.gravity = getGravity();
        lp.x = getXOffset();
        lp.y = getYOffset();
        return lp;
    }

    @Override
    public WindowManager getWMManager() {
        //context非Activity时会抛出异常:Unable to add window -- token null is not valid; is your activity running?
        if (mContext instanceof Activity) {
            return ((Activity) mContext).getWindowManager();
        } else {
            return null;
        }
    }
}
