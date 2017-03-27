package com.tianshen.cash.model;

import java.io.Serializable;

/**
 * Created by crslljj on 17/1/12.
 */

public class JpushAddBorrowTermBean implements Serializable{
    private static final long serialVersionUID = 1L;
    private String msg_type; // 消费类型 1-审核结果（待审核订单）
    private MsgContent msg_content;

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public MsgContent getMsg_content() {
        return msg_content;
    }

    public void setMsg_content(MsgContent msg_content) {
        this.msg_content = msg_content;
    }

    public class MsgContent implements Serializable {
        private static final long serialVersionUID = 1L;
        private String borrow_time; // xx天

        public String getBorrow_time() {
            return borrow_time;
        }

        public void setBorrow_time(String borrow_time) {
            this.borrow_time = borrow_time;
        }
    }
}
