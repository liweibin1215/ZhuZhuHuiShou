package com.rys.smartrecycler.db.controller;

import com.rys.smartrecycler.db.DbHelper;
import com.rys.smartrecycler.db.greendao.FinishLogBeanDao;
import com.rys.smartrecycler.db.greendao.LogBeanDao;
import com.rys.smartrecycler.db.retbean.FinishLogBean;

import java.util.List;

/**
 * 创建时间：2019/6/18
 * 作者：李伟斌
 * 功能描述:
 */
public class FinishLogController {
    public static FinishLogBeanDao getFinishLogBeanDao(){
        return DbHelper.getInstance().getDaoSession().getFinishLogBeanDao();
    }

    /**
     * 创建操作日志
     * @param logName
     * @param logDesc
     */
    public static void insrtOperatorLog(String logName, String logDesc){
        insrtLog(0,logName,logDesc);
    }

    /**
     * 创建操作日志
     * @param logName
     * @param logDesc
     */
    public static void insrtAlarmLog(String logName, String logDesc){
        insrtLog(1,logName,logDesc);
    }

    /**
     * 创建操作日志
     * @param logName
     * @param logDesc
     */
    public static void insrtExecptionLog(String logName, String logDesc){
        insrtLog(2,logName,logDesc);
    }

    /**
     * 创建日志
     * @param logType
     * @param logName
     * @param logDesc
     */
    public static void insrtLog(int logType, String logName, String logDesc){
        getFinishLogBeanDao().insert(new FinishLogBean(logType,logName,logDesc,String.valueOf(System.currentTimeMillis()/1000),0));
    }
    /**
     * 插入新日志
     * @param vo
     */
    public static void inserLog(FinishLogBean vo){
        getFinishLogBeanDao().insert(vo);
    }

    /**
     * 获取所有日志记录
     * @return
     */
    public static List<FinishLogBean> getAllLogBean(){
        return getFinishLogBeanDao().loadAll();
    }

    /**
     * 获取所有待上传的订单，限制一次最多十条
     * @return
     */
    public static List<FinishLogBean> getAllLogBeanNeedUpdate(){
        return getFinishLogBeanDao().queryBuilder().where(LogBeanDao.Properties.Upflag.eq(0)).limit(10).list();
    }

    /**
     * 修改日志信息
     * @param vos
     */
    public static void updateLogInfo(List<FinishLogBean> vos){
        getFinishLogBeanDao().updateInTx(vos);
    }

    /**
     * 删除日志记录
     * @param vos
     */
    public static void deleteLogBean(List<FinishLogBean> vos){
        getFinishLogBeanDao().deleteInTx(vos);
    }

    /**
     * 分页查找
     * @param page
     * @param size
     * @return
     */
    public static List<FinishLogBean> getLogsByPage(int page,int size){
        return getFinishLogBeanDao().queryBuilder().orderDesc(FinishLogBeanDao.Properties.Id).offset((page-1)*size).limit(size).list();
    }
}
