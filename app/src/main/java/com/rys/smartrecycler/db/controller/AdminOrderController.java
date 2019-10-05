package com.rys.smartrecycler.db.controller;

import com.rys.smartrecycler.db.DbHelper;
import com.rys.smartrecycler.db.greendao.AdminOrderDao;
import com.rys.smartrecycler.db.retbean.AdminOrder;

import java.util.List;

/**
 * 创建时间：2019/5/22
 * 作者：李伟斌
 * 功能描述:
 */
public class AdminOrderController {
    /**
     *
     * @return 通用设置实例
     */
    public static AdminOrderDao getAdminOrderDao(){
        return DbHelper.getInstance().getDaoSession().getAdminOrderDao();
    }

    /**
     * 新增用户投递订单
     * @param vo
     */
    public static void insertAdminOrder(AdminOrder vo){
        getAdminOrderDao().insert(vo);
    }

    /**
     * 获取所有订单
     * @return
     */
    public static List<AdminOrder> getAllAdminOrder(){
        return getAdminOrderDao().loadAll();
    }

    /**
     * 获取该用户所有订单
     * @param phone
     * @return
     */
    public static List<AdminOrder> getAdminOrderByPhone(String phone){
        return getAdminOrderDao().queryBuilder().where(AdminOrderDao.Properties.UserPhone.eq(phone)).list();
    }

    /**
     * 分页加载
     * @param page
     * @param pageSize
     * @return
     */
    public static List<AdminOrder> getAdminOrderByPage(int page,int pageSize){
        return getAdminOrderDao().queryBuilder().orderDesc(AdminOrderDao.Properties.Id).offset((page-1)*pageSize).limit(pageSize).list();
    }
}
