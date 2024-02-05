package com.itheima.qqcommon;

import java.io.Serializable;

/**
 * 表示一个用户/客户信息
 */
public class User implements Serializable {

    private static final long serialVersionID = 1L;
    //增强兼容性操作

    private String userId;  //用户名
    private String psw; //password

    public User() {
    }

    public User(String userId, String psw) {
        this.userId = userId;
        this.psw = psw;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }
}
