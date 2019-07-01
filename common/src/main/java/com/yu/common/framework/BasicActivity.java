package com.yu.common.framework;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.yu.common.R;
import com.yu.common.launche.LauncherHelper;
import com.yu.common.loading.LoadingDialog;
import com.yu.common.navigation.StatusBarFontColorUtil;
import com.yu.common.utils.NetWorkUtil;
import com.yu.common.utils.ReflexUtils;

public abstract class BasicActivity extends AbstractExtendsActivity {

  private Fragment currentFragment;
  private View mNetworkErrorView;
  private View mContentView;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setView(savedInstanceState);
    if (needCheckNetWork()) {
      checkNetworkLoadData();
    } else {
      loadData();
    }
  }



  protected void checkNetworkLoadData() {
    if (NetWorkUtil.isNetworkAvailable(this)) {
      loadData();
    } else {
      if (mNetworkErrorView != null && mContentView != null) {
        mContentView.setVisibility(View.GONE);
        mNetworkErrorView.setVisibility(View.VISIBLE);
        handleNetWorkError(mNetworkErrorView);
      }
    }
  }

  protected abstract void handleNetWorkError(View view);


  protected void reload() {
    if (NetWorkUtil.isNetworkAvailable(this)) {
      mContentView.setVisibility(View.VISIBLE);
      mNetworkErrorView.setVisibility(View.GONE);
      loadData();
    }
  }


  protected boolean needCheckNetWork() {
    return false;
  }


  protected boolean darkMode() {
    return true;
  }

  @Override public final void setContentView(@LayoutRes int layoutResID) {
    setContentView(onReplaceRootView(layoutResID));
  }

  @Override public void setContentView(View view) {
    super.setContentView(view);
    if (darkMode()) {
      try {
        StatusBarFontColorUtil.statusBarLightMode(this);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }


  protected View onReplaceRootView(@LayoutRes int layoutResID) {
    ViewGroup rootView =
        (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_base_content, null);
    mContentView = View.inflate(this, layoutResID, (ViewGroup) rootView.findViewById(R.id.content_container));
    mNetworkErrorView =
        View.inflate(this, R.layout.inclue_networ_error_view, (ViewGroup) rootView.findViewById(R.id.network_error));
    return rootView;
  }


  @Override protected void onPause() {
    super.onPause();
  }

  @Override protected void onDestroy() {
    LoadingDialog.dismissLoading(this);
    super.onDestroy();
    currentFragment = null;
  }

  protected abstract void setView(@Nullable Bundle savedInstanceState);

  protected abstract void loadData();

  public Fragment getCurrentFragment() {
    return currentFragment;
  }


  protected void showFragment(@NonNull Fragment fragment, int attachLayoutId, Bundle extra) {
    if (isFinishing() || isDestroyed()) {
      return;
    }
    try {
      if (extra != null && !fragment.isStateSaved()) {
        fragment.setArguments(extra);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      commitFragment(attachLayoutId, currentFragment, fragment);
      currentFragment = fragment;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected void showFragment(@NonNull Fragment fragment, int attachLayoutId) {
    showFragment(fragment, attachLayoutId, null);
  }

  protected void showFragment(@NonNull Class<? extends Fragment> fragmentClass, int attachLayoutId,
                              Bundle extra) {
    if (currentFragment != null && currentFragment.getClass() == fragmentClass) {
      return;
    }
    showFragment(ReflexUtils.newClass(fragmentClass), attachLayoutId, extra);
  }

  protected void showFragment(@NonNull Class<? extends Fragment> fragmentClass,
      int attachLayoutId) {
    showFragment(fragmentClass, attachLayoutId, null);
  }

  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
    return super.onKeyDown(keyCode, event);
  }


  private void commitFragment(@IdRes int attachLayoutId, Fragment currentFragment,
      Fragment newFragment) {
    if (newFragment == null) {
      return;
    }
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

    if (currentFragment != null && currentFragment != newFragment && currentFragment.isAdded()) {
      transaction = transaction.hide(currentFragment);
    }
    if (newFragment.isAdded() || currentFragment == newFragment) {
      transaction.show(newFragment).commitAllowingStateLoss();
    } else {
      transaction.add(attachLayoutId, newFragment).commitAllowingStateLoss();
    }
  }

  protected LauncherHelper getLaunchHelper() {
    return LauncherHelper.from(getActivity());
  }
}
