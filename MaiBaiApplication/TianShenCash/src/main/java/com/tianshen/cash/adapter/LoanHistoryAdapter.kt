package com.tianshen.cash.adapter

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tianshen.cash.R
import com.tianshen.cash.model.WithdrawalsRecordItemBean
import com.tianshen.cash.utils.MoneyUtils
import kotlinx.android.synthetic.main.item_loan_history.view.*

class LoanHistoryAdapter(private var bankListItemBean: MutableList<WithdrawalsRecordItemBean>,
                         val onClick: (WithdrawalsRecordItemBean) -> Unit)
    : RecyclerView.Adapter<LoanHistoryAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindData(bankListItemBean[position])
    }

    override fun getItemCount(): Int = bankListItemBean.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return LayoutInflater.from(parent?.context).inflate(R.layout.item_loan_history, parent, false).let {
            ViewHolder(it, onClick)
        }
    }

    class ViewHolder(itemView: View, val onClick: (WithdrawalsRecordItemBean) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bindData(itemBean: WithdrawalsRecordItemBean) {
            with(itemBean) {
                var amount = itemBean.amount
                if (TextUtils.isEmpty(amount)) {
                    amount = "0"
                }
                itemView.tv_loan_money.text = "¥" + MoneyUtils.changeF2Y(amount)
                var consume_time = itemBean.consume_time
                if (TextUtils.isEmpty(consume_time)) {
                    consume_time = ""
                }
                itemView.tv_loan_time.text = consume_time
                val status = itemBean.status
                when (status) {
//                    "0" 新用户，没有提交过订单；
                    "1" -> itemView.tv_loan_status.text = "待审核"
                    "2" -> itemView.tv_loan_status.text = "审核通过"
                    "3" -> itemView.tv_loan_status.text = "放款成功"
                    "4" -> itemView.tv_loan_status.text = "审核失败"
                    "5" -> itemView.tv_loan_status.text = "放款失败"
                    "6" -> itemView.tv_loan_status.text = "放款中"
                    "7" -> itemView.tv_loan_status.text = "已还款"
                    "8" -> itemView.tv_loan_status.text = "提交还款"
                    "9" -> itemView.tv_loan_status.text = "审核失败"
                    "10" -> itemView.tv_loan_status.text = "订单取消"
                    "12" -> itemView.tv_loan_status.text = "还款失败"
                }
            }
        }
    }

    fun setData(data: List<WithdrawalsRecordItemBean>) {
        bankListItemBean.clear()
        bankListItemBean.addAll(data)
    }
}