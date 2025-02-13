package com.lwb.framelibrary.net.download;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;

/**
 *文件下载
 */

public abstract class FileDownLoadObserver<T> {


    //可以重写，具体可由子类实现
    public void onComplete() {
    }

    //下载成功的回调
    public abstract int onDownLoadSuccess(T t);

    //下载失败回调
    public abstract void onDownLoadFail(Throwable throwable);

    //下载进度监听
    public abstract void onProgress(int progress, long total);

    public File saveFile(ResponseBody responseBody, String destFileDir, String destFileName) throws IOException {
        return saveFile(responseBody,destFileDir,destFileName,false);
    }
        /**
         * 将文件写入本地
         *
         * @param responseBody 请求结果全体
         * @param destFileDir  目标文件夹
         * @param destFileName 目标文件名
         * @return 写入完成的文件
         * @throws IOException IO异常
         */
    public File saveFile(ResponseBody responseBody, String destFileDir, String destFileName,boolean isNeedUnzip) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = responseBody.byteStream();
            final long total = responseBody.contentLength();
            long sum = 0;

            File dir = new File(destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                final long finalSum = sum;
                onProgress((int) (finalSum * 100 / total), total);
            }
            fos.flush();
//            if(isNeedUnzip)
//                FileTools.unZipFile(destFileDir+destFileName, destFileDir);
            return file;

        } finally {
            try {
                if (is != null) is.close();
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                is = null;
                fos = null;
                buf = null;
            }

        }
    }


}
