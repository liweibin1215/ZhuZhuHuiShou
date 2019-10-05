package com.lwb.framelibrary.net.execption;


public class ApiException extends RuntimeException {
    public static final int Code_Data_Null = 5001;//响应成功，但data为null
    public static final String Code_Data_Null_Result = "请求数据为空";
    public static final int Code_Net_Error = 5002;//网络故障
    public static final String Code_Net_Error_Result = "请求失败,请检查您的网络状态";
    public static final int Code_Net_TimeOut = 5003;//请求超时超时
    public static final String Code_Net_TimeOut_Result = "请求超时,请稍后重试";
    public static final int Code_Josn_Error = 5004;////数据异常
    public static final String Code_Josn_Error_Result = "服务器数据异常,请稍后重试";
    public static final int Code_Server_Error = 5004;//数据异常
    public static final String Code_Server_Error_Result = "服务器请求异常,请稍后重试";
    /**服务器返回的错误码*/
    private int resultCode;
    public ApiException(int resultCode, String msg) {
        this(msg);
        this.resultCode = resultCode;
    }
    private ApiException(String detailMessage) {
        super(detailMessage);
    }

    public int getResultCode() {
        return resultCode;
    }
}

