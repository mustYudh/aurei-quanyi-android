package com.yu.share;

import com.umeng.socialize.bean.SHARE_MEDIA;
import java.io.Serializable;

/**
 * @author yudneghao
 * @date 2019/4/15
 */
public class SharesBean implements Serializable{
  public SHARE_MEDIA type = SHARE_MEDIA.WEIXIN;
  public String title = "212123";
  public String content = "1235";
  public String targetUrl = "https://www.baidu.com";

  public String iconUrl = "https://maskball.oss-cn-beijing.aliyuncs.com/1/head_c0a387e1-e0d0-4957-a026-3e2357db8ae7.jpg";


}
