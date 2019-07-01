package com.yu.common.toast;

import android.content.Context;
import android.view.Gravity;
import com.yu.common.CommonInit;
import com.yu.common.R;
import com.yu.common.utils.DensityUtil;

/**
 * @author yudneghao
 * @date 2019/3/22
 */
public class ToastUtils {



    public static void show(Context mContext, String msg) {
        if (mContext == null || msg == null) return;
        DToast.make(mContext)
                .setText(R.id.tv_content_default, msg)
                .setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, DensityUtil.dip2px(50f))
                .show();
    }


    public static void show(String msg) {
        if (msg == null) return;
        DToast.make(CommonInit.getContext())
            .setText(R.id.tv_content_default, msg)
            .setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, DensityUtil.dip2px(50f))
            .show();
    }


    //退出APP时调用
    public static void cancelAll() {
        DToast.cancel();
    }
}
