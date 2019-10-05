package com.rys.smartrecycler.viewapi.presenter;

import android.content.Context;
import android.os.Handler;

import com.lwb.framelibrary.view.base.BasePresenterImpl;
import com.rys.smartrecycler.viewapi.api.MainHomeApi;
import com.rys.smartrecycler.viewapi.mode.MainHomeMdel;

/**
 * 创建时间：2019/4/29
 * 作者：李伟斌
 * 功能描述:
 */

public class MainHomePresenter extends BasePresenterImpl<MainHomeApi.View,MainHomeApi.Model> implements MainHomeApi.Presenter<MainHomeApi.View>{
    private boolean isAdvTimeCheck = false;
    private long timeLen = 60;
    private Handler advHandler = new Handler();
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timeLen--;
            if (timeLen > 0 && isAdvTimeCheck) {
                advHandler.postDelayed(this, 1000);
            }else{
                getView().isTimeToShowAdvertiment();
            }
        }
    };
    public MainHomePresenter(Context context) {
        super(context);
    }

    @Override
    public MainHomeApi.Model attachModel() {
        return new MainHomeMdel(mContext);
    }

    @Override
    public void startOrStopAdvTimeCheck(boolean isShow) {
        if(isShow){
            if(advHandler != null){
                timeLen = 60;
                advHandler.removeCallbacksAndMessages(null);
                isAdvTimeCheck = true;
                advHandler.postDelayed(runnable,1000);
            }
        }else{
            isAdvTimeCheck = false;
            if(advHandler != null){
                advHandler.removeCallbacksAndMessages(null);
            }
        }
    }

    @Override
    public void resetShowAdvertismentTime() {
        timeLen = 60;
    }
}
