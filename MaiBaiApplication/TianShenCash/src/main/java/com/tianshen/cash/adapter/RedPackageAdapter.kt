package com.tianshen.cash.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tianshen.cash.R
import com.tianshen.cash.model.WithDrawalsListBean
import kotlinx.android.synthetic.main.item_red_package_history.view.*

class RedPackageAdapter(private val itemBean: MutableList<WithDrawalsListBean>,
                        val onClick: (WithDrawalsListBean) -> Unit)
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

    class ViewHolder(itemView: View, val onClick: (WithDrawalsListBean) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bindData(itemBean: WithDrawalsListBean) {
            with(itemBean) {
                itemView.tv_item_red_package_history_title.text = itemBean.title;
                itemView.tv_item_red_package_history_time.text = itemBean.add_time_string
                itemView.tv_item_red_package_history_money.text = itemBean.money_string
                if ("3".equals(itemBean.status)) {
                    itemView.tv_item_red_package_history_withdrawals.visibility = View.VISIBLE
                } else {
                    itemView.tv_item_red_package_history_withdrawals.visibility = View.GONE
                }
            }
        }
    }

    fun setData(data: List<WithDrawalsListBean>) {
        itemBean.clear()
        itemBean.addAll(data)
    }

}