package com.tianshen.cash.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.tianshen.cash.R
import com.tianshen.cash.model.RequiredBean
import com.tianshen.cash.utils.ImageLoader
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.item_not_required.view.*
import java.util.concurrent.TimeUnit

class AuthNotRequiredAdapter(private var messageBeans: MutableList<RequiredBean>,
                             val onClick: (RequiredBean) -> Unit)
    : RecyclerView.Adapter<AuthNotRequiredAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindData(messageBeans[position])
    }

    override fun getItemCount(): Int = messageBeans.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return LayoutInflater.from(parent?.context).inflate(R.layout.item_not_required, parent, false).let {
            ViewHolder(it, onClick)
        }
    }

    class ViewHolder(itemView: View, val onClick: (RequiredBean) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bindData(itemBean: RequiredBean) {
            with(itemBean) {
                itemView.tv_item_not_required_name.text = itemBean.credit_name

                when (status) {
                    "0" -> itemView.iv_item_not_required_status.setImageResource(R.drawable.ic_arraw_right2)
                    "1" -> itemView.iv_item_not_required_status.setImageResource(R.drawable.authed_statue)
                }

                ImageLoader.load(itemView.context, itemBean.icon, itemView.iv_item_not_required_icon)
                RxView.clicks(itemView.rl_item_not_required_root)//1秒钟之内禁用重复点击
                        .throttleFirst(1, TimeUnit.SECONDS)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            onClick(this)
                        }

            }
        }
    }

    fun setData(data: MutableList<RequiredBean>) {
        messageBeans = data
    }
}