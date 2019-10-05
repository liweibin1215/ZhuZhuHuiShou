package com.rys.smartrecycler.db.retbean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 创作时间： 2019/5/5 on 下午10:35
 * <p>
 * 描述：
 * <p>
 * 作者：lwb
 */
@Entity
public class FinishLogBean {
    @Id(autoincrement = true)
    private Long id;//主键，自增
    private int     logType;//日志类型
    private String  logName;//日志简称
    private String  logDesc;//日志描述
    private String  createTime;//生成时间
    private int     upflag;//上传标识，0待上传、1已上传

    public FinishLogBean(int logType, String logName, String logDesc, String createTime, int upflag) {
        this.logType = logType;
        this.logName = logName;
        this.logDesc = logDesc;
        this.createTime = createTime;
        this.upflag = upflag;
    }

    @Generated(hash = 213784014)
    public FinishLogBean(Long id, int logType, String logName, String logDesc,
            String createTime, int upflag) {
        this.id = id;
        this.logType = logType;
        this.logName = logName;
        this.logDesc = logDesc;
        this.createTime = createTime;
        this.upflag = upflag;
    }
    @Generated(hash = 1383433647)
    public FinishLogBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getLogType() {
        return this.logType;
    }
    public void setLogType(int logType) {
        this.logType = logType;
    }
    public String getLogName() {
        return this.logName;
    }
    public void setLogName(String logName) {
        this.logName = logName;
    }
    public String getLogDesc() {
        return this.logDesc;
    }
    public void setLogDesc(String logDesc) {
        this.logDesc = logDesc;
    }
    public String getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public int getUpflag() {
        return this.upflag;
    }
    public void setUpflag(int upflag) {
        this.upflag = upflag;
    }


}
