package com.aurei.quanyi.base;

import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.aurei.quanyi.R;
import com.yu.common.ui.BarIconContainer;
import com.yu.common.utils.BarUtils;

public abstract class BaseBarActivity extends BaseActivity {

  protected int getActionBarLayoutId() {
    return R.layout.action_bar_white_web_view;
  }

  @Override protected boolean darkMode() {
    return getActionBarLayoutId() != R.layout.action_bar_white_web_view;
  }

  @Override protected void onViewCreated(View view) {
    super.onViewCreated(view);
    initBack();
  }

  @Override protected View onReplaceRootView(@LayoutRes int layoutResID) {
    View rootView = super.onReplaceRootView(layoutResID);
    FrameLayout container =
        isImmersionBar() ? rootView.findViewById(R.id.immersion_action_bar_container)
            : rootView.findViewById(R.id.action_bar_container);
    container.setVisibility(View.VISIBLE);
    View actionBar = getLayoutInflater().inflate(getActionBarLayoutId(), container, false);
    container.addView(actionBar);
    BarUtils.setActionBarLayout(actionBar);
    return rootView;
  }

  public boolean isImmersionBar() {
    return false;
  }

  private void initBack() {
    BarIconContainer back = findViewById(R.id.back);
    if (back != null) {
      back.setOnClickListener(v -> finish());
    }
  }

  public void setTitle(String titleName) {
    if (!TextUtils.isEmpty(titleName)) {
      TextView title = (TextView) findViewById(R.id.action_title);
      if (title != null) {
        title.setText(titleName);
      }
    }
  }
}
