package com.rys.smartrecycler.tool;

import android.util.Log;

import com.rys.smartrecycler.BuildConfig;


/**
 * 创作时间： 2018/8/23 on 上午10:24
 * <p>
 * 描述：调试跟踪
 * <p>
 * 作者：lwb
 */

public class LogDebug {
    /**
     * 问题跟踪打印
     * @param Tag
     * @param msg
     */
    public static void e(String Tag,String msg){
        if(BuildConfig.DEBUG)
            Log.e(Tag,msg);
    }

    /**
     * 控制台输出日志
     * @param msg
     */
    public static void println(String msg){
        if(BuildConfig.DEBUG)
            System.out.println(msg);
    }

    /**
     * 跟踪记录
     * @param tag
     * @param msg
     */
    public static void d(String tag,String msg){
        if(BuildConfig.DEBUG)
            Log.d(tag,msg);
    }

}
