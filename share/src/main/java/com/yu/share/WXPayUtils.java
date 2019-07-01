package com.yu.share;

import android.content.Context;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * @author yudneghao
 * @date 2019-04-30
 */
public class WXPayUtils {

  private static IWXAPI api;

  private static final WXPayUtils ourInstance = new WXPayUtils();

  public static WXPayUtils getInstance(Context context) {
    init(context);
    return ourInstance;
  }

  private static void init(Context context) {
    api = WXAPIFactory.createWXAPI(context, Key.WX_APP_ID, true);
    api.registerApp(Key.WX_APP_ID);
  }

  private WXPayUtils() {

  }

  public void sendRequest(final PayReq request) {
    Runnable payRunnable = new Runnable() {
      @Override
      public void run() {
        api.sendReq(request);//发送调起微信的请求
      }
    };
    Thread payThread = new Thread(payRunnable);
    payThread.start();
  }


}
