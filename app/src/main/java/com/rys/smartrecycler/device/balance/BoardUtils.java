package com.rys.smartrecycler.device.balance;

import com.lwb.devices.serialport.CRC16;
import com.lwb.framelibrary.utils.DataTyeConvertUtil;

/**
 * 创作时间： 2019/5/8 on 上午10:48
 * <p>
 * 描述：
 * <p>
 * 作者：lwb
 */

public class BoardUtils {
    public static final byte HEAD[] = { DataTyeConvertUtil.HexToByte("19"), DataTyeConvertUtil.HexToByte("04"), DataTyeConvertUtil.HexToByte("20") };// 帧头

    /**
     * 通用线程睡眠
     * @param sleepTime
     */
    public static void mSleep(long sleepTime){
        try {
            Thread.sleep(sleepTime);
        }catch (InterruptedException e){

        }
    }
    /**
     * 判断帧头所在的位置
     *
     * @param
     * @param pattern
     * @return
     */
    public static int IndexOf(byte[] src, byte[] pattern) {
        try {
            int slen = src.length;
            int plen = pattern.length;
            boolean researchFlag = false;
            if(slen < plen)
                return -1;
            for (int i = 0; i <= slen - plen; i++) {
                researchFlag = true;
                for (int j = 0; j < plen; j++) {
                    if (src[i + j] != pattern[j]) {
                        researchFlag = false;
                        break;
                    }
                }
                if (researchFlag)
                    return i;
            }
        } catch (Exception e) {
        }
        return -1;
    }

    /**
     * 主板通信命令包装
     * @param cmdCode 命令
     * @param adress1 板地址
     * @param adress2 板地址
     * @return
     */
    public static String packageCmd(String cmdCode, int adress1, int adress2) {
        String hexAdress1 = null;
        String hexAdress2 = null;
        if (adress1 <= 15) {
            hexAdress1 = "0" + Integer.toHexString(adress1);
        } else {
            hexAdress1 = Integer.toHexString(adress1);
        }

        if (adress1 <= 15) {
            hexAdress2 = "0" + Integer.toHexString(adress2);
        } else {
            hexAdress2 = Integer.toHexString(adress2);
        }
        byte[] cmd = new byte[]{0x19, (byte) 0x04, (byte) 0x20, 0x03, DataTyeConvertUtil.HexToByte(cmdCode), DataTyeConvertUtil.HexToByte(hexAdress1), DataTyeConvertUtil.HexToByte(hexAdress2)};
        int intCmd = CRC16.calcCrc16(cmd);
        String crcstr = Integer.toHexString(intCmd);
        while (crcstr.length() < 4) {
            crcstr = "0" + crcstr;
        }
        String cmdString = "19042003" + cmdCode + hexAdress1 + hexAdress2 + crcstr;
        return cmdString;
    }

    /**
     * 获取命令
     * @param cmdCode
     * @param adress1
     * @param adress2
     * @return
     */
    public static byte[] getCmd(String cmdCode, int adress1, int adress2){
        return DataTyeConvertUtil.HexToByteArr(packageCmd(cmdCode,adress1,adress2));
    }


    /**
     * 获取读取电子秤重量命令
     * @param adress
     * @param cmdCode
     * @return
     */
    public static byte[] getWeightCmd(String adress,String cmdCode){
        byte[] cmd = new byte[]{DataTyeConvertUtil.HexToByte(adress), DataTyeConvertUtil.HexToByte(cmdCode), (byte)0x00,(byte)0x2A,(byte)0x00,(byte)0x01};
        int intCmd = CRC16.calcCrc16L(cmd);
        String crcstr = Integer.toHexString(intCmd);
        while (crcstr.length() < 4) {
            crcstr = "0" + crcstr;
        }
        String cmdString = adress + cmdCode + "002A0001" + crcstr;
        return DataTyeConvertUtil.HexToByteArr(cmdString);
    }

    /**
     * 获取电子秤模块归零命令
     * @param adress
     * @param cmdCode
     * @return
     */
    public static String getZeroCmd(String adress,String cmdCode){
        byte[] cmd = new byte[]{DataTyeConvertUtil.HexToByte(adress), DataTyeConvertUtil.HexToByte(cmdCode), (byte)0x00,(byte)0x24,(byte)0x00,(byte)0x00};
        int intCmd = CRC16.calcCrc16L(cmd);
        String crcstr = Integer.toHexString(intCmd);
        while (crcstr.length() < 4) {
            crcstr = "0" + crcstr;
        }
        String cmdString = adress + cmdCode + "00240000" + crcstr;
        return cmdString;
    }

