package com.rys.smartrecycler.view.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.widget.Toast;

import com.lwb.framelibrary.tool.AppManagerUtils;
import com.lwb.framelibrary.view.activity.BaseMvpActivity;
import com.lwb.framelibrary.view.base.BasePresenter;
import com.lwb.framelibrary.view.base.BaseView;
import com.rys.smartrecycler.inter.AcFragmentCallListener;
import com.rys.smartrecycler.inter.Observer;
import com.rys.smartrecycler.tool.TouchManager;

/**
 * 创建时间：2019/4/23
 * 作者：李伟斌
 * 功能描述:activity管理基类
 * 页面管理、通知提示、基础参数
 */

public abstract class BaseTitleActivity<V extends BaseView,P extends BasePresenter<V>> extends BaseMvpActivity<V,P> implements AcFragmentCallListener{
    private TouchManager manager = new TouchManager();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManagerUtils.getInstance().addActivity(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(manager != null){
            manager.removeAll();
            manager = null;
        }
        AppManagerUtils.getInstance().removeActivity(this);
    }
    @Override
    public void showToast(int type, String msg) {
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            manager.notifyObservers(ev);// 通知所有在队列中的观察者，已经点击了
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void addObserver(Observer observer) {
        manager.addObserver(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        manager.removeObserver(observer);
    }
}
