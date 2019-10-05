package com.lwb.devices.serialport;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;

/**
 * 通用串口接收服务
 * @author lwb
 * @Time 2017年9月18日
 * @Description
 */
public abstract class BaseSerialPortService extends Service {
    protected IOReadSeriaPort mIOReadSeriaPort;
    protected abstract IOReadSeriaPort openSerialPort();
    protected abstract void onDataReceived(final String backData, final int size);
    private ReadThread mReadThread;
    private int readMode;//接收数据模式：0扫描器接收 1身份证识别器接收 2其他
    private byte[] buffer = new byte[512];
    private int currentLen = 0;
    public static int SCANNER_TIMER = 0;
    private boolean mRunning = false;
    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            if(readMode == 0){
                ScannerRead();
            }
        }
        private void ScannerRead() {
            while (mRunning && !isInterrupted()) {
                try {
                    if(mIOReadSeriaPort == null) break;
                    if (SCANNER_TIMER > 0)
                        SCANNER_TIMER++;
                    byte[] readBuffer = mIOReadSeriaPort.recvData();//接收数据
                    if (readBuffer != null) {
                        int len = readBuffer.length;
                        if((currentLen+len) < 512){
                            if (buffer == null)
                                buffer = new byte[512];
                            SCANNER_TIMER++;
                            System.arraycopy(readBuffer, 0, buffer, currentLen, len);
                            currentLen = currentLen + len;
                        }
                    }
                    if (SCANNER_TIMER > 3 && currentLen > 0) {
                        SCANNER_TIMER = 0;
                        byte[] resultBuffer = new byte[currentLen];
                        System.arraycopy(buffer, 0, resultBuffer, 0, currentLen);
                        String resut = new String(resultBuffer);
                        buffer = null;
                        currentLen = 0;
                        onDataReceived(resut.replace("\r", ""), resut.length());
                    }
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    mRunning = false;
                } catch (Exception e) {
                    if (mRunning) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e1) {
                            mRunning = false;
                        }
                    }
                }
            }
        }
    }

    public void onCreate(int deviceName) {
        super.onCreate();
        readMode = deviceName;
        new Thread(new Runnable() {
            @Override
            public void run() {
                startOpenSerialPort();
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseResource();
    }

    @SuppressLint("NewApi")
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    private void startOpenSerialPort() {
        mIOReadSeriaPort = openSerialPort();
        if (mIOReadSeriaPort != null) {
            mRunning = true;
            mReadThread = new ReadThread();
            mReadThread.start();
        } else {
            stopSelf();
        }
    }

    private void releaseResource() {
        mRunning = false;
        if (mReadThread != null)
            mReadThread.interrupt();
        if (mIOReadSeriaPort != null) {
            mIOReadSeriaPort.closeDevice();
            mIOReadSeriaPort = null;
        }
    }
}
