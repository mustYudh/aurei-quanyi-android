package com.aurei.quanyi.module.web.presenter

import android.annotation.SuppressLint
import com.aurei.quanyi.http.lifecycle.RxLifecycle
import com.aurei.quanyi.http.loading.NetLoadingDialog
import com.aurei.quanyi.module.web.bea.UploadInfo
import com.qianchang.optimizetax.http.subscriber.TipRequestSubscriber
import com.xuexiang.xhttp2.XHttp
import com.xuexiang.xhttp2.exception.ApiException
import com.yu.common.framework.BaseViewPresenter
import java.io.File


/**
 * @author yudneghao
 * @date 2019-07-09
 */
class WebViewPresenter(viewer: WebViewViewer) : BaseViewPresenter<WebViewViewer>(viewer) {

    @SuppressLint("CheckResult")
    fun uploadImage(file: File) {
        NetLoadingDialog.showLoading(activity, false)
        XHttp.post("/web/user_info/upload")
            .uploadFile(
                "file", file
            ) { _, _, _ -> }.execute(UploadInfo::class.java)
            .compose(RxLifecycle.with(activity).bindToLifecycle<UploadInfo>()).subscribeWith(
                object : TipRequestSubscriber<UploadInfo>() {
                    override fun onSuccess(t: UploadInfo?) {
                        getViewer()?.uploadImageSuccess(t?.url!!)
                        NetLoadingDialog.dismissLoading()
                    }

                    override fun onError(e: ApiException) {
                        super.onError(e)
                        NetLoadingDialog.dismissLoading()
                    }

                }
            )

    }
}

