package com.yu.share;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.yu.share.callback.ShareCallback;

/**
 * @author yudneghao
 * @date 2019/4/16
 */
public class ShareHelp {

  private Activity mActivity;
  private ShareCallback callback;

  public ShareHelp(Activity activity) {
    mActivity = activity;
  }

  public ShareHelp share(SharesBean bean) {
    if (bean == null) {
      Toast.makeText(mActivity, "分享数据为空", Toast.LENGTH_SHORT).show();
    } else {
      ShareAction action = (new ShareAction(this.mActivity)).setPlatform(bean.type)
          .setCallback(new UMShareListener() {
            @Override public void onStart(SHARE_MEDIA shareMedia) {
              if (callback != null) {
                callback.onShareStart(shareMedia);
              }
            }

            @Override public void onResult(SHARE_MEDIA shareMedia) {
              if (callback != null) {
                callback.onShareSuccess(shareMedia);
              }
            }

            @Override public void onError(SHARE_MEDIA shareMedia, Throwable throwable) {
              if (callback != null) {
                callback.onShareFailed(shareMedia, throwable);
              }
            }

            @Override public void onCancel(SHARE_MEDIA shareMedia) {
              if (callback != null) {
                callback.onShareCancel(shareMedia);
              }
            }
          });
      if (action != null) {
        UMWeb web = null;
        if (bean.targetUrl != null) {
          web = new UMWeb(bean.targetUrl);
          if (!TextUtils.isEmpty(bean.title)) {
            web.setTitle(bean.title);
          }
          if (!TextUtils.isEmpty(bean.iconUrl)) {
            web.setThumb(new UMImage(this.mActivity, bean.iconUrl));
          }
          if (!TextUtils.isEmpty(bean.content)) {
            web.setDescription(bean.content);
          }
        }
        if (web != null) {
          action.withMedia(web).share();
        } else {
          action.share();
        }
      }
    }
    return this;
  }

  public ShareHelp callback(ShareCallback callback) {
    this.callback = callback;
    return this;
  }
}
