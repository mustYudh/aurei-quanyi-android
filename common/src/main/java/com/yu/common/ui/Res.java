package com.yu.common.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

public class Res {
    private static Context context;

    public Res() {
    }

    public static void init(Context context) {
        Res.context = context.getApplicationContext();
    }

    public static String string(@StringRes int id) {
        return context.getString(id);
    }

    public static int color(@ColorRes int id) {
        return ContextCompat.getColor(context, id);
    }

    public static Drawable drawable(@DrawableRes int id) {
        return ContextCompat.getDrawable(context, id);
    }

    public static float dimen(@DimenRes int id) {
        return context.getResources().getDimension(id);
    }

    public static float dimensionPixelSize(@DimenRes int id) {
        return (float) context.getResources().getDimensionPixelSize(id);
    }


}
