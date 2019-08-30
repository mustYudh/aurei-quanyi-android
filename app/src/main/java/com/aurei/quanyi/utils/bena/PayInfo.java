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
   * appId : wx860254f6f0a55fc6
   * nonceStr : 6b2ad39a1121484c9247c771523f5e33
   * package : Sign=WXPay
   * partnerId : 1530876981
   * prepayId : wx301621314489471ea78c026d0905693010
   * sign : 43CA1AE15EA8BCB6A13A1B36C40DA7230499D0A9C453419F03C18EACA4DC913E
   * timeStamp : 1556612491
   */

  public int lOrderId = -1;
  public String appId;
  public String nonceStr;
  @SerializedName("package")
  public String packageX;
  public String partnerId;
  public String prepayId;
  public String sign;
  public String timeStamp;
  public String sPaySign;
  public long s3rdUserId;
  public long s3rdOrderId;

  @Override
  public String toString() {
    return "{\n" +
            "  \"timeStamp\": " + timeStamp  + "," +
            "  \"package\":  " + packageX  + "," +
            "  \"appId\":  " + appId  + "," +
            "  \"sign\":  " + sign  + "," +
            "  \"prepayId\":  " + prepayId  + "," +
            "  \"partnerId\":  " + partnerId  + "," +
            "  \"nonceStr\":  " + nonceStr  + "," +
            "}";
  }
}
