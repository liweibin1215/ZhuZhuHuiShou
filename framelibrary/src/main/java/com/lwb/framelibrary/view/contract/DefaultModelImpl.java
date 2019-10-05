package com.lwb.framelibrary.view.contract;

import android.content.Context;

import com.lwb.framelibrary.view.base.BaseModelImpl;


/**
 * 创建时间：2017/11/13
 * 作者：李伟斌
 * 功能描述: 默认实现基类
 */

public class DefaultModelImpl extends BaseModelImpl implements DefaultView.Model{
    public DefaultModelImpl(Context context) {
        super(context);
    }

    @Override
    public void canCelRequest() {

    }
}
