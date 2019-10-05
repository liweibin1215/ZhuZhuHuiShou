package com.rys.smartrecycler.view.activity.entrance;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rys.smartrecycler.R;
import com.rys.smartrecycler.inter.AcFragmentCallListener;
import com.rys.smartrecycler.view.base.BaseTitleActivity;
import com.rys.smartrecycler.view.fragment.AdminHomeFragment;
import com.rys.smartrecycler.viewapi.api.MainHomeApi;
import com.rys.smartrecycler.viewapi.presenter.MainHomePresenter;


public class AdminHomeActivity extends BaseTitleActivity<MainHomeApi.View,MainHomeApi.Presenter<MainHomeApi.View>> implements MainHomeApi.View,AcFragmentCallListener{
    private RelativeLayout rlTitleName;
    private TextView       tvTitleName;
    private TextView       tvTime;
    @Override
    protected void startInvoke(Bundle savedInstanceState) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        initView();
        addHomeFragment();
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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    }
    @SuppressLint("NewApi")
    public void addHomeFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fy_admin_id, new AdminHomeFragment(), AdminHomeFragment.TAG).commitAllowingStateLoss();
    }

    @Override
    public void excuteFragmentAction(int type, String... params) {
        if(type == 1000){
            rlTitleName.setVisibility(View.VISIBLE);
            tvTitleName.setText(params[0]);
        }else if(type == 1001){
            rlTitleName.setVisibility(View.GONE);
        }
    }

    @Override
    public void showTimeInfo(String time) {
        tvTime.setText(time);
    }

    public void initView(){
        rlTitleName = findViewById(R.id.rl_title_name);
        tvTitleName = findViewById(R.id.tv_title_name);
        tvTime = findViewById(R.id.tv_time);
    }
}
