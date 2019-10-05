package com.rys.smartrecycler.device.balance;

import com.lwb.devices.serialport.BaseIOInterface;
import com.lwb.framelibrary.utils.DataTyeConvertUtil;

/**
 * 创作时间： 2019/5/8 on 上午10:38
 * <p>
 * 描述：
 * <p>
 * 作者：lwb
 */

public class IoProtocol {
    private long defaultTime = 1000;
    private BaseIOInterface ioInterface;
    public IoProtocol(BaseIOInterface ioInterface){
        this.ioInterface = ioInterface;
    }

    /**
     * 发送数据
     * @param data
     */
    public void sendCmd(byte[] data){
        ioInterface.clearBuffer();//清空缓存
        ioInterface.sendData(data, data.length);
    }


    /**
     * 主板通信接收基类
     * @param timeout
     * @return
     */
    public byte[] recv(long timeout,byte[] head,int dataLen) {
        byte findHeadBuf[] = new byte[30];
        byte recvBuf[] = new byte[30];
        int copyPos = 0;
        int recvLen = 0;
        int headIndex = -1;
        boolean findHead = false;
        long cur_time = System.currentTimeMillis();
        while (System.currentTimeMillis() - cur_time < timeout) {
            recvLen = ioInterface.recvData(findHeadBuf, copyPos);
            if (recvLen <= 0) {
                BoardUtils.mSleep(5);
                continue;
            }
            copyPos += recvLen;
            if (!findHead && copyPos > 2) {
                headIndex = BoardUtils.IndexOf(findHeadBuf, head);
                if (headIndex < 0) {
                    System.arraycopy(findHeadBuf, 1, findHeadBuf, 0, copyPos - 1);
                    copyPos--;
                } else {
                    findHead = true;
                    System.arraycopy(findHeadBuf, headIndex, recvBuf, 0, copyPos - headIndex);
                    copyPos = copyPos - headIndex;
                    findHeadBuf = recvBuf;
                    break;
                }
            }
        }
        if(copyPos == 0){
            return null;
        }else if (copyPos < 3 || !findHead) {
            if(copyPos > 0 && copyPos <= 30){
                byte[] results = new byte[copyPos];
                System.arraycopy(findHeadBuf, 0, results, 0, copyPos);
                return results;
            }else{
                return findHeadBuf;
            }
        }
        int leftLen = dataLen - copyPos;// 剩余长度
        while (System.currentTimeMillis() - cur_time < timeout && leftLen > 0) {
            recvLen = ioInterface.recvData(recvBuf, copyPos);
            if (recvLen <= 0) {
                BoardUtils.mSleep(5);
                continue;
            }
            copyPos += recvLen;
            leftLen -= recvLen;
        }
        if (leftLen != 0) {
            return recvBuf;// 接收超时
        }
        byte[] results = new byte[copyPos];
        System.arraycopy(recvBuf, 0, results, 0, copyPos);
        return results;
    }

    /**
     * 执行命令
     * @param cmd
     * @param readTime
     * @param dataLen
     * @return
     */
    public String sendCmd(byte[] cmd,long readTime,int dataLen){
        byte[] head = new byte[]{cmd[0],cmd[1]};
        sendCmd(cmd);
        if(readTime <= 0){
            return "";
        }
        byte[] recResult = recv(readTime,head,dataLen);
        if(recResult == null || recResult.length < 14){
            return "";
        }
        return DataTyeConvertUtil.byteArrToHex(recResult);
    }

    /**
     * 获取指定副柜的上的重量值
     * @param adress
     * @return
     */
    public String readWeigth(String adress){
        String result = sendCmd(BoardUtils.getWeightCmd(adress,"03"),defaultTime,7);
        if(result == null || !result.startsWith(adress+"03") || result.length() < 14){
            return "";
        }
        String weight = result.substring(6, 10);
        int value = DataTyeConvertUtil.hexToInt(weight);
        if (value > 30000) {
            return String.valueOf(value-65536);
        }
        return String.valueOf(value);
    }

    /**
     * 指定副柜上电子秤归零
     * @param adress
     * @return
     */
    public boolean writeZero(String adress){
        String cmd = BoardUtils.getZeroCmd(adress,"06").toUpperCase();
        String result = sendCmd(DataTyeConvertUtil.HexToByteArr(cmd),defaultTime,8);
        if(!result.startsWith(adress+"06") || result.length() < 16 || !cmd.equals(result.toUpperCase())){
            return false;
        }
        return true;
    }

    /**
     * 最大量程设置
     * @param adress
     * @param maxValue
     * @return
     */
    public boolean writeMaxValue(String adress,int maxValue){
        String cmd = BoardUtils.getWriteMaxCmd(adress,"10",maxValue).toUpperCase();
        String result = sendCmd(DataTyeConvertUtil.HexToByteArr(cmd),defaultTime,8);
        if(!result.startsWith(adress+"10") || result.length() < 16){
            return false;
        }
        return true;
    }

    /**
     * 设置最小分度值
     * @param adress
     * @param minDevision
     * @return
     */
    public boolean writeMinDevision(String adress,int minDevision){
        String cmd = BoardUtils.getWriteDevisionCmd(adress,"06",minDevision).toUpperCase();
        String result = sendCmd(DataTyeConvertUtil.HexToByteArr(cmd),defaultTime,8);
        if(!result.startsWith(adress+"06") || result.length() < 16 || !cmd.equals(result.toUpperCase())){
            return false;
        }
        return true;
    }


    /**
     * 空载标定
     * @param adress
     * @return
     */
    public boolean writeLDW(String adress){
        String cmd = BoardUtils.getWriteLDWCmd(adress,"10").toUpperCase();
        String result = sendCmd(DataTyeConvertUtil.HexToByteArr(cmd),defaultTime,8);
        if(!result.startsWith(adress+"10") || result.length() < 16){
            return false;
        }
        return true;
    }

    /**
     * 定值标定
     * @param adress
     * @param value
     * @return
     */
    public boolean writeLWT(String adress,int value){
        String cmd = BoardUtils.getWriteLWTCmd(adress,"10",value);
        String result = sendCmd(DataTyeConvertUtil.HexToByteArr(cmd),defaultTime,8);
        if(!result.startsWith(adress+"10") || result.length() < 16){
            return false;
        }
        return true;
    }

    /**
     * 获取当前最大分度值（量程）
     * @param adress
     * @return
     */
    public String getMaxValue(String adress){
        String cmd = BoardUtils.getReadMaxValue(adress,"03");
        String result = sendCmd(DataTyeConvertUtil.HexToByteArr(cmd),defaultTime,9);
        if(result == null || !result.startsWith(adress+"03") || result.length() < 14){
            return "";
        }
        String weight = result.substring(6, 10);
        int value = DataTyeConvertUtil.hexToInt(weight);
        return String.valueOf(value);
    }

}
