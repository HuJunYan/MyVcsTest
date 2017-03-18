package com.maibei.merchants.model;

import java.io.Serializable;

public class ResponseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int SUCCESS = 0; //成功
	private int code;  // 返回码
	private String msg;  // 消息内容

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
