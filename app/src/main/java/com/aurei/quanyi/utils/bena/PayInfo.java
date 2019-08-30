package com.aurei.quanyi.utils.bena;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author yudneghao
 * @date 2019-04-30
 */
public class PayInfo implements Serializable {

  /**
   * server_timestamp : 1556612491212
   * lOrderId : 109
   * appid : wx860254f6f0a55fc6
   * noncestr : 6b2ad39a1121484c9247c771523f5e33
   * package : Sign=WXPay
   * partnerid : 1530876981
   * prepayid : wx301621314489471ea78c026d0905693010
   * sign : 43CA1AE15EA8BCB6A13A1B36C40DA7230499D0A9C453419F03C18EACA4DC913E
   * timestamp : 1556612491
   */

  public long server_timestamp;
  public int lOrderId = -1;
  public String appid;
  public String noncestr;
  @SerializedName("package")
  public String packageX;
  public String partnerid;
  public String prepayid;
  public String sign;
  public String timestamp;
  public String sPaySign;
  public long s3rdUserId;
  public long s3rdOrderId;
}
