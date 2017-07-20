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
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val NORMAL_TYPE = 0
    private val FOOT_TYPE = 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is NormalViewHolder) {
            holder.bindData(itemBean[position])
        } else if (holder is FootViewHolder) {
            holder.bindData(itemBean[position])
        }
    }

    override fun getItemCount(): Int = if (itemBean == null) 0 else itemBean.size

    override fun getItemViewType(position: Int): Int {
        if (position == itemBean.size - 1) {
            return FOOT_TYPE
        }
        return NORMAL_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == NORMAL_TYPE) {
            return LayoutInflater.from(parent?.context).inflate(R.layout.item_red_package_history, parent, false).let {
                NormalViewHolder(it, onClick)
            }
        } else {
            return LayoutInflater.from(parent?.context).inflate(R.layout.layout_red_package_list_footer, parent, false).let {
                FootViewHolder(it, onClick)
            }
        }

    }

    class NormalViewHolder(itemView: View, val onClick: (WithDrawalsListBean) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bindData(itemBean: WithDrawalsListBean) {
            with(itemBean) {
                itemView.tv_item_red_package_history_title.text = itemBean.title;
                itemView.tv_item_red_package_history_time.text = itemBean.add_time_string
                itemView.tv_item_red_package_history_money.text = itemBean.money_string
                if ("3" == itemBean.status) {
                    itemView.tv_item_red_package_history_withdrawals.visibility = View.VISIBLE
                } else {
                    itemView.tv_item_red_package_history_withdrawals.visibility = View.GONE
                }
            }
        }
    }

    class FootViewHolder(itemView: View, val onClick: (WithDrawalsListBean) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bindData(itemBean: WithDrawalsListBean) {
            with(itemBean) {
            }
        }
    }

    fun setData(data: List<WithDrawalsListBean>) {
        itemBean.clear()
        itemBean.addAll(data)
    }

}