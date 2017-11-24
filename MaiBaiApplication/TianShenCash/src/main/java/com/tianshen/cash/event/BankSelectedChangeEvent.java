package com.tianshen.cash.event;

/**
 * Created by wang on 2017/11/24.
 */

public class BankSelectedChangeEvent {

    public int currentPosition;
    public boolean isAlipay;
    public BankSelectedChangeEvent(int currentPosition,boolean isAlipay){
        this.currentPosition = currentPosition;
        this.isAlipay = isAlipay;
    }
}
