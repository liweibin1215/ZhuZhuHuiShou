package com.lwb.framelibrary.view.base;

import android.content.Context;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * 创建时间：2017/11/14
 * 作者：李伟斌
 * 功能描述:
 */

public abstract class BasePresenterImpl<V extends BaseView, M extends BaseModel> implements BasePresenter<V> {
    private Reference<V> reference;
    protected Context mContext;
    private M mBaseModelImpl;

    public BasePresenterImpl(Context context) {
        this.mContext = context;
        //绑定model层
        this.mBaseModelImpl = attachModel();
    }

    public abstract M attachModel();

    public M getModel() {
        return mBaseModelImpl;
    }

    @Override
    public void attachView(V view) {
        reference = new WeakReference<>(view);
    }

    @Override
    public void detachView() {
        if (reference != null) {
            reference.clear();
            reference = null;
        }
    }

    //解除model层的绑定
    @Override
    public void detachModel() {
        if (mBaseModelImpl != null) {
            //解绑 取消请求
            mBaseModelImpl.canCelRequest();
            mBaseModelImpl = null;
        }
    }

    @Override
    public V getView() {
        return reference == null ? null : reference.get();
    }

    @Override
    public boolean isViewAttached() {
        return reference != null && reference.get() != null;
    }

    @Override
    public boolean isModelAttached() {
        return mBaseModelImpl != null;
    }
}
