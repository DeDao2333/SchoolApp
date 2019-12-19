package com.example.java.algorithm.javabean;

import cn.bmob.v3.BmobObject;

/**
 * author: DeDao233.
 * time: 2018/5/15.
 */
//TODO 好友管理：9.1、创建好友表
public class Friend extends BmobObject {

    private _User user;
    private _User friendUser;

    private transient String pinyin;

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public _User getUser() {
        return user;
    }

    public void setUser(_User user) {
        this.user = user;
    }

    public _User getFriendUser() {
        return friendUser;
    }

    public void setFriendUser(_User friendUser) {
        this.friendUser = friendUser;
    }
}
