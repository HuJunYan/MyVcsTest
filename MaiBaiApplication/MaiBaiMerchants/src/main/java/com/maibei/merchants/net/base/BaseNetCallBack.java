package com.maibei.merchants.net.base;

public abstract interface BaseNetCallBack<T>
{
  public abstract void onSuccess(T paramT);

  public abstract void onFailure(String url, int errorType, int errorCode);
}
