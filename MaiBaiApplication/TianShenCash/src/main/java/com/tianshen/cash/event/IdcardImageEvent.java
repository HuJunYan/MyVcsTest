package com.tianshen.cash.event;

/**
 * Created by wang on 2017/11/7.
 */

public class IdcardImageEvent {
    public byte[] bytes;

    public IdcardImageEvent(byte[] bytes) {
        this.bytes = bytes;
    }
}
