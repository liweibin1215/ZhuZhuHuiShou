package com.rys.smartrecycler.control.presenter;

import android.content.Context;

import com.lwb.framelibrary.view.base.BasePresenterImpl;
import com.rys.smartrecycler.control.api.RecycleFinishApi;
import com.rys.smartrecycler.control.model.RecycleFinishModel;

/**
 * 创作时间： 2019/5/26 on 下午4:59
 * <p>
 * 描述：
 * <p>
 * 作者：lwb
 */

public class RecycleFinishPresenter extends BasePresenterImpl<RecycleFinishApi.View,RecycleFinishApi.Model> implements RecycleFinishApi.Presenter<RecycleFinishApi.View> {

    public RecycleFinishPresenter(Context context) {
        super(context);
    }

    @Override
    public RecycleFinishApi.Model attachModel() {
        return new RecycleFinishModel(mContext);
    }
}
