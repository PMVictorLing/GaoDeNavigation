package com.lwc.gaodetest.http;

import android.content.Context;
import android.util.ArrayMap;

import java.util.HashMap;
import java.util.Map;

/**
 * 自己的一套实现
 * <p>
 * Created by lingwancai on
 * 2018/9/29 14:53
 */
public class HttpUtils {

    //http
    private String mUrl = "";

    //请求方式
    public int mType = GET_REQUEST;
    public static final int GET_REQUEST = 0x0001;
    public static final int POST_REQUEST = 0x0002;

    private Map<String, Object> mParams;

    //上下文
    private Context mContext;

    private HttpUtils(Context mContext) {
        this.mContext = mContext;
        mParams = new HashMap<>();
    }

    //获取实例 -- 这里千万不要用单利，会有内存泄露
    public static HttpUtils with(Context context) {
        return new HttpUtils(context);
    }

    //采用链式调用
    public HttpUtils Url(String url) {
        mUrl = url;
        return this;
    }

    //采用链式调用
    public HttpUtils post() {
        mType = POST_REQUEST;
        return this;
    }

    //采用链式调用
    public HttpUtils get() {
        mType = GET_REQUEST;
        return this;
    }

    //添加一个参数
    public HttpUtils addParam(String key, Object value) {
        mParams.put(key, value);
        return this;
    }

    //添加多个参数
    public HttpUtils addParam(Map<String, Object> params) {
        mParams.putAll(params);
        return this;
    }

    //添加回调
    public void execute(EngineCallBack callBack) {
        if (callBack == null)
            callBack = EngineCallBack.DEFUALT_CALL_BACK;


        //判断执行
        if (mType == POST_REQUEST) {
            post(mUrl, mParams, callBack);
        }

        //判断执行
        if (mType == GET_REQUEST) {
            get(mUrl, mParams, callBack);
        }

    }

    public void execute() {
        execute(null);
    }


    //默认OkHttpEngine
    private static IHttpEngine mIHttpEngine = new OkHttpEngine();

    //在Application初始化引擎
    public static void init(IHttpEngine engine) {
        mIHttpEngine = engine;
    }

    /**
     * 每次可以自带引擎
     *
     * @param engine
     */
    public void exChangeEngine(IHttpEngine engine) {
        mIHttpEngine = engine;
    }

    public void get(String url, Map<String, Object> params, EngineCallBack callBack) {
        mIHttpEngine.get(url, params, callBack);
    }

    public void post(String url, Map<String, Object> params, EngineCallBack callBack) {
        mIHttpEngine.post(url, params, callBack);
    }
}
