package com.rys.smartrecycler.tool.glide;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.youth.banner.loader.ImageLoader;

/**
 * 创建时间：2018/3/30
 * 作者：李伟斌
 * 功能描述:
 */

public class MyImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Picasso 加载图片简单用法
        if(path instanceof String)
            Picasso.with(context).load((String) path).into(imageView);
        else
            Picasso.with(context).load((Integer) path).into(imageView);
    }
}
