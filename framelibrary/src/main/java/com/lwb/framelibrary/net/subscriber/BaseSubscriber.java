package com.lwb.framelibrary.net.subscriber;


import com.lwb.framelibrary.net.callback.OnSubscribeBack;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * 创建时间：2018/1/22
 * 作者：李伟斌
 * 功能描述:
 */
public abstract class BaseSubscriber<T> implements Observer<T> ,OnSubscribeBack<T> {
    private Disposable mDisposable;
    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
    }

    @Override
    public void onNext(T t) {
        accept(t);
    }

    @Override
    public void onError(Throwable e) {

    }
    @Override
    public void onComplete() {

    }
    /**
     * 取消订阅
     */
    public void unSubscribe() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
