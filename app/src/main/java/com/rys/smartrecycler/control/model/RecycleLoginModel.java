package com.rys.smartrecycler.control.model;

import android.content.Context;

import com.lwb.framelibrary.net.response.base.HttpResponse;
import com.lwb.framelibrary.net.subscriber.BaseHttpSubscriber;
import com.lwb.framelibrary.view.base.BaseModelImpl;
import com.rys.smartrecycler.application.BaseApplication;
import com.rys.smartrecycler.control.api.RecycleLoginApi;
import com.rys.smartrecycler.inter.RequestCallBackListener;
import com.rys.smartrecycler.net.CommonHttpManager;
import com.rys.smartrecycler.net.CommonHttpParse;
import com.rys.smartrecycler.net.bean.AdminLoginBean;

/**
 * 创建时间：2019/5/24
 * 作者：李伟斌
 * 功能描述:
 */
public class RecycleLoginModel extends BaseModelImpl implements RecycleLoginApi.Model {
    private BaseHttpSubscriber<String> mSubcriber;
    private BaseHttpSubscriber<AdminLoginBean> adminLoginSubcriber;

    public RecycleLoginModel(Context mContext) {
        super(mContext);
    }

    @Override
    public void canCelRequest() {
        if(mSubcriber != null){
            mSubcriber.unSubscribe();
        }
        if(adminLoginSubcriber != null){
            adminLoginSubcriber.unSubscribe();
        }
    }

    @Override
    public void startGetPwdInfo(String phone, final RequestCallBackListener<String> listener) {
        CommonHttpManager.observeWithNoDataTrans(CommonHttpManager.getInstance().getmApiService().userGetPassword(phone)).subscribe(mSubcriber = new BaseHttpSubscriber<String>() {
            @Override
            public void onSuccess(String s) {
                HttpResponse baseBean = CommonHttpParse.parseCommonHttpResult(s);
                if(baseBean != null && baseBean.isSuccess()){
                    listener.onSuccess("短信已发送");
                }else{
                    listener.onFailed(0,"短信发送失败，请重试");
                }
            }

            @Override
            public void onError(int code, String errorMsg) {
                listener.onFailed(code,errorMsg);

            }
        });
    }

    @Override
    public void startGoLogin(String phone, String pwd, RequestCallBackListener<String> listener) {
        BaseApplication.getInstance().setLoginInfo(true,"17602198669",1);//标记登录
        listener.onSuccess("");
//        CommonHttpManager.commonObserve(CommonHttpManager.getInstance().getmApiService().adminLogin(phone,pwd)).subscribe(adminLoginSubcriber = new BaseHttpSubscriber<AdminLoginBean>() {
//            @Override
//            public void onSuccess(AdminLoginBean userLoginBean) {
//                if(userLoginBean != null){
//                    BaseApplication.getInstance().setLoginInfo(true,userLoginBean.getPhone(),userLoginBean.getId());//标记登录
//                    listener.onSuccess("");
//                }else{
//                    listener.onFailed(0,"回收员信息为空");
//                }
//            }
//
//            @Override
//            public void onError(int code, String errorMsg) {
//                listener.onFailed(code,errorMsg);
//            }
//        });
    }
}
