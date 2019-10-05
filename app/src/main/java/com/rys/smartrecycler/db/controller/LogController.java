package com.rys.smartrecycler.db.controller;

import com.rys.smartrecycler.db.DbHelper;
import com.rys.smartrecycler.db.greendao.LogBeanDao;
import com.rys.smartrecycler.db.retbean.LogBean;

import java.util.List;

/**
 * 创建时间：2019/5/22
 * 作者：李伟斌
 * 功能描述:
 */
public class LogController {
    /**
     *
     * @return 通用设置实例
     */
    public static LogBeanDao getLogBeanDao(){
        return DbHelper.getInstance().getDaoSession().getLogBeanDao();
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
        getLogBeanDao().insert(new LogBean(logType,logName,logDesc,String.valueOf(System.currentTimeMillis()/1000),0));
    }

    /**
     * 插入新日志
     * @param vo
     */
    public static void inserLog(LogBean vo){
        getLogBeanDao().insert(vo);
    }

    /**
     * 获取所有日志记录
     * @return
     */
    public static List<LogBean> getAllLogBean(){
        return getLogBeanDao().loadAll();
    }

    /**
     *  释放前两百个日志记录
     * @return
     */
    public static List<LogBean> getAllNeedUnLockLogBean(){
        return getLogBeanDao().queryBuilder().where(LogBeanDao.Properties.Upflag.eq(10)).limit(200).list();
    }
    /**
     * 修改日志信息
     * @param vos
     */
    public static void updateLogInfo(List<LogBean> vos){
        getLogBeanDao().updateInTx(vos);
    }

    /**
     * 获取所有待上传的订单，限制一次最多十条
     * @return
     */
    public static List<LogBean> getAllLogBeanNeedUpdate(){
        return getLogBeanDao().queryBuilder().where(LogBeanDao.Properties.Upflag.eq(0)).limit(10).list();
    }

    /**
     *
     * @return
     */
    public static List<LogBean> getNeedUpload(){
        return getLogBeanDao().queryBuilder().where(LogBeanDao.Properties.Upflag.eq(0)).limit(10).list();
    }
    /**
     * 删除日志记录
     * @param vos
     */
    public static void deleteLogBean(List<LogBean> vos){
        getLogBeanDao().deleteInTx(vos);
    }
    /**
     * 删除日志记录
     * @param vo
     */
    public static void deleteLogBean(LogBean vo){
        getLogBeanDao().delete(vo);
    }
    /**
     * 分页查找
     * @param page
     * @param size
     * @return
     */
    public static List<LogBean> getLogsByPage(int page, int size){
        return getLogBeanDao().queryBuilder().orderDesc(LogBeanDao.Properties.Id).offset((page-1)*size).limit(size).list();
    }

    /**
     * 批量修改信息
     * @param vos
     */
    public static void updateLogInfos(List<LogBean> vos){
        getLogBeanDao().updateInTx(vos);
    }
}
