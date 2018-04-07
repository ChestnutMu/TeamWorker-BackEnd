package com.info.xiaotingtingBackEnd.controller;

import com.info.xiaotingtingBackEnd.model.Address;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.repository.base.SearchBean;
import com.info.xiaotingtingBackEnd.repository.base.SearchCondition;
import com.info.xiaotingtingBackEnd.service.AddressService;
import com.info.xiaotingtingBackEnd.util.DataCheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/7 22:22:36
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@RestController
@RequestMapping("address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @RequestMapping(value = "getAddresses", method = RequestMethod.POST)
    public ApiResponse<List<Address>> getAddresses(@RequestBody Map<String, String> params) {
        String prAddressId = params.get("prAddressId");
        String level = params.get("level");
        String pageSize = params.get("pageSize");
        String pageNum = params.get("pageNum");
        SearchCondition searchCondition = new SearchCondition();
        if (!DataCheckUtil.isEmpty(pageSize))
            searchCondition.setSize(Integer.valueOf(pageSize));
        if (!DataCheckUtil.isEmpty(pageNum))
            searchCondition.setPageNum(Integer.valueOf(pageNum));
        if (prAddressId != null) {
            if ("".equals(prAddressId))
                searchCondition.addSearchBean("prAddressId", prAddressId, SearchBean.OPERATOR_IS_NULL);
            else
                searchCondition.addSearchBean("prAddressId", prAddressId, SearchBean.OPERATOR_EQ);
        }
        if (!DataCheckUtil.isEmpty(level))
            searchCondition.addSearchBean("level", Integer.valueOf(level), SearchBean.OPERATOR_EQ);
        searchCondition.addSortBean("name", "asc", SearchBean.OPERATOR_SORT);
        ApiResponse<List<Address>> response = addressService.getPageBySearchCondition(searchCondition);
        return response;
    }
}
