package com.aurei.quanyi.module.login.presenter

import android.app.Activity
import android.util.Log
import cn.com.chinatelecom.account.sdk.AuthPageConfig
import cn.com.chinatelecom.account.sdk.CtAuth
import com.aurei.quanyi.R
import com.aurei.quanyi.http.loading.NetLoadingDialog
import com.aurei.quanyi.utils.showToast
import com.yu.common.framework.BaseViewPresenter
import org.json.JSONObject


/**
 * @author yudneghao
 * @date 2019-07-09
 */
class AutoLoginPresenter(viewer: AutoLoginViewer) : BaseViewPresenter<AutoLoginViewer>(viewer) {
    fun requestPreLogin() {
        NetLoadingDialog.showLoading(activity!!, false)
        CtAuth.getInstance().requestPreLogin(null) {
            NetLoadingDialog.dismissLoading()
            val configBuilder = AuthPageConfig.Builder()
                //设置“登录界面”的布局文件ID
                .setAuthActivityLayoutId(R.layout.ct_account_auth_activity)
                //设置“登录界面”的控件ID
                .setAuthActivityViewIds(
                    R.id.ct_account_nav_goback, //导航栏返回按钮ID
                    R.id.ct_account_desensphone, //脱敏号码文本控件ID
                    R.id.ct_account_brand_view, //品牌标识文本控件ID
                    R.id.ct_account_login_btn, //登录按钮控件ID
                    R.id.ct_account_login_loading, //登录加载中控件ID（必须为ImageView控件）
                    R.id.ct_account_login_text, //登录按钮文本控件ID
                    R.id.ct_account_other_login_way, //其他登录方式控件ID
                    R.id.ct_auth_privacy_checkbox, //隐私协议勾选框控件ID
                    R.id.ct_auth_privacy_text
                )  //“服务与隐私协议”文本控件ID

                //设置隐私协议对话框的布局文件ID
                .setPrivacyDialogLayoutId(R.layout.ct_account_privacy_dialog)
                //设置隐私协议对话框的控件ID
                .setPrivacyDialogViewIds(
                    R.id.ct_account_dialog_link, //“服务与隐私协议”文本控件ID
                    R.id.ct_account_dialog_cancel, // 返回按钮控件ID
                    R.id.ct_account_dialog_confirm
                ) //确认按钮控件ID

                //设置隐私协议WebviewActivity的布局文件ID
                .setWebviewActivityLayoutId(R.layout.ct_account_privacy_webview_activity)
                //设置隐私协议界面的控件ID
                .setWebviewActivityViewIds(
                    R.id.ct_account_webview_goback, //导航栏返回按钮ID
                    R.id.ct_account_progressbar_gradient, //进度条控件ID（ProgressBar控件）
                    R.id.ct_account_webview
                )


            val authPageConfig = configBuilder.build()

            CtAuth.getInstance().openAuthActivity(activity, authPageConfig) { result ->
                val json = JSONObject(result)
                val code = json.optInt("result")
                val message = json.optString("msg")
                val data = json.optJSONObject("data")
                when (code) {
                    80201 -> activity.finish()
                    0 -> {
                        if (data != null) {
                            val accessCode: String = data.optString("accessCode")
                            val expiredTime: Long = data.optLong("expiredTime")
                            val operatorType: String = data.optString("operatorType")
                            val authCode: String = data.optString("authCode")
                            Log.e("======>","accessCode:$accessCode,expiredTime:$expiredTime,operatorType:$operatorType,authCode:$authCode")
                        }
                        activity.setResult(Activity.RESULT_OK)
                        activity.finish()

                    }
                    else -> showToast(message)
                }
                CtAuth.getInstance().finishAuthActivity()
            }
        }
    }


}