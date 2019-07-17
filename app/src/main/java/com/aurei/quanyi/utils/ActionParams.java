package com.aurei.quanyi.utils;


import android.text.TextUtils;

import java.io.Serializable;
import java.util.HashMap;

public class ActionParams implements Serializable {

  private String action;
  private HashMap<String, String> params;

  private ActionParams(String action, HashMap<String, String> params) {
    this.action = action;
    this.params = params;
  }

  /**
   * 解析action
   */
  public static ActionParams parse(String urlStr) {
    if (TextUtils.isEmpty(urlStr)) {
      return new ActionParams("", null);
    }
    urlStr = urlStr.trim();

    int hasParamsIndex = urlStr.indexOf("?");
    if (hasParamsIndex >= 0) {
      HashMap<String, String> paramsMap = new HashMap<>();
      String actionHeader = urlStr.substring(0, hasParamsIndex);
      String[] ps = urlStr.substring(hasParamsIndex + 1).split("&");
      for (String p : ps) {
        String[] kvs = p.split("=");
        if (kvs.length == 2 && !TextUtils.isEmpty(kvs[0]) && !TextUtils.isEmpty(kvs[1])) {
          paramsMap.put(kvs[0].trim(), kvs[1].trim());
        }
      }
      return new ActionParams(actionHeader, paramsMap);
    } else {
      return new ActionParams(urlStr, null);
    }
  }

  public String getAction() {
    return action == null ? "" : action;
  }

  public HashMap<String, String> getParams() {
    return params;
  }

  public String get(String key, String defValue) {
    if (params != null) {
      return params.get(key) == null ? defValue : params.get(key);
    } else {
      return defValue;
    }
  }


}
