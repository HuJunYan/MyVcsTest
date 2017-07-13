package com.tianshen.cash.activity

import android.support.v7.widget.LinearLayoutManager
import com.github.ui.adapter.RedPackageAdapter
import com.tianshen.cash.R
import com.tianshen.cash.base.BaseActivity
import com.tianshen.cash.model.RedPackageHistoryBean
import com.tianshen.cash.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_red_package.*


class RedPackageActivity : BaseActivity() {

    private var mAdapter: RedPackageAdapter? = null

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

        initRecyclerview()
    }


    private fun initRecyclerview() {


        var data = initData();

        if (mAdapter == null) {
            xrecyclerview_red_package.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            xrecyclerview_red_package.setPullRefreshEnabled(false)
            xrecyclerview_red_package.setLoadingMoreEnabled(false)

            mAdapter = RedPackageAdapter(data, {
            })
            xrecyclerview_red_package.adapter = mAdapter
        } else {
            mAdapter?.setData(data)
            mAdapter?.notifyDataSetChanged()
        }
    }

    /**
     * 模拟一些数据
     */
    private fun initData(): MutableList<RedPackageHistoryBean> {
        var bean1 = RedPackageHistoryBean()
        bean1.money = "500"
        bean1.is_withdrawals = "1"
        bean1.time = "2017/05/03 10:00"
        bean1.title = "提现"
        bean1.type = "0"

        var bean2 = RedPackageHistoryBean()
        bean2.money = "10"
        bean2.is_withdrawals = "0"
        bean2.time = "2017/05/03 10:00"
        bean2.title = "邀请注册-13123123123"
        bean2.type = "1"

        var bean3 = RedPackageHistoryBean()
        bean3.money = "20"
        bean3.is_withdrawals = "0"
        bean3.time = "2017/05/03 10:00"
        bean3.title = "邀请注册-13123123123"
        bean3.type = "1"
        return mutableListOf<RedPackageHistoryBean>(bean1, bean2, bean3)
    }

}
