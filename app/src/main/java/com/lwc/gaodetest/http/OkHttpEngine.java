package com.lwc.gaodetest.http;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 默认引擎
 * <p>
 * Created by lingwancai on
 * 2018/9/29 14:57
 */
public class OkHttpEngine implements IHttpEngine {

    private static OkHttpClient mOkHttpClient = new OkHttpClient();

    @Override
    public void get(Context context, String url, Map<String, Object> params, final EngineCallBack callBack) {
        //打印参数
        String joinUrl = HttpUtils.onJointParams(url, params);
        Log.e("get请求参数", "url=>" + joinUrl);
        Request request = new Request.Builder()
                .url(url)
                .tag(context)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.OnError(e);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("get返回参数", "result=>" + result);
                callBack.Success(result);
            }
        });
    }

    @Override
    public void post(Context context, String url, Map<String, Object> params, final EngineCallBack callBack) {

        //打印参数
        String joinUrl = HttpUtils.onJointParams(url, params);
        Log.e("post请求参数", "url=>" + joinUrl);

        RequestBody requestBody = appendBody(params);
        final Request request = new Request.Builder()
                .url(url)
                .tag(context)
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {

            /**
             * 这个方法回调都不是在主线程中
             *
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.OnError(e);

            }

            /**
             * 这个方法回调都不是在主线程中
             *
             * @param call
             * @param response
             * @throws IOException
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("post返回参数", "result=>" + result);
                callBack.Success(result);
            }
        });
    }

    //组装post请求参数body
    protected RequestBody appendBody(Map<String, Object> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        addParams(builder, params);
        return builder.build();
    }

    private void addParams(MultipartBody.Builder builder, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key) + "");
                Object value = params.get(key);
                if (value instanceof File) {
                    //处理文件 --- object file
                    File file = (File) value;
                    builder.addFormDataPart(key, file.getName(), RequestBody
                            .create(MediaType.parse(guessMimeType(file
                                    .getAbsolutePath())), file));
                } else if (value instanceof List) {//提交list集合
                    List<File> listFiles = (List<File>) value;
                    for (int i = 0; i < listFiles.size(); i++) {
                        //获取文件
                        File file = listFiles.get(i);
                        builder.addFormDataPart(key + i, file.getName(), RequestBody
                                .create(MediaType.parse(guessMimeType(file
                                        .getAbsolutePath())), file));
                    }

                } else {
                    builder.addFormDataPart(key, value + "");
                }

            }
        }

    }

    //猜测文件类型
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}
