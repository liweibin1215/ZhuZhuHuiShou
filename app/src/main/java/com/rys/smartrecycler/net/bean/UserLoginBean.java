package com.rys.smartrecycler.net.bean;

/**
 * 创作时间： 2019/5/27 on 下午6:28
 * <p>
 * 描述：
 * <p>
 * 作者：lwb
 */

public class UserLoginBean {
    private int id;
    private String nick_name;
    private String avatar_url;
    private String phone;
    private String openid;
    private int    ep_num;
    private String ep_money;
    private String ep_money_all;
    private String create_at;
    private String update_at;
    private String register_time;

    public UserLoginBean(int id, String nick_name, String avatar_url, String phone, String openid, int ep_num, String ep_money, String ep_money_all) {
        this.id = id;
        this.nick_name = nick_name;
        this.avatar_url = avatar_url;
        this.phone = phone;
        this.openid = openid;
        this.ep_num = ep_num;
        this.ep_money = ep_money;
        this.ep_money_all = ep_money_all;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public int getEp_num() {
        return ep_num;
    }

    public void setEp_num(int ep_num) {
        this.ep_num = ep_num;
    }

    public String getEp_money() {
        return ep_money;
    }

    public void setEp_money(String ep_money) {
        this.ep_money = ep_money;
    }

    public String getEp_money_all() {
        return ep_money_all;
    }

    public void setEp_money_all(String ep_money_all) {
        this.ep_money_all = ep_money_all;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(String update_at) {
        this.update_at = update_at;
    }

    public String getRegister_time() {
        return register_time;
    }

    public void setRegister_time(String register_time) {
        this.register_time = register_time;
    }
}
