package com.info.xiaotingtingBackEnd.service.base;

import com.info.xiaotingtingBackEnd.model.Department;
import com.info.xiaotingtingBackEnd.model.Message;
import com.info.xiaotingtingBackEnd.repository.DepartmentRelationRep;
import com.info.xiaotingtingBackEnd.repository.DepartmentRep;
import com.info.xiaotingtingBackEnd.repository.MessageRep;
import com.info.xiaotingtingBackEnd.repository.UserRep;
import com.info.xiaotingtingBackEnd.socket.SenderEventHandler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Copyright (c) 2017, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2017/12/31 09:47:22
 * Description：Service基类
 * Email: xiaoting233zhang@126.com
 */
public abstract class BaseService {
    @Autowired
    public UserRep userRep;
    @Autowired
    public DepartmentRep departmentRep;
    @Autowired
    public DepartmentRelationRep departmentRelationRep;
    @Autowired
    public MessageRep messageRep;

    @Autowired
    public SenderEventHandler handler;
}
