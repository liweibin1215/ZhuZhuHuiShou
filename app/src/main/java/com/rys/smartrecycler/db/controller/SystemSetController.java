package com.rys.smartrecycler.db.controller;

import com.rys.smartrecycler.db.DbHelper;
import com.rys.smartrecycler.db.greendao.SystemSetBeanDao;
import com.rys.smartrecycler.db.retbean.SystemSetBean;

/**
 * 创作时间： 2019/5/7 on 下午8:36
 * <p>
 * 描述：
 * <p>
 * 作者：lwb
 */

public class SystemSetController {
    /**
     *
     * @return 通用设置实例
     */
    public static SystemSetBeanDao getSystemSetBeanDao(){
        return DbHelper.getInstance().getDaoSession().getSystemSetBeanDao();
    }

    /**
     * 修改设置
     * @param keyName
     * @param keyVaule
     */
    public static void updateSystemSet(String keyName,String keyVaule){
        SystemSetBean vo = getSystemSetBeanDao().queryBuilder().where(SystemSetBeanDao.Properties.KeyName.eq(keyName)).unique();
        if(vo != null){
            vo.setKeyValue(keyVaule);
        }else{
            vo = new SystemSetBean(keyName,keyVaule,"");
        }
        getSystemSetBeanDao().insertOrReplace(vo);
    }

    /**
     * 添加设置
     * @param keyName
     * @param keyVaule
     * @param keyParas
     */
    public static void addSystemSet(String keyName,String keyVaule,String keyParas){
        SystemSetBean vo = getSystemSetBeanDao().queryBuilder().where(SystemSetBeanDao.Properties.KeyName.eq(keyName)).unique();
        if(vo != null){
            vo.setKeyValue(keyVaule);
            vo.setKeyParas(keyParas);
        }else{
            vo = new SystemSetBean(keyName,keyVaule,keyParas);
        }
        getSystemSetBeanDao().insertOrReplace(vo);
    }

    public static String getSysInfo(String keyName){
        SystemSetBean bean = getSystemSetBeanDao().queryBuilder().where(SystemSetBeanDao.Properties.KeyName.eq(keyName)).build().unique();
        return bean == null?"":bean.getKeyValue();
    }

}
