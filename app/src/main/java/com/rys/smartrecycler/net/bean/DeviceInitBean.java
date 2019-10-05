package com.rys.smartrecycler.net.bean;

/**
 * 创建时间：2019/5/28
 * 作者：李伟斌
 * 功能描述:
 */
public class DeviceInitBean {
    private String token;
    private MqttInitBean mqtt;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public MqttInitBean getMqtt() {
        return mqtt;
    }

    public void setMqtt(MqttInitBean mqtt) {
        this.mqtt = mqtt;
    }

    public DeviceInitBean(String token, MqttInitBean mqtt) {
        this.token = token;
        this.mqtt = mqtt;
    }
}
