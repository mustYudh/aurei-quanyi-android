package com.yu.share;

import com.umeng.socialize.bean.SHARE_MEDIA;
import java.io.Serializable;

/**
 * @author yudneghao
 * @date 2019/4/15
 */
public class SharesBean implements Serializable{
  public SHARE_MEDIA type;
  public String title;
  public String content;
  public String targetUrl;
  public String iconUrl;


}
