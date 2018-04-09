package com.info.xiaotingtingBackEnd.model.vo;

import com.info.xiaotingtingBackEnd.model.User;

/**
 * Created by king on 21:38 2018/4/9
 */
public class FriendUserVo {
    /*基本信息*/
    private User user;

    private boolean friend;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isFriend() {
        return friend;
    }

    public void setFriend(boolean friend) {
        this.friend = friend;
    }
}
