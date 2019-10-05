package com.rys.smartrecycler.viewapi.presenter;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;

import com.lwb.framelibrary.view.base.BasePresenterImpl;
import com.rys.smartrecycler.db.controller.AdvController;
import com.rys.smartrecycler.db.retbean.AdvInfo;
import com.rys.smartrecycler.viewapi.api.AdvertismentApi;
import com.rys.smartrecycler.viewapi.mode.AdvertismentModel;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间：2019/4/29
 * 作者：李伟斌
 * 功能描述:
 */

public class AdvertismentPresenter extends BasePresenterImpl<AdvertismentApi.View,AdvertismentApi.Model> implements AdvertismentApi.Presenter<AdvertismentApi.View>, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    private MediaPlayer mPlayer;
    private List<AdvInfo> vos = new ArrayList<>();
    private List<Object> picVos;
    private String lastAdvName = "";
    private boolean isSurfaceCreate = false;
    private String lastErrorVedio = "";
    private int    vedioErrorTime =0;
    private Handler handler = new Handler();
    public AdvertismentPresenter(Context context) {
        super(context);
    }

    @Override
    public AdvertismentApi.Model attachModel() {
        return new AdvertismentModel(mContext);
    }

    @Override
    public void getLocalAdvInfos() {
        vos = AdvController.getAllAdvInfos(1);
    }

    @Override
    public void mSurfaceDestroyed() {
        isSurfaceCreate = false;
        if (mPlayer != null) {
            mPlayer.stop();
        }
    }

    @Override
    public void mSurfaceStop() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }

    @Override
    public void mSurfaceCreated() {
        isSurfaceCreate = true;
        playAdv();
    }

    @Override
    public void resetAdvPlay() {
        if(vos !=null){
            vos.clear();
        }
        vos = AdvController.getAllAdvInfos(1);
        if(mPlayer != null && (mPlayer.isPlaying() || mPlayer.isLooping())){
            mPlayer.stop();
            mPlayer.reset();
        }
        playAdv();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (!isViewAttached() || !isModelAttached()) {
            return;
        }
        mPlayer.reset();
        handler.postDelayed(() -> playAdv(),1000);
    }

    public void playAdv(){
        if(!isViewAttached())return;
        if(vos == null || vos.size() <= 0){
            vos = AdvController.getAllAdvInfos(1);
        }
        lastAdvName = getModel().getAllVedioList(lastAdvName,vos);
        if(!"".equals(lastAdvName) && (!lastErrorVedio.equals(lastAdvName) || (lastErrorVedio.equals(lastAdvName) && vedioErrorTime < 3))){
            if(isSurfaceCreate){
                if (mPlayer == null) {
                    mPlayer = getMediaPlayer(mContext);
                    mPlayer.setOnCompletionListener(this);
                    mPlayer.setOnErrorListener(this);
                }
                mPlayer.setDisplay(getView().getHolder());
                getModel().startPlayVedio(mPlayer,lastAdvName,0,this);
            }else{
                handler.postDelayed(() -> {
                    if(isViewAttached()){
                        getView().showVedioPlay();
                    }
                },5000);
            }
            return;
        }
        if(picVos == null){
            picVos = new ArrayList<>();
        }
        if(picVos.size() > 0)picVos.clear();
        picVos.addAll(getModel().getAllPicList(vos));
        if(picVos != null && picVos.size() > 0){
            getView().startShowBinnerPlay(picVos);
        }else{
            getView().startShowBinnerPlay(getModel().getAllStaticPics());
        }
    }

    private MediaPlayer getMediaPlayer(Context context) {
        MediaPlayer mediaplayer = new MediaPlayer();
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            return mediaplayer;
        }
        try {
            Class<?> cMediaTimeProvider = Class.forName("android.media.MediaTimeProvider");
            Class<?> cSubtitleController = Class.forName("android.media.SubtitleController");
            Class<?> iSubtitleControllerAnchor = Class.forName("android.media.SubtitleController$Anchor");
            Class<?> iSubtitleControllerListener = Class.forName("android.media.SubtitleController$Listener");
            Constructor constructor = cSubtitleController.getConstructor(
                    new Class[]{Context.class, cMediaTimeProvider, iSubtitleControllerListener});
            Object subtitleInstance = constructor.newInstance(context, null, null);
            Field f = cSubtitleController.getDeclaredField("mHandler");
            f.setAccessible(true);
            try {
                f.set(subtitleInstance, new Handler());
            } catch (IllegalAccessException e) {
                return mediaplayer;
            } finally {
                f.setAccessible(false);
            }
            Method setsubtitleanchor = mediaplayer.getClass().getMethod("setSubtitleAnchor",
                    cSubtitleController, iSubtitleControllerAnchor);
            setsubtitleanchor.invoke(mediaplayer, subtitleInstance, null);
        } catch (Exception e) {
        }
        return mediaplayer;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if(lastAdvName.equals(lastErrorVedio)){
            vedioErrorTime++;
        }else{
            lastErrorVedio = lastAdvName;
            vedioErrorTime=1;
        }
        return false;
    }
}
