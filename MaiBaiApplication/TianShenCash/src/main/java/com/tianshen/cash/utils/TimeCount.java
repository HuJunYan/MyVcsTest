package com.tianshen.cash.utils;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;

import com.tianshen.cash.R;

public class TimeCount extends CountDownTimer {
    private Button button;
    private TextView textView;
    private String msg;
    private ColorStateList color;
    private boolean isChangeColor = true;

    public TimeCount(long millisInFuture, long countDownInterval) {
        this(null, millisInFuture, countDownInterval, "");
    }

    public TimeCount(TextView textView, long millisInFuture, long countDownInterval, String msg, boolean isChangeColor) {
        this(textView, millisInFuture, countDownInterval, msg);
        this.isChangeColor = isChangeColor;
    }


    public TimeCount(TextView textView, long millisInFuture, long countDownInterval, String msg) {
        super(millisInFuture, countDownInterval);
        if (textView != null) {
            textView.setClickable(false);
        }
        if (textView != null) {
            this.color = textView.getTextColors();
        }
        this.textView = textView;
        this.msg = msg;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (textView != null) {
            if (isChangeColor) {
                textView.setTextColor(Color.GRAY);
                textView.setText(millisUntilFinished / 1000 + "");
            }
            if (!isChangeColor) {
                textView.setBackground(textView.getContext().getResources().getDrawable(R.drawable.shape_login_right_background2));
                textView.setText(millisUntilFinished / 1000 + "S后重发");
            }
        }
    }

    @Override
    public void onFinish() {
        if (textView != null) {
            textView.setClickable(true);
            textView.setTextColor(color);
            textView.setText(msg);
            if (!isChangeColor) {
                textView.setBackground(textView.getContext().getResources().getDrawable(R.drawable.shape_verify_code));
            }
        }
    }

    public void finish() {
        mHandler.sendEmptyMessage(0);
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            TimeCount.this.onFinish();
            TimeCount.this.cancel();
        }
    };
}
