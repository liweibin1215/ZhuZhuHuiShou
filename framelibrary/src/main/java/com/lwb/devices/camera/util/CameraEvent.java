package com.lwb.devices.camera.util;

/**
 * 创建时间：2018/5/31
 * 作者：李伟斌
 * 功能描述:
 * 1000 关闭摄像头
 * 1001 打开摄像头
 *
 */

public class CameraEvent {
    private int code;
    private String msg;
    private String orderId;
    private String phoneNum;
    private String operator;
    private String remark;
    private long startTime;
    private int type;
    public CameraEvent(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public CameraEvent(int code, String orderId, String phoneNum, String operator, long startTime, String remark, int type){
        this.code = code;
        this.orderId=orderId;
        this.phoneNum = phoneNum;
        this.operator = operator;
        this.startTime= startTime;
        this.remark = remark;
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
