/*
 *             				  _ooOoo_  
 *                           o8888888o  
 *                           88" . "88  
 *                           (| -_- |)  
 *                            O\ = /O  
 *                        ____/`---'\____  
 *                      .   ' \\| |// `.  
 *                       / \\||| : |||// \  
 *                     / _||||| -:- |||||- \  
 *                       | | \\\ - /// | |  
 *                     | \_| ''\---/'' | |  
 *                      \ .-\__ `-` ___/-. /  
 *                   ___`. .' /--.--\ `. . __  
 *                ."" '< `.___\_<|>_/___.' >'"".  
 *               | | : `- \`.;`\ _ /`;.`/ - ` : | |  
 *                 \ \ `-. \_ __\ /__ _/ .-` / /  
 *         ======`-.____`-.___\_____/___.-`____.-'======  
 *                            `=---='  
 *  
 *         .............................................  
 *                  	       佛祖保佑             永无BUG 
 *         		佛曰:  
 *                      写字楼里写字间，写字间里程序员；  
 *                 	            程序人员写程序，又拿程序换酒钱。  
 *                      酒醒只在网上坐，酒醉还来网下眠；  
 *                      酒醉酒醒日复日，网上网下年复年。  
 *                      但愿老死电脑间，不愿鞠躬老板前；  
 *                      奔驰宝马贵者趣，公交自行程序员。  
 *                      别人笑我忒疯癫，我笑自己命太贱；  
 *                 	            不见满街漂亮妹，哪个归得程序员？  
 * 
 * Copyright (C) 2014  
 * 版权所有
 *
 * 功能描述： 提示框工具类
 *
 * 创建标识： Xuxq 2014-11-19
 *
 * 修改标识：
 * 修改描述：
 */
package com.lwb.framelibrary.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lwb.framelibrary.R;


/**
 * 创建时间：2017/09/10
 * 作者：李伟斌
 * 功能描述:
 */
public class ToastUtil {
    private static Toast toast;
    private static LinearLayout toastView;

    /**
     * 修改原布局的Toast
     */
    public ToastUtil() {

    }

    /**
     * 完全自定义布局Toast
     *
     * @param context
     * @param view
     */
    public ToastUtil(Context context, View view, int duration) {
        toast = new Toast(context);
        toast.setView(view);
        toast.setDuration(duration);
    }

    /**
     * 向Toast中添加自定义view
     *
     * @param view
     * @param postion
     * @return
     */
    public ToastUtil addView(View view, int postion) {
        toastView = (LinearLayout) toast.getView();
        toastView.addView(view, postion);

        return this;
    }

    /**
     * 设置Toast字体及背景颜色
     *
     * @param messageColor
     * @param backgroundColor
     * @return
     */
    public ToastUtil setToastColor(int messageColor, int backgroundColor) {
        View view = toast.getView();
        if (view != null) {
            TextView message = ((TextView) view.findViewById(android.R.id.message));
            message.setBackgroundColor(backgroundColor);
            message.setTextColor(messageColor);
        }
        return this;
    }


    /**
     * 设置Toast字体及背景
     *
     * @param messageColor
     * @param background
     * @return
     */
    public ToastUtil setToastBackground(int messageColor, int background) {
        View view = toast.getView();
        if (view != null) {
            TextView message = ((TextView) view.findViewById(android.R.id.message));
            message.setBackgroundResource(background);
            message.setTextColor(messageColor);
        }
        return this;
    }

    /**
     * 短时间显示Toast
     */
    public ToastUtil Short(Context context, CharSequence message) {
        if (toast == null || (toastView != null && toastView.getChildCount() > 1)) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toastView = null;
        } else {
            toast.setText(message);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        return this;
    }

    /**
     * 短时间显示Toast
     */
    public ToastUtil Short(Context context, int message) {
        if (toast == null || (toastView != null && toastView.getChildCount() > 1)) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toastView = null;
        } else {
            toast.setText(message);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        return this;
    }

    /**
     * 长时间显示Toast
     */
    public ToastUtil Long(Context context, CharSequence message) {
        if (toast == null || (toastView != null && toastView.getChildCount() > 1)) {
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toastView = null;
        } else {
            toast.setText(message);
            toast.setDuration(Toast.LENGTH_LONG);
        }
        return this;
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public ToastUtil Long(Context context, int message) {
        if (toast == null || (toastView != null && toastView.getChildCount() > 1)) {
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toastView = null;
        } else {
            toast.setText(message);
            toast.setDuration(Toast.LENGTH_LONG);
        }
        return this;
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public ToastUtil Indefinite(Context context, CharSequence message, int duration) {
        if (toast == null || (toastView != null && toastView.getChildCount() > 1)) {
            toast = Toast.makeText(context, message, duration);
            toastView = null;
        } else {
            toast.setText(message);
            toast.setDuration(duration);
        }
        return this;
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public ToastUtil Indefinite(Context context, int message, int duration) {
        if (toast == null || (toastView != null && toastView.getChildCount() > 1)) {
            toast = Toast.makeText(context, message, duration);
            toastView = null;
        } else {
            toast.setText(message);
            toast.setDuration(duration);
        }
        return this;
    }

    /**
     * 显示Toast
     *
     * @return
     */
    public ToastUtil show() {
        toast.show();

        return this;
    }

    /**
     * 获取Toast
     *
     * @return
     */
    public Toast getToast() {
        return toast;
    }

    /**
     * 居中带图片的toast
     *
     * @param context
     * @param duration 事件间隔
     */
    public static void showToastByPic(Context context, String msg, int duration) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastView = (LinearLayout) toast.getView();
        ImageView imageCodeProject = new ImageView(context);
        //imageCodeProject.setImageResource(R.mipmap.ic_launcher);
        toastView.addView(imageCodeProject, 0);
        toast.show();
    }

    /******************* 多次触发Toast，只显示一次 *******************************/
    /**
     * @author tu
     * @since 2015/4/27
     */
    private static String oldMsg;
    private static long oneTime = 0;
    private static long twoTime = 0;
    private static Toast mToast = null;

    public static void showToast(Context context, String s) {
        if (mToast == null) {
            mToast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
            mToast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    mToast.show();
                }
            } else {
                oldMsg = s;
                mToast.setText(s);
                mToast.show();
            }
        }
        oneTime = twoTime;
    }

    /**
     * 多次触发Toast，只显示一次
     *
     * @param context
     * @param resId
     */
    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }

    /**
     * 普通toast
     *
     * @param ctx
     * @param string
     */
    public static void showMessage(Context ctx, String string) {
        if (ctx != null)
            Toast.makeText(ctx, string, Toast.LENGTH_SHORT).show();

    }

    public static void showWhite(Context context, String string) {
        if (toast == null || (toastView != null && toastView.getChildCount() > 1)) {
            toast = new Toast(context);
            toastView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_toast, null);
            TextView textView = (TextView) toastView.findViewById(R.id.tv_info);
            textView.setText(string);
            toast.setView(toastView);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast.show();
        }

    }

    /**
     * 自定义toast
     * @param context
     * @param msg
     */
    public static void showCenterToast(Context context, String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (toast != null){
            toast.cancel();
        }
        View view = View.inflate(context, R.layout.layout_baby, null);
        TextView textView = (TextView) view.findViewById(R.id.msg);
        textView.setText(msg);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 150);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }
}
