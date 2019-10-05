package com.lwb.framelibrary.net.BaseExample;


import com.lwb.framelibrary.net.response.BaseEntity;
import com.lwb.framelibrary.net.response.base.HttpResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * 创建时间：2018/08/26
 * 作者：李伟斌
 * 功能描述: 递管家接口定义声明
 */
public interface ExampleApiService {
    /**
     * 用户登录
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("api/SmartCourier/LoginInfo")
    Observable<HttpResponse<BaseEntity>> userLogin(@Body RequestBody body);



}
