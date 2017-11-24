package com.tianshen.cash.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianshen.cash.R;

/**
 * Created by wang on 2017/11/24.
 */

public class RepayBankItemView extends LinearLayout {

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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_repay_bank_item, null, false);
        addView(view);
        mIvBankIcon = (ImageView) view.findViewById(R.id.iv_repay_bank_icon);
        mTvBankInfo = (TextView) view.findViewById(R.id.tv_repay_bank_name_and_card);
        mIvCheck = (ImageView) view.findViewById(R.id.iv_repay_bank_check);
        mTvAlipayInfo = (TextView) view.findViewById(R.id.tv_repay_bank_alipay_info);

    }

    public RepayBankItemView setData(String bankInfo, @DrawableRes int leftResId, int currentPosition, int itemPosition) {
        mTvBankInfo.setText(bankInfo);
        mIvBankIcon.setImageResource(leftResId);
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
        mTvBankInfo.setText(name);
        mTvAlipayInfo.setVisibility(View.VISIBLE);
        mTvAlipayInfo.setText(money);
        this.itemPosition = itemPosition;
        if (itemPosition == currentPosition) {
            mIvCheck.setBackgroundResource(R.drawable.repay_circle_selected);
        } else {
            mIvCheck.setBackgroundResource(R.drawable.repay_circle_unselected);
        }
        return this;
    }
}
