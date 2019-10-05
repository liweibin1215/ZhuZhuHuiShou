package com.rys.smartrecycler.db.controller;

import com.rys.smartrecycler.db.DbHelper;
import com.rys.smartrecycler.db.greendao.DeviceInfoDao;
import com.rys.smartrecycler.db.retbean.DeviceInfo;

/**
 * 创建时间：2019/5/28
 * 作者：李伟斌
 * 功能描述:
 */
public class DeviceInfoController {
    /**
     *
     * @return 通用设置实例
     */
    public static DeviceInfoDao getDeviceInfoDao(){
        return DbHelper.getInstance().getDaoSession().getDeviceInfoDao();
    }

    /**
     * 插入设备信息
     * @param
     */
    public static void insertDesk(DeviceInfo vo){
        getDeviceInfoDao().deleteAll();
        getDeviceInfoDao().insertInTx(vo);
    }

    /**
     * 获取唯一
     * @return
     */
    public static DeviceInfo getDeviceInfo(){
        return getDeviceInfoDao().queryBuilder().unique();
    }

    /**
     * 获取唯一
     * @return
     */
    public static DeviceInfo getDeviceInfoByDeviceSn(String deviceSn){
        return getDeviceInfoDao().queryBuilder().where(DeviceInfoDao.Properties.DeviceSn.eq(deviceSn)).unique();
    }

    /**
     * 修改设备信息
     * @param vo
     */
    public static void updateDeviceInfo(DeviceInfo vo){
        getDeviceInfoDao().updateInTx(vo);
    }
}
