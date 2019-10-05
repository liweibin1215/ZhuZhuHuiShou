package com.rys.smartrecycler.control.model;

import android.content.Context;

import com.lwb.framelibrary.view.base.BaseModelImpl;
import com.rys.smartrecycler.control.api.RecycleFinishApi;

/**
 * 创作时间： 2019/5/26 on 下午4:58
 * <p>
 * 描述：
 * <p>
 * 作者：lwb
 */

public class RecycleFinishModel extends BaseModelImpl implements RecycleFinishApi.Model{

    public RecycleFinishModel(Context mContext) {
        super(mContext);
    }

    @Override
    public void canCelRequest() {

    }
}
