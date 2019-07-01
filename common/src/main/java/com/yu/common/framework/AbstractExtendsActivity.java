package com.yu.common.framework;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.yu.common.loading.LoadingDialog;
import com.yu.common.navigation.NavigationUtils;

public class AbstractExtendsActivity extends AbstractPresenterActivity {

  @Override public void onCreate(@Nullable Bundle savedInstanceState,
      @Nullable PersistableBundle persistentState) {
    super.onCreate(savedInstanceState, persistentState);
  }

  @Override protected void onViewCreated(View view) {
    super.onViewCreated(view);
    NavigationUtils.setNavigationBaColor(getWindow(), Color.BLACK);
  }


  @Override protected void onDestroy() {
    super.onDestroy();
    LoadingDialog.dismissLoading(this);
  }


}
