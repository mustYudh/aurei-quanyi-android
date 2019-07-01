package com.yu.common.framework;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Field;

public class AbstractSupportFragment extends Fragment {

  public void finish() {
    if (getActivity() != null) {
      getActivity().finish();
    }
  }

  public final View findViewById(@IdRes int id) {
    if (getView() == null) {
      return null;
    }
    return getView().findViewById(id);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onDestroy() {
    try {
      Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
      childFragmentManager.setAccessible(true);
      childFragmentManager.set(this, null);
    } catch (Exception e) {
      e.printStackTrace();
    }
    super.onDestroy();
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
}
