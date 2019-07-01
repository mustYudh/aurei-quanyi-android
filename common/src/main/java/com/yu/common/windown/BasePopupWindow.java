package com.yu.common.windown;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.PopupWindow;

import com.yu.common.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 1. 通过PopupCollector实现BaseActivity对PopupWindow的管理
 * 2. 实现默认动画
 */
public abstract class BasePopupWindow extends PopupWindow {

  private Activity activity;
  private boolean isDismissing = false;
  private OnPopupShowingListener onPopupShowingListener;

  public interface OnPopupShowingListener {
    void onShow();

    void onDismiss();
  }

  public void setOnPopupShowingListener(OnPopupShowingListener onPopupShowingListener) {
    this.onPopupShowingListener = onPopupShowingListener;
  }

  public BasePopupWindow(Context context, View contentView, int width, int height) {
    this(context, contentView, width, height, false);
  }


  public BasePopupWindow(Context context, View contentView, int w, int h, boolean focusable) {
    super(contentView, w, h, focusable);
    initContext(context);
  }

  public BasePopupWindow(Context context, int layoutId) {
    super(RootPopupLinearLayout.wrapper((Activity) context, layoutId),
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
    initContext(context);
  }

  public Context getContext() {
    return activity;
  }

  private void initContext(Context context) {
    this.activity = (Activity) context;
    setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
  }

  public boolean isDismissing() {
    return isDismissing;
  }

  public final View getRootView() {
    return getContentView();
  }

  public void showPopupWindow() {
    showPopupWindow(Gravity.CENTER, 0, 0);
  }

  public boolean isActivityAttached() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      return activity != null && !activity.isFinishing() && !activity.isDestroyed();
    } else {
      return activity != null && !activity.isFinishing();
    }
  }

  public void showPopupWindow(final int gravity, final int left, final int top) {
    if (isActivityAttached()) {
      showAtLocation(activity.findViewById(android.R.id.content), gravity, left, top);
    }
  }

  @Override
  public void showAtLocation(final View parent, final int gravity, final int x, final int y) {
    if (isActivityAttached()) {
      isDismissing = false;
      startShowAnim();
      BasePopupWindow.super.showAtLocation(parent, gravity, x, y);

      if (onPopupShowingListener != null) {
        onPopupShowingListener.onShow();
      }
    }
  }

  @Override
  public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
    if (anchor == null) {
      showPopupWindow();
      return;
    }
    if (isActivityAttached()) {
      isDismissing = false;

      if (Build.VERSION.SDK_INT >= 24) {
        Rect visibleFrame = new Rect();
        anchor.getGlobalVisibleRect(visibleFrame);
        int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
        setHeight(height);
      }
      super.showAsDropDown(anchor, xoff, yoff, gravity);
      if (onPopupShowingListener != null) {
        onPopupShowingListener.onShow();
      }
    }
  }

  protected void superDismissPopup() {
    if (isActivityAttached()) {
      super.dismiss();
      if (onPopupShowingListener != null) {
        onPopupShowingListener.onDismiss();
      }
    }
    isDismissing = false;
  }

  @Override
  public void dismiss() {
    if (isDismissing) {
      return;
    }
    isDismissing = true;
    startDismissAnim();
  }

  private void startShowAnim() {
    if (isActivityAttached()) {
      displayShowAnim();
    }
  }

  private void startDismissAnim() {
    if (isActivityAttached()) {
      displayDismissAnim();
    } else {
      superDismissPopup();
    }
  }

  /******************待继承重写的方法********************/

  protected abstract View getBackgroundShadow();

  protected abstract View getContainer();

  /**
   * 默认实现底部渐变，内容从下往上动画
   */
  private void displayShowAnim() {
    ValueAnimator[] animators = getShowAnimators(getContainer(), getBackgroundShadow());
    if (animators == null || animators.length == 0) {
      return;
    }
    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playTogether(animators);
    animatorSet.start();
  }

  /**
   * 容器高度
   */
  protected int getContainerHeight() {
    View view = getContainer();
    if (view != null && view.getMeasuredHeight() > 0) {
      return view.getMeasuredHeight();
    }
    return DensityUtil.dip2px(getContext(), 250);
  }

  /**
   * 设置消失动画
   */
  protected ValueAnimator[] getShowAnimators(@Nullable View contentContainer,
                                             @Nullable View backgroundShadow) {
    List<ObjectAnimator> objectAnimators = new ArrayList<>();
    if (contentContainer != null) {
      ObjectAnimator animator =
          ObjectAnimator.ofFloat(contentContainer, PropertyName.TranY, getContainerHeight(), 0)
              .setDuration(250);
      animator.setInterpolator(new DecelerateInterpolator());
      objectAnimators.add(animator);
    }
    if (backgroundShadow != null) {
      ObjectAnimator animator =
          ObjectAnimator.ofFloat(backgroundShadow, PropertyName.Alpha, 0, 1).setDuration(360);
      animator.setInterpolator(new DecelerateInterpolator());
      objectAnimators.add(animator);
    }
    return objectAnimators.toArray(new ObjectAnimator[objectAnimators.size()]);
  }

  /**
   * 默认实现是渐变退出
   */
  private void displayDismissAnim() {
    ValueAnimator[] animators = getDismissAnimators(getContainer(), getBackgroundShadow());
    if (animators == null || animators.length == 0) {
      superDismissPopup();
      return;
    }

    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playTogether(animators);
    animatorSet.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        if (isDismissing) {
          superDismissPopup();
        }
      }
    });
    animatorSet.start();
  }

  /**
   * 设置消失动画
   */
  protected ValueAnimator[] getDismissAnimators(@Nullable View contentContainer,
                                                @Nullable View backgroundShadow) {
    List<ObjectAnimator> objectAnimators = new ArrayList<>();
    if (contentContainer != null) {
      ObjectAnimator animator =
          ObjectAnimator.ofFloat(contentContainer, PropertyName.TranY, 0, getContainerHeight())
              .setDuration(250);
      animator.setInterpolator(new AccelerateDecelerateInterpolator());
      objectAnimators.add(animator);
    }
    if (backgroundShadow != null) {
      ObjectAnimator animator =
          ObjectAnimator.ofFloat(backgroundShadow, PropertyName.Alpha, 1, 0).setDuration(250);
      animator.setInterpolator(new AccelerateDecelerateInterpolator());
      objectAnimators.add(animator);
    }
    return objectAnimators.toArray(new ObjectAnimator[objectAnimators.size()]);
  }

  /******************简化View的获取********************/

  public View findViewById(@IdRes int id) {
    return getContentView().findViewById(id);
  }

  public <T extends View> T bindView(@IdRes int id) {
    return (T) findViewById(id);
  }

  public <T extends View> T bindView(@IdRes int id, View.OnClickListener listener) {
    T t = (T) findViewById(id);
    if (t != null) {
      t.setOnClickListener(listener);
    }
    return t;
  }

  public <T extends View> T bindView(@IdRes int id, boolean visible) {
    T t = (T) findViewById(id);
    if (t != null) {
      t.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
    return t;
  }
}
