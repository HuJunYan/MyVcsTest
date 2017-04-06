package com.tianshen.cash.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;

public class TimeCount extends CountDownTimer {
    private Button button;
    private TextView textView;
    private String msg;

    public TimeCount(long millisInFuture, long countDownInterval) {
        this(null, millisInFuture, countDownInterval, "");
    }

    public TimeCount(TextView textView, long millisInFuture, long countDownInterval, String msg) {
        super(millisInFuture, countDownInterval);
        if (textView != null) {
            textView.setClickable(false);
        }
        this.textView = textView;
        this.msg = msg;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (textView != null) {
            textView.setTextColor(Color.GRAY);
            textView.setText(millisUntilFinished / 1000 + "");
        }
    }

    @Override
    public void onFinish() {
        if (textView != null) {
            textView.setClickable(true);
            textView.setTextColor(Color.parseColor("#FF1FA5DF"));
            textView.setText(msg);
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
