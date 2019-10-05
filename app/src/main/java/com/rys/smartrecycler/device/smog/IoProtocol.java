package com.rys.smartrecycler.device.smog;

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
    public byte[] recv(long timeout) {
        byte[] Head = BoardUtils.HEAD;
        int totalLength = 0;// 根据命令类型获取总接收长度
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
                headIndex = BoardUtils.IndexOf(findHeadBuf, Head);
                if (headIndex < 0) {
                    System.arraycopy(findHeadBuf, 1, findHeadBuf, 0, copyPos - 1);
                    copyPos--;
                } else {
                    findHead = true;
                    System.arraycopy(findHeadBuf, headIndex, recvBuf, 0, copyPos - headIndex);
                    copyPos = copyPos - headIndex;
                    findHeadBuf = recvBuf;
                }
            }
            if (findHead && headIndex >= 0 && copyPos > Head.length) {// 如果找到头部,则判断数据长度
                String lenData = DataTyeConvertUtil.byteToHex(findHeadBuf[Head.length]);
                int len = Integer.parseInt(lenData, 16);
                totalLength = len + 5;// (数据长度+2帧头长度+2校验位长度+1数据位长度)
                break;
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
        int leftLen = totalLength - copyPos;// 剩余长度
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
     * 执行命令，并接收相应，默认接收时间
     * @param deskNo
     * @return
     */
    public String sendCmd(int deskNo){
        return sendCmd(deskNo,defaultTime);
    }

    /**
     * 执行命令，并接收相应
     * @param deskNo
     * @param readTime
     * @return
     */
    public String sendCmd(int deskNo,long readTime){
        sendCmd(BoardUtils.getCmd(deskNo,"03"));
        if(readTime <= 0){
            return "";
        }
        byte[] recResult = recv(readTime);
        if(recResult == null || recResult.length <= 0){
            return "";
        }
        String result = DataTyeConvertUtil.byteArrToHex(recResult);
        String hexAdress1;
        if (deskNo <= 15) {
            hexAdress1 = "0" + Integer.toHexString(deskNo);
        } else {
            hexAdress1 = Integer.toHexString(deskNo);
        }
        if(result == null || "".equals(result) || result.length() < 14){
            return "";
        }
        if(!result.startsWith(hexAdress1+"03")){
            return "";
        }
        return result;
    }
}
