package com.rys.smartrecycler.control.api;

import com.lwb.framelibrary.view.base.BaseModel;
import com.lwb.framelibrary.view.base.BasePresenter;
import com.lwb.framelibrary.view.base.BaseView;

/**
 * 创建时间：2019/6/18
 * 作者：李伟斌
 * 功能描述:
 */
public interface DeliveryFinishApi {
    interface View extends BaseView{

    }

    interface Presenter<V extends BaseView> extends BasePresenter<V>{
        void loadDeskConfigInfo(int deskNo);
        void closeDeliverDoor();
    }
    interface Model extends BaseModel{

    }
}
