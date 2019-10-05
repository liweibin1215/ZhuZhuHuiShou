package com.lwb.framelibrary.view.base;

/**
 * 创建时间：2017/11/14
 * 作者：李伟斌
 * 功能描述:
 */

public interface
BasePresenter<V extends BaseView> {
    /**
     *  创建view并得到对Avtivity的引用
     * @param view
     */
    void attachView(V view);

    /**
     * 断开对Activity的引用
     */
    void detachView();

    /**
     *  断开对model的引用
     */
    void detachModel();

    /**
     *
     * @return 得到Activity的引用
     */
    V getView();

    /**
     *
     * @return 判断View是否断开
     */
    boolean isViewAttached();

    /**
     *
     * @return 判断Model是否断开
     */
    boolean isModelAttached();
}
