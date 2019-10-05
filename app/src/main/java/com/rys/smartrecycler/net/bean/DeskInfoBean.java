package com.rys.smartrecycler.net.bean;

/**
 * 创建时间：2019/5/28
 * 作者：李伟斌
 * 功能描述:
 */
public class DeskInfoBean {
    private int index;
    private String number;
    private String type;
    private String sum;
    private int err_type;
    private int is_full;
    private int percent;
    private String create_at;
    private String update_at;

    public DeskInfoBean(int index, String number, String type, String sum, int err_type, int is_full, int percent, String create_at, String update_at) {
        this.index = index;
        this.number = number;
        this.type = type;
        this.sum = sum;
        this.err_type = err_type;
        this.is_full = is_full;
        this.percent = percent;
        this.create_at = create_at;
        this.update_at = update_at;
    }

    public int getId() {
        return index;
    }

    public void setId(int index) {
        this.index = index;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public int getErr_type() {
        return err_type;
    }

    public void setErr_type(int err_type) {
        this.err_type = err_type;
    }

    public int getIs_full() {
        return is_full;
    }

    public void setIs_full(int is_full) {
        this.is_full = is_full;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
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
}
