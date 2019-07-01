package com.yu.common.windown;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yu.common.R;
import com.yu.common.navigation.StatusBarUtils;



public abstract class BaseDialog extends Dialog {

  private Activity activity;
  private boolean isDismissing = false;


  public BaseDialog(@NonNull Context context) {
    this(context, R.style.broker_common_ui_base_dialog);
    setOwnerActivity((Activity) context);
  }

  public BaseDialog(@NonNull Context context, @StyleRes int themeResId) {
    super(context, themeResId);
    setOwnerActivity((Activity) context);
    initContext(context);
  }

  private void initContext(Context context) {
    activity = (Activity) context;
  }

  public final View getRootView() {
    return getWindow().getDecorView();
  }

  public LinearLayout getContainer() {
    return ((LinearLayout) findViewById(R.id.base_content));
  }

  public View getBackgroundShadow() {
    return findViewById(R.id.base_shadow);
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
  public void setContentView(@LayoutRes int layoutId) {
    super.setContentView(R.layout.lb_cm_dailog_base_container_view);
    View childView = getLayoutInflater().inflate(layoutId, getContainer(), false);
    getContainer().addView(childView);
    bindView(R.id.base_shadow, new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });
    matchWindows(hasEdit(childView));
  }

  /**
   * 判断是否有edittext
   */
  private boolean hasEdit(View view) {
    if (view == null) return false;
    if (view instanceof EditText) return true;
    if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
      for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
        if (hasEdit(((ViewGroup) view).getChildAt(i))) {
          return true;
        }
      }
    }
    return false;
  }

  protected void superShowDialog() {
    isDismissing = false;
    if (isActivityAttached()) {
      super.show();
    }
  }

  /**
   * 最好在BaseActivity处理，这边先简单处理下
   */
  @Override
  public void show() {
    if (isActivityAttached()) {
      isDismissing = false;
      startEnterAnim();
    }
  }


  protected void superDismissDialog() {
    if (isActivityAttached()) {
      super.dismiss();
    }
    isDismissing = false;
  }

  @Override
  public void dismiss() {
    if (isDismissing) return;
    isDismissing = true;
    startOutAnim();
  }

  private void startEnterAnim() {
    if (!isActivityAttached()) {
      return;
    }
    displayEnterAnim();
  }

  private void startOutAnim() {
    if (isActivityAttached()) {
      displayOutAnim();
    } else {
      superDismissDialog();
    }
  }

  /**
   * 是否定死宽高
   */
  public void matchWindows(boolean match) {
    if (match) {
      if (getWindow() != null) {
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);
      }
    } else {
      int screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
      int statusBarHeight = StatusBarUtils.getStatusBarHeight(getContext());
      int dialogHeight = screenHeight - statusBarHeight;
      if (getWindow() != null) {
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
            dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
      }
    }
  }

  /******************待继承重写的方法********************/
  /**
   * 默认实现是变大+渐变动画
   */
  protected void displayEnterAnim() {
    if (getWindow() == null) return;
    getWindow().setWindowAnimations(0); //设置窗口弹出动画
    ObjectAnimator animator1 =
        ObjectAnimator.ofFloat(getBackgroundShadow(), PropertyName.Alpha, 0, 1).setDuration(360);
    ObjectAnimator animator2 =
        ObjectAnimator.ofFloat(getContainer(), PropertyName.ScaleX, 0.5f, 1f).setDuration(180);
    ObjectAnimator animator3 =
        ObjectAnimator.ofFloat(getContainer(), PropertyName.ScaleY, 0.5f, 1f).setDuration(180);
    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playTogether(animator1, animator2, animator3);
    animatorSet.setInterpolator(new DecelerateInterpolator());
    animatorSet.start();
    superShowDialog();
  }

  /**
   * 默认实现渐变退出
   */
  protected void displayOutAnim() {
    ObjectAnimator animator =
        ObjectAnimator.ofFloat(getRootView(), PropertyName.Alpha, 1, 0).setDuration(250);
    animator.setInterpolator(new AccelerateDecelerateInterpolator());
    animator.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        if (isDismissing) {
          superDismissDialog();
        }
      }
    });
    animator.start();
  }

  /******************简化View的获取********************/

  public <T> T bindView(@IdRes int resId) {
    return (T) findViewById(resId);
  }

  public <T extends View> T bindView(@IdRes int resId, boolean visible) {
    T t = (T) findViewById(resId);
    if (t != null) t.setVisibility(visible ? View.VISIBLE : View.GONE);

    return t;
  }

  public <T extends View> T bindView(int resId, View.OnClickListener listener) {
    T v = (T) findViewById(resId);
    if (v != null) {
      v.setOnClickListener(listener);
    }

    return v;
  }

  public <T> T bindText(@IdRes int resId, CharSequence charSequence) {
    View view = bindView(resId);

    if (view != null && view instanceof TextView) {
      ((TextView) view).setText(charSequence);
    }
    return (T) view;
  }
}
