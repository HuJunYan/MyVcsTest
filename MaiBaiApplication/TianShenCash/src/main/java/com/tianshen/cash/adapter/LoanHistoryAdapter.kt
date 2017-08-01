package com.tianshen.cash.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tianshen.cash.R
import com.tianshen.cash.model.WithdrawalsRecordItemBean
import kotlinx.android.synthetic.main.consumption_item.view.*

class LoanHistoryAdapter(private val bankListItemBean: MutableList<WithdrawalsRecordItemBean>,
                         val onClick: (WithdrawalsRecordItemBean) -> Unit)
    : RecyclerView.Adapter<LoanHistoryAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindData(bankListItemBean[position])
    }

    override fun getItemCount(): Int = if (bankListItemBean == null) 0 else bankListItemBean.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return LayoutInflater.from(parent?.context)
                .inflate(R.layout.consumption_item, parent, false).let {
            ViewHolder(it, onClick)
        }
    }

    class ViewHolder(itemView: View, val onClick: (WithdrawalsRecordItemBean) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bindData(itemBean: WithdrawalsRecordItemBean) {
            with(itemBean) {
                var status = itemBean.status
                itemView.tv_consume_status.text = status
            }
        }
    }

    fun setData(data: List<WithdrawalsRecordItemBean>) {
        bankListItemBean.clear()
        bankListItemBean.addAll(data)
    }

}