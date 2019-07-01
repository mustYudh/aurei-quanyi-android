package com.yu.common.ui.base;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import com.yu.common.ui.Res;
import com.yu.common.utils.DensityUtil;


public class BaseTextView extends AppCompatTextView {

  public BaseTextView(Context context) {
    this(context, null);
  }

  public BaseTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    analyzeAttributeSet(context, attrs);
  }

  public BaseTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    analyzeAttributeSet(context, attrs);
  }

  private void analyzeAttributeSet(Context context, AttributeSet attrs) {

    adjustTextSize();
  }

  private void adjustTextSize() {
    Context c = getContext();
    Resources r;
    if (c == null) {
      r = Resources.getSystem();
    } else {
      r = c.getResources();
    }
    setTextSize(getTextSize() / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 1,
        r.getDisplayMetrics()));
  }

  public void setCompoundDrawables(@DrawableRes int leftId, @DrawableRes int topId,
      @DrawableRes int rightId, @DrawableRes int bottomId) {
    int[] ids = new int[] { leftId, topId, rightId, bottomId };
    Drawable[] drawables = getCompoundDrawables();
    for (int i = 0; i < ids.length; i++) {
      if (ids[i] == 0) {
        drawables[i] = null;
      } else if (ids[i] != -1) {
        Drawable icon = Res.drawable(ids[i]);
        if (icon != null) {
          icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
          drawables[i] = icon;
        }
      }
    }
    setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
  }

  @Override public void setTextSize(float size) {
    float scale = (DensityUtil.getScreenHeight() * 108f) / (DensityUtil.getScreenWidth() * 192f);
    super.setTextSize((int) (size * Math.min(1, scale)));
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
  }
}

