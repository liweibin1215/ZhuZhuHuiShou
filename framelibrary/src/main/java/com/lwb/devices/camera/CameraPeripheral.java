package com.lwb.devices.camera;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.os.AsyncTask;

import com.lwb.devices.camera.util.Constant;
import com.lwb.devices.camera.util.OnCameraOpenListener;
import com.lwb.devices.camera.util.OnGetPictureListener;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * 自定义摄像设备
 *
 * @author lwb
 * @Time 2017年9月8日
 * @Description 检验设备是否存在、开启、获取设备流、获取图像资源等等
 * <p>
 * 修改：
 * 20180608 修改摄像头打开方式，不再限定某一个摄像头，先检测摄像头是否存在，然后判断是否有读写权限，然后在赋予权限开启
 */
public class CameraPeripheral {
    private Bitmap bmp = null;
    private Bitmap targetBitmap = null;
    private boolean isPause = false;// 标记暂停刷新视图
    private boolean isPreviewImg = false;// 标记停止刷新视图
    private boolean isDeviceOpen = false;
    private boolean isneedClose = false;
    private OnCameraOpenListener onCameraOpenListener;
    private Thread cmareaThread;

    public CameraPeripheral() {
    }

    static {
        System.loadLibrary("camera_dev");
    }

    public native int prepareCamera(int videoid);

    public native int prepareCameraWithBase(int videoid, int camerabase);

    public native int processCamera(int currentTimes);

    public native void stopCamera();

    public native void pixeltobmp(Bitmap bitmap);

    public native int checkDeviceIsExist(int videoid);

