package com.yu.common;

import android.content.Context;
import com.yu.common.ui.Res;

/**
 * @author yudneghao
 * @date 2019/3/22
 */
public class CommonInit {
    private final static CommonInit commonInit = new CommonInit();
    private static Context CONTEXT = null;

    private CommonInit() {

    }

    public static CommonInit init(Context context) {
        CONTEXT = context;
        Res.init(context);
        return commonInit;
    }

    public static Context getContext() {
        return CONTEXT;
    }
}
