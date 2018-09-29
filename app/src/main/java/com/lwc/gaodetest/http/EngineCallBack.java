package com.lwc.gaodetest.http;

/**
 * Created by lingwancai on
 * 2018/9/29 14:56
 */
public interface EngineCallBack {
    //错误
    void OnError(Exception e);

    //成功
    void Success(String result);


    //默认
    public final EngineCallBack DEFUALT_CALL_BACK = new EngineCallBack() {
        @Override
        public void OnError(Exception e) {

        }

        @Override
        public void Success(String result) {

        }
    };
}
