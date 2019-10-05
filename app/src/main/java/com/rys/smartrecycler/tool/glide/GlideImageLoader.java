package com.rys.smartrecycler.tool.glide;

import android.content.Context;
import android.widget.ImageView;

import com.lwb.framelibrary.tool.glide.common.BaseGlideUtil;
import com.youth.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        BaseGlideUtil.loadImage(context,path,imageView);
    }

}
