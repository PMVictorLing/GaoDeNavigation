package com.lwc.gaodetest;

import android.app.Application;

import com.lwc.gaodetest.http.HttpUtils;
import com.lwc.gaodetest.http.OkHttpEngine;

/**
 * Created by lingwancai on
 * 2018/9/29 16:31
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化网络引擎
        HttpUtils.init(new OkHttpEngine());
    }
}
