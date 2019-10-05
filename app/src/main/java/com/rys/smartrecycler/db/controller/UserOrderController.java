package com.rys.smartrecycler.db.controller;

import com.rys.smartrecycler.db.DbHelper;
import com.rys.smartrecycler.db.greendao.UserOrderDao;
import com.rys.smartrecycler.db.retbean.UserOrder;

import java.util.List;

/**
 * 创建时间：2019/5/22
 * 作者：李伟斌
 * 功能描述:
 */
public class UserOrderController {
    /**
     *
     * @return 通用设置实例
     */
    public static UserOrderDao getUserOrderDao(){
        return DbHelper.getInstance().getDaoSession().getUserOrderDao();
    }

    /**
     * 新增用户投递订单
     * @param vo
     */
    public static void insertUserOrder(UserOrder vo){
        getUserOrderDao().insert(vo);
    }

    /**
     * 获取所有订单
     * @return
     */
    public static List<UserOrder> getAllUserOrder(){
        return getUserOrderDao().loadAll();
    }

    /**
     * 获取该用户所有订单
     * @param phone
     * @return
     */
    public static List<UserOrder> getUserOrderByPhone(String phone){
        return getUserOrderDao().queryBuilder().where(UserOrderDao.Properties.UserPhone.eq(phone)).list();
    }

    /**
     * 分页加载订单数据
     * @param page
     * @param pageSize
     * @return
     */
    public static List<UserOrder> getUserOrderByPage(int page,int pageSize){
        return getUserOrderDao().queryBuilder().orderDesc(UserOrderDao.Properties.Id).offset((page-1)*pageSize).limit(pageSize).list();
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    public static List<UserOrder> getOrderListByLimit(int page,int pageSize){
        return getUserOrderDao().queryBuilder().orderDesc(UserOrderDao.Properties.CreateTime).offset((page-1)*pageSize).limit(pageSize).list();
    }

    /**
     * 获取总数
     * @return
     */
    public static long getCurrentOrderCount(){
        return getUserOrderDao().queryBuilder().buildCount().count();
    }
}
