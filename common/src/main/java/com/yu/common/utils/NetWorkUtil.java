package com.yu.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;


public class NetWorkUtil {

  public static final String CONN_TYPE_WIFI = "wifi";
  public static final String CONN_TYPE_GPRS = "gprs";
  public static final String CONN_TYPE_NONE = "none";

  public static boolean isNetworkAvailable(Context context) {
    NetworkInfo networkInfo = getNetworkInfo(context);
    return networkInfo != null && networkInfo.isConnected();
  }

  /**
   * 判断wifi
   */
  public static boolean isWifi(Context context) {
    if (context == null) {
      return false;
    }
    NetworkInfo networkInfo = getNetworkInfo(context.getApplicationContext());
    return (networkInfo != null && networkInfo.isAvailable()) && (networkInfo.getType()
        == ConnectivityManager.TYPE_WIFI && networkInfo.getState() == NetworkInfo.State.CONNECTED);
  }

  private static NetworkInfo getNetworkInfo(Context context) {
    try {
      ConnectivityManager connManager =
          (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      if (connManager == null) {
        return null;
      }
      NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
      if (networkInfo == null && Build.VERSION.SDK_INT >= 23) {
        Network network = connManager.getActiveNetwork();
        networkInfo = connManager.getNetworkInfo(network);
      }
      return networkInfo;
    } catch (Exception var4) {
      return null;
    }
  }

  public static String getNetConnType(Context context) {
    ConnectivityManager connManager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connManager == null) {
      return CONN_TYPE_NONE;
    }
    NetworkInfo info = null;
    info = connManager.getNetworkInfo(1);
    if (info != null) {
      NetworkInfo.State wifiState = info.getState();
      if (NetworkInfo.State.CONNECTED == wifiState) {
        return CONN_TYPE_WIFI;
      }
    }
    info = connManager.getNetworkInfo(0);
    if (info != null) {
      NetworkInfo.State mobileState = info.getState();
      if (NetworkInfo.State.CONNECTED == mobileState) {
        return CONN_TYPE_GPRS;
      }
    }
    return CONN_TYPE_NONE;
  }
}
