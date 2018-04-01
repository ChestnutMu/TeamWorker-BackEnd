package com.info.xiaotingtingBackEnd.service;

import com.info.xiaotingtingBackEnd.model.User;
import com.info.xiaotingtingBackEnd.service.base.BaseService;
import org.springframework.stereotype.Service;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/1 14:23:58
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@Service
public class UserService extends BaseService {

    public boolean idAuth(String uid, String token) {
        User user = userRep.findOne(uid);
        if (null == user) {
            return false;
        }
        if (user.getToken().equals(token)) {
            return true;
        }
        return false;
    }
}
