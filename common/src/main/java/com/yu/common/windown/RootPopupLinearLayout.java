package com.yu.common.windown;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class RootPopupLinearLayout extends LinearLayout {
  private static final String TAG = "RootPopupLinearLayout";
  private Activity context;

  public static RootPopupLinearLayout wrapper(Activity activity, @LayoutRes int layoutId) {
    RootPopupLinearLayout rootLinearLayout = new RootPopupLinearLayout(activity);
    rootLinearLayout.addView(
        LayoutInflater.from(activity).inflate(layoutId, rootLinearLayout, false));

    rootLinearLayout.setOrientation(VERTICAL);

    return rootLinearLayout;
  }

  public RootPopupLinearLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public RootPopupLinearLayout(Context context) {
    this(context, null);
  }

  public RootPopupLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.context = (Activity) context;
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    if (getPaddingBottom() != getRootViewPaddingBottom()) {
      setPadding(0, 0, 0, getRootViewPaddingBottom());
    }
  }

  private int getRootViewPaddingBottom() {
    if (context == null) {
      return 0;
    }
    int decorViewHeight = context.getWindow().getDecorView().getHeight();
    ViewGroup activityRootView =context.findViewById(android.R.id.content);
    if (activityRootView.getChildCount() == 0) {
      return decorViewHeight - activityRootView.getHeight();
    }
    View contentRootView = activityRootView.getChildAt(0);
    int navigationBarHeight = decorViewHeight - contentRootView.getHeight();
    return Math.max(navigationBarHeight, contentRootView.getPaddingBottom());
  }
}