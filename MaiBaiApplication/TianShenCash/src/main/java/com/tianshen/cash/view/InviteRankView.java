package com.tianshen.cash.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianshen.cash.R;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/12.
 */

public class InviteRankView extends FrameLayout {
    ImageView iv_rank;
    TextView tv_rank_phone_number;
    TextView tv_rank_people_sum;
    TextView tv_rank_money_number;

    public InviteRankView(@NonNull Context context) {
        super(context);
        init();
    }

    public InviteRankView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InviteRankView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_invite_rank, null);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        addView(view);
        ButterKnife.bind(view);
        iv_rank = (ImageView) view.findViewById(R.id.iv_rank);
        tv_rank_money_number = (TextView) view.findViewById(R.id.tv_rank_money_number);
        tv_rank_phone_number = (TextView) view.findViewById(R.id.tv_rank_phone_number);
        tv_rank_people_sum = (TextView) view.findViewById(R.id.tv_rank_people_sum);
    }

    public InviteRankView setData(int resId, String phoneString, String peopleString, String moneyString) {
        iv_rank.setImageResource(resId);
        tv_rank_phone_number.setText(phoneString);
        tv_rank_people_sum.setText(peopleString);
        tv_rank_money_number.setText(moneyString);
        return this;
    }
}
