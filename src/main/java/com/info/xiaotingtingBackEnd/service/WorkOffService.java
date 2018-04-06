package com.info.xiaotingtingBackEnd.service;

import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.model.Message;
import com.info.xiaotingtingBackEnd.model.WorkOff;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.info.xiaotingtingBackEnd.repository.WorkOffRep;
import com.info.xiaotingtingBackEnd.service.base.BaseService;
import com.info.xiaotingtingBackEnd.util.EntityUtil;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/5 21:09:53
 * Description：
 * Email: xiaoting233zhang@126.com
 */
@Service
public class WorkOffService extends BaseService<WorkOff, String, WorkOffRep> {
    @Override
    public WorkOffRep getRepo() {
        return workOffRep;
    }

    public void saveAndSendApplication(WorkOff workOff) {
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
    }

    public ApiResponse approveWorkOff(WorkOff workOff) {
        workOffRep.save(workOff);
        ApiResponse workOffApiResponse = new ApiResponse<>();
        workOffApiResponse.setStatus(HttpResponseCodes.SUCCESS);
        workOffApiResponse.setMessage("已处理该请假");
        return workOffApiResponse;
    }
}
