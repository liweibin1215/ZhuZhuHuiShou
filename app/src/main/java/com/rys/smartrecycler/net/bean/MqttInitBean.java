package com.rys.smartrecycler.net.bean;

/**
 * 创建时间：2019/5/28
 * 作者：李伟斌
 * 功能描述:
 */
public class MqttInitBean {
    private int id;
    private String ProductKey;
    private String DeviceName;
    private String DeviceSecret;
    private String IotId;
    private String create_at;

    public MqttInitBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductKey() {
        return ProductKey;
    }

    public void setProductKey(String productKey) {
        ProductKey = productKey;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String deviceName) {
        DeviceName = deviceName;
    }

    public String getDeviceSecret() {
        return DeviceSecret;
    }

    public void setDeviceSecret(String deviceSecret) {
        DeviceSecret = deviceSecret;
    }

    public String getIotId() {
        return IotId;
    }

    public void setIotId(String iotId) {
        IotId = iotId;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }
}
