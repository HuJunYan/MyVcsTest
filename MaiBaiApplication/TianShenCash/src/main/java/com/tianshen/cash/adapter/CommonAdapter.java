package com.tianshen.cash.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected Handler mHandler;

    public CommonAdapter(Context context, List<T> datas) {
        this(context, datas, null);
    }

    public CommonAdapter(Context context, List<T> datas, Handler handler) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = datas;
        this.mHandler = handler;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setList(List<T> datas) {
        this.mDatas = datas;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

}
