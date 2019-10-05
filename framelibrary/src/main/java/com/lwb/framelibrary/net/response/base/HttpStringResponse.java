package com.lwb.framelibrary.net.response.base;


/**
 * 创建时间：2018/1/22
 */
public class HttpStringResponse {
    private boolean IsSuccess;
    private int Code;
    private String Message;
    private String RespTime;
    private String Data;

    public HttpStringResponse(boolean isSuccess, int code, String message, String respTime, String data) {
        IsSuccess = isSuccess;
        Code = code;
        Message = message;
        RespTime = respTime;
        Data = data;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public boolean isSuccess() {
        return IsSuccess;
    }
    public boolean isSuccessful(){
        return Code ==200;
    }
    public void setSuccess(boolean success) {
        IsSuccess = success;
    }

    public String getRespTime() {
        return RespTime;
    }

    public void setRespTime(String respTime) {
        RespTime = respTime;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
