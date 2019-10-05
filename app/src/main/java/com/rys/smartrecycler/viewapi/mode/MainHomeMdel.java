package com.rys.smartrecycler.viewapi.mode;

import android.content.Context;

import com.lwb.framelibrary.view.base.BaseModelImpl;
import com.rys.smartrecycler.viewapi.api.MainHomeApi;

/**
 * 创建时间：2019/4/29
 * 作者：李伟斌
 * 功能描述:
 */

public class MainHomeMdel extends BaseModelImpl implements MainHomeApi.Model{

    public MainHomeMdel(Context mContext) {
        super(mContext);
    }

    @Override
    public void canCelRequest() {

    }
}
