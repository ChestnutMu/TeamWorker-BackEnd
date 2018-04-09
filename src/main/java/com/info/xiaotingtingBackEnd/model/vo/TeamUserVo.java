package com.info.xiaotingtingBackEnd.model.vo;

import java.util.Date;

/**
 * Created by king on 21:02 2018/4/9
 */
public class TeamUserVo {
    /*基本信息*/
    private String userId;

    private String nickname;

    private String avatar;

    private String telephone;

    private String sex;

    private String birthday;

    private String region;

    /*与团队关系*/
    /*队员类型 TeamConstants*/
    private Integer type;

    /*加入团队或者更新type时间*/
    private Date updateTime;

    public TeamUserVo(String userId, String nickname, String avatar, String telephone, String sex, String birthday, String region, Integer type, Date updateTime) {
        this.userId = userId;
        this.nickname = nickname;
        this.avatar = avatar;
        this.telephone = telephone;
        this.sex = sex;
        this.birthday = birthday;
        this.region = region;
        this.type = type;
        this.updateTime = updateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
