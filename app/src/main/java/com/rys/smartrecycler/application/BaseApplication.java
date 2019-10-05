package com.rys.smartrecycler.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.rys.smartrecycler.db.DbHelper;
import com.rys.smartrecycler.db.controller.DeviceInfoController;
import com.rys.smartrecycler.db.retbean.DeviceInfo;


public class BaseApplication extends Application {

    private static BaseApplication instance;
    private boolean isLogin = false;
    private String  userPhone = "";
    private int     userId = 0;
    private int     loginStatus = 0;//不可登录 1支持用户登录 2支持回收员登录

    public int getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }

    public static BaseApplication getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Not yet initialized");
        }
        return instance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token = "";
    private String terminalId = "";

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public void setTerminalIdPwd(String terminalIdPwd) {
        this.terminalIdPwd = terminalIdPwd;
    }

    private String terminalIdPwd = "";

    public String getTerminalId() {
        return terminalId;
    }

    public String getTerminalIdPwd() {
        return terminalIdPwd;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        CrashHandler.getInstance().init(this.getApplicationContext());
        DbHelper.getInstance(this).getWriteableDatabase();//创建或者打开数据库
        DeviceInfo deviceInfo = DeviceInfoController.getDeviceInfo();
        if(deviceInfo != null){
            terminalId = deviceInfo.getDeviceSn();
            terminalIdPwd =deviceInfo.getDevicePwd();
        }

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public boolean isLogin() {
        return isLogin;
    }

    public String getUserPhone() {
        return "17602198669";
    }

    public int getUserId() {
        return userId;
    }
    public void setLoginInfo(boolean isLogin,String userPhone,int userId){
        this.isLogin = isLogin;
        this.userPhone = userPhone;
        this.userId = userId;
    }
    public void setLoginInfoOff(){
        if(this.isLogin){
            this.isLogin = false;
            this.userPhone = "";
            this.userId = 0;
        }
    }
    public boolean isMqttConnect() {
        return isMqttConnect;
    }

    public void setMqttConnect(boolean mqttConnect) {
        isMqttConnect = mqttConnect;
    }

    private boolean isMqttConnect = false;
}
