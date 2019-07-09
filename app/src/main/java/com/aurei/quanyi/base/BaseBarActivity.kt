package com.aurei.quanyi.base

import android.support.annotation.LayoutRes
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.aurei.quanyi.R
import com.yu.common.ui.BarIconContainer
import com.yu.common.utils.BarUtils

abstract class BaseBarActivity : BaseActivity() {

    protected val actionBarLayoutId: Int
        get() = R.layout.action_bar_white_web_view

    val isImmersionBar: Boolean
        get() = false

    override fun darkMode(): Boolean {
        return actionBarLayoutId == R.layout.action_bar_white_web_view
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        initBack()
    }

    override fun onReplaceRootView(@LayoutRes layoutResID: Int): View {
        val rootView = super.onReplaceRootView(layoutResID)
        val container = if (isImmersionBar)
            rootView.findViewById(R.id.immersion_action_bar_container)
        else
            rootView.findViewById<FrameLayout>(R.id.action_bar_container)
        container.visibility = View.VISIBLE
        val actionBar = layoutInflater.inflate(actionBarLayoutId, container, false)
        container.addView(actionBar)
        BarUtils.setActionBarLayout(actionBar)
        return rootView
    }

    private fun initBack() {
        val back = findViewById<BarIconContainer>(R.id.back)
        back?.setOnClickListener { v -> finish() }
    }

    override fun setTitle(titleName: CharSequence) {
        if (!TextUtils.isEmpty(titleName)) {
            val title = findViewById<View>(R.id.action_title) as TextView
            if (title != null) {
                title.text = titleName
            }
        }
    }
}
