package com.tianshen.cash.fragment

import android.view.View
import com.tianshen.cash.R
import com.tianshen.cash.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_auth_center_menu.*

class AuthCenterMenuFragment : BaseFragment() {

    override fun setContentView() = R.layout.fragment_auth_center_menu

    override fun findViews(rootView: View) {

    }

    override fun setListensers() {
    }

    override fun initView() {
    }

    override fun initData() {
    }

    fun setPicAndTxt(id: Int, txt: String) {
        tv_auth_center_menu_step.text = txt
        iv_auth_center_menu_step.setImageResource(id)
    }

}