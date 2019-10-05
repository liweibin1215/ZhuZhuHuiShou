package com.lwb.framelibrary.net.interceptor;


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
    private String Token = "";
    public CustomInterceptor() {
    }
    public CustomInterceptor(String Token) {
        this.Token = Token;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request.Builder builder = originalRequest.newBuilder()
                .header("HttpVersion", String.format("Android_SmartBox_%s",""));
        if(Token != null && !"".equals(Token)){
            builder.header("token", Token);
        }
        Request myRequest = builder.build();
        return chain.proceed(myRequest);
    }
}
