package com.lwb.devices.camera.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.lwb.devices.camera.CameraPeripheral;


/**
 *
 * @author lwb
 * @Time 2017年9月8日
 * @Description
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private CameraPeripheral cameraDev;
    private boolean visible = false;
    private Thread picThread = null;
    private Rect rect = new Rect(0, 0, 600, 600);
    private boolean mDrawFlag = true;

    public CameraView(Context context) {
        super(context);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mDrawFlag = true;
        visible = true;
        picThread = new Thread(this);
        picThread.start();
    }

    public void setCameraPeripheral(CameraPeripheral cameraDev) {
        this.cameraDev = cameraDev;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @SuppressLint("NewApi")
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        visible = false;
        if (holder != null) {
            try {
                holder.getSurface().release();
            } catch (Exception e) {
            }
        }
        if(picThread != null )
            picThread.interrupt();
        //cameraDev.closeCameraDevice();//自动关闭
        synchronized (this) {
            mDrawFlag = false;
        }
        picThread = null;
    }

    @Override
    public void run() {
        while (visible) {
            if (cameraDev != null && cameraDev.isCameraOpen()) {
                synchronized (this) {
                    if (mDrawFlag) {
                        Bitmap bitmap = cameraDev.getBitmap();
                        Canvas canvas = getHolder().lockCanvas();
                        if (canvas != null) {
                            try {
                                if (bitmap != null) {
                                    canvas.drawBitmap(bitmap, null, rect, null);
                                }
                            } catch (Exception e) {
                            } finally {
                                try {
                                    getHolder().unlockCanvasAndPost(canvas);
                                } catch (Exception e) {
                                }
                            }
                        }
                        try {
                            new Thread().sleep(50);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }else{
                try {
                    new Thread().sleep(100);
                } catch (InterruptedException e) {
                }
            }
        }
    }

}
