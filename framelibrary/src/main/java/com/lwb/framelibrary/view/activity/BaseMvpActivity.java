package com.lwb.framelibrary.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lwb.framelibrary.utils.VersionUtil;
import com.lwb.framelibrary.view.base.BasePresenter;
import com.lwb.framelibrary.view.base.BaseView;


/**
 * 创建时间：2017/11/14
 * 作者：李伟斌
 * 功能描述:
 */
public abstract class BaseMvpActivity<V extends BaseView, P extends BasePresenter<V>> extends BaseActivity implements BaseView {

    public Context mContext;
    private P mPresenter;

    public P getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //提前初始化presenter
        mContext = this;
        initWidget();
        super.onCreate(savedInstanceState);
    }

    /**
     * 初始化mvp
     */
    private void initWidget() {
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
    }

    /**
     * 创建presenter对象
     *
     * @return
     */
    public abstract P createPresenter();



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
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter.detachModel();
        }
    }
}
