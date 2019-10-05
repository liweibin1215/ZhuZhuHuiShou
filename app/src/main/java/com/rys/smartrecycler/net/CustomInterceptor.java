package com.rys.smartrecycler.net;


import com.rys.smartrecycler.application.BaseApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 创建时间：2018/1/22
 * 作者：李伟斌
 * 功能描述: 接口头部拦截
 */

public class CustomInterceptor implements Interceptor {
    public CustomInterceptor() {
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request.Builder builder = originalRequest.newBuilder()
                .header("HttpVersion", String.format("Android_SmartBox_%s",""));
        builder.header("token", BaseApplication.getInstance().getToken());
        Request myRequest = builder.build();
        return chain.proceed(myRequest);
    }
}
