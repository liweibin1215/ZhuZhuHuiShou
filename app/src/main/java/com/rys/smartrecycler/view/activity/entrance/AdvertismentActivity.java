package com.rys.smartrecycler.view.activity.entrance;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.rys.smartrecycler.R;
import com.rys.smartrecycler.event.AdvertismentEvent;
import com.rys.smartrecycler.tool.glide.MyImageLoader;
import com.rys.smartrecycler.view.base.BaseTitleActivity;
import com.rys.smartrecycler.viewapi.api.AdvertismentApi;
import com.rys.smartrecycler.viewapi.presenter.AdvertismentPresenter;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;

public class AdvertismentActivity extends BaseTitleActivity<AdvertismentApi.View, AdvertismentApi.Presenter<AdvertismentApi.View>> implements AdvertismentApi.View, SurfaceHolder.Callback {
    private Banner banner;
    private SurfaceHolder sf_holder;
    private SurfaceView sfAdv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisment);
        ButterKnife.bind(this);
        sfAdv = findViewById(R.id.sf_adv);
        sf_holder = sfAdv.getHolder();
        sf_holder.addCallback(this);
        initBanner();
        getPresenter().getLocalAdvInfos();
        EventBus.getDefault().register(this);
    }

    private void initBanner() {
        banner =findViewById(R.id.banner);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new MyImageLoader());
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.ScaleInOut);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(5000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);//设置指示器位置
        //banner设置方法全部调用完毕时最后调用
        banner.setOnBannerListener(position -> goToHomeActivity());
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AdvertismentEvent event) {
        if (event != null && event.getCode() == 1000) {
            getPresenter().mSurfaceStop();
            getPresenter().resetAdvPlay();
        }
    }
    @Override
    public AdvertismentApi.Presenter<AdvertismentApi.View> createPresenter() {
        return new AdvertismentPresenter(mContext);
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
        EventBus.getDefault().unregister(false);
    }

    @Override
    protected void startInvoke(Bundle savedInstanceState) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        finish();
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public SurfaceHolder getHolder() {
        return sf_holder;
    }

    @Override
    public void startShowBinnerPlay(List<Object> vos) {
        //设置图片集合
        if(vos != null && vos.size() > 0){
            banner.setImages(vos);
            banner.setVisibility(View.VISIBLE);
            sfAdv.setVisibility(View.GONE);
            banner.start();
        }
    }

    @Override
    public void goToHomeActivity() {
        finish();
    }

    @Override
    public void showVedioPlay() {
        banner.setVisibility(View.GONE);
        banner.stopAutoPlay();
        sfAdv.setVisibility(View.VISIBLE);
    }

    @Override
    public void resetPlayStatus() {

    }

    @Override
    public void excuteFragmentAction(int type, String... params) {

    }

    @Override
    public void showTimeInfo(String time) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        getPresenter().mSurfaceCreated();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        getPresenter().mSurfaceDestroyed();
    }
}
