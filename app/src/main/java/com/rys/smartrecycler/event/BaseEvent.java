package com.rys.smartrecycler.event;

/**
 * 创建时间：2019/6/27
 * 作者：李伟斌
 * 功能描述:
 */
public class BaseEvent {
    public int code;
    public String msg;

    public BaseEvent(int code, String msg) {
        this.code = code;
        this.msg = msg;
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
