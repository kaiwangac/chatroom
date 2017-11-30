package com.chatroom.constant;

public class RouterPath {
    private RouterPath() {

    }

    public static final String STATIC = "/static/*";
    public static final String REST = "/rest/v1";
    public static final String ROOMS = "/rooms";
    public static final String MESSAGES = "/rooms/:roomNumber/messages";
    public static final String MESSAGE = "/rooms/:roomNumber/messages/:messageId";
}
