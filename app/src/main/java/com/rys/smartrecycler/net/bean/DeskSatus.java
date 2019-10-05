package com.rys.smartrecycler.net.bean;

/**
 * 创建时间：2019/6/20
 * 作者：李伟斌
 * 功能描述:
 */
public class DeskSatus {
    private int box_id;
    private String number;
    private String network;
    private String version;
    private int    bottle;//箱满阀值
    private int    metal;//箱满阀值
    private int    plastic;//箱满阀值

    public int getBottle() {
        return bottle;
    }

    public void setBottle(int bottle) {
        this.bottle = bottle;
    }

    public int getMetal() {
        return metal;
    }

    public void setMetal(int metal) {
        this.metal = metal;
    }

    public int getPlastic() {
        return plastic;
    }

    public void setPlastic(int plastic) {
        this.plastic = plastic;
    }

    public int getPaper() {
        return paper;
    }

    public void setPaper(int paper) {
        this.paper = paper;
    }

    public int getSpin() {
        return spin;
    }

    public void setSpin(int spin) {
        this.spin = spin;
    }

    public int getGlass() {
        return glass;
    }

    public void setGlass(int glass) {
        this.glass = glass;
    }

    private int    paper;//箱满阀值
    private int    spin;//箱满阀值
    private int    glass;//箱满阀值
    private String update_at;
}
