package com.lwb.framelibrary.net.response;

/**
 * 创建时间：2018/6/30
 * 作者：李伟斌
 * 功能描述:
 */

public class BaseEntity {
    private boolean  ExcuteResult;
    private String   ExcuteMsg;

    public BaseEntity(boolean excuteResult, String excuteMsg) {
        ExcuteResult = excuteResult;
        ExcuteMsg = excuteMsg;
    }

    public boolean isExcuteResult() {
        return ExcuteResult;
    }

    public void setExcuteResult(boolean excuteResult) {
        ExcuteResult = excuteResult;
    }

    public String getExcuteMsg() {
        return ExcuteMsg;
    }

    public void setExcuteMsg(String excuteMsg) {
        ExcuteMsg = excuteMsg;
    }
}
