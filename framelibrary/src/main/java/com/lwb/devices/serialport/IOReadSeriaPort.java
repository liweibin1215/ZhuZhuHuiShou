package com.lwb.devices.serialport;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author lwb
 * @Time 2017年5月11日
 * @Description 通用串口读取
 */
public class IOReadSeriaPort extends SerialPort implements BaseIOInterface {
    public IOReadSeriaPort(File device, int baudrate, int flags, int databits, int stopbits, int parity)
            throws SecurityException, IOException {
        super(device, baudrate, flags,databits,stopbits,parity);
    }
    @Override
    public void sendData(byte[] data, int datalen) {
        try {
            getOutputStream().write(data, 0, datalen);
        } catch (IOException e) {
        }
    }
    @Override
    public int recvData(byte[] recvbuf, int offset) {
        try {
            if (getInputStream().available() > 0)
                return getInputStream().read(recvbuf, offset, recvbuf.length - offset);
        } catch (Exception e) {
        }
        return 0;
    }

    @Override
    public void clearBuffer() {
        byte buffer[] = new byte[2048];
        try {
            while (getInputStream().available() > 0)
                getInputStream().read(buffer);
        } catch (IOException e) {
        }
    }

    @Override
    public byte[] recvData() {
        try {
            int size = getInputStream().available();
            if ( size > 0){
                byte buffer[] = new byte[size];
                int len = getInputStream().read(buffer, 0, size);
                return len>0?buffer:null;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
