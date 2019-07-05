package com.yu.common.web;

import android.content.Context;
import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.File;

/**
 * @author changwei
 * @date 2017/8/22
 */
public class WebSettingUtils {
  // web缓存目录
  private static final String APP_CACHE_DIRNAME = "/webcache";
  public static void setWebSetting(Context context, WebView web) {
    if (web == null || context == null) {
      return;
    }
    WebSettings set = web.getSettings();
//    set.setAppCachePath(context.getCacheDir().getAbsolutePath());
    set.setJavaScriptEnabled(true);
    set.setJavaScriptCanOpenWindowsAutomatically(true);
    set.setRenderPriority(WebSettings.RenderPriority.HIGH);
    set.setLoadWithOverviewMode(true);
    set.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
    set.setUseWideViewPort(true);
//    set.setAppCacheMaxSize(1024 * 1024 * 8);
    set.setGeolocationEnabled(true);
    set.setAllowFileAccess(true);
    set.setBlockNetworkImage(false);
    set.setSupportZoom(true);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      set.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
    }
//    if (NetWorkUtil.isNetworkAvailable(context)) {
//      set.setCacheMode(WebSettings.LOAD_DEFAULT);
//    } else {
//      set.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//    }
    set.setDomStorageEnabled(true);
    set.setDatabaseEnabled(true);
//    String cacheDirPath = context.getFilesDir().getAbsolutePath()
//        + APP_CACHE_DIRNAME;
//    set.setDatabasePath(cacheDirPath);
//    set.setAppCachePath(cacheDirPath);
//    set.setAppCacheEnabled(true);
//    deleteWebCache(context);
  }

  private static void deleteWebCache(Context context) {
    clearWebViewCache(context);
    File appCacheDir = new File(context.getFilesDir().getAbsolutePath()
        + APP_CACHE_DIRNAME);
    File webviewCacheDir = new File(context.getCacheDir().getAbsolutePath()
        + "/webviewCache");
    // 删除webView缓存目录
    long lastModified = appCacheDir.lastModified();
    long currentTime = System.currentTimeMillis();
    long difTime = (currentTime - lastModified) / 1000 / 60 / 60;
    if (difTime > 24 * 4) {
      if (webviewCacheDir.exists()) {
        deleteFile(webviewCacheDir);
      }
      // 删除webView缓存，缓存目录
      if (appCacheDir.exists()) {
        deleteFile(appCacheDir);
      }
    }
  }

  public static void clearWebViewCache(Context context) {
    // 清理WebView缓存数据库
    try {
      context.deleteDatabase("webview.db");
      context.deleteDatabase("webviewCache.db");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void deleteFile(File file) {
    if (file.exists()) {
      if (file.isFile()) {
        file.delete();
      } else if (file.isDirectory()) {
        File files[] = file.listFiles();
        for (int i = 0; i < files.length; i++) {
          deleteFile(files[i]);
        }
      }
      file.delete();
    }
  }
}
