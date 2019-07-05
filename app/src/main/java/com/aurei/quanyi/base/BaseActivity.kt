package com.aurei.quanyi.base

import android.view.View
import com.umeng.analytics.MobclickAgent
import com.umeng.socialize.UMShareAPI
import com.yu.common.framework.BasicActivity

abstract class BaseActivity : BasicActivity() {

    override fun handleNetWorkError(view: View) {}

    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        UMShareAPI.get(this).release()
    }
}
