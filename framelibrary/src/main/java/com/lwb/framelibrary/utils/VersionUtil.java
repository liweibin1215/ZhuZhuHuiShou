package com.lwb.framelibrary.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by mayn on 2018/3/27.
 */

public class VersionUtil {

    /**
     * 获取当前应用的版本号
     *
     * @param
     */
    public static int getVersionCode(Context context) {
        int versionCode = 1;
        try {
            PackageManager pm = context.getPackageManager();
            versionCode = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS).versionCode;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }
    /**
     * 获取当前应用的版本号名字
     *
     * @param
     */
    public static String getVersionName(Context context) {
        String versionName="";
        try {
            PackageManager pm = context.getPackageManager();
            versionName = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS).versionName;
            return versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }
}
