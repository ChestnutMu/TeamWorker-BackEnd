package com.info.xiaotingtingBackEnd.pojo;

/**
 * Created by king on 16:48 2018/4/9
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
