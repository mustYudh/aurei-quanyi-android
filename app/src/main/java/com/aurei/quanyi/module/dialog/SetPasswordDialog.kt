package com.aurei.quanyi.module.dialog

import android.app.Activity
import com.aurei.quanyi.R
import com.yu.common.windown.BaseDialog

/**
 * @author yudneghao
 * @date 2019-07-09
 */
class SetPasswordDialog(activity: Activity) : BaseDialog(activity) {
    init {
        setContentView(R.layout.dialog_set_password_layout)
    }
}