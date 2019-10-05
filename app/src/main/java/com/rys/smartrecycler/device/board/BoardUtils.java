package com.rys.smartrecycler.device.board;

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

        if (adress2 <= 15) {
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


    public static String checkCRC(String replyDatas) {
        if (replyDatas != null && replyDatas.length() >= 18 && replyDatas.startsWith("190420")) {
            int len = Integer.parseInt(replyDatas.substring(6, 8), 16);
            int totalLen = (len + 6) * 2;
            if (totalLen > replyDatas.length()) {
                return "";//数据不全
            } else if (totalLen < replyDatas.length()) {
                replyDatas = replyDatas.substring(0, totalLen);//目标数据
            }
            String data = replyDatas.substring(0, totalLen - 4);
            String CRC = replyDatas.substring(totalLen - 4, totalLen);
            String calCRC = Integer.toHexString(CRC16.calcCrc16(DataTyeConvertUtil.HexToByteArr(data)));
            while (calCRC.length() < 4) {
                calCRC = "0" + calCRC;
            }
            if (CRC.toUpperCase().equals(calCRC.toUpperCase())) {
                return replyDatas;
            } else {
                return "";
            }
        } else {
            return "";
        }
    }
    public static String checkCRCs(String replyDatas) {
        if (replyDatas != null && replyDatas.length() >= 14) {
            int totalLen = replyDatas.length();
            String data = replyDatas.substring(0, totalLen - 4);
            String CRC = replyDatas.substring(totalLen - 4, totalLen);
            String calCRC = Integer.toHexString(CRC16.calcCrc16(DataTyeConvertUtil.HexToByteArr(data)));
            while (calCRC.length() < 4) {
                calCRC = "0" + calCRC;
            }
            if (CRC.toUpperCase().equals(calCRC.toUpperCase())) {
                return replyDatas;
            } else {
                return "";
            }
        } else {
            return "";
        }
    }
    /**
     * 判定接收的数据是否满足CRC校验
     * @param replyDatas
     * @return
     */
    public static String checkCRCIsOk(String replyDatas) {
        if (replyDatas.startsWith("042003")) {
            return checkCRC("19"+replyDatas);
        } else if (replyDatas.startsWith("2003")) {
            return checkCRC("1904"+replyDatas);
        } else if (replyDatas.startsWith("03")) {
            return checkCRC("190420"+replyDatas);
        }else{
            return checkCRC(replyDatas);
        }
    }
    //#################################################################################################################
    public static byte[] getCmd(int deskNo,String number,String cmdType,String cmdCode){
        return DataTyeConvertUtil.HexToByteArr(packCmd(deskNo,number,cmdType,cmdCode));
    }

    public static String packCmd(int deskNo,String number,String cmdType,String cmdCode){
        String deskHex;
        if (deskNo <= 15) {
            deskHex = "0" + Integer.toHexString(deskNo);
        } else {
            deskHex = Integer.toHexString(deskNo);
        }
        String cmdHead = deskHex+number+cmdType+cmdCode;
        int intCmd = CRC16.calcCrc16(DataTyeConvertUtil.HexToByteArr(cmdHead));
        String crcstr = Integer.toHexString(intCmd);
        while (crcstr.length() < 4) {
            crcstr = "0" + crcstr;
        }
        return cmdHead+crcstr;
    }






    public static void main(String[]  args){
        System.out.println(packageCmd("01",0,0));
//        System.out.println(packageCmd("02",0,0));
//        System.out.println(packageCmd("03",0,0));
//        System.out.println(packageCmd("04",0,0));
//        System.out.println(packageCmd("05",0,0));
//        System.out.println(packageCmd("06",0,0));
//        System.out.println(packageCmd("07",0,0));
//        System.out.println(packageCmd("08",0,0));
//        System.out.println(packageCmd("09",0,0));
    }
}
