package com.github.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.tianshen.cash.R
import com.tianshen.cash.model.GetBankListItemBean
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.bank_card_list_item.view.*
import java.util.concurrent.TimeUnit

class MyBankCardAdapter(private val bankListItemBean: MutableList<GetBankListItemBean>,
                        val onClick: (GetBankListItemBean) -> Unit)
    : RecyclerView.Adapter<MyBankCardAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindData(bankListItemBean[position])
    }

    override fun getItemCount(): Int = if (bankListItemBean == null) 0 else bankListItemBean.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return LayoutInflater.from(parent?.context)
                .inflate(R.layout.bank_card_list_item, parent, false).let {
            ViewHolder(it, onClick)
        }
    }

    class ViewHolder(itemView: View, val onClick: (GetBankListItemBean) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bindData(bankListItemBean: GetBankListItemBean) {
            with(bankListItemBean) {
                itemView.bank_name.text = bankListItemBean.bank_name
                val bankCardNum = bankListItemBean.getCard_num()
                if (bankCardNum.length < 4) {
                    itemView.bank_card_num.text = bankCardNum
                } else {
                    itemView.bank_card_num.text = bankCardNum.substring(bankCardNum.length - 4, bankCardNum.length)
                }
                RxView.clicks(itemView)//1秒钟之内禁用重复点击
                        .throttleFirst(1, TimeUnit.SECONDS)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            onClick(this)
                        }

            }
        }
    }

    fun setData(data: List<GetBankListItemBean>) {
        bankListItemBean.clear()
        bankListItemBean.addAll(data)
    }

}