package com.lwb.framelibrary.tool;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 创作时间： 2018/11/12 on 下午3:39
 * <p>
 * 描述：短音频播放工具,备注：只建议播放短音频
 * <p>
 * 作者：lwb
 */

public class SoundPoolManager {
    public Context mContext;
    public SoundPool soundPool = null;
    public Map<String,Integer> soundMaps = null;
    public static SoundPoolManager instance;
    public boolean isNoVoice = false;
    public int     mapSize = 0;
    public Integer lastVoice;
    public static synchronized SoundPoolManager getInstance(){
        if(instance == null){
            synchronized(SoundPoolManager.class){
                if(instance == null){
                    instance = new SoundPoolManager();
                }
            }
        }
        return instance;
    }

    /**
     * 音频初始化，默认初始化数字声音
     * @param mContext
     * @throws IOException
     */
    public void init(Context mContext) {
        this.mContext = mContext;
        if(soundPool == null || soundMaps == null){
            soundMaps = new HashMap<>();
            soundPool = new SoundPool(10,
                    AudioManager.STREAM_SYSTEM, 1);
            AssetManager am = mContext.getAssets();
            for (int i = 0;i<10;i++){
                try {
                    soundMaps.put(String.valueOf(i), soundPool.load(am.openFd("keysound/digit"+i+".wav"), 1));
                    mapSize++;
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 释放资源
     */
    public void release(){
        if(soundMaps != null){
            soundMaps.clear();
        }
        if(soundPool != null){
            soundPool.release();
            soundPool = null;
        }
    }

    /**
     * 播放数字声音
     * @param number
     */
    public void startPlayWithOnlyNum(final int number){
        if(!isNoVoice && number >= 0 && number < 10){
            if(soundPool != null && soundMaps != null){
                Integer id = soundMaps.get(String.valueOf(number));
                if(id != null){
                    startPlay(id,false);
                }else{
                    AssetManager am = mContext.getAssets();
                    try {
                        soundMaps.put(String.valueOf(number), soundPool.load(am.openFd("digit/digit"+number+".wav"), 1));
                        mapSize++;
                        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                            @Override
                            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                                startPlay(soundMaps.get(String.valueOf(number)),false);
                            }
                        });
                    } catch (IOException e) {
                    }
                }
            }
        }
    }


    /**
     * 根据资源ID播放音频
     * @param resId
     */
    public void startPlayWithResId(int resId){
        if(!isNoVoice && soundPool != null && soundMaps != null){
            final String key = String.valueOf("resId"+resId);
            Integer id = soundMaps.get(String.valueOf(key));
            if(id == null){
                resetSoundPool();
                soundMaps.put(key,soundPool.load(mContext,resId,1));
                mapSize++;
                soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                        startPlay(soundMaps.get(key),true);
                    }
                });
            }else{
                startPlay(id,true);
            }
        }
    }

    /**
     * 根据Asset资源路径播放音频
     * @param assetPath
     */
    public void startPlayWithAsset(final String assetPath){
        if(!isNoVoice && soundPool != null && soundMaps != null){
            Integer id = soundMaps.get(String.valueOf(assetPath));
            if(id == null){
                try {
                    resetSoundPool();
                    AssetManager am = mContext.getAssets();
                    soundMaps.put(assetPath,soundPool.load(am.openFd(assetPath),1));
                    mapSize++;
                    soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                        @Override
                        public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                            startPlay(soundMaps.get(assetPath),true);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                startPlay(id,true);
            }
        }
    }

    /**
     * 根据文件路径加载音频
     * @param filePath
     */
    public void startPlayWithPath(String filePath){
        if(!isNoVoice && soundPool != null && soundMaps != null){
            final String key = filePath.contains("/")?filePath.substring(filePath.indexOf("/")+1):filePath;
            Integer id = soundMaps.get(String.valueOf(key));
            if(id == null){
                resetSoundPool();
                soundMaps.put(key,soundPool.load(filePath,1));
                mapSize++;
                soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                        startPlay(soundMaps.get(key),true);
                    }
                });
            }else{
                startPlay(id,true);
            }
        }
    }

    private void startPlay(int id,boolean isStopLast){
        if(isStopLast && lastVoice != null){
            soundPool.stop(lastVoice);
        }
        lastVoice = soundPool.play(id,0.8f,0.8f,1,0,1);
    }

    /**
     * 判断当前加载音频数量，如果超过了255个，则重新释放音效池
     */
    public void resetSoundPool(){
        if(mapSize >=255){//if(soundMaps.keySet().size() >=255){
            soundMaps.clear();
            soundPool.release();
            soundPool = null;
            mapSize = 0;
            soundPool = new SoundPool(10,
                    AudioManager.STREAM_SYSTEM, 1);
        }
    }

    /**
     * 重置静音设置开关
     * @param isNoVoice
     */
    public void resetIsVoice(boolean isNoVoice){
        this.isNoVoice = isNoVoice;
    }

    /**
     * 停止最新的一个音频播放
     */
    public void stopLastVoice(){
        if(soundPool != null && lastVoice != null){
            soundPool.stop(lastVoice);
        }
    }
}
