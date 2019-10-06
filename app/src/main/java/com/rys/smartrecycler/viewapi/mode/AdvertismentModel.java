package com.rys.smartrecycler.viewapi.mode;

import android.content.Context;
import android.media.MediaPlayer;

import com.lwb.framelibrary.view.base.BaseModelImpl;
import com.rys.smartrecycler.R;
import com.rys.smartrecycler.constant.CommonConstant;
import com.rys.smartrecycler.db.retbean.AdvInfo;
import com.rys.smartrecycler.viewapi.api.AdvertismentApi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间：2019/4/29
 * 作者：李伟斌
 * 功能描述:
 */

public class AdvertismentModel extends BaseModelImpl implements AdvertismentApi.Model {
    public AdvertismentModel(Context mContext) {
        super(mContext);
    }

    @Override
    public String getAllVedioList(String lastAdvName, List<AdvInfo> vos) {
        if(vos == null || vos.size() <= 0)return "";
        String[] paths;
        int position;
        for (int i = 0;i<vos.size();i++){
            if("".equals(vos.get(i).getVideoPath()))continue;
            paths = vos.get(i).getVideoPath().split(",");
            if(paths == null || paths.length <= 0)continue;
            position = -1;
            for (int j=0;j<paths.length;j++){
                if("".equals(paths[j]) || !paths[j].toLowerCase().endsWith(".mp4"))continue;
                if("".equals(lastAdvName)){
                    if(new File(CommonConstant.SYSTEM_SOURSE_PATH+paths[j]).exists()){
                        return paths[j];
                    }else{
                        continue;
                    }
                }
                if(lastAdvName.equals(paths[j])){
                    position = j;
                    continue;
                }
                if(position >= 0 && paths[j].toLowerCase().endsWith(".mp4") && new File(CommonConstant.SYSTEM_SOURSE_PATH+paths[j]).exists()){
                    return paths[j];
                }
            }
        }
        for (int i = 0;i<vos.size();i++){
            if("".equals(vos.get(i).getVideoPath()))continue;
            paths = vos.get(i).getVideoPath().split(",");
            if(paths == null || paths.length <= 0)continue;
            for (int j=0;j<paths.length;j++){
                if("".equals(paths[j]) || !paths[j].toLowerCase().endsWith(".mp4"))continue;
                if(new File(CommonConstant.SYSTEM_SOURSE_PATH+paths[j]).exists()){
                    return paths[j];
                }else{
                    continue;
                }
            }
        }
        return "";
    }

    @Override
    public List<String> getAllPicList(List<AdvInfo> vos) {
        List<String> picList = new ArrayList<>();
        String[] paths;
        for (int i = 0;i<vos.size();i++){
            if("".equals(vos.get(i).getImgPath()))continue;
            paths = vos.get(i).getImgPath().split(",");
            if(paths == null || paths.length <= 0)continue;
            for (int j=0;j<paths.length;j++){
                if("".equals(paths[j]))continue;
                if(paths[j].toLowerCase().endsWith(".png") || paths[j].toLowerCase().endsWith(".jpg")||paths[j].toLowerCase().endsWith(".jpeg")){
                    if(new File(CommonConstant.SYSTEM_SOURSE_PATH+paths[j]).exists()){
                        picList.add("file://"+ CommonConstant.SYSTEM_SOURSE_PATH+"/"+paths[j]);
                    }
                }
            }
        }
        return picList;
    }

    @Override
    public List<Object> getAllStaticPics() {
        List<Object> infos = new ArrayList<>();
        File root = new File(CommonConstant.SYSTEM_SOURSE_PATH);
        if(root.exists()){
            File files[] = root.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                    } else {
                        String name = f.getName();
                        if (name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg")|| name.toLowerCase().endsWith(".jpeg")) {
                            infos.add("file://"+ CommonConstant.SYSTEM_SOURSE_PATH+"/"+name);
                            if(infos.size() >= 10) break;//最多只加载10张图
                        }
                    }
                }
            }
        }else{
            root.mkdirs();
        }
      if(infos == null || infos.size() <= 0){
          infos.add(R.drawable.adv_default1);
          infos.add(R.drawable.adv_default2);
      }
        return infos;
    }

    @Override
    public void startPlayVedio(MediaPlayer mediaPlayer, String fileName, final int currentPosition, MediaPlayer.OnErrorListener onErrorListener) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(CommonConstant.SYSTEM_SOURSE_PATH+fileName);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.seekTo(currentPosition);
                    mp.setVolume(0.2f, 0.2f);
                    mp.start();
                }
            });
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            onErrorListener.onError(mediaPlayer,-1,0);
        }
    }

    @Override
    public void canCelRequest() {

    }
}
