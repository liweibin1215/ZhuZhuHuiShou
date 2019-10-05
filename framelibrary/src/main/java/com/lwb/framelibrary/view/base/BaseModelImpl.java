package com.lwb.framelibrary.view.base;

import android.content.Context;

/**
 * 创建时间：2017/11/14
 * 作者：李伟斌
 * 功能描述:
 */

public abstract  class BaseModelImpl implements BaseModel{
    public Context mContext;
    public BaseModelImpl(Context mContext){
        this.mContext = mContext;
    }
}
