package com.rys.smartrecycler.control.api;

import com.lwb.framelibrary.view.base.BaseModel;
import com.lwb.framelibrary.view.base.BasePresenter;
import com.lwb.framelibrary.view.base.BaseView;
import com.rys.smartrecycler.inter.RequestCallBackListener;

/**
 * 创建时间：2019/5/24
 * 作者：李伟斌
 * 功能描述:
 */
public interface RecycleOperatorApi {
    interface View extends BaseView{
        void showDeviceError(int type, String msg);
        void setErrorInfo(String errorInfo);
        void setDeskName(String deskName);
        void recycleFinished(String deskName,String recycleNum);
        int getDeskNo();
    }
    interface Presenter<V extends BaseView> extends BasePresenter<V>{
        void loadDeskConfigInfo(int deskNo);
        void recycleFinished();
        void openDeviceDoor();

    }

    interface Model extends BaseModel{
        void recycleFinishOk(String box_number,String box_attach_id,String type,String sum,String tel, RequestCallBackListener listener);
    }
}
