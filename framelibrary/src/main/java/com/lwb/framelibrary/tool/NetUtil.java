package com.lwb.framelibrary.tool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 创建时间：2019/2/18
 * 作者：李伟斌
 * 功能描述:网络管理工具类
 */

public class NetUtil {

    public static boolean isNetAvaliable(Context mContext){
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean connected = (null != activeNetworkInfo) && activeNetworkInfo.isConnected();
        return connected;
    }

    public static  boolean isNetworkOnline() {
        Process ipProcess = null;
        Runtime runtime = Runtime.getRuntime();
        try {
            ipProcess = runtime.exec("ping -c 3 114.114.114.114");//114.114.114.114
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
        }finally {
            if(ipProcess != null){
                ipProcess.destroy();
            }
        }
        return false;
    }

    public static boolean hasNetworkConnection(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean connected = (null != activeNetworkInfo) && activeNetworkInfo.isConnected();
        if(!connected) return false;
        boolean routeExists ;
        Socket s;
        try {
            s = new Socket();
            InetAddress host = InetAddress.getByName("114.114.114.114");//国内使用114.114.114.114，如果全球通用google：8.8.8.8
            s.connect(new InetSocketAddress(host,80),5000);//google:53
            routeExists = true;
            s.close();
        } catch (IOException e) {
            routeExists = false ;
        }
        return routeExists ;
    }

}
