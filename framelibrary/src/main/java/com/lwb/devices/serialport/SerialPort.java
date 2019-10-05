package com.lwb.devices.serialport;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * @author lwb
 *
 * 修改：
 * 20180608：添加设备节点判断，如果设备接点不存在或者没有读写权限，则抛出异常
 */
public class SerialPort {
	private FileDescriptor mFd;
	private FileInputStream mFileInputStream;
	private FileOutputStream mFileOutputStream;
	
	public SerialPort(File device, int baudrate, int flags, int databits, int stopbits, int parity)
			throws SecurityException, IOException {
        if(!device.exists() || !device.canRead() || !device.canRead()){//如果不存在或者没有权限
            throw new IOException();
        }
		mFd = open(device.getAbsolutePath(), baudrate, flags,databits,stopbits,parity);
		if (mFd == null) {
			throw new IOException();
		}
		mFileInputStream = new FileInputStream(mFd);
		mFileOutputStream = new FileOutputStream(mFd);
	}
	public InputStream getInputStream() {
		return mFileInputStream;
	}
	public OutputStream getOutputStream() {
		return mFileOutputStream;
	}
	public FileInputStream getFileInputStream() {
		return mFileInputStream;
	}
	public FileOutputStream getFileOutputStream() {
		return mFileOutputStream;
	}
	public void closeDevice(){
        try {
            if(mFileOutputStream != null)
                mFileOutputStream.close();
            if(mFileInputStream != null)
                mFileInputStream.close();
        } catch (IOException e) {
        }
		close();
	}
	private native static FileDescriptor open(String path, int baudrate,
                                              int flags, int databits, int stopbits, int parity);
	public native void close();
	static {
		System.loadLibrary("serial_port");
	}
}
