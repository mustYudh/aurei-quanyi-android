package com.yu.common.web.base;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.yu.common.launche.LauncherHelper;

/**
 * @author chenwei
 * @date 2018/1/25
 */
public class BaseWebViewClient extends WebViewClient {

  @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
    if (TextUtils.isEmpty(url)) {
      return true;
    }
    if (url.startsWith("http://") || url.startsWith("https://")) {
      view.loadUrl(url);
      return false;
    } else {
      LauncherHelper.from(view.getContext())
          .startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
      return true;
    }
  }
}