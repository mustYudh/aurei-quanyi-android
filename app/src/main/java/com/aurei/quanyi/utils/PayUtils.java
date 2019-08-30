package com.aurei.quanyi.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.aurei.quanyi.module.web.bea.PayResult;
import com.aurei.quanyi.utils.bena.PayInfo;
import com.aurei.quanyi.wxapi.WXPayEntryActivity;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yu.common.toast.ToastUtils;
import com.yu.share.WXPayUtils;

import java.util.Map;

/**
 * @author yudneghao
 * @date 2019-04-30
 */
@SuppressLint("CheckResult")
public class PayUtils {
    private static final PayUtils ourInstance = new PayUtils();
    public final static String WX_PAY_INFO = "wx_pay_info";
    public final static String PAY_TYPE = "pay_type";
    private RxCountDown looperTime = new RxCountDown();
    private Handler handler = new Handler();
    private PayCallBack payCallback;

    private boolean needCheck = false;
    private boolean isStart = false;


    public interface PayCallBack {
        void onPaySuccess(int type);

        void onFailed(int type);
    }

    public static PayUtils getInstance() {
        return ourInstance;
    }

    private PayUtils() {
    }

    private void startPay(Activity context, int type, PayInfo info) {
        if (type == 1) {
            if (checkAliPayInstalled(context)) {
                final Runnable payRunnable = () -> {
                    PayTask aliPay = new PayTask(context);
                    Map<String, String> result = aliPay.payV2(info.sPaySign, true);
                    PayResult payResult = new PayResult(result);
                    context.runOnUiThread(() -> {
                        String resultStatus = payResult.getResultStatus();
                        if (TextUtils.equals(resultStatus, "9000")) {
                            payCallback.onPaySuccess(type);
                        } else {
                            if (payCallback != null) {
                                payCallback.onFailed(type);
                            }
                        }
                    });
                };
                Thread payThread = new Thread(payRunnable);
                payThread.start();
            } else {
                ToastUtils.show("请先安装支付宝");
            }

        } else if (type == 2) {
            PayReq request = new PayReq();
            request.appId = info.appId;
            request.partnerId = info.partnerId;
            request.prepayId = info.prepayId;
            request.packageValue = info.packageX;
            request.nonceStr = info.nonceStr;
            request.timeStamp = info.timeStamp;
            request.sign = info.sign;
            WXPayUtils.getInstance(context).sendRequest(request);
        } else {
            needCheck = false;
            if (payCallback != null) {
                payCallback.onPaySuccess(type);
            }
        }

    }


    public PayUtils getPayResult(PayCallBack payCallback) {
        this.payCallback = payCallback;
        return this;
    }

    public PayCallBack getWXPayCallback() {
        return payCallback;
    }


    private void startWXPay(Activity context, int type, PayInfo info) {
        boolean installWeChat =
                UMShareAPI.get(context).isInstall(context, SHARE_MEDIA.WEIXIN);
        if (installWeChat) {
            Intent starter = new Intent(context, WXPayEntryActivity.class);
            starter.putExtra(WX_PAY_INFO, info);
            starter.putExtra(PAY_TYPE, type);
            context.startActivity(starter);
        } else {
            ToastUtils.show("请先安装微信");
        }
    }


    public PayUtils pay(Activity context, int type, PayInfo info) {
        needCheck = true;
        if (type == 2) {
            startWXPay(context, type, info);
        } else {
            startPay(context, type, info);
        }

        return this;
    }


    public PayUtils wxPay(Activity context, int type,PayInfo info) {
        startPay(context, type, info);
        return this;
    }


    private static boolean checkAliPayInstalled(Context context) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }


}
