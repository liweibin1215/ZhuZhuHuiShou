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
public interface FrontDeliveryApi {
    interface View extends BaseView{
        void initDeskType1Icon();
        void initDeskType2Icon();
        void initDeskType3Icon();
        void initDeskType4Icon();
        void initDeskType5Icon();
        void initDeskType6Icon();
        void showDeviceError(int type,String msg);
        void setErrorInfo(String errorInfo);
        void setDeskName(String deskName);
        void setCurrentDeliveryNum(String deliveryNum);
        void deliveryFinished(int deskType,int result,String deskName,String num,String money);
    }
    interface Presenter<V extends BaseView> extends BasePresenter<V>{
        void loadDeskConfigInfo(int deskNo);
        void confirmDeliveryOk();
        void openDeviceDoor();
        void stopReceivertask();
    }

    interface Model extends BaseModel{
        void createDeliveryOrder(String box_number,String box_attach_id,String type,String sum,String tel,double price, RequestCallBackListener listener);
    }
}
