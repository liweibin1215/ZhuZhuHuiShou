package com.lwb.framelibrary.view.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lwb.framelibrary.view.base.BasePresenter;
import com.lwb.framelibrary.view.base.BaseView;

import java.lang.reflect.ParameterizedType;

/**
 * 创建时间：2017/11/14
 * 作者：李伟斌
 * 功能描述:
 * <p>
 * <B>MVP模式基本fragment，<br>
 * V:Presenter层处理数据时提供回调给View层的接口interface，此接口继承于{@link BaseView}；<br>
 * T：在此页面中，处理数据所使用的Presenter层，继承自{@link BasePresenter}</B><br>
 * <p>
 */
public abstract class BaseMvpFragment<V extends BaseView,  P extends BasePresenter<V>>  extends BaseJumpFragment implements BaseView {

    protected P mPresenter;
    protected Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPresenter = createPresenter();
        if (mPresenter != null) {
            try {
                mPresenter.attachView((V) this);
            } catch (Exception e) {
                ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
                throw new IllegalArgumentException("创建了Presenter类:" + mPresenter.getClass().getSimpleName()
                        + "，但是没有实现对应的View层接口:" + type.getActualTypeArguments()[0]);
            }
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 创建presenter对象
     *
     * @return
     */
    public abstract P createPresenter();

    public P getPresenter() {
        return mPresenter;
    }

    /**
     * 显示加载框
     */
    @Override
    public void showProgress(String message) {

    }

    /**
     * 隐藏加载框
     */
    @Override
    public void dismissProgress() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter.detachModel();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext =context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mContext =null;

    }
}
