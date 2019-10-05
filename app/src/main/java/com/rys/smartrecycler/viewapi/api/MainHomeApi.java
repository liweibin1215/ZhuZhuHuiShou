package com.rys.smartrecycler.viewapi.api;

import com.lwb.framelibrary.view.base.BaseModel;
import com.lwb.framelibrary.view.base.BasePresenter;
import com.lwb.framelibrary.view.base.BaseView;

/**
 * 创建时间：2019/4/29
 * 作者：李伟斌
 * 功能描述:
 */

public interface MainHomeApi {
    interface View extends BaseView{
        void isTimeToShowAdvertiment();
    }
    interface Presenter<V extends BaseView> extends BasePresenter<V>{
        void startOrStopAdvTimeCheck(boolean isShow);
        void resetShowAdvertismentTime();
    }

    interface Model extends BaseModel{

    }
}
