package com.lwb.framelibrary.view.base;

/**
 * 创建时间：2017/11/14
 * 作者：李伟斌
 * 功能描述:
 */

public interface BaseView {
    /**
     * 显示调试toast
     * @param type
     * @param msg
     */
    void showToast(int type, String msg);
    /**
     * 显示加载框
     */
    void showProgress(String message);

    /**
     * 隐藏加载框
     */
    void dismissProgress();
}
