package com.rys.smartrecycler.db.retbean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 创建时间：2019/5/28
 * 作者：李伟斌
 * 功能描述:
 */
@Entity
public class DeviceInfo {
    @Id(autoincrement = true)
    private Long id;
    private String deviceSn;//设备编号
    private String devicePwd;//设备密码
    private String deviceName;//柜机名称
    private String adminPwd;//后台管理密码
    private String keFuPhone;//客服电话
    private String province;//省
    private String city;//市
    private String area;//区
    private String adress;//详细地址
    private String create_at;//创建时间
    private String ProductKey;//产品key
    private String DeviceSecret;//设备秘钥
    @Generated(hash = 78022381)
    public DeviceInfo(Long id, String deviceSn, String devicePwd, String deviceName,
            String adminPwd, String keFuPhone, String province, String city,
            String area, String adress, String create_at, String ProductKey,
            String DeviceSecret) {
        this.id = id;
        this.deviceSn = deviceSn;
        this.devicePwd = devicePwd;
        this.deviceName = deviceName;
        this.adminPwd = adminPwd;
        this.keFuPhone = keFuPhone;
        this.province = province;
        this.city = city;
        this.area = area;
        this.adress = adress;
        this.create_at = create_at;
        this.ProductKey = ProductKey;
        this.DeviceSecret = DeviceSecret;
    }

    public DeviceInfo(String deviceSn, String devicePwd, String productKey, String deviceSecret) {
        this.deviceSn = deviceSn;
        this.devicePwd = devicePwd;
        ProductKey = productKey;
        DeviceSecret = deviceSecret;
    }

    @Generated(hash = 2125166935)
    public DeviceInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDeviceSn() {
        return this.deviceSn;
    }
    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }
    public String getDevicePwd() {
        return this.devicePwd;
    }
    public void setDevicePwd(String devicePwd) {
        this.devicePwd = devicePwd;
    }
    public String getDeviceName() {
        return this.deviceName;
    }
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    public String getAdminPwd() {
        return this.adminPwd;
    }
    public void setAdminPwd(String adminPwd) {
        this.adminPwd = adminPwd;
    }
    public String getKeFuPhone() {
        return this.keFuPhone;
    }
    public void setKeFuPhone(String keFuPhone) {
        this.keFuPhone = keFuPhone;
    }
    public String getProvince() {
        return this.province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getCity() {
        return this.city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getArea() {
        return this.area;
    }
    public void setArea(String area) {
        this.area = area;
    }
    public String getAdress() {
        return this.adress;
    }
    public void setAdress(String adress) {
        this.adress = adress;
    }
    public String getCreate_at() {
        return this.create_at;
    }
    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }
    public String getProductKey() {
        return this.ProductKey;
    }
    public void setProductKey(String ProductKey) {
        this.ProductKey = ProductKey;
    }
    public String getDeviceSecret() {
        return this.DeviceSecret;
    }
    public void setDeviceSecret(String DeviceSecret) {
        this.DeviceSecret = DeviceSecret;
    }



}
