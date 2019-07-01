package com.yu.common.loading;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import com.yu.common.R;

public class LoadingProgressImpl extends Dialog implements LoadingProgress {

    private Activity activity;




    public LoadingProgressImpl(Context context,View contentView) {
        super(context, R.style.common_ui_loading_progress);
        setContentView(contentView);
        initContext(context);
    }

    private void initContext(Context context) {
        activity = (Activity) context;
    }

    public boolean isActivityAttached() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return activity != null
                    && !activity.isFinishing()
                    && !activity.isDestroyed()
                    && getWindow() != null;
        } else {
            return activity != null && !activity.isFinishing() && getWindow() != null;
        }
    }


    @Override
    public void showLoading(String message) {
        if (isActivityAttached()) {
            if (!isShowing()) {
                super.show();
            }
        }
    }

    @Override
    public void dismissLoading() {
        if (isActivityAttached()) {
            if (isShowing()) {
                super.dismiss();
            }
        }
    }
}
