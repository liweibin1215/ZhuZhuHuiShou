package com.rys.smartrecycler.db.controller;

import com.rys.smartrecycler.db.DbHelper;
import com.rys.smartrecycler.db.greendao.AdvInfoDao;
import com.rys.smartrecycler.db.retbean.AdvInfo;

import java.util.List;

/**
 * 创建时间：2019/6/24
 * 作者：李伟斌
 * 功能描述:
 */
public class AdvController {
    public static AdvInfoDao getAdvInfoDao(){
        return DbHelper.getInstance().getDaoSession().getAdvInfoDao();
    }

    public static List<AdvInfo> getAllAdvInfos(int type){
        return getAdvInfoDao().queryBuilder().where(AdvInfoDao.Properties.Type.eq(type)).list();
    }

    public static void updateAdvInfos(AdvInfo vo){
        getAdvInfoDao().update(vo);
    }
    public static void updateAdvInfos(List<AdvInfo> vos){
        getAdvInfoDao().updateInTx(vos);
    }
    public static void insertAdvInfosWithUniquea(AdvInfo vo){
        getAdvInfoDao().insert(vo);
    }

    public static void deleteAll(List<AdvInfo> vos){
        if(vos == null)return;
        for (AdvInfo vo:vos){
            getAdvInfoDao().delete(vo);
        }
    }

    public static List<AdvInfo> getAllNeedDownloadInfos(){
        return getAdvInfoDao().queryBuilder().where(AdvInfoDao.Properties.Upflag.eq(0)).list();
    }

    public static List<AdvInfo> getNeedUnlockInfos(){
        return getAdvInfoDao().queryBuilder().where(AdvInfoDao.Properties.Upflag.eq(10)).limit(10).list();
    }
}
