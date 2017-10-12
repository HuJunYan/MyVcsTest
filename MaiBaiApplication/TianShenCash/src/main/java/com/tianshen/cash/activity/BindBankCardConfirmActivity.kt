package com.tianshen.cash.activity

import com.tianshen.cash.R
import com.tianshen.cash.base.BaseActivity
import kotlinx.android.synthetic.main.activity_bind_bank_card_confirm.*

class BindBankCardConfirmActivity : BaseActivity() {
    override fun setContentView() = R.layout.activity_bind_bank_card_confirm

    override fun findViews() {

    }

    override fun setListensers() {
        tv_auth_bank_card_back.setOnClickListener { backActivity() }
    }

}
