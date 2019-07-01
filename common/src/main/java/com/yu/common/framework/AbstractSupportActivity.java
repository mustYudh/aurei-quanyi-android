package com.yu.common.framework;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import com.yu.common.navigation.StatusBarUtils;
import com.yu.common.utils.BaseUtils;

public abstract class AbstractSupportActivity extends AppCompatActivity {

  private boolean afterSaveInstanceState;

  protected void beforeOnCreate(@Nullable Bundle savedInstanceState) {
    StatusBarUtils.setTranslucent(this, 0);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    try {
      beforeOnCreate(savedInstanceState);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      super.onCreate(savedInstanceState);
      afterSaveInstanceState = false;
    }
  }

  @Override public void setContentView(@LayoutRes int id) {
    super.setContentView(id);
    handleViewCreated(getWindow().getDecorView());
  }

  @Override public void setContentView(View view) {
    super.setContentView(view);
    handleViewCreated(view);
  }

  private boolean viewCreated = false;

  private void handleViewCreated(View view) {
    if (!viewCreated) {
      onViewCreated(view);
      viewCreated = true;
    }
  }


  protected abstract void onViewCreated(View view);


  public FragmentActivity getActivity() {
    return this;
  }

  @Override public boolean isDestroyed() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      return super.isDestroyed();
    } else {
      return super.isFinishing();
    }
  }


  @Override public boolean dispatchTouchEvent(final MotionEvent ev) {
    if (softInputClickOutsideAutoDismiss() && ev.getAction() == MotionEvent.ACTION_DOWN) {
      View v = getCurrentFocus();
      if (BaseUtils.isShouldHideInput(v, ev, getWindow().getDecorView())
          && v.getWindowToken() != null) {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (im != null) {
          im.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
      }
    }
    return super.dispatchTouchEvent(ev);
  }


  protected boolean softInputClickOutsideAutoDismiss() {
    return true;
  }


  public <T extends View> T bindView(@IdRes int id) {
    return (T) findViewById(id);
  }

  public <T extends View> T bindEnable(@IdRes int id, boolean enable) {
    View view = bindView(id);
    if (view != null) {
      view.setEnabled(enable);
    }
    return (T) view;
  }

  public <T extends View> T bindView(@IdRes int id, View.OnClickListener onClickListener) {
    View view = bindView(id);
    if (view != null) {
      view.setOnClickListener(onClickListener);
    }
    return (T) view;
  }

  public <T extends View> T bindView(@IdRes int id, boolean visible) {
    View view = bindView(id);
    if (view != null) {
      view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
    return (T) view;
  }

  public <T extends View> T bindText(@IdRes int id, CharSequence charSequence) {
    View view = bindView(id);
    if (view instanceof TextView) {
      ((TextView) view).setText(charSequence == null ? "" : charSequence);
    }
    return (T) view;
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    afterSaveInstanceState = true;
  }

  @Override public void onBackPressed() {
    if (afterSaveInstanceState) {
      finish();
    } else {
      super.onBackPressed();
    }
  }
}
