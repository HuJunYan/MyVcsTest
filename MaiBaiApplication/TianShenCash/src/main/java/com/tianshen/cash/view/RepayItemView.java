package com.tianshen.cash.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianshen.cash.R;

/**
 * Created by wang on 2017/11/23.
 */

public class RepayItemView extends LinearLayout {

    private TextView tv_repay_item_title;
    private TextView tv_repay_item_money;

    public RepayItemView(Context context) {
        this(context, null);
    }

    public RepayItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RepayItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_repay_item, null, false);
        addView(view);
        tv_repay_item_title = (TextView) view.findViewById(R.id.tv_repay_item_title);
        tv_repay_item_money = (TextView) view.findViewById(R.id.tv_repay_item_money);
    }
    public RepayItemView setData(String title,String money){
        tv_repay_item_title.setText(title);
        tv_repay_item_money.setText(money);
        return this;
    }
}
