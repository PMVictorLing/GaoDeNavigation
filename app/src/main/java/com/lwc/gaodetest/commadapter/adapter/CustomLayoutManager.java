package com.lwc.gaodetest.commadapter.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by lingwancai on
 * 2018/10/16 11:39
 */
public class CustomLayoutManager extends LinearLayoutManager {
    //此处我直接设置为不允许滑动，需要时可以使用setScrollEnabled（）动态设置
    private boolean isScrollEnabled = false;


    public CustomLayoutManager(Context context, int orientation) {
        super(context, orientation, false);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }

    @Override
    public boolean canScrollHorizontally() {
        return isScrollEnabled && super.canScrollHorizontally();
    }
}
