package com.yu.share;

import android.app.Activity;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yu.share.callback.AuthLoginCallback;
import java.util.Map;

/**
 * @author yudneghao
 * @date 2019/4/16
 */
public class AuthLoginHelp {
  private Activity activity;
  private AuthLoginCallback mCallback;

  public AuthLoginHelp(Activity activity) {
    this.activity = activity;
  }

  public AuthLoginHelp login(SHARE_MEDIA media) {
    if (activity != null) {
      UMAuthListener listener = new UMAuthListener() {
        @Override public void onStart(SHARE_MEDIA media) {
          if (mCallback != null) {
            mCallback.onStart(media);
          }
        }

        @Override public void onComplete(SHARE_MEDIA media, int i, Map<String, String> map) {
          if (mCallback != null) {
            mCallback.onComplete(media, i, map);
          }
        }

        @Override public void onError(SHARE_MEDIA media, int i, Throwable throwable) {
          if (mCallback != null) {
            mCallback.onError(media, i, throwable);
          }
        }

        @Override public void onCancel(SHARE_MEDIA media, int i) {
          if (mCallback != null) {
            mCallback.onCancel(media, i);
          }
        }
      };
      UMShareAPI.get(activity).getPlatformInfo(activity, media, listener);
    }
    return this;
  }

  public AuthLoginHelp callback(AuthLoginCallback callback) {
    this.mCallback = callback;
    return this;
  }
}
