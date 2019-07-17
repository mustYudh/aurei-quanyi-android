package com.aurei.quanyi.module.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.aurei.quanyi.R
import com.aurei.quanyi.base.BaseActivity
import com.aurei.quanyi.module.login.presenter.LoginPresenter
import com.aurei.quanyi.module.login.presenter.LoginViewer
import com.aurei.quanyi.utils.getEditText
import com.aurei.quanyi.utils.getPassword
import com.aurei.quanyi.utils.registerUrl
import com.qianli.housekeeper.data.PublicProfile
import com.yu.common.mvp.PresenterLifeCycle
import com.yu.common.navigation.StatusBarFontColorUtil
import kotlinx.android.synthetic.main.activity_login_layout.*

class LoginActivity : BaseActivity(), View.OnClickListener, LoginViewer {

    @PresenterLifeCycle
    private val mPresenter = LoginPresenter(this)
    private val OPEN_AUTO_LOGIN_REQUEST_CODE = 1


    override fun setView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_login_layout)
        StatusBarFontColorUtil.statusBarDarkMode(activity)
    }


    companion object {
        private var loginCallBack: (() -> Unit)? = null
        private var backCallBack: (() -> Unit)? = null
        fun getIntent(context: Context, call: () -> Unit, back: (() -> Unit)?): Intent {
            this.loginCallBack = call
            this.backCallBack = back
            return Intent(context, LoginActivity::class.java)
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
                mPresenter.login(user_account.getEditText(), password.getEditText(), loginCallBack)
            }
            R.id.register -> {
                registerUrl(activity)
            }
            R.id.forget_password -> {
                getPassword(activity)
            }
            R.id.sim_login -> {
                launchHelper.startActivityForResult(AutoLoginActivity::class.java, OPEN_AUTO_LOGIN_REQUEST_CODE)
            }
            R.id.back -> {
                if (backCallBack != null) {
                    backCallBack?.let {
                        it()
                        finish()
                    }
                } else {
                    finish()
                }


            }
        }
    }

    override fun onBackPressed() {
        if (backCallBack != null) {
            backCallBack?.let {
                it()
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == OPEN_AUTO_LOGIN_REQUEST_CODE) {
            finish()
        }
    }


}
