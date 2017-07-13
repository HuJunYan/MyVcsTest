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

/**
 * Created by Administrator on 2017/7/13.
 */

public class InviteRuleView extends FrameLayout {
    private ImageView iv_invite_rule;
    private TextView tv_invite_rule;

    public InviteRuleView(@NonNull Context context) {
        super(context);
        init();
    }

    public InviteRuleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InviteRuleView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_invite_rule, null);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        addView(view);
        iv_invite_rule = (ImageView) view.findViewById(R.id.iv_invite_rule);
        tv_invite_rule = (TextView) view.findViewById(R.id.tv_invite_rule);
    }

    public InviteRuleView setData(int resId, String ruleStr) {
        iv_invite_rule.setImageResource(resId);
        tv_invite_rule.setText(ruleStr);
        return this;
    }
}
