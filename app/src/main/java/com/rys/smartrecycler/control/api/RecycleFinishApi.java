package com.rys.smartrecycler.control.api;

import com.lwb.framelibrary.view.base.BaseModel;
import com.lwb.framelibrary.view.base.BasePresenter;
import com.lwb.framelibrary.view.base.BaseView;

/**
 * 创作时间： 2019/5/26 on 下午4:54
 * <p>
 * 描述：
 * <p>
 * 作者：lwb
 */

public interface RecycleFinishApi {
    interface View extends BaseView{
        void setCurrentDesk(int desk);
        void setCurrentDeskName(String deskName);
        void setCuurentRecycleNum(String recycleNum);
        void setCurrentMsg(String msg);
        void showCurrentResultIcon(boolean isOk);
    }
    interface Presenter<V extends BaseView> extends BasePresenter<V>{

    }
    interface  Model extends BaseModel{

    }
}
