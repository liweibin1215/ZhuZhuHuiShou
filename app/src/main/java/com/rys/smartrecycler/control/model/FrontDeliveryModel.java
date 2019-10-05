package com.rys.smartrecycler.control.model;

import android.content.Context;

import com.lwb.framelibrary.net.response.base.HttpResponse;
import com.lwb.framelibrary.net.subscriber.BaseHttpSubscriber;
import com.lwb.framelibrary.view.base.BaseModelImpl;
import com.rys.smartrecycler.control.api.FrontDeliveryApi;
import com.rys.smartrecycler.inter.RequestCallBackListener;
import com.rys.smartrecycler.net.CommonHttpManager;
import com.rys.smartrecycler.net.CommonHttpParse;
import com.rys.smartrecycler.tool.AppTool;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建时间：2019/5/24
 * 作者：李伟斌
 * 功能描述:
 */
public class FrontDeliveryModel extends BaseModelImpl implements FrontDeliveryApi.Model{
    private BaseHttpSubscriber<String> mSubcriber;
    public FrontDeliveryModel(Context mContext) {
        super(mContext);
    }

    @Override
    public void canCelRequest() {
        if(mSubcriber != null){
            mSubcriber.unSubscribe();
        }
    }


    @Override
    public void createDeliveryOrder(String box_number, String box_attach_id, String type, String sum, String tel,double price, RequestCallBackListener listener) {
        Map<String,Object> params = new HashMap<>();
        params.put("box_number",box_number);
        params.put("box_attach_id",box_attach_id);
        params.put("type", AppTool.getTypeName(type));
        params.put("sum",sum);
        params.put("local_at",System.currentTimeMillis()/1000);
        params.put("tel",tel);
        params.put("price",price);
        CommonHttpManager.observeWithNoDataTrans(CommonHttpManager.getInstance().getmApiService().userOrderCreate(params)).subscribe(mSubcriber = new BaseHttpSubscriber<String>() {
            @Override
            public void onSuccess(String s) {
                HttpResponse baseBean = CommonHttpParse.parseCommonHttpResult(s);
                if(baseBean != null && baseBean.isSuccess()){
                    listener.onSuccess("投递成功");
                }else{
                    listener.onFailed(0,"投递成功失败，请重试");
                }
            }

            @Override
            public void onError(int code, String errorMsg) {
                listener.onFailed(code,errorMsg);
            }
        });
    }

}
