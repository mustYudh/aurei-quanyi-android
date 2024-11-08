package com.aurei.quanyi.module.web.presenter

import com.aurei.quanyi.module.web.bea.UploadInfo
import com.yu.common.mvp.Viewer

/**
 * @author yudneghao
 * @date 2019-07-09
 */
interface WebViewViewer : Viewer {
    fun uploadImageSuccess(url: UploadInfo?)
}