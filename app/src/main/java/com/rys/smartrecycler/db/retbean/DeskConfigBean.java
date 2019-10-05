package com.rys.smartrecycler.db.retbean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 创作时间： 2019/5/6 on 下午9:14
 * <p>
 * 描述：
 * <p>
 * 作者：lwb
 */
@Entity
public class DeskConfigBean {
    @Id(autoincrement = true)
    private Long id;
    private int serverId;//平台id
    private int  deskType;//副柜类型：1饮料瓶、2纸类、3纺织物、4生活塑料、5金属、6玻璃
    private String deskName;//副柜类型名称：纸箱、瓶子、金属
    @Unique
    private int  deskNo;//副柜编号，不可重复
    private int  fullMin;//箱满阀值
    private String  unitPrice;//回收单价
    private String  bottleNum;//该副柜回收瓶子总数
    private String  totalWeigth;//该副柜回收总重量
    private String totalMoney;//该副柜当前回收总金额
    private int  percent;//当前百分比
    private int  upflag;//上传标识 0需要上传、1不需要上传
    private int  fullStatus;//箱满状态0未满 1已满
    private int  lockStatus;//锁定状态，0未锁定、1已锁定（故障后）
    private String errorStatus;//故障状态、O正常，1故障 十六进制（01011001）网络状态、投递门状态、回收门状态、电子称状态、烟雾报警状态、备用、备用、备用
    private String updateTime;//更新配置时间
    public DeskConfigBean(int deskType, String deskName, int deskNo,int fullMin,String unitPrice,
                          String bottleNum, String totalWeigth, String totalMoney, int percent,
                          int upflag,int fullStatus,int lockStatus, String errorStatus, String updateTime) {
        this.deskType = deskType;
        this.deskName = deskName;
        this.deskNo = deskNo;
        this.fullMin = fullMin;
        this.unitPrice = unitPrice;
        this.bottleNum = bottleNum;
        this.totalWeigth = totalWeigth;
        this.totalMoney = totalMoney;
        this.percent = percent;
        this.upflag = upflag;
        this.fullStatus = fullStatus;
        this.lockStatus = lockStatus;
        this.errorStatus = errorStatus;
        this.updateTime = updateTime;
    }

    @Generated(hash = 25735945)
    public DeskConfigBean(Long id, int serverId, int deskType, String deskName, int deskNo, int fullMin,
            String unitPrice, String bottleNum, String totalWeigth, String totalMoney, int percent, int upflag,
            int fullStatus, int lockStatus, String errorStatus, String updateTime) {
        this.id = id;
        this.serverId = serverId;
        this.deskType = deskType;
        this.deskName = deskName;
        this.deskNo = deskNo;
        this.fullMin = fullMin;
        this.unitPrice = unitPrice;
        this.bottleNum = bottleNum;
        this.totalWeigth = totalWeigth;
        this.totalMoney = totalMoney;
        this.percent = percent;
        this.upflag = upflag;
        this.fullStatus = fullStatus;
        this.lockStatus = lockStatus;
        this.errorStatus = errorStatus;
        this.updateTime = updateTime;
    }

    @Generated(hash = 1426712148)
    public DeskConfigBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDeskType() {
        return this.deskType;
    }

    public void setDeskType(int deskType) {
        this.deskType = deskType;
    }

    public String getDeskName() {
        return this.deskName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public int getDeskNo() {
        return this.deskNo;
    }

    public void setDeskNo(int deskNo) {
        this.deskNo = deskNo;
    }

    public String getUnitPrice() {
        return this.unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getBottleNum() {
        return this.bottleNum;
    }

    public void setBottleNum(String bottleNum) {
        this.bottleNum = bottleNum;
    }

    public String getTotalWeigth() {
        return this.totalWeigth;
    }

    public void setTotalWeigth(String totalWeigth) {
        this.totalWeigth = totalWeigth;
    }

    public String getTotalMoney() {
        return this.totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public int getPercent() {
        return this.percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public int getUpflag() {
        return this.upflag;
    }

    public void setUpflag(int upflag) {
        this.upflag = upflag;
    }

    public int getFullStatus() {
        return this.fullStatus;
    }

    public void setFullStatus(int fullStatus) {
        this.fullStatus = fullStatus;
    }

    public int getLockStatus() {
        return this.lockStatus;
    }

    public void setLockStatus(int lockStatus) {
        this.lockStatus = lockStatus;
    }

    public String getErrorStatus() {
        return this.errorStatus;
    }

    public void setErrorStatus(String errorStatus) {
        this.errorStatus = errorStatus;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getFullMin() {
        return this.fullMin;
    }

    public void setFullMin(int fullMin) {
        this.fullMin = fullMin;
    }

    public int getServerId() {
        return this.serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }
}
