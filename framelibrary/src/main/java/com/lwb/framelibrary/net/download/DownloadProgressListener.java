package com.lwb.framelibrary.net.download;

/**
 * 创建日期：2018/4/4 on 14:51
 * 作者 by Mr.Dong
 * 描述 :自定义进度条接口（成功回调处理）
 */

public interface DownloadProgressListener {
    /**
     * 下载进度
     * @param read
     * @param count
     * @param done
     */
    void update(long read, long count, boolean done);
}
