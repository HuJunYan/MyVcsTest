package com.tianshen.cash.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.event.BankSelectedChangeEvent;
import com.tianshen.cash.utils.SpannableUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by wang on 2017/11/24.
 */

public class RepayBankItemView extends LinearLayout implements View.OnClickListener {

    private ImageView mIvBankIcon;
    private TextView mTvBankInfo;
    private ImageView mIvCheck;
    private int itemPosition;
    private boolean isAliPay;
    private TextView mTvAlipayInfo;

    public RepayBankItemView(Context context) {
        this(context, null);
    }

    public RepayBankItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RepayBankItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        EventBus.getDefault().register(this);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_repay_bank_item, null, false);
        addView(view);
        mIvBankIcon = (ImageView) view.findViewById(R.id.iv_repay_bank_icon);
        mTvBankInfo = (TextView) view.findViewById(R.id.tv_repay_bank_name_and_card);
        mIvCheck = (ImageView) view.findViewById(R.id.iv_repay_bank_check);
        mTvAlipayInfo = (TextView) view.findViewById(R.id.tv_repay_bank_alipay_info);
        setOnClickListener(this);
    }

    public RepayBankItemView setData(String bankInfo, int leftResId, int currentPosition, int itemPosition) {
        mTvBankInfo.setText(bankInfo);
        if (leftResId != -1){
            mIvBankIcon.setImageResource(leftResId);
        }
        this.itemPosition = itemPosition;
        if (itemPosition == currentPosition) {
            mIvCheck.setBackgroundResource(R.drawable.repay_circle_selected);
        } else {
            mIvCheck.setBackgroundResource(R.drawable.repay_circle_unselected);
        }
        return this;
    }

    public RepayBankItemView setAliPayData(String name, String money, int itemPosition, int currentPosition) {
        mIvBankIcon.setImageResource(R.drawable.alipay_icon);
        isAliPay = true;
        String format = String.format(getResources().getString(R.string.text_repay_bank_item_info), money);

        mTvBankInfo.setText(name);
        mTvAlipayInfo.setVisibility(View.VISIBLE);
//        mTvAlipayInfo.setText(format);
        SpannableUtils.setSpannableStringColor(mTvAlipayInfo, format, money, "元", getResources().getColor(R.color.global_txt_orange));
        this.itemPosition = itemPosition;
        if (itemPosition == currentPosition) {
            mIvCheck.setBackgroundResource(R.drawable.repay_circle_selected);
        } else {
            mIvCheck.setBackgroundResource(R.drawable.repay_circle_unselected);
        }
        return this;
    }

    @Subscribe
    public void onBankSelectedChangeEvent(BankSelectedChangeEvent event) {
        if (itemPosition == event.currentPosition) {
            mIvCheck.setBackgroundResource(R.drawable.repay_circle_selected);
        } else {
            mIvCheck.setBackgroundResource(R.drawable.repay_circle_unselected);
        }
    }

    @Override
    public void onClick(View v) {
        EventBus.getDefault().post(new BankSelectedChangeEvent(itemPosition, isAliPay));
    }
}
