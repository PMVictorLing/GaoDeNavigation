package com.lwc.gaodetest;

import android.view.View;
import android.view.ViewGroup;

/**
 *
 * 指示的适配器
 *
 * Created by lingwancai on
 * 2018/11/1 09:20
 */
public abstract class IndicatorBaseAdapter {

    //返回条目
    public abstract int getCount();

    //根据position获取View
    public abstract View getView(int position, ViewGroup viewGroup);
}
