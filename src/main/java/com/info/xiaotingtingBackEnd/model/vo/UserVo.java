package com.info.xiaotingtingBackEnd.model.vo;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/7 19:04:02
 * Description：
 * Email: xiaoting233zhang@126.com
 */
public class UserVo {
    private String userId;

    private String account;

    private String nickname;

    private String avatar;

    private String telephone;

    private String sex;

    private String birthday;

    private String region;

    public UserVo(String userId, String account, String nickname, String avatar, String telephone, String sex, String birthday, String region) {
        this.userId = userId;
        this.account = account;
        this.nickname = nickname;
        this.avatar = avatar;
        this.telephone = telephone;
        this.sex = sex;
        this.birthday = birthday;
        this.region = region;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
