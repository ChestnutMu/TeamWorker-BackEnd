package com.info.xiaotingtingBackEnd.controller;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.model.Message;
import com.info.xiaotingtingBackEnd.model.WorkOff;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.repository.WorkOffRep;
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
    WorkOffRep workOffRep;

    @Autowired
    SenderEventHandler handler;

    @RequestMapping(value = "applyWorkOff", method = RequestMethod.POST)
    public ApiResponse<WorkOff> applyWorkOff(@RequestBody WorkOff workOff) {
        workOff.setStatus(0);
        workOffRep.save(workOff);

        Message message = new Message();
        message.setChatId(EntityUtil.getIdByTimeStampAndRandom());
        message.setMessageType(1);
        message.setSend(false);
        message.setRead(false);
        message.setSenderId(workOff.getUserId());
        message.setReceiverId(workOff.getApproverId());
        message.setChatName("工作通知");
        message.setContent(workOff.getUserName() + "的请假需要您的审批");
        message.setTime(new Date());
        handler.sendMessage(message);

        ApiResponse<WorkOff> workOffApiResponse = new ApiResponse<>();
        workOffApiResponse.setStatus(HttpResponseCodes.SUCCESS);
        workOffApiResponse.setMessage("您的请假申请已提交");
        workOffApiResponse.setData(workOff);
        return workOffApiResponse;
    }

    @RequestMapping(value = "approveWorkOff", method = RequestMethod.POST)
    public ApiResponse approveWorkOff(@RequestBody WorkOff workOff) {
        workOffRep.save(workOff);
        ApiResponse workOffApiResponse = new ApiResponse<>();
        workOffApiResponse.setStatus(HttpResponseCodes.SUCCESS);
        workOffApiResponse.setMessage("已处理该请假");
        return workOffApiResponse;
    }


}
