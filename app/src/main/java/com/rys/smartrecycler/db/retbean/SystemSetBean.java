package com.rys.smartrecycler.db.retbean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 创作时间： 2019/5/6 on 下午9:37
 * <p>
 * 描述：
 * <p>
 * 作者：lwb
 */
@Entity
public class SystemSetBean {
    @Id(autoincrement = true)
    private Long id;
    @Unique
    private String keyName;//系统参数key
    private String keyValue;//系统参数value
    private String keyParas;//系统参数描述
    private int    upflag;//上传标识，0 需要上传、1不需要上传
    @Generated(hash = 2117336874)
    public SystemSetBean(Long id, String keyName, String keyValue, String keyParas,
            int upflag) {
        this.id = id;
        this.keyName = keyName;
        this.keyValue = keyValue;
        this.keyParas = keyParas;
        this.upflag = upflag;
    }
    public SystemSetBean(String keyName, String keyValue, String keyParas) {
        this.keyName = keyName;
        this.keyValue = keyValue;
        this.keyParas = keyParas;
    }
    @Generated(hash = 874991827)
    public SystemSetBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getKeyName() {
        return this.keyName;
    }
    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }
    public String getKeyValue() {
        return this.keyValue;
    }
    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }
    public String getKeyParas() {
        return this.keyParas;
    }
    public void setKeyParas(String keyParas) {
        this.keyParas = keyParas;
    }
    public int getUpflag() {
        return this.upflag;
    }
    public void setUpflag(int upflag) {
        this.upflag = upflag;
    }
}
