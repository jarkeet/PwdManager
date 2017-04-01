package com.cmcc.jeff.pwdmanager.event;

/**
 * Created by wang on 2017/3/23.
 */

public class MessageAddEvent {

    public final String message;

    public final String tag;

    public MessageAddEvent(String message, String tag) {
        this.message = message;
        this.tag = tag;
    }
}
