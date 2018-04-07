package com.info.xiaotingtingBackEnd.service;

import com.info.xiaotingtingBackEnd.model.Address;
import com.info.xiaotingtingBackEnd.repository.AddressRep;
import com.info.xiaotingtingBackEnd.service.base.BaseService;
import org.springframework.stereotype.Service;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/7 22:23:51
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@Service
public class AddressService extends BaseService<Address,String,AddressRep>{

    @Override
    public AddressRep getRepo() {
        return addressRep;
    }
}
