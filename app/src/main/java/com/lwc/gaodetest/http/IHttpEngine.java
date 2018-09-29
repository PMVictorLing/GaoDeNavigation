package com.lwc.gaodetest.http;

import java.util.Map;

/**
 * 引擎的规范
 * <p>
 * Created by lingwancai on
 * 2018/9/29 14:53
 */
public interface IHttpEngine {
    //get请求
    void get(String url, Map<String, Object> params, EngineCallBack callBack);

    //post请求
    void post(String url, Map<String, Object> params, EngineCallBack callBack);

    //文件下载

    //文件上传

    //https 添加证书
}
