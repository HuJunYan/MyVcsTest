package com.tianshen.cash.model;

import java.io.Serializable;

/**
 * Created by crslljj on 16/12/28.
 */

public class JpushLotOfVerifyStatusBean implements Serializable{
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
        private String consume_id; // 消费id

        public String getConsume_id() {
            return consume_id;
        }

        public void setConsume_id(String consume_id) {
            this.consume_id = consume_id;
        }
    }
}
