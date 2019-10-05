package com.rys.smartrecycler.net.bean;

import java.util.List;

/**
 * 创建时间：2019/5/28
 * 作者：李伟斌
 * 功能描述:设备信息获取
 */
public class DeviceInfoBean {
    private int id;
    private int collector_id;
    private int fix_status;
    private int ad1;
    private int ad2;
    private String number;
    private String password;
    private String name;
    private String sheng;
    private String shi;
    private String qu;
    private String address;
    private String latitude;
    private String longitude;
    private String admin_pw;
    private String start_time;
    private String end_time;
    private String create_at;
    private String kefu;
    private DeskPriceBean price;
    private DeskSatus status;
    private List<DeskInfoBean> attach;

    public DeviceInfoBean() {
    }

    public DeskSatus getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCollector_id() {
        return collector_id;
    }

    public void setCollector_id(int collector_id) {
        this.collector_id = collector_id;
    }

    public int getFix_status() {
        return fix_status;
    }

    public void setFix_status(int fix_status) {
        this.fix_status = fix_status;
    }

    public int getAd1() {
        return ad1;
    }

    public void setAd1(int ad1) {
        this.ad1 = ad1;
    }

    public int getAd2() {
        return ad2;
    }

    public void setAd2(int ad2) {
        this.ad2 = ad2;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSheng() {
        return sheng;
    }

    public void setSheng(String sheng) {
        this.sheng = sheng;
    }

    public String getShi() {
        return shi;
    }

    public void setShi(String shi) {
        this.shi = shi;
    }

    public String getQu() {
        return qu;
    }

    public void setQu(String qu) {
        this.qu = qu;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAdmin_pw() {
        return admin_pw;
    }

    public void setAdmin_pw(String admin_pw) {
        this.admin_pw = admin_pw;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getKefu() {
        return kefu;
    }

    public void setKefu(String kefu) {
        this.kefu = kefu;
    }

    public DeskPriceBean getPrice() {
        return price;
    }

    public void setPrice(DeskPriceBean price) {
        this.price = price;
    }

    public List<DeskInfoBean> getAttach() {
        return attach;
    }

    public void setAttach(List<DeskInfoBean> attach) {
        this.attach = attach;
    }
}
