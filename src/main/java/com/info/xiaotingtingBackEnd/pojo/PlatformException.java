package com.info.xiaotingtingBackEnd.pojo;

/**
 * Copyright (c) 2018, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/10 15:23:00
 * Description：
 * Email: xiaoting233zhang@126.com
 */
public class PlatformException extends Exception {

    private int event;
    private String message;

    public String getMessage() {
        return message;
    }

    public int getEvent() {
        return event;
    }


    public PlatformException(int event, String message) {
        super(message);
        this.message = message;
        this.event = event;
    }
}
