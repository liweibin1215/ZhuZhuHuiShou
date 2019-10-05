package com.lwb.devices.serialport;
/**
 *
 * @author lwb
 * @Time 2017年5月11日
 * @Description 自定义串口读取接口基类
 */
public interface BaseIOInterface{
    /**
     * 发送数据接口
     * @Title sendData
     * @param data 待发送的数据
     * @param datalen 数据长度
     */
    void sendData(byte[] data, int datalen);
    /**
     * 接收数据接口
     * @Title recvData
     * @param recvbuf 接收buf
     * @param offset buf内的偏移量
     * @return 接收到的长度
     */
    int recvData(byte recvbuf[], int offset);
    /**
     * 清空接收缓存
     * @Title clear
     */
    void clearBuffer();

    /**
     *
     * @return
     */
    byte[] recvData();
}