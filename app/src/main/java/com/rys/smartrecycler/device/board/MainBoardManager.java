package com.rys.smartrecycler.device.board;

import com.lwb.devices.serialport.IOReadSeriaPort;
import com.lwb.framelibrary.utils.DataTyeConvertUtil;
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

public class MainBoardManager {
    public static MainBoardManager instance;
    private IOReadSeriaPort mIOReadSeriaPort;
    private IoProtocol mIoProtocol;
    private long defaultTime = 1000;
    public MainBoardManager() {

    }

    public static MainBoardManager getInstance() {
        if (null == instance) {
            synchronized (MainBoardManager.class) {
                if (null == instance) {
                    instance = new MainBoardManager();
                }
            }
        }
        return instance;
    }

    /**
     * 打开串口
     */
    public boolean openMainBoardDevice() {
        try {
            String broadPath = SystemSetController.getSysInfo("MainboardCom");
            int broadBaudrate = 9600;
            if ("".equals(broadPath)) {
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
     *
     * @return
     */
    public boolean isDeviceOpened() {
        if (BuildConfig.IS_AUTO_TEST) {
            return true;
        } else {
            return mIoProtocol != null && mIOReadSeriaPort != null;
        }
    }

    /**
     * 发：00 80 03 01 00 A6 55
     * 收：00 81 03 01 00 01 00 01 00 7A 87
     * 获取版本信息
     * @param deskNo
     * @return
     */
    public String getBoardVersion(int deskNo){
        if (BuildConfig.IS_AUTO_TEST) {
            return "";
        }else{
            String result = mIoProtocol.sendCmd(deskNo,"80","03","0100",defaultTime,11);
            if(result == null || "".equals(result) || result.length() != 22){
                return "";
            }
            String value = new String(new char[]{result.charAt(10),result.charAt(11)}).toUpperCase();
            return value.substring(10,17);
        }
    }

    /**
     * 发：00 80 03 01 01 B6 74
     * 收：00 81 03 01 01 02 39 0E
     * 获取柜机类型：主柜、副柜
     * @param deskNo
     * @return
     */
    public String getBoardType(int deskNo){
        if (BuildConfig.IS_AUTO_TEST) {
            return "主柜";
        }else{
            String result = mIoProtocol.sendCmd(deskNo,"80","03","0101",defaultTime,8);
            if(result == null || "".equals(result)|| result.length() != 16){
                return "";
            }
            String value = new String(new char[]{result.charAt(10),result.charAt(11)}).toUpperCase();
            if("01".equals(value)){
                return "主柜";
            }else{
                return "副柜";
            }
        }
    }

    /**
     * 发：00 80 03 02 00 F3 06
     * 收：00 81 03 02 00 01 63 0C
     * 检测副柜是否箱满
     * @param deskNo
     * @return
     */
    public int checkDeskIsFull(int deskNo) {
        if (BuildConfig.IS_AUTO_TEST) {
            return 1;
        } else {
            String result = mIoProtocol.sendCmd(deskNo,"80","03","0200",defaultTime,8);
            if(result == null || "".equals(result) || result.length() != 16){
                return -1;
            }
            String value = new String(new char[]{result.charAt(10),result.charAt(11)}).toUpperCase();
            if("01".equals(value)){
                return 0;//箱满
            }else if("00".equals(value)){
                return 1;//未满
            }
            return -1;
        }
    }


    /**
     * 发：00 80 03 02 01 E3 27
     * 收：00 81 03 02 01 5F EB 06
     * 检测副柜使用的百分比
     *
     * @param deskNo
     * @return
     */
    public int checkDeskUsePercent(int percent, int deskNo) {
        if (BuildConfig.IS_AUTO_TEST) {
            return percent + 10;
        } else {
            String result = mIoProtocol.sendCmd(deskNo,"80","03","0201",defaultTime,8);
            if(result == null || "".equals(result) || result.length() != 16){
                return -1;
            }
            String value = new String(new char[]{result.charAt(10),result.charAt(11)}).toUpperCase();
            return DataTyeConvertUtil.hexToInt(value);//单位%
        }
    }

    /**
     *发：00 80 03 02 02 E3 27
     * 收：00 81 03 02 02 5F EB 06
     * 获取设备内部温度
     * @param deskNo
     * @return
     */
    public int getDeviceTemperature(int deskNo) {
        if (BuildConfig.IS_AUTO_TEST) {
            return 0;
        } else {
            String result = mIoProtocol.sendCmd(deskNo,"80","03","0202",defaultTime,8);
            if(result == null || "".equals(result) || result.length() != 16){
                return -1;
            }
            String value = new String(new char[]{result.charAt(10),result.charAt(11)}).toUpperCase();
            return DataTyeConvertUtil.hexToInt(value);//单位°
        }
    }

    /**
     * 开绿灯
     * 发：00 80 06 03 00 02 01 05 F4
     * 收：00 81 06 03 00 02 01 40 54
     * 关绿灯
     * 发：00 80 06 03 00 02 00 15 D5
     * 收：00 81 06 03 00 02 00 50 75
     * 打开或者关闭射灯
     * @param deskNo 副柜号
     * @param color 灯光颜色 0红色 1蓝色 2 绿色 3白色
     * @param isOpen 打开或者关闭
     * @return
     */
    public int openDeviceLight(int deskNo,int color,boolean isOpen){
        if (BuildConfig.IS_AUTO_TEST) {
            return 0;
        } else {
            String colorNum = "03";
            if(color == 0){
                colorNum = "00";
            }else if(color == 1){
                colorNum = "01";
            }else if(color == 2){
                colorNum = "02";
            }
            String result = mIoProtocol.sendCmd(deskNo,"80","06","0300"+colorNum+(isOpen?"01":"00"),defaultTime,9);
            if(result == null || "".equals(result) || result.length() != 18){
                return -1;
            }
            return 0;
        }
    }

    /**
     * 发：00 80 03 03 01 D0 16
     * 收：00 81 03 03 01 01 67 0D
     * 获取副柜灯开关状态
     * @param deskNo
     * @return
     */
    public int getDeviceLightStatus(int deskNo){
        if (BuildConfig.IS_AUTO_TEST) {
            return 0;
        } else {
            String result = mIoProtocol.sendCmd(deskNo,"80","03","0301",defaultTime,8);
            if(result == null || "".equals(result) || result.length() != 16){
                return -1;
            }
            String value = new String(new char[]{result.charAt(10),result.charAt(11)}).toUpperCase();
            if("00".equals(value)){
                return 0;//关闭状态
            }else{
                return 1;//打开状态
            }
        }
    }


    /**
     * 发：00 80 06 04 00 01 C7 B8
     * 收：00 81 06 04 00 01 6D E9
     * 打开或者关闭投递门
     * @param deskNo
     * @return
     */
    public int openDeskDoor(int deskNo){
        if (BuildConfig.IS_AUTO_TEST) {
            return 0;
        } else {
            String result = mIoProtocol.sendCmd(deskNo,"80","06","040001",defaultTime,8);
            if(result == null || "".equals(result) || result.length() != 16){
                return -1;
            }
            return 0;
        }
    }

    /**
     *
     * 发：00 80 06 04 00 00 D7 99
     * 收：00 81 06 04 00 00 7D C8
     * 关闭副柜投递口
     * @param deskNo
     */
    public int closeDeskDoor(int deskNo) {
        if (BuildConfig.IS_AUTO_TEST) {
            return 0;
        } else {
            String result = mIoProtocol.sendCmd(deskNo,"80","06","040000",defaultTime,8);
            if(result == null || "".equals(result) || result.length() != 16){
                return -1;
            }
            return 0;
        }
    }

    /**
     * 发：00 80 03 04 01 49 81
     * 收：00 81 03 04 01 03 C2 DF
     * 检测投递门的开关状态
     * @param deskNo
     * @return
     */
    public int checkDeskDoorStatus(int deskNo){
        if (BuildConfig.IS_AUTO_TEST) {
            return 0;
        } else {
            String result = mIoProtocol.sendCmd(deskNo,"80","03","0401",defaultTime,8);
            if(result == null || "".equals(result) || result.length() != 16){
                return -1;
            }
            String value = new String(new char[]{result.charAt(10),result.charAt(11)}).toUpperCase();
            if("00".equals(value)){
                return 0;//关闭状态
            }else{
                return 1;//打开状态
            }
        }
    }

    /**
     * 同时开上下柜门
     * 发：00 80 06 05 00 03 01 11 5C
     * 收：00 81 06 05 00 03 01 54 FC
     * 打开副柜回收门
     * @param deskNo 副柜号
     * @param openType 1开上门、2开下门、3双门开
     */
    public int openDeskRecycDoor(int deskNo,int openType) {
        if (BuildConfig.IS_AUTO_TEST) {
            return 0;
        }else{
            String type = "03";
            if(openType==1){
                type = "01";
            }else if(openType == 2){
                type = "02";
            }
            String result = mIoProtocol.sendCmd(deskNo,"80","06","0500"+type+"01",defaultTime,9);
            if(result == null || "".equals(result) || result.length() != 18){
                return -1;
            }
            return 0;
        }
    }


    /**
     * 发：00 80 03 05 01 7A B0
     * 收：00 81 03 05 01 00 00 05 E9
     * 检测回收门是否关闭
     * @param deskNo
     * @return
     */
    public int checkDeskRecycDoorIsClosed(int deskNo) {
        if (BuildConfig.IS_AUTO_TEST) {
            return 1;
        } else {
            String result = mIoProtocol.sendCmd(deskNo,"80","03","0501",defaultTime,9);
            if(result == null || "".equals(result) || result.length() != 18){
                return -1;
            }
            String valueleft = new String(new char[]{result.charAt(10),result.charAt(11)}).toUpperCase();
            String valueright = new String(new char[]{result.charAt(12),result.charAt(13)}).toUpperCase();
            if("00".equals(valueleft) && "00".equals(valueright)){
                return 0;//关闭状态
            }else{
                return 1;//打开状态
            }
        }
    }

    /**
     * 发：00 80 03 06 00 3F C2
     * 收：00 81 03 06 00 00 AF ED
     * 获取设备报警编码
     * @param deskNo
     * @return
     */
    public int getDeviceAlarmCode(int deskNo){
        if (BuildConfig.IS_AUTO_TEST) {
            return 1;
        } else {
            String result = mIoProtocol.sendCmd(deskNo,"80","03","0600",defaultTime,8);
            if(result == null || "".equals(result) || result.length() != 16){
                return -1;
            }
            String value = new String(new char[]{result.charAt(10),result.charAt(11)}).toUpperCase();
            return DataTyeConvertUtil.hexToInt(value);//错误编码
        }
    }

    /**
     * 检测是否有瓶子投入
     *
     * @param readTime
     * @return
     */
    public int recvBottleIn(long readTime) {
        if (BuildConfig.IS_AUTO_TEST) {
            return 1;
        } else {
            String result = mIoProtocol.startRecv(readTime);
            if(result == null || "".equals(result) || result.length() < 18){
                return 0;
            }
            String value = new String(new char[]{result.charAt(12),result.charAt(13)}).toUpperCase();
            if("FF".equals(value)){
                return 1;//有瓶子投入
            }else{
                return 0;
            }
        }
    }

    public void closeDevice(){
        if(mIOReadSeriaPort != null){
            mIOReadSeriaPort.closeDevice();
            mIOReadSeriaPort = null;
            mIoProtocol = null;
        }
    }
}
