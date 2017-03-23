package com.cmcc.jeff.pwdmanager.event;

/**
 * Created by wang on 2017/3/23.
 */

public class MessageEvent {

    public final String message;

    public final String tag;

    public MessageEvent(String message, String tag) {
        this.message = message;
        this.tag = tag;
    }
}
