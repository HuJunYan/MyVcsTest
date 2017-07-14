package com.github.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tianshen.cash.R
import com.tianshen.cash.model.RedPackageHistoryBean
import kotlinx.android.synthetic.main.item_red_package_history.view.*

class RedPackageAdapter(private val itemBean: MutableList<RedPackageHistoryBean>,
                        val onClick: (RedPackageHistoryBean) -> Unit)
    : RecyclerView.Adapter<RedPackageAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindData(itemBean[position])
    }

    override fun getItemCount(): Int = if (itemBean == null) 0 else itemBean.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_red_package_history, parent, false).let {
            ViewHolder(it, onClick)
        }
    }

    class ViewHolder(itemView: View, val onClick: (RedPackageHistoryBean) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bindData(itemBean: RedPackageHistoryBean) {
            with(itemBean) {
                itemView.tv_item_red_package_history_title.text = itemBean.title;
                itemView.tv_item_red_package_history_time.text = itemBean.time
                var money = itemBean.money
                if ("0".equals(itemBean.type)) {
                    money = "-" + money
                } else if ("1".equals(itemBean.type)) {
                    money = "+" + money
                }
                itemView.tv_item_red_package_history_money.text = money

                if ("0".equals(itemBean.is_withdrawals)) {
                    itemView.tv_item_red_package_history_withdrawals.visibility = View.GONE
                } else if ("1".equals(itemBean.is_withdrawals)) {
                    itemView.tv_item_red_package_history_withdrawals.visibility = View.VISIBLE
                }
            }
        }
    }

    fun setData(data: List<RedPackageHistoryBean>) {
        itemBean.clear()
        itemBean.addAll(data)
    }

}