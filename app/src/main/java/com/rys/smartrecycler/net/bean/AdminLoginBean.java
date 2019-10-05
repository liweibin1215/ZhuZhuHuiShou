package com.rys.smartrecycler.net.bean;

/**
 * 创作时间： 2019/5/27 on 下午6:37
 * <p>
 * 描述：
 * <p>
 * 作者：lwb
 */

public class AdminLoginBean {
    private int id;
    private String name;
    private String phone;
    private String idcard;
    private String head_img;
    private String create_at;

    public AdminLoginBean(int id, String name, String phone, String idcard, String head_img, String create_at) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.idcard = idcard;
        this.head_img = head_img;
        this.create_at = create_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }
}
