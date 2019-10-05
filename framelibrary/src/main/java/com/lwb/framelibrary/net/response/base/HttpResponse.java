package com.lwb.framelibrary.net.response.base;



/**
 * 创建时间：2018/1/22
 * 作者：李伟斌
 * 功能描述:平台标准响应结构
 */
public class HttpResponse<T> {
    private boolean isSuccess;
    private int code;
    private String msg;
//    private String RespTime;
    private T data;
    private HttpResponse( T data){
        this.data = data;
    }
    public HttpResponse(boolean isSuccess, int code, String message, String respTime, T data) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.msg = message;
//        RespTime = respTime;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String message) {
        this.msg = message;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
    public boolean isSuccessful(){
        return this.code == 200;
    }
    public void setSuccess(boolean success) {
        isSuccess = success;
    }
//
//    public String getRespTime() {
//        return RespTime;
//    }
//
//    public void setRespTime(String respTime) {
//        RespTime = respTime;
//    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
