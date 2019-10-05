package com.lwb.framelibrary.view.contract;

import android.content.Context;

import com.lwb.framelibrary.view.base.BasePresenterImpl;


/**
 * 创建时间：2017/11/13
 * 作者：李伟斌
 * 功能描述: 默认实现基类
 */

public class DefaultPresenterImpl extends BasePresenterImpl<DefaultView.View,DefaultView.Model> implements DefaultView.Presenter<DefaultView.View>{

    public DefaultPresenterImpl(Context context) {
        super(context);
    }

    @Override
    public DefaultView.Model attachModel() {
        return new DefaultModelImpl(mContext);
    }
}
