package com.rys.smartrecycler.event;

/**
 * 创建时间：2019/6/24
 * 作者：李伟斌
 * 功能描述:
 */
public class LoginEvent {
    private int    code;
    private int    loginType;
    private String loginUser;
    private int    userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LoginEvent(int code, int loginType, int userId, String loginUser) {
        this.code = code;
        this.userId = userId;
        this.loginType = loginType;
        this.loginUser = loginUser;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public String getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }
}
