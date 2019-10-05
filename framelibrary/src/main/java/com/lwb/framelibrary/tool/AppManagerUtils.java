package com.lwb.framelibrary.tool;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 创作时间： 2018/8/13 on 下午4:32
 * <p>
 * 描述：统一Activity管理类
 * <p>
 * 作者：lwb
 */

public class AppManagerUtils {
    private List<Activity> activityStack = new ArrayList<>();
    private static AppManagerUtils instance;
    public static synchronized AppManagerUtils getInstance() {
        if (instance == null) {//第一层判断
            synchronized (AppManagerUtils.class) {
                if (instance == null) {//第二层判断，确保单例
                    instance = new AppManagerUtils();
                }
            }
        }
        return instance;
    }
    /**
     * 添加Activity到堆栈，并将最后添加的置于栈顶(模拟后进先出原则)
     */
    public void addActivity(Activity activity){
        activityStack.add(0,activity);
    }
    /**
     * 获取当前Activity
     */
    public Activity getTopActivity(){
        if(activityStack.size() == 0)return null;
        Iterator<Activity> iterator = activityStack.iterator();
        Activity act;
        if (iterator.hasNext()) {
            act = iterator.next();
            return act;
        }
        return null;
    }
    /**
     * 结束当前Activity
     */
    public void finisTopActivity(){
        if(activityStack.size() ==0)return;
        Iterator<Activity> iterator = activityStack.iterator();
        Activity act;
        if (iterator.hasNext()) {
            act = iterator.next();
            iterator.remove();
            act.finish();
        }
    }
    /**
     * 判断当前Acivity是否在栈顶
     * @param activity
     * @return
     */
    public boolean checkCurrentActivityIsTop(Activity activity){
        Iterator<Activity> iterator = activityStack.iterator();
        Activity act;
        if (iterator.hasNext()) {
            act = iterator.next();
            if(activity == act){
                return true;
            }
        }
        return false;
    }
    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity){
        Iterator<Activity> iterator = activityStack.iterator();
        Activity act;
        while (iterator.hasNext()) {
            act = iterator.next();
            if(activity == act){
                iterator.remove();
                activity.finish();
            }
        }
    }
    /**
     * 移除队列中的Activity
     */
    public void removeActivity(Activity activity){
        Iterator<Activity> iterator = activityStack.iterator();
        Activity act;
        while (iterator.hasNext()) {
            act = iterator.next();
            if(activity == act){
                iterator.remove();
            }
        }
    }
    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls){
        Iterator<Activity> iterator = activityStack.iterator();
        Activity activity;
        while (iterator.hasNext()) {
            activity = iterator.next();
            if(activity.getClass().equals(cls) ){
                iterator.remove();
                activity.finish();
            }
        }
    }

    /**
     * 结束指定activity之外的所有
     * @param cls
     */
    public void finishAllOtherActivity(Class<?> cls){
        Iterator<Activity> iterator = activityStack.iterator();
        Activity activity;
        while (iterator.hasNext()) {
            activity = iterator.next();
            if(!activity.getClass().equals(cls) ){
                iterator.remove();
                activity.finish();
            }
        }
    }
    /**
     * 结束所有Activity
     */
    public void finishAllActivity(){
        Iterator<Activity> iterator = activityStack.iterator();
        Activity activity;
        while (iterator.hasNext()) {
            activity = iterator.next();
            iterator.remove();
            activity.finish();
        }
    }
    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
