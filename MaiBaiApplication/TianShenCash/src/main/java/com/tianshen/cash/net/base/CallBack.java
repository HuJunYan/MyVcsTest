package com.tianshen.cash.net.base;

public abstract class CallBack
{
  public abstract void onSuccess(String result, String url);

  public abstract void onFailure(String result, int errorType, int errorCode);
}
