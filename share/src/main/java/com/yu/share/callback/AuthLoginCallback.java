package com.yu.share.callback;

import com.umeng.socialize.bean.SHARE_MEDIA;
import java.util.Map;

/**
 * @author yudneghao
 * @date 2019/4/16
 */
public interface AuthLoginCallback {

  public void onStart(SHARE_MEDIA media);

  public void onComplete(SHARE_MEDIA media, int i, Map<String, String> map);

  public void onError(SHARE_MEDIA media, int i, Throwable throwable);

  public void onCancel(SHARE_MEDIA media, int i);
}
