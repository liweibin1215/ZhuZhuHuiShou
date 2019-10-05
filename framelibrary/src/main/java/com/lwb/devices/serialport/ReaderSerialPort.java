package com.lwb.devices.serialport;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author lwb
 * @Time 2017年8月28日
 * @Description
 */
public class ReaderSerialPort implements DataTransInterface {
    Context mContext;
    public OutputStream mOutputStream;
    public InputStream mInputStream;
    public ReaderSerialPort(Context mContext, OutputStream mOutputStream, InputStream mInputStream )
    {
        this.mInputStream=mInputStream;
        this.mOutputStream=mOutputStream;
        this.mContext=mContext;
    }
    @Override
    public void sendData(byte[] data, int datalen) {
        try {
            mOutputStream.write(data, 0, datalen);
        } catch (IOException e) {
        }
    }
    @Override
    public int recvData(byte[] recvbuf, int offset) {
        try{
            if(mInputStream.available()>0)
                return mInputStream.read(recvbuf, offset, recvbuf.length-offset);
        }
        catch (Exception e) {
        }
        return 0;
    }

    @Override
    public void clear() {
        byte buffer[]=new byte[4096];
        try {
            while(mInputStream.available()>0)
                mInputStream.read(buffer);
        } catch (IOException e) {
        }
    }
    @Override
    public void close(){
        try {
            if(mOutputStream != null)
                mOutputStream.close();
            if(mInputStream != null)
                mInputStream.close();
        } catch (IOException e) {
        }
    }
}
