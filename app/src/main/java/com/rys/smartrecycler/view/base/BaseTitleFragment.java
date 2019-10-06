package com.rys.smartrecycler.view.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lwb.framelibrary.utils.ActivityUtil;
import com.lwb.framelibrary.utils.ToastUtil;
import com.lwb.framelibrary.view.base.BasePresenter;
import com.lwb.framelibrary.view.base.BaseView;
import com.lwb.framelibrary.view.fragment.BaseMvpFragment;
import com.rys.smartrecycler.R;
import com.rys.smartrecycler.application.BaseApplication;
import com.rys.smartrecycler.inter.AcFragmentCallListener;
import com.rys.smartrecycler.inter.Observer;
import com.rys.smartrecycler.tool.dialog.NoticeDialog;
import com.rys.smartrecycler.view.activity.entrance.AdvertismentActivity;
import com.rys.smartrecycler.view.fragment.FrontHomeFragment;

import java.lang.ref.WeakReference;

/**
 * 创建时间：2019/4/23
 * 作者：李伟斌
 * 功能描述:
 */

public abstract class BaseTitleFragment<V extends BaseView,P extends BasePresenter<V>>extends BaseMvpFragment<V,P> implements Observer {
    public String childTag = "";
    public View conventView;
    public AcFragmentCallListener mAcFragmentCall;
    public int timeLen = 180;
    public boolean isTimeStart = true;
    protected abstract int doThingsBeforeExit();
    protected abstract void onClickBack();
    private Handler TimeHandler = null;
    private Button btnbBack;
    private NoticeDialog noticeDialog;
    public boolean  isHomeFragment = false;
    @Override
    public void myTouched(MotionEvent event) {
        if(isHomeFragment){
            timeLen = 30;
        }else{
            timeLen = 180;
        }
    }

    public static class MyHandler extends Handler{
        private WeakReference<BaseTitleFragment> weakReference ;
        public MyHandler(BaseTitleFragment fragment){
            weakReference = new WeakReference<>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(weakReference == null){
                return;
            }
            BaseTitleFragment fragment = weakReference.get();
            if(fragment != null && msg.what == 200){
                if (fragment.timeLen < 1) {
                    fragment.setTextTime();
                    fragment.isTimeFinished();
                }else{
                    if(fragment.isTimeStart){
                        fragment.TimeHandler.sendEmptyMessageDelayed(200,1000);
                    }
                    fragment.setTextTime();
                }
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AcFragmentCallListener) {
            mAcFragmentCall = (AcFragmentCallListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TimeHandler = new MyHandler(this);
    }

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        childTag = initChildTag();
        if (getLayoutId() > 0 && (childTag == null || !"".equals(childTag))) {
            conventView = inflater.inflate(getLayoutId(), container, false);
        } else {
            throw new ExceptionInInitializerError();
        }
        btnbBack = conventView.findViewById(R.id.btn_back);
        if(btnbBack != null){
            conventView.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseTitleFragment.this.onClickBack();
                }
            });
        }
        if(mAcFragmentCall != null) {
            mAcFragmentCall.addObserver(this);
        }
        return conventView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(isTimeStart && TimeHandler != null){
            if(isHomeFragment){
                timeLen = 30;
            }else{
                timeLen = 180;
            }
            TimeHandler.removeCallbacksAndMessages(null);
            TimeHandler.sendEmptyMessageDelayed(200,100);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(TimeHandler != null){
            TimeHandler.removeCallbacksAndMessages(null);
        }
        if(noticeDialog != null && noticeDialog.isShowing()){
            noticeDialog.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mAcFragmentCall != null){
            mAcFragmentCall.removeObserver(this);
        }
        if(noticeDialog != null) {
            noticeDialog.realease();
            noticeDialog = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(TimeHandler != null){
            isTimeStart = false;
            TimeHandler.removeCallbacksAndMessages(null);
            TimeHandler = null;
        }
    }

    @Override
    public void showToast(int type, String msg) {
        if(type == 0){
            ToastUtil.showCenterToast(mContext,msg);
        }else{
            if (noticeDialog == null) {
                noticeDialog = new NoticeDialog(mContext);
            }
            if(!noticeDialog.isShowing()){
                noticeDialog.show(msg, type);
            }
        }
    }
    public abstract @LayoutRes int getLayoutId();
    public abstract String initChildTag();
    public void setTextTime(){
        if(mAcFragmentCall != null && !isHomeFragment){
            if(timeLen <= 0 || timeLen > 150){
                mAcFragmentCall.showTimeInfo("");
            }else{
                mAcFragmentCall.showTimeInfo(timeLen+"s");
            }
        }
        timeLen--;
    }
    public void isTimeFinished(){
        if(doThingsBeforeExit() == 0){
            BaseApplication.getInstance().setLoginInfoOff();//标记退出登录状态
            startFragmentAndFinishSelf(this,new FrontHomeFragment(),FrontHomeFragment.TAG, R.id.fy_content_id);
        } else if(doThingsBeforeExit() == -1){
            ActivityUtil.jump(mContext,AdvertismentActivity.class);//
        }else{
            getActivity().finish();//后台界面
        }
    }
}
