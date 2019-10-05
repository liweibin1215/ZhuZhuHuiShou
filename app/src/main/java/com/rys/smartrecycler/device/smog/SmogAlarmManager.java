package com.rys.smartrecycler.device.smog;

import com.lwb.devices.serialport.IOReadSeriaPort;
import com.rys.smartrecycler.BuildConfig;
import com.rys.smartrecycler.db.controller.SystemSetController;

import java.io.File;

/**
 * 创作时间： 2019/5/8 on 上午10:30
 * <p>
 * 描述：
 * <p>
 * 作者：lwb
 */

public class SmogAlarmManager {
    public static SmogAlarmManager instance;
    private IOReadSeriaPort mIOReadSeriaPort;
    private IoProtocol mIoProtocol;
    public SmogAlarmManager(){

    }
    public static SmogAlarmManager getInstance(){
        if(null == instance){
            synchronized (SmogAlarmManager.class){
                if(null == instance){
                    instance = new SmogAlarmManager();
                }
            }
        }
        return instance;
    }

    /**
     * 判断设备是否开启
     * @return
     */
    public boolean isDeviceOpened(){
        if(BuildConfig.IS_AUTO_TEST) {
            return true;
        } else {
            return mIoProtocol != null && mIOReadSeriaPort != null;
        }
    }

    /**
     * 打开串口
     */
    public boolean openMainBoardDevice(){
        if(BuildConfig.IS_AUTO_TEST) {
            return true;
        } else {
            try {
                String broadPath = SystemSetController.getSysInfo("SmogAlarmCom");
                int broadBaudrate = 4800;
                if("".equals(broadPath)){
                    return false;
                }
                if (mIOReadSeriaPort != null) {
                    mIOReadSeriaPort.closeDevice();
                    mIOReadSeriaPort = null;
                }
                mIOReadSeriaPort = new IOReadSeriaPort(new File(broadPath), broadBaudrate, 0, 8, 1, 'N');
                mIoProtocol = new IoProtocol(mIOReadSeriaPort);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    /**
     * 询问报警器工作状态
     * @param desk
     * @return
     */
    public int checkSmogAlarmStatus(int desk){
        if(BuildConfig.IS_AUTO_TEST) {
            return 0;
        } else {
            String result = mIoProtocol.sendCmd(desk);
            if("".equals(result)){
                return -1;
            }
            String value = new String(new char[]{result.charAt(8),result.charAt(9)});
            if("00".equals(value)){
                return 0;//正常
            }else if("01".equals(value)){
                return 1;//报警
            }else{
                return -1;//打开
            }
        }
    }

    public void closeDevice(){
        if (mIOReadSeriaPort != null) {
            mIOReadSeriaPort.closeDevice();
            mIOReadSeriaPort = null;
        }
    }
}
