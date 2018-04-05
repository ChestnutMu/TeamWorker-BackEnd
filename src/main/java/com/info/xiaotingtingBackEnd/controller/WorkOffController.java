package com.info.xiaotingtingBackEnd.controller;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.model.Message;
import com.info.xiaotingtingBackEnd.model.WorkOff;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.repository.WorkOffRep;
import com.info.xiaotingtingBackEnd.service.WorkOffService;
import com.info.xiaotingtingBackEnd.socket.SenderEventHandler;
import com.info.xiaotingtingBackEnd.util.EntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/2 17:47:27
 * Description：请假
 * Email: xiaoting233zhang@126.com
 */
@RestController
@RequestMapping("workoff")
public class WorkOffController {

    @Autowired
    WorkOffService workOffService;

    @RequestMapping(value = "applyWorkOff", method = RequestMethod.POST)
    public ApiResponse<WorkOff> applyWorkOff(@RequestBody WorkOff workOff) {
        workOff.setStatus(0);
        workOffService.saveAndSendApplication(workOff);
        ApiResponse<WorkOff> workOffApiResponse = new ApiResponse<>();
        workOffApiResponse.setStatus(HttpResponseCodes.SUCCESS);
        workOffApiResponse.setMessage("您的请假申请已提交");
        workOffApiResponse.setData(workOff);
        return workOffApiResponse;
    }

    @RequestMapping(value = "approveWorkOff", method = RequestMethod.POST)
    public ApiResponse approveWorkOff(@RequestBody WorkOff workOff) {
        workOffService.save(workOff);
        ApiResponse workOffApiResponse = new ApiResponse<>();
        workOffApiResponse.setStatus(HttpResponseCodes.SUCCESS);
        workOffApiResponse.setMessage("已处理该请假");
        return workOffApiResponse;
    }


}
