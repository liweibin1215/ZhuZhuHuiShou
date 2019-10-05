package com.rys.smartrecycler.device.balance;

import com.lwb.devices.serialport.IOReadSeriaPort;
import com.rys.smartrecycler.BuildConfig;
import com.rys.smartrecycler.db.controller.SystemSetController;

import java.io.File;
import java.util.Random;

/**
 * 创作时间： 2019/5/8 on 上午10:30
 * <p>
 * 描述：电子秤通用工具类
 * <p>
 * 作者：lwb
 */

public class BalanceManager {
    public static BalanceManager instance;
    private IOReadSeriaPort mIOReadSeriaPort;
    private IoProtocol mIoProtocol;
    public BalanceManager(){

    }
    public static BalanceManager getInstance(){
        if(null == instance){
            synchronized (BalanceManager.class){
                if(null == instance){
                    instance = new BalanceManager();
                }
            }
        }
        return instance;
    }

    /**
     * 打开串口
     */
    public boolean openMainBoardDevice(){
        try {
            String broadPath = SystemSetController.getSysInfo("BalanceCom");
            int broadBaudrate = 19200;
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

    /**
     * 判断设备是否开启
     * @return
     */
    public boolean isDeviceOpened(){
        if(BuildConfig.IS_AUTO_TEST){
            return true;
        }else{
            return mIoProtocol != null && mIOReadSeriaPort != null;
        }
    }
    /**
     * 称重
     * @param adress
     * @return
     */
    public String getWeight(int lastBalanceWeight,String adress){
        if(BuildConfig.IS_AUTO_TEST){
            return (new Random().nextInt(200)+lastBalanceWeight)+"";
        }else{
            return mIoProtocol == null?"":mIoProtocol.readWeigth(adress);
        }
    }

    /**
     * 获取量程
     * @param adress
     * @return
     */
    public String getMaxRange(String adress){
        if(BuildConfig.IS_AUTO_TEST){
            return "500000";
        }else{
            return mIoProtocol == null?"":mIoProtocol.getMaxValue(adress);
        }
    }

    /**
     * 设置量程
     * @param adress
     * @param maxValue
     * @return
     */
    public boolean setMaxRange(String adress,int maxValue){
        if(BuildConfig.IS_AUTO_TEST){
            return true;
        }else{
            return mIoProtocol == null?false:mIoProtocol.writeMaxValue(adress,maxValue);
        }
    }


    /**
     * 清零
     * @param adress
     * @return
     */
    public boolean setZero(String adress){
        if(BuildConfig.IS_AUTO_TEST){
            return true;
        }else{
            return mIoProtocol == null?false:mIoProtocol.writeZero(adress);
        }
    }

    /**
     * 设置最小分度值
     * @param adress
     * @param minDevision
     * @return
     */
    public boolean setMinDevision(String adress,int minDevision){
        if(BuildConfig.IS_AUTO_TEST){
            return true;
        }else{
            return mIoProtocol == null?false:mIoProtocol.writeMinDevision(adress,minDevision);
        }
    }

    /**
     * 空载标定
     * @param adress
     * @return
     */
    public boolean setLDW(String adress){
        if(BuildConfig.IS_AUTO_TEST){
            return true;
        }else{
            return mIoProtocol == null?false:mIoProtocol.writeLDW(adress);
        }
    }

    /**
     * 固定值标定
     * @param adress
     * @param value
     * @return
     */
    public boolean setLWT(String adress,int value){
        if(BuildConfig.IS_AUTO_TEST){
            return true;
        }else{
            return mIoProtocol == null?false:mIoProtocol.writeLWT(adress,value);
        }
    }

    /**
     * 关闭设备
     */
    public void closeDevice(){
        if(mIOReadSeriaPort != null){
            mIOReadSeriaPort.closeDevice();
            mIOReadSeriaPort = null;
            mIoProtocol = null;
        }
    }

}
