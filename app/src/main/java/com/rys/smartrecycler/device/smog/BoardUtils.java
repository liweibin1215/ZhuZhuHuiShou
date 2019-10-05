package com.rys.smartrecycler.device.smog;

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
    public static final byte HEAD[] = { DataTyeConvertUtil.HexToByte("01"), DataTyeConvertUtil.HexToByte("03")};// 帧头

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
     * @param adress1 板地址
     * @return
     */
    public static String packageCmd(int adress1,String cmdCode) {
        String hexAdress1 = null;
        if (adress1 <= 15) {
            hexAdress1 = "0" + Integer.toHexString(adress1);
        } else {
            hexAdress1 = Integer.toHexString(adress1);
        }
        byte[] cmd = new byte[]{DataTyeConvertUtil.HexToByte(hexAdress1), DataTyeConvertUtil.HexToByte(cmdCode),(byte)0x00, (byte)0x03, (byte)0x00,(byte)0x01};
        int intCmd = CRC16.calcCrc16L(cmd);
        String crcstr = Integer.toHexString(intCmd);
        while (crcstr.length() < 4) {
            crcstr = "0" + crcstr;
        }
        String cmdString =hexAdress1 + cmdCode+"00030001" + crcstr;
        return cmdString;
    }

    /**
     * 获取命令
     * @param adress1
     * @return
     */
    public static byte[] getCmd(int adress1,String cmdCode){
        return DataTyeConvertUtil.HexToByteArr(packageCmd(adress1,cmdCode));
    }


    public static void main(String[]  args){
        System.out.println(packageCmd(1,"03"));
    }
}
