package com.rys.smartrecycler.inter;

/**
 * 创建时间：2019/4/29
 * 作者：李伟斌
 * 功能描述:
 */

public interface RequestCallBackListener<T> {
    void onSuccess(T data);
    void onFailed(int code,String msg);
}
