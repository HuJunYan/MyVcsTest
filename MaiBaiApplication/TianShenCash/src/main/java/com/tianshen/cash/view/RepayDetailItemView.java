package com.tianshen.cash.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.model.UserRepayDetailBean;

/**
 * Created by Administrator on 2017/8/25.
 */

public class RepayDetailItemView extends LinearLayout {

    private TextView tv_dialog_title;
    private TextView tv_dialog_value;
    private TextView tv_dialog_value2;
    private View view_base_line;

    public RepayDetailItemView(Context context) {
        this(context, null);
    }

    public RepayDetailItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RepayDetailItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_dialog_repay_detail_item, null, false);
        addView(view);
        tv_dialog_title = (TextView) view.findViewById(R.id.tv_dialog_title);
        tv_dialog_value = (TextView) view.findViewById(R.id.tv_dialog_value);
        tv_dialog_value2 = (TextView) view.findViewById(R.id.tv_dialog_value2);
        view_base_line = view.findViewById(R.id.view_base_line);
    }

    public RepayDetailItemView setData(UserRepayDetailBean.DetailInfo detailInfo) {
        tv_dialog_title.setText(detailInfo.title);
        tv_dialog_value.setText(detailInfo.value);
        if (detailInfo.value2 != null) {
            tv_dialog_value2.setText(detailInfo.value2);
            tv_dialog_value2.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public RepayDetailItemView isLast() {
        tv_dialog_title.setTextColor(getResources().getColor(R.color.global_txt_black1));
        tv_dialog_value.setTextColor(getResources().getColor(R.color.red));
        view_base_line.setVisibility(View.GONE);
        return this;
    }
}
