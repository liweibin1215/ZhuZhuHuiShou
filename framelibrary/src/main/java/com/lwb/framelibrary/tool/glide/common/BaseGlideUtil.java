package com.lwb.framelibrary.tool.glide.common;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.lwb.framelibrary.R;
import com.lwb.framelibrary.tool.glide.BlurTransformation;


public class BaseGlideUtil {
    public static RequestOptions optionsDefault = new RequestOptions()
            .placeholder(R.drawable.ic_launcher)
            .error(R.drawable.ic_launcher)
            .diskCacheStrategy(DiskCacheStrategy.ALL) //默认缓存处理后的图片
            .centerCrop();

    public static RequestOptions optionsDefault2 = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL) //默认缓存处理后的图片
            .centerCrop();


    public static RequestOptions optionsNoDisk = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE) //默认缓存处理后的图片
            ;

    /**
     * 加载图片
     *
     * @param mContext  activity对象
     * @param url       图片url、res、file
     * @param imageView 图片组件
     */
    public static void loadImage(Context mContext, Object url, ImageView imageView) {
        //设置图片并且去除换成防止下次加载显示上张图片禁用磁盘缓存
        Glide.with(mContext).load(url)
                //过渡动画
                .transition(DrawableTransitionOptions.withCrossFade())
                //V4 写法
                .apply(optionsDefault)
                .into(imageView);
    }

    /**
     * 加载图片
     *
     * @param mContext  activity对象
     * @param url       图片url、res、file
     * @param imageView 图片组件
     */
    public static void loadImageNoPlaceholder(Context mContext, Object url, ImageView imageView) {
        //设置图片并且去除换成防止下次加载显示上张图片禁用磁盘缓存
        Glide.with(mContext).load(url)
                //过渡动画
                .transition(DrawableTransitionOptions.withCrossFade())
                //V4 写法
                .apply(new RequestOptions()
                        .error(R.drawable.ic_launcher)
                        .centerCrop())
                .into(imageView);
    }

    public static void loadImageNoError(Context mContext, Object url, ImageView imageView) {
        //设置图片并且去除换成防止下次加载显示上张图片禁用磁盘缓存
        Glide.with(mContext).load(url)
                //过渡动画
                .transition(DrawableTransitionOptions.withCrossFade())
                //V4 写法
                .apply(optionsDefault2)
                .into(imageView);
    }

    /**
     * 加载圆形图片
     *
     * @param mContext  activity对象
     * @param url       图片url、res、file
     * @param imageView 图片组件
     */
    public static void loadCircleImage(Context mContext, Object url, ImageView imageView) {
        //设置图片并且去除换成防止下次加载显示上张图片禁用磁盘缓存
        Glide.with(mContext).load(url)
                //过渡动画
//                .transition(DrawableTransitionOptions.withCrossFade())
                //V4 写法
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_launcher)
                        .error(R.drawable.ic_launcher)
                        .diskCacheStrategy(DiskCacheStrategy.ALL) //默认缓存处理后的图片
                        .centerCrop()
                        .optionalCircleCrop())
                .into(imageView);
    }

    /**
     * 加载毛玻璃
     *
     * @param mContext  activity对象
     * @param url       图片url、res、file
     * @param imageView 图片组件
     */
    public static void loadBlurImage(Context mContext, Object url, ImageView imageView) {
        //设置图片并且去除换成防止下次加载显示上张图片禁用磁盘缓存
        Glide.with(mContext).load(url)
                //过渡动画
//                .transition(DrawableTransitionOptions.withCrossFade())
                //V4 写法
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_launcher)
                        .error(R.drawable.ic_launcher)
                        .transform(new BlurTransformation(mContext, 25)))
                .into(imageView);
    }

    /**
     * 加载圆角图片
     *
     * @param mContext  activity对象
     * @param url       图片url、res、file
     * @param imageView 图片组件
     */
    public static void loadRoundedImage(Context mContext, Object url, ImageView imageView, int roundingRadius) {
        //设置图片并且去除换成防止下次加载显示上张图片禁用磁盘缓存
        Glide.with(mContext).load(url)
                //过渡动画
                .transition(DrawableTransitionOptions.withCrossFade())
                //V4 写法
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_launcher)
                        .error(R.drawable.ic_launcher)
                        .transform(new RoundedCorners(roundingRadius)))
                .into(imageView);
    }

    /**
     * 加载图片
     *
     * @param mContext  activity对象
     * @param url       图片url、res、file
     * @param imageView 图片组件
     */
    public static void loadImage(Context mContext, Object url, ImageView imageView, @DrawableRes int errorRes) {
        //设置图片并且去除换成防止下次加载显示上张图片禁用磁盘缓存
        Glide.with(mContext).load(url)
                //过渡动画
                .transition(DrawableTransitionOptions.withCrossFade())
                //V4 写法
                .apply(new RequestOptions()
                        .placeholder(errorRes)
                        .error(errorRes)
                        .centerCrop())
                .into(imageView);
    }

    /**
     * 加载图片
     *
     * @param mContext  activity对象
     * @param url       图片url、res、file
     * @param imageView 图片组件
     */
    public static void loadImage(Context mContext, Object url, ImageView imageView, boolean centerInside, @DrawableRes int errorRes) {
        RequestOptions options = new RequestOptions()
                .placeholder(errorRes)
                .error(errorRes);
        options = centerInside ? options.fitCenter() : options.centerCrop();

        //设置图片并且去除换成防止下次加载显示上张图片禁用磁盘缓存
        Glide.with(mContext).load(url)
                //过渡动画
                .transition(DrawableTransitionOptions.withCrossFade())
                //V4 写法
                .apply(options)
                .into(imageView);
    }

}
