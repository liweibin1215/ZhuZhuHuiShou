package com.rys.smartrecycler.net.api;


import com.lwb.framelibrary.net.response.base.HttpResponse;
import com.rys.smartrecycler.net.bean.AdminLoginBean;
import com.rys.smartrecycler.net.bean.AdvBean;
import com.rys.smartrecycler.net.bean.DeviceInfoBean;
import com.rys.smartrecycler.net.bean.DeviceInitBean;
import com.rys.smartrecycler.net.bean.UserLoginBean;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 创建时间：2018/08/26
 * 作者：李伟斌
 * 功能描述: 递管家接口定义声明
 */
public interface ApiService {
    /**
     * 设备初始化验证
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/v1/box/login")
    Observable<HttpResponse<DeviceInitBean>> deviceInit(@Field("number") String number, @Field("password") String password);

    /**
     * 设备基础信息或区域
     *
     * @return
     */
    @GET("api/v1/box/info")
    Observable<HttpResponse<DeviceInfoBean>> deviceInfo();

    /**
     * 设备基础信息或区域
     *
     * @return
     */
    @GET("api/v1/box/ad")
    Observable<HttpResponse<AdvBean>> getAdvInfos(@Query("type") int type, @Query("number") String number);


    /**
     * 用户登录
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/v1/sms")
    Observable<String> userGetPassword(@Field("tel") String tel);

    /**
     * 用户登录
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/v1/box/user_login")
    Observable<HttpResponse<UserLoginBean>> userLogin(@Field("tel") String tel, @Field("code") String pwd);

    /**
     * 用户登录
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/v1/box/collector_login")
    Observable<HttpResponse<AdminLoginBean>> adminLogin(@Field("tel") String tel, @Field("code") String pwd);


    /**
     * 用户订单上传
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/v1/box/user_order")
    Observable<String> userOrderCreate(@FieldMap Map<String,Object> params);



    /**
     * 回收员订单上传
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/v1/box/collector_order")
    Observable<String> adminOrderCreate(@FieldMap Map<String,Object> params);


    /**
     * 柜机日志上传
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/v1/box/log")
    Observable<String> appLogUpload(@FieldMap Map<String,Object> params);

    @FormUrlEncoded
    @POST("api/v1/box/attach_status")
    Observable<String> uploadDeskInfo(@FieldMap Map<String,Object> params);

    /*断点续传下载接口*/
    @Streaming/*大文件需要加入这个判断，防止下载过程中写入到内存中*/
    @GET
    Observable<ResponseBody> downLoadFile(@Url String url);
}
