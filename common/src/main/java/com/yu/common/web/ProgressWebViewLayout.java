package com.yu.common.web;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.yu.common.R;
//import ren.yale.android.cachewebviewlib.CacheWebView;

/**
 * @author chenwei
 * @date 2017/8/23
 */
public class ProgressWebViewLayout extends LinearLayout {

  private WebView webView;
  private ProgressBar progressBar;

  public ProgressWebViewLayout(Context context) {
    this(context, null);
  }

  public ProgressWebViewLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    initAttrs(context, attrs);
  }

  public ProgressWebViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initAttrs(context, attrs);
  }

  private void initAttrs(Context context, AttributeSet attrs) {
    LayoutInflater.from(context).inflate(R.layout.lb_cm_layout_common_webview, this, true);
    webView = (WebView) findViewById(R.id.webView);
    progressBar = (ProgressBar) findViewById(R.id.webview_progress);

    //设置加载进度监听
    doCommonSetting();
    addProgressChromeClient();
  }

  /**
   * 获取WebView
   */
  public WebView getWebView() {
    return webView;
  }

  public LinearLayout emptyView() {
    return  findViewById(R.id.web_empty_view);
  }

  /**
   * 通用设置
   */
  private void doCommonSetting() {
    WebSettingUtils.setWebSetting(getContext(), webView);
//    webView.setWebViewClient(new BaseWebViewClient());
  }

  private void addProgressChromeClient() {
    webView.setWebChromeClient(new ProgressWebChromeClient(progressBar));
  }

  public ProgressBar getProgressBar() {
    return progressBar;
  }
}
