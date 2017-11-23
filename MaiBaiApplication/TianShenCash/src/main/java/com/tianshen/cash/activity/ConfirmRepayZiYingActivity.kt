package com.tianshen.cash.activity

import android.os.Bundle
import android.view.KeyEvent
import com.tianshen.cash.R
import com.tianshen.cash.base.BaseActivity
import com.tianshen.cash.event.RepayFailureEvent
import kotlinx.android.synthetic.main.activity_confirm_repay_zi_ying.*
import org.greenrobot.eventbus.EventBus

class ConfirmRepayZiYingActivity : BaseActivity() {
    override fun setContentView() = R.layout.activity_confirm_repay_zi_ying

    override fun findViews() {
    }

    override fun setListensers() {
        tv_confirm_money_back.setOnClickListener { EventBus.getDefault().post(RepayFailureEvent());backActivity() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            EventBus.getDefault().post(RepayFailureEvent())
        }
        return super.onKeyDown(keyCode, event)
    }
}
