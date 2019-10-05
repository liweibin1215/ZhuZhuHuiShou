package com.lwb.framelibrary.net.subscriber;

import com.google.gson.JsonParseException;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.lwb.framelibrary.net.callback.OnResultCallBack;
import com.lwb.framelibrary.net.execption.ApiException;

import org.json.JSONException;

import java.io.EOFException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;


/**
 * 创建时间：2018/1/22
 * 作者：李伟斌
 * 功能描述:
 */
public abstract class BaseHttpSubscriber<T> implements Observer<T> ,OnResultCallBack<T> {
    private Disposable mDisposable;

    public BaseHttpSubscriber() {
    }

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof CompositeException) {
            CompositeException compositeE = (CompositeException) e;
            for (Throwable throwable : compositeE.getExceptions()) {
                dealWithException(throwable);
            }
        } else {
            dealWithException(e);
        }
    }
    @Override
    public void onComplete() {

    }

    private void dealWithException(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            onError(ApiException.Code_Net_TimeOut, ApiException.Code_Net_TimeOut_Result);
        }  else if (e instanceof SocketTimeoutException) {
            onError(ApiException.Code_Net_TimeOut, ApiException.Code_Net_TimeOut_Result);
        }else if (e instanceof ConnectException) {
            onError(ApiException.Code_Net_Error, ApiException.Code_Net_Error_Result);
        } else if (e instanceof UnknownHostException) {
            onError(ApiException.Code_Net_Error, ApiException.Code_Net_Error_Result);
        } else if (e instanceof SocketException) {
            onError(ApiException.Code_Net_Error, ApiException.Code_Net_Error_Result);//可能是应用禁止联网了
        } else if (e instanceof HttpException) {
            onError(ApiException.Code_Server_Error, ApiException.Code_Server_Error_Result);//服务器端异常 可能是WiFi无网络
        } else if (e instanceof EOFException) {
            onError(ApiException.Code_Server_Error, ApiException.Code_Server_Error_Result); //服务器端异常
        } else if (e instanceof IllegalStateException) {
            onError(ApiException.Code_Josn_Error, ApiException.Code_Josn_Error_Result);//数据解析异常，Gson解析出错
        }  else if (e instanceof JSONException) {
            onError(ApiException.Code_Josn_Error, ApiException.Code_Josn_Error_Result);//数据解析异常
        }  else if (e instanceof JsonParseException) {
            onError(ApiException.Code_Josn_Error, ApiException.Code_Josn_Error_Result);//数据解析异常
        }else if (e instanceof ApiException) {
            int errorCode = ((ApiException) e).getResultCode();//服务器返回的错误
            if(errorCode == ApiException.Code_Data_Null){
                onError(errorCode,ApiException.Code_Data_Null_Result);
            }else{
                onError(errorCode, e.getMessage());
            }
        }else {
            onError(ApiException.Code_Server_Error, ApiException.Code_Server_Error_Result);//其他异常统称为服务器错误
            return;
        }
    }

    /**
     * 取消订阅
     */
    public void unSubscribe() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
