package com.info.xiaotingtingBackEnd.model.vo;

import com.info.xiaotingtingBackEnd.model.User;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/10 15:23:00
 * Description：
 * Email: xiaoting233zhang@126.com
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
