package com.rys.smartrecycler.view.activity.entrance;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;

import com.lwb.framelibrary.utils.ActivityUtil;
import com.rys.smartrecycler.R;
import com.rys.smartrecycler.service.dataup.DataUpService;
import com.rys.smartrecycler.service.mqtt.MqttService;
import com.rys.smartrecycler.view.base.BaseTitleActivity;
import com.rys.smartrecycler.view.fragment.FrontHomeFragment;
import com.rys.smartrecycler.viewapi.api.MainHomeApi;
import com.rys.smartrecycler.viewapi.presenter.MainHomePresenter;


public class MainHomeActivity extends BaseTitleActivity<MainHomeApi.View,MainHomeApi.Presenter<MainHomeApi.View>> implements MainHomeApi.View{
    private TextView tvTime;

    @Override
    protected void startInvoke(Bundle savedInstanceState) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTime = findViewById(R.id.tv_time);
        addHomeFragment();
        startService(new Intent(this, MqttService.class));
        startService(new Intent(this, DataUpService.class));
    }

    @Override
    public MainHomeApi.Presenter<MainHomeApi.View> createPresenter() {
        return new MainHomePresenter(mContext);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        checkHomeFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, MqttService.class));
        stopService(new Intent(this, DataUpService.class));
    }

    @Override
    public void showToast(int type, String msg) {

    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            getPresenter().resetShowAdvertismentTime();
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void isTimeToShowAdvertiment() {
        ActivityUtil.jump(this,AdvertismentActivity.class);
    }
    @SuppressLint("NewApi")
    public void addHomeFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fy_content_id, new FrontHomeFragment(), FrontHomeFragment.TAG).commitAllowingStateLoss();
    }

    @Override
    public void excuteFragmentAction(int type, String... params) {
        if(type == 2001){
            if("true".equals(params[0])){
                getPresenter().startOrStopAdvTimeCheck(true);
            }else{
                getPresenter().startOrStopAdvTimeCheck(false);
            }
        }
    }

    @Override
    public void showTimeInfo(String time) {
        tvTime.setText(time);
    }

    public void checkHomeFragment(){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = manager.findFragmentById(R.id.fy_content_id);
        if(fragment != null && !(fragment instanceof FrontHomeFragment)){
            transaction.replace(R.id.fy_content_id, new FrontHomeFragment(), FrontHomeFragment.TAG).commitAllowingStateLoss();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
