package com.tianshen.cash.activity

import com.tianshen.cash.R
import com.tianshen.cash.base.BaseActivity
import kotlinx.android.synthetic.main.activity_auth_center_menu.*

/**
 * Created by cuiyue on 2017/11/6.
 */
class AuthCenterMenuActivity : BaseActivity() {

    override fun setContentView() = R.layout.activity_auth_center_menu

    override fun findViews() {
    }

    override fun setListensers() {
        tv_auth_center_menu_back.setOnClickListener { backActivity() }
        tv_identity.setOnClickListener {
            gotoActivity(mContext, RiskPreAuthIdentityActivity::class.java, null)
        }
        tv_person_info.setOnClickListener {
            gotoActivity(mContext, AuthMyInfoActivity::class.java, null)
        }
        tv_credit.setOnClickListener {
            gotoActivity(mContext, AuthMyInfoActivity::class.java, null)
        }
    }

}