    public boolean resetVedioRoot(int num) {
        Process su = null;
        DataOutputStream os = null;
        try {
            su = Runtime.getRuntime().exec("/system/xbin/su");
            os = new DataOutputStream(su.getOutputStream());
            String cmd = "chmod 666 /dev/video" + num + "\n" + "exit\n";
            os.write(cmd.getBytes());
            if (su.waitFor() == 0) {
                return true;
            }
        } catch (Exception e) {
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (su != null)
                    su.destroy();
            } catch (Exception e) {
            }
        }
        return false;
    }

    /**
     * 判断可用USB设备
     *
     * @return
     */
    public int startOpenCamera() {
        for (int i = 0; i < 4; i++) {
            File device = new File("/dev/video" + i);
            if (device.exists()) {
                if (device.canRead() || device.canWrite()) {
                    if (prepareCameraWithBase(i, 0) == -1) {
                        continue;
                    } else {
                        return 0;
                    }
                }
                if (resetVedioRoot(i)) {
                    if (prepareCameraWithBase(i, 0) == -1) {
                        continue;
                    } else {
                        return 0;
                    }
                } else {
                    return -1;
                }
            }
        }
        return -1;
    }

    /**
     * 只开启摄像头，不刷新绘图
     *
     * @return 返回0表示打开成功，-1为打开失败
     */
    public int openCamera(OnCameraOpenListener listener) {
        this.onCameraOpenListener = listener;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (bmp == null) {
                    bmp = Bitmap.createBitmap(640, 480, Bitmap.Config.ARGB_8888);
                }
                int cameraNum = startOpenCamera();
                if (cameraNum < 0) {
                    if (onCameraOpenListener != null)
                        onCameraOpenListener.onCameraOpCallback(false);
                    isDeviceOpen = false;
                    isneedClose = false;
                    return;// 未找到摄像头
                }
                isDeviceOpen = true;
                isneedClose = true;
                if (onCameraOpenListener != null)
                    onCameraOpenListener.onCameraOpCallback(true);
            }
        }).start();

        return 0;
    }

    /**
     * 关闭摄像头
     *
     * @return 0 成功
     */
    public int closeCameraDevice() {
        if (!isPause || isPreviewImg) {// 判断线程是否存在，关闭线程
            isPause = true;
            isPreviewImg = false;
        }
        if (bmp != null)
            bmp.recycle();
        bmp = null;
        if (targetBitmap != null)
            targetBitmap.recycle();
        onCameraOpenListener = null;
        targetBitmap = null;
        if (isneedClose)
            stopCamera();
        isneedClose = false;
        return 0;
    }

    /**
     * 保存图片
     *
     * @return
     */
    public boolean saveCurrentPhoto() {
        boolean result;
        try {
            String fileName = "last_login_camera";
            File f = new File(Constant.SYSTEM_SOURSE_PATH_PICTURE + "/", fileName + ".jpg");
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            if (targetBitmap != null) {
                targetBitmap.compress(CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                result = true;
            } else {
                result = false;
            }
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    /**
     * 开启线程刷新视图 备注：此方法一般只提供需要实时显示绘图时调用
     *
     * @return -1 开启失败、0开启成功
     */
    public void startRefreshView() {
        if (isDeviceOpen && !isPreviewImg) {
            isPreviewImg = true;
            cmareaThread = new Thread(new ReadImage());
            cmareaThread.start();
        }
    }

    /**
     * 暂停刷新
     */
    public void stopRefreshView() {
        if (cmareaThread != null) {
            isPreviewImg = false;
            cmareaThread.interrupt();
            cmareaThread = null;
        }
    }

    /**
     * 获取视图
     *
     * @return Bitmap
     */
    public Bitmap getBitmap() {
        return targetBitmap;
    }

    /**
     * 是否已经暂停
     *
     * @return
     */
    public boolean isPased() {
        return isPause;
    }

    /**
     * 是否开启摄像头
     *
     * @return
     */
    public boolean isCameraOpen() {
        return isDeviceOpen;
    }

    class ReadImage implements Runnable {
        @Override
        public void run() {
            try {
                int w = 0,h = 0,flag = 0;
                Matrix matrix = new Matrix();
                matrix.postScale(-1, 1); // 镜像水平翻转
                while (isPreviewImg) {
                    if(processCamera(1) >= 0){
                        flag = 0;
                        pixeltobmp(bmp);
                        w = bmp.getWidth();
                        h = bmp.getHeight();
                        targetBitmap = Bitmap.createBitmap(bmp, 0, 0, w, h, matrix, true);
                        try {
                            new Thread().sleep(20);
                        } catch (InterruptedException e) {
                        }
                    }else{
                        try {
                            flag ++;
                            if(flag >= 5){
                                isPreviewImg = false;
                                isDeviceOpen = false;
                                break;
                            }
                            new Thread().sleep(100);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            } catch (Exception e) {
                isPreviewImg = false;
            }
            isPreviewImg = false;// 用来标记线程结束
        }
    }


    /**
     * 开始拍照
     *
     * @param mListener
     */
    public void startTakePicture(String fileName,
                                 OnGetPictureListener mListener) {
        new savePicTask(fileName,mListener).execute();
    }

    class savePicTask extends AsyncTask<String, Void, String> {
        private OnGetPictureListener mListener;
        private String mfileName = "";
        public savePicTask(String fileName, OnGetPictureListener mListener) {
            this.mListener = mListener;
            this.mfileName = fileName;
        }
        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... params) {
            try {
                if(isDeviceOpen){
                    if(refreshCamera() != 0){
                        return "false";
                    }
                    File f = new File(Constant.SYSTEM_SOURSE_PATH_PICTURE + "/", mfileName);
                    if (f.exists()) {
                        f.delete();
                    }
                    FileOutputStream out = new FileOutputStream(f);
                    targetBitmap.compress(CompressFormat.JPEG, 80, out);
                    out.flush();
                    out.close();
                }else{
                    return "false";
                }

            } catch (Exception e) {
                return "false";
            }
            return "true";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if ("true".equals(result) && mfileName != null) {
                mListener.bitmapCreated(mfileName);
            } else if(mfileName != null){
                mListener.bitmapCreated("false");
            }
        }

        public int refreshCamera() throws Exception {
            int flag = 0;
            for (int i = 0; i < 5; i++) {
                flag += processCamera(i);
            }
            if(flag <= -5){
                isDeviceOpen = false;
                return -1;
            }
            if(bmp  != null){
                bmp.recycle();
                bmp = null;
            }
            bmp = Bitmap.createBitmap(640, 480, Bitmap.Config.ARGB_8888);
            pixeltobmp(bmp);
            int w = bmp.getWidth();
            int h = bmp.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(-1, 1); // 镜像水平翻转
            targetBitmap = Bitmap.createBitmap(bmp, 0, 0, w, h, matrix, true);
            return 0;
        }
    }


}
