package com.yu.share.callback;

import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * @author yudneghao
 * @date 2019/4/16
 */
public interface ShareCallback {

  public void onShareStart(SHARE_MEDIA shareMedia);

  public void onShareSuccess(SHARE_MEDIA media);

  public void onShareFailed(SHARE_MEDIA media, Throwable throwable);

  public void onShareCancel(SHARE_MEDIA shareMedia);
}
