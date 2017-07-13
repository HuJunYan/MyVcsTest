package com.tianshen.cash.activity

import com.tianshen.cash.R
import com.tianshen.cash.base.BaseActivity
import com.tianshen.cash.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_red_package.*

class RedPackageActivity : BaseActivity() {

    override fun setContentView(): Int = R.layout.activity_red_package

    override fun findViews() {
    }

    override fun setListensers() {

        tv_red_package_back.setOnClickListener {
            backActivity()
        }

        tv_get_red_package.setOnClickListener {
            ToastUtil.showToast(mContext, "点击了提现")
        }
    }

}
