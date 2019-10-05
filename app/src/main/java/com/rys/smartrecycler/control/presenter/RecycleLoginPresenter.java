package com.rys.smartrecycler.control.presenter;

import android.content.Context;
import android.os.Handler;

import com.lwb.framelibrary.view.base.BasePresenterImpl;
import com.rys.smartrecycler.control.api.RecycleLoginApi;
import com.rys.smartrecycler.control.model.RecycleLoginModel;
import com.rys.smartrecycler.db.controller.LogController;
import com.rys.smartrecycler.inter.RequestCallBackListener;

/**
 * 创建时间：2019/5/24
 * 作者：李伟斌
 * 功能描述:
 */
public class RecycleLoginPresenter extends BasePresenterImpl<RecycleLoginApi.View,RecycleLoginApi.Model> implements RecycleLoginApi.Presenter<RecycleLoginApi.View>{
    private int time = 60;
    private Handler timeHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time--;
            if(time <= 0){
                time = 60;
                getView().setCurrentPwdInfo("获取验证码");
            }else{
                getView().setCurrentPwdInfo(time+"s");
                if(timeHandler != null)
                    timeHandler.postDelayed(this,1000);
            }
        }
    };
    public RecycleLoginPresenter(Context context) {
        super(context);
    }

    @Override
    public RecycleLoginApi.Model attachModel() {
        return new RecycleLoginModel(mContext);
    }

    @Override
    public void startGetPwdInfo() {
        if(time != 60){
            return;
        }
        final String phone = getView().getLoginPhone();
        if("".equals(phone) || phone.length() != 11){
            getView().showToast(0,"账号错误");
            return;
        }
        if(isModelAttached()){
            getModel().startGetPwdInfo(phone, new RequestCallBackListener<String>() {
                @Override
                public void onSuccess(String data) {
                    if(isViewAttached()){
                        if(timeHandler != null){
                            timeHandler.removeCallbacks(runnable);
                            timeHandler.postDelayed(runnable,1000);
                            getView().setCurrentPwdInfo("60s");
                        }
                        LogController.insrtOperatorLog("回收员回收", phone+"获取登录验证码成功");
                    }
                }

                @Override
                public void onFailed(int code, String msg) {
                    if(isViewAttached()){
                        getView().showToast(0,"账号错误");
                    }
                }
            });
        }
    }

    @Override
    public void startGoLogin() {
        String phone = getView().getLoginPhone();
        String pwd = getView().getLoginPwd();
        if("".equals(phone) || phone.length() != 11){
            getView().showToast(0,"账号错误");
            return;
        }
        if("".equals(phone) || pwd.length() < 4){
            getView().showToast(0,"密码错误");
            return;
        }
        if(isModelAttached()){
            getModel().startGoLogin(phone, pwd, new RequestCallBackListener<String>() {
                @Override
                public void onSuccess(String data) {
                    if(isViewAttached()){
                        getView().loginSuccess();
                    }
                }

                @Override
                public void onFailed(int code, String msg) {
                    if(isViewAttached()){
                        getView().showToast(0,"账号错误");
                    }
                }
            });
        }

    }

    @Override
    public void releaseTimeHandler() {
        if(timeHandler != null){
            time = 0;
            timeHandler.removeCallbacks(runnable);
            timeHandler = null;
        }
    }
}
