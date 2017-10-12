package com.chatroom.verticle;

import io.vertx.core.Vertx;

/**
 * Created by wangkai on 2017/10/11.
 */
public class Application {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(MainVerticle.class.getName());
    }
}
