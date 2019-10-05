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
public interface RecycleLoginApi {
    interface View extends BaseView{
        void setCurrentPwdInfo(String info);
        void loginSuccess();
        String getLoginPhone();
        void   setLoginPhone(String phone);
        String getLoginPwd();
        void   setLoginPwd(String pwd);


    }

    interface Presenter<V extends BaseView> extends BasePresenter<V> {
        void startGetPwdInfo();
        void startGoLogin();
        void releaseTimeHandler();
    }

    interface Model extends BaseModel {
        void startGetPwdInfo(String phone, RequestCallBackListener<String> listener);
        void startGoLogin(String phone, String pwd, RequestCallBackListener<String> listener);
    }
}