    /**
     * 获取量程设置命令
     * @param adress
     * @param cmdCode
     * @param maxValue
     * @return
     */
    public static String getWriteMaxCmd(String adress,String cmdCode,int maxValue){
        String cmdCheck = adress+cmdCode+"0000000204"+get4ByteHex(maxValue);
        int intCmd = CRC16.calcCrc16L(DataTyeConvertUtil.HexToByteArr(cmdCheck));
        String crcstr = Integer.toHexString(intCmd);
        while (crcstr.length() < 4) {
            crcstr = "0" + crcstr;
        }
        return cmdCheck + crcstr;
    }


    /**
     * 获取分度设置命令
     * @param adress
     * @param cmdCode
     * @param value
     * @return
     */
    public static String getWriteDevisionCmd(String adress,String cmdCode,int value){
        String cmdCheck = adress+cmdCode+"0004"+DataTyeConvertUtil.int2Hex(value,4);
        int intCmd = CRC16.calcCrc16L(DataTyeConvertUtil.HexToByteArr(cmdCheck));
        String crcstr = Integer.toHexString(intCmd);
        while (crcstr.length() < 4) {
            crcstr = "0" + crcstr;
        }
        return cmdCheck + crcstr;
    }

    /**
     * 获取空载标定命令
     * @param adress
     * @param cmdCode
     * @return
     */
    public static String getWriteLDWCmd(String adress,String cmdCode){
        String cmdCheck = adress+cmdCode+"000800020400000000";
        int intCmd = CRC16.calcCrc16L(DataTyeConvertUtil.HexToByteArr(cmdCheck));
        String crcstr = Integer.toHexString(intCmd);
        while (crcstr.length() < 4) {
            crcstr = "0" + crcstr;
        }
        return cmdCheck + crcstr;
    }


    /**
     * 获取标定标定
     * @param adress
     * @param cmdCode
     * @param value
     * @return
     */
    public static String getWriteLWTCmd(String adress,String cmdCode,int value){
        String cmdCheck = adress+cmdCode+"0010000204"+get4ByteHex(value);
        int intCmd = CRC16.calcCrc16L(DataTyeConvertUtil.HexToByteArr(cmdCheck));
        String crcstr = Integer.toHexString(intCmd);
        while (crcstr.length() < 4) {
            crcstr = "0" + crcstr;
        }
        return cmdCheck + crcstr;
    }

    /**
     * 获取当前量程
     * @param adress
     * @param cmdCode
     * @return
     */
    public static String getReadMaxValue(String adress,String cmdCode){
        String cmdCheck = adress+cmdCode+"00000002";
        int intCmd = CRC16.calcCrc16L(DataTyeConvertUtil.HexToByteArr(cmdCheck));
        String crcstr = Integer.toHexString(intCmd);
        while (crcstr.length() < 4) {
            crcstr = "0" + crcstr;
        }
        return cmdCheck + crcstr;
    }


    public static String get4ByteHex(int value){
        String adress = DataTyeConvertUtil.int2Hex(value);
        if (adress.length() % 2 != 0) {
            adress = "0" + adress;
        }
        if (adress.length() <= 4) {
            while (adress.length() < 4) {
                adress = "0"+adress;
            }
            adress = adress+"0000";
        } else {
            String lData = adress.substring(adress.length() - 4, adress.length());
            String hData = adress.substring(0, adress.length() - 4);
            while (hData.length() < 8) {
                hData = "0" + hData;
            }
            adress = lData + hData;
        }
        return adress;
    }
    public static void main(String[] args){
        System.out.println(DataTyeConvertUtil.byteArrToHex(getWeightCmd("1F","03")));
        System.out.println(getZeroCmd("1F","06"));
        System.out.println(getWriteMaxCmd("1F","10",0));
        System.out.println(getWriteLDWCmd("1F","10"));
        System.out.println(getWriteLWTCmd("1F","10",5000));
        System.out.println(getReadMaxValue("1F","03"));
    }
}
