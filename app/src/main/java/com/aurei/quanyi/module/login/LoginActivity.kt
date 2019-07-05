package com.aurei.quanyi.module.login

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.aurei.quanyi.R
import com.aurei.quanyi.base.BaseActivity
import com.aurei.quanyi.getEditText
import com.aurei.quanyi.module.login.presenter.LoginPresenter
import com.aurei.quanyi.module.login.presenter.LoginViewer
import com.aurei.quanyi.module.web.WebViewActivity
import com.qianli.housekeeper.data.PublicProfile
import com.yu.common.mvp.PresenterLifeCycle
import com.yu.common.navigation.StatusBarFontColorUtil
import kotlinx.android.synthetic.main.activity_login_layout.*

class LoginActivity : BaseActivity(), View.OnClickListener, LoginViewer {

    @PresenterLifeCycle
    private val mPresenter = LoginPresenter(this)



    override fun setView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_login_layout)
        StatusBarFontColorUtil.statusBarDarkMode(activity)
    }


    companion object {
        private var loginCallBack: (() -> Unit)? = null
        fun getIntent(context: Context,call: () -> Unit) {
            this.loginCallBack  = call
        }
    }

    override fun loadData() {
        login.setOnClickListener(this)
        register.setOnClickListener(this)
        forget_password.setOnClickListener(this)
        sim_login.setOnClickListener(this)
        back.setOnClickListener(this)
        if (!TextUtils.isEmpty(PublicProfile.userAccount)) {
            user_account.setText(PublicProfile.userAccount)
            user_account.setSelection(PublicProfile.userAccount?.length!!)
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.login -> {
                mPresenter.login(user_account.getEditText(), password.getEditText(),loginCallBack)
            }
            R.id.register -> {
                launchHelper.startActivity(WebViewActivity.callIntent(activity, "https://www.baidu.com"))
            }
            R.id.forget_password -> {
                launchHelper.startActivity(WebViewActivity.callIntent(activity, "https://www.baidu.com"))
            }
            R.id.sim_login -> {
                launchHelper.startActivity(WebViewActivity.callIntent(activity, "https://www.baidu.com"))
            }
            R.id.back -> {
                finish()
            }
        }
    }


}
