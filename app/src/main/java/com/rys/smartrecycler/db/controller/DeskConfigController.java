package com.rys.smartrecycler.db.controller;

import com.rys.smartrecycler.db.DbHelper;
import com.rys.smartrecycler.db.greendao.DeskConfigBeanDao;
import com.rys.smartrecycler.db.retbean.DeskConfigBean;

import java.util.List;

/**
 * 创建时间：2019/5/22
 * 作者：李伟斌
 * 功能描述:
 */
public class DeskConfigController {
    /**
     *
     * @return 通用设置实例
     */
    public static DeskConfigBeanDao getDeskConfigBeanDao(){
        return DbHelper.getInstance().getDaoSession().getDeskConfigBeanDao();
    }

    /**
     * 获取所有配置
     * @return
     */
    public static List<DeskConfigBean> getAllDesks(){
        return getDeskConfigBeanDao().queryBuilder().list();
    }

    /**
     *
     * @return
     */
    public static List<DeskConfigBean> getAllNeedUnLockDesks(){
        return getDeskConfigBeanDao().queryBuilder().where(DeskConfigBeanDao.Properties.Upflag.eq(10)).list();
    }
    /**
     * 插入副柜
     * @param vos
     */
    public static void insertDesk(List<DeskConfigBean> vos){
        if(vos == null || vos.size() < 0)return;
        getDeskConfigBeanDao().deleteAll();
        getDeskConfigBeanDao().insertInTx(vos);
    }

    /**
     * 修改副柜信息
     * @param vo
     */
    public static void updateDesk(DeskConfigBean vo){
        getDeskConfigBeanDao().insertOrReplace(vo);
    }

    /**
     * 批量修改副柜信息
     * @param vos
     */
    public static void updateDeskInfos(List<DeskConfigBean> vos){
        getDeskConfigBeanDao().updateInTx(vos);
    }

    /**
     * 根据副柜号获取副柜内容
     * @param deskNo
     * @return
     */
    public static DeskConfigBean getDeskByDeskNo(int deskNo){
        return getDeskConfigBeanDao().queryBuilder().where(DeskConfigBeanDao.Properties.DeskNo.eq(deskNo)).unique();
    }

    public static long getCuurengDeskNum(){
        return getDeskConfigBeanDao().queryBuilder().buildCount().count();
    }

    /**
     * 获取所有需要上传更新的副柜状态
     * @return
     */
    public static List<DeskConfigBean> getAllNeedUploadDeskInfo(){
        return getDeskConfigBeanDao().queryBuilder().where(DeskConfigBeanDao.Properties.Upflag.eq(0)).list();
    }

}
