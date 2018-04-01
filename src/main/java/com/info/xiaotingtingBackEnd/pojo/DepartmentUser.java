package com.info.xiaotingtingBackEnd.pojo;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/1 15:33:05
 * Description：
 * Email: xiaoting233zhang@126.com
 */
public class DepartmentUser {

    private String userId;

    private String avatar;

    private String nickname;

    public DepartmentUser(String userId, String avatar, String nickname) {
        this.userId = userId;
        this.avatar = avatar;
        this.nickname = nickname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
