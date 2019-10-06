package com.rys.smartrecycler.control.model;

import android.content.Context;

import com.lwb.framelibrary.net.response.base.HttpResponse;
import com.lwb.framelibrary.net.subscriber.BaseHttpSubscriber;
import com.lwb.framelibrary.view.base.BaseModelImpl;
import com.rys.smartrecycler.control.api.RecycleOperatorApi;
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
public class RecycleOperatorModel extends BaseModelImpl implements RecycleOperatorApi.Model{
    private BaseHttpSubscriber<String> mSubcriber;

    public RecycleOperatorModel(Context mContext) {
        super(mContext);
    }

    @Override
    public void canCelRequest() {
        if(mSubcriber != null){
            mSubcriber.unSubscribe();
        }
    }


    @Override
    public void recycleFinishOk(String box_number, String box_attach_id, String type, String sum, String tel, final RequestCallBackListener listener) {
        Map<String,Object> params = new HashMap<>();
        params.put("box_number",box_number);
        params.put("box_attach_id",box_attach_id);
        params.put("type", AppTool.getTypeName(type));
        params.put("sum",sum);
        params.put("local_at",System.currentTimeMillis()/1000);
        params.put("tel",tel);
        CommonHttpManager.observeWithNoDataTrans(CommonHttpManager.getInstance().getmApiService().adminOrderCreate(params)).subscribe(mSubcriber = new BaseHttpSubscriber<String>() {
            @Override
            public void onSuccess(String s) {
                HttpResponse baseBean = CommonHttpParse.parseCommonHttpResult(s);
                if(baseBean != null && baseBean.isSuccess()){
                    listener.onSuccess("回收成功");
                }else{
                    listener.onFailed(0,"回收成功失败，请重试");
                }
            }

            @Override
            public void onError(int code, String errorMsg) {
                listener.onFailed(code,errorMsg);
            }
        });
    }
}
