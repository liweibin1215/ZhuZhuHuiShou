package com.rys.smartrecycler.event;

/**
 * 创作时间： 2019/6/22 on 下午3:06
 * <p>
 * 描述：
 * <p>
 * 作者：lwb
 */

public class MqttEvent {
    private int code;
    private String productKey;
    private String deviceName;
    private String deviceSecret;

    public MqttEvent(int code, String productKey, String deviceName, String deviceSecret) {
        this.code = code;
        this.productKey = productKey;
        this.deviceName = deviceName;
        this.deviceSecret = deviceSecret;
    }

    public int getCode() {
        return code;

    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceSecret() {
        return deviceSecret;
    }

    public void setDeviceSecret(String deviceSecret) {
        this.deviceSecret = deviceSecret;
    }
}
