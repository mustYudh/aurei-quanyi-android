package com.aurei.quanyi.module.login

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import com.aurei.quanyi.R
import com.aurei.quanyi.base.BaseBarActivity
import com.aurei.quanyi.module.login.presenter.AutoLoginPresenter
import com.aurei.quanyi.module.login.presenter.AutoLoginViewer
import com.aurei.quanyi.utils.showToast
import com.yu.common.mvp.PresenterLifeCycle
import kotlinx.android.synthetic.main.activity_auto_login_layout.*


/**
 * @author yudneghao
 * @date 2019-07-09
 */
class AutoLoginActivity : BaseBarActivity(), View.OnClickListener,AutoLoginViewer {
    @PresenterLifeCycle
    private val mPresenter = AutoLoginPresenter(this)
    private var isAgree = true


    override fun setView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_auto_login_layout)
    }


    override fun loadData() {
        title = "SIM登录"
        initView()
        unified_argument_btn.setOnClickListener(this)
        unified_argument_btn.isSelected = true
        what_is_avoid_close_login.setOnClickListener(this)
        login.setOnClickListener(this)
        problem.setOnClickListener(this)
        normal_login.setOnClickListener(this)
    }

    private fun initView() {
        val str = "登录即同意《服务与隐私协议》，并授权澳雷 古雅获取本机号码"
        unified_argument.isSelected = true
        val spannableBuilder = SpannableStringBuilder(str)
        val colorSpan = ForegroundColorSpan(Color.parseColor("#47BBED"))
        spannableBuilder.setSpan(colorSpan, 5, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val clickableSpanOne: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
//                launchHelper.startActivity(MainWebViewActivity.callIntent(activity, "https:www.baidu.com"))
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = ds.linkColor
                ds.isUnderlineText = false
            }
        }
        spannableBuilder.setSpan(clickableSpanOne, 5, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        login_agreement.movementMethod = LinkMovementMethod.getInstance()
        login_agreement.highlightColor = Color.parseColor("#00FFFFFF")
        login_agreement.text = spannableBuilder
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.unified_argument_btn -> {
                unified_argument.isSelected = !isAgree
                isAgree = !isAgree
            }
            R.id.what_is_avoid_close_login -> {
//                launchHelper.startActivity(MainWebViewActivity.callIntent(activity, "https:www.baidu.com"))
            }
            R.id.login -> {
                if (isAgree) {
                    mPresenter.requestPreLogin()
                } else {
                    showToast("请先确认《服务与隐私协议》")
                }
            }
            R.id.problem -> {
//                launchHelper.startActivity(MainWebViewActivity.callIntent(activity, "https:www.baidu.com"))
            }
            R.id.normal_login -> {
                finish()
            }
        }
    }
}