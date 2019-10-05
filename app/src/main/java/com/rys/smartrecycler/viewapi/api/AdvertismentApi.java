package com.rys.smartrecycler.viewapi.api;

import android.media.MediaPlayer;
import android.view.SurfaceHolder;

import com.lwb.framelibrary.view.base.BaseModel;
import com.lwb.framelibrary.view.base.BasePresenter;
import com.lwb.framelibrary.view.base.BaseView;
import com.rys.smartrecycler.db.retbean.AdvInfo;

import java.util.List;

/**
 * 创建时间：2019/4/29
 * 作者：李伟斌
 * 功能描述:
 */

public interface AdvertismentApi {
    interface View extends BaseView{
        SurfaceHolder getHolder();
        void startShowBinnerPlay(List<Object> vos);
        void goToHomeActivity();
        void showVedioPlay();
        void resetPlayStatus();
    }
    interface Presenter<V extends BaseView> extends BasePresenter<V>{
        void getLocalAdvInfos();
        void mSurfaceDestroyed();
        void mSurfaceStop();
        void mSurfaceCreated();
        void resetAdvPlay();
    }
    interface Model extends BaseModel{
        String getAllVedioList(String lastAdvName, List<AdvInfo> vos);
        List<String> getAllPicList(List<AdvInfo> vos);
        List<Object> getAllStaticPics();
        void startPlayVedio(MediaPlayer mediaPlayer, String fileName, int currentPosition, MediaPlayer.OnErrorListener onErrorListener);
    }
}
