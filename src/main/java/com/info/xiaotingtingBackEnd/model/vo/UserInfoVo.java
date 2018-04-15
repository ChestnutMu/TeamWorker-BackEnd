package com.info.xiaotingtingBackEnd.model.vo;

/**
 * Created by king on 9:54 2018/4/12
 */
public class UserInfoVo {
    private String userId;

    private String nickname;

    private String avatar;

    public UserInfoVo(String userId, String nickname, String avatar) {
        this.userId = userId;
        this.nickname = nickname;
        this.avatar = avatar;
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
}
