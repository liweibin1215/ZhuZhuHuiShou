package com.rys.smartrecycler.db.retbean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 创作时间： 2019/5/6 on 下午9:43
 * <p>
 * 描述：
 * <p>
 * 作者：lwb
 */
@Entity
public class UserOrder {
    @Id(autoincrement = true)
    private Long id;
    private String orderNo;//订单编号
    private String deviceSn;//设备编号
    private int deskType;//回收类型
    private int deskNo;//回收的副柜号
    private int userId;//用户id
    private String userPhone;//用户手机号
    private String recycleSum;//当前回总量数量/重量
    private String recycleMoney;//环保金额
    private long createTime;//回收时间
    private int upflag;//上传标识：0待上传、1已上传

    public UserOrder(String orderNo, String deviceSn, int deskType, int deskNo, int userId, String userPhone, String recycleSum, String recycleMoney, long createTime, int upflag) {
        this.orderNo = orderNo;
        this.deviceSn = deviceSn;
        this.deskType = deskType;
        this.deskNo = deskNo;
        this.userId = userId;
        this.userPhone = userPhone;
        this.recycleSum = recycleSum;
        this.recycleMoney = recycleMoney;
        this.createTime = createTime;
        this.upflag = upflag;
    }

    @Generated(hash = 657407022)
    public UserOrder(Long id, String orderNo, String deviceSn, int deskType,
            int deskNo, int userId, String userPhone, String recycleSum,
            String recycleMoney, long createTime, int upflag) {
        this.id = id;
        this.orderNo = orderNo;
        this.deviceSn = deviceSn;
        this.deskType = deskType;
        this.deskNo = deskNo;
        this.userId = userId;
        this.userPhone = userPhone;
        this.recycleSum = recycleSum;
        this.recycleMoney = recycleMoney;
        this.createTime = createTime;
        this.upflag = upflag;
    }
    @Generated(hash = 1552988665)
    public UserOrder() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getOrderNo() {
        return this.orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public String getDeviceSn() {
        return this.deviceSn;
    }
    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }
    public int getDeskType() {
        return this.deskType;
    }
    public void setDeskType(int deskType) {
        this.deskType = deskType;
    }
    public int getDeskNo() {
        return this.deskNo;
    }
    public void setDeskNo(int deskNo) {
        this.deskNo = deskNo;
    }
    public int getUserId() {
        return this.userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getUserPhone() {
        return this.userPhone;
    }
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
    public String getRecycleSum() {
        return this.recycleSum;
    }
    public void setRecycleSum(String recycleSum) {
        this.recycleSum = recycleSum;
    }
    public String getRecycleMoney() {
        return this.recycleMoney;
    }
    public void setRecycleMoney(String recycleMoney) {
        this.recycleMoney = recycleMoney;
    }
    public long getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    public int getUpflag() {
        return this.upflag;
    }
    public void setUpflag(int upflag) {
        this.upflag = upflag;
    }

}